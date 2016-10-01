import java.awt.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

class Doce {
	public int 		tipo;
	public String 	nome;
	public double 	fator;
	
	public Doce( int tipo , String nome , double fator ) {
		this.tipo = tipo;
		this.nome = nome;
		this.fator = fator;
	}
}

class PunhadoDeDoce extends Doce {
	public int quantidade = 0;
	
	public PunhadoDeDoce( int tipo , String nome , double fator , int quantidade ) {
		super( tipo , "NA" , fator );
		this.quantidade = quantidade;
	}
}

class Cesta {
	// retorna o melhor doce de uma lista de doces
	static public Doce melhorDoce( ArrayList<Doce> doces ) {
		Doce melhor = doces.get(0);
		
		for( Doce doc : doces ) {
			if( doc.fator > melhor.fator ) {
				melhor = doc;
			}
		}
		
		return melhor;
	}
	
	// retorna o pior doce de uma lista de doces
	static public Doce piorDoce( ArrayList<Doce> doces ) {
		Doce pior = doces.get(0);
		
		for( Doce doc : doces ) {
			if( doc.fator < pior.fator ) {
				pior = doc;
			}
		}
		
		return pior;
	}	
	
	public ArrayList<PunhadoDeDoce> inventarioOriginal = new ArrayList<PunhadoDeDoce>();
	
	public Cesta() {
		this("cesta.txt");
	}
	
	public Cesta(String arquivo) {
		 CarregarArquivo(arquivo);
	}
	
	public void CarregarArquivo(String arquivo_cesta ) {
	    Scanner file;
	    file = null;
	    
	    try {
	        file = new Scanner(Paths.get(arquivo_cesta));
	    } catch (FileNotFoundException ex) {
	    	System.out.print("Arquivo "+arquivo_cesta+" Nao Encontrado!\n");
	        //Logger.getLogger(Skills.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (IOException e) {
	    	System.out.println("Abertura de "+arquivo_cesta+" falhou!");
			e.printStackTrace();
		}
	    
	    // Limpando ambiente
	    inventarioOriginal.clear();
	    
	    // Criando inventario original
	    String elementos[];
	    int id = 0;
	    while( file.hasNext() ) {
	    	elementos = file.nextLine().split(";");
	    	inventarioOriginal.add( new PunhadoDeDoce( id , elementos[2] , Float.parseFloat(elementos[1]) , Integer.parseInt(elementos[0]) ) );
	    	id++;
	    }
	    
	    file.close();
	}
}



class Encontro {
	public double custoOriginal;
	public ArrayList<Doce> doces = new ArrayList<Doce>();
	
	public Encontro( double custoOriginal ) {
		this.custoOriginal = custoOriginal;
	}	
	
	public ArrayList<Doce> docesQueFaltam( ArrayList<Doce> cesta ) {
		ArrayList<Doce> docesFaltantes = new ArrayList<Doce>(cesta);
		ArrayList<Doce> docesARemover = new ArrayList<Doce>();
		
		for( Doce doceEncontro : this.doces ) {
			for( Doce doceCesta : cesta ) {			
				if( doceEncontro.tipo == doceCesta.tipo ) {
					//docesFaltantes.remove( doceCesta );
					docesARemover.add(doceCesta);
				}
			}
		}
		
		docesFaltantes.removeAll( docesARemover );
		
		return docesFaltantes;
	}
	
	public void adicionarDoce( Doce doc ) {
		doces.add(doc);
	}

	public double calcularCustoEncontro( Doce docAdicional , Doce docIgnorado ) {
		double fator = 0;
		
		if( docAdicional != null ) {
			fator = docAdicional.fator;
		} 
		
		for( Doce doceEncontro : this.doces ) {		
			if( docIgnorado == null || docIgnorado.tipo != doceEncontro.tipo ) {
				fator = fator + doceEncontro.fator;
			}
		}
		
		if( fator <= 0.01 ) {
			fator = 1;
		}
		
		return ( this.custoOriginal / fator );
	}	
	
	public double calcularCustoEncontro( Doce doc ) {
		return this.calcularCustoEncontro( doc , null );
	}	
	
	public double simularDiferenca( Doce doc ) {
		double custoAtual = this.calcularCustoEncontro( null );
		double custoSimulado = this.calcularCustoEncontro( doc );
		
		return (custoAtual - custoSimulado);
	}	
}


public class Encontros {
	private ArrayList<Encontro> encontros =  new ArrayList<Encontro>();
	private Cesta cesta;

	private Doce doceDaVovo;	

	public int encontrosCalculados;
	
	public Encontros() {
		this( "encontros.txt" );
	}
	
	public Encontros( String arquivo_encontro ) {
		this( arquivo_encontro , "cesta.txt" );
	}

	public Encontros( String arquivo_encontro , String arquivo_cesta ) {
		CarregarArquivo( arquivo_encontro );
		cesta = new Cesta( arquivo_cesta );
	}

	public void CarregarArquivo( String arquivo ) {
	    Scanner file;
	    file = null;
	    
	    // Carregando tabela de encontros
	    try {
	        file = new Scanner(Paths.get(arquivo));
	    } catch (FileNotFoundException ex) {
	    	System.out.print("Arquivo "+arquivo+" Nao Encontrado!\n");
	        //Logger.getLogger(Skills.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (IOException e) {
	    	System.out.println("Abertura de "+arquivo+" falhou!");
			e.printStackTrace();
		}	    
	    
	    this.encontros.clear();
	    
	    while( file.hasNextLine() ) {
	    	this.encontros.add( new Encontro(Double.parseDouble(file.nextLine())) );
	    }
	    
	    file.close();
	}
	
	public void CalcularEncontros( int qtd ) {
		this.CalcularEncontros( qtd , 1 );
	}
	
	public void CalcularEncontros( int qtd , int algoritmo ) {
		this.encontrosCalculados = qtd;
		
		switch( algoritmo ) {
			case 1:
				this.AlgoritmoGuloso1( qtd );
				break;
			
			case 2:
				this.AlgoritmoForcaBruta1( qtd );
				break;
			
			default:
				break;
		}
	}
	
	private void AlgoritmoGuloso1( int qtd ) {	
		// Prepara lista de doces disponiveis
		ArrayList<Doce> docesDisponiveis = new ArrayList<Doce>();
		for( PunhadoDeDoce punDoces : this.cesta.inventarioOriginal ) {
			int j;
			for( j = 0 ; j < punDoces.quantidade ; j++) {
				docesDisponiveis.add( punDoces );
			}
		}
		
		// Separa o doce da vovo
		this.doceDaVovo = Cesta.piorDoce( docesDisponiveis );
		docesDisponiveis.remove(doceDaVovo);
		
		// Cria lista combinando as possiveis N-uplas de encontros
		ArrayList<Encontro> EncontrosParticipantes = new ArrayList<Encontro>();
		ArrayList<ArrayList<ArrayList<Encontro>>> uplas = new ArrayList<ArrayList<ArrayList<Encontro>>>();
		for( int i = 0 ; i < qtd ; i ++ ) {
			uplas.add( new ArrayList<ArrayList<Encontro>>() );
			EncontrosParticipantes.add( this.encontros.get(i) );
		}	
		this.AlgoritmoGuloso1_aux1_combinacao( uplas , EncontrosParticipantes , new ArrayList<Encontro>() , 0 );
		
			
		// Distribui todos disponiveis para todos os encontros
		this.AlgoritmoGuloso1_aux2_distribuicao( uplas.get(qtd-1).get(0) , docesDisponiveis );
		
		// Testa redistribuições para encontros em duplas, triplas, quadruplas ... n-uplas
		for( int i = uplas.size() - 2 ; i >= 0 ; i-- ) {
			for( int j = 1 ; j < uplas.get(i).size() ; j++ ) {
				this.AlgoritmoGuloso1_aux2_distribuicao( uplas.get(i).get(j) , new ArrayList<Doce>() );
				//AlgoritmoGuloso1_aux3_trocas( uplas.get(qtd-1).get(0) );	
			}
		}	
	}
	
	private void AlgoritmoGuloso1_aux1_combinacao( ArrayList<ArrayList<ArrayList<Encontro>>> listaEncontros , ArrayList<Encontro> encontrosOriginais ,  ArrayList<Encontro> ultimaLista , int pos ) {
		if( pos >= encontrosOriginais.size() ) {
			return;
		}
		
		for( int i = pos ; i < encontrosOriginais.size() ; i ++ ) {
			ArrayList<Encontro> novaLista = new ArrayList<Encontro>(ultimaLista);
			novaLista.add( encontrosOriginais.get(i) );
			listaEncontros.get( novaLista.size()-1 ).add( novaLista );
			
			this.AlgoritmoGuloso1_aux1_combinacao( listaEncontros , encontrosOriginais , novaLista , (i+1) );
		}
		
	}	
	
	private void AlgoritmoGuloso1_aux2_distribuicao( ArrayList<Encontro> encontros , ArrayList<Doce> docesDisponiveis ) {
		// Armazena estado atual
		double custoTotalOriginal = 0;
		double custoTotalFinal = 0;
		
		ArrayList<Encontro> encontrosTemporarios = new ArrayList<Encontro>();
		
		for( Encontro enc : encontros ) {
			custoTotalOriginal += enc.calcularCustoEncontro(null);		
			encontrosTemporarios.add( new Encontro( enc.custoOriginal ) );
			
			for( Doce doc : enc.doces ) {
				docesDisponiveis.add(doc);
			}
		}
		
		ArrayList<Encontro> encontrosAReceberDoce = new ArrayList<Encontro>(encontrosTemporarios);
		
		// Variaveis locais
		Encontro encontroGanhador;
		Doce doceGanhador;
		double diferencaGanhadora;
		
		// Distribui doces minimos - minimiza diferenças		
		while( encontrosAReceberDoce.size() > 0 ) {
			doceGanhador = Cesta.melhorDoce(docesDisponiveis);
			
			encontroGanhador = encontrosAReceberDoce.get(0);
			diferencaGanhadora = encontroGanhador.simularDiferenca( doceGanhador );	
			
			for( Encontro enc : encontrosAReceberDoce ) {
				double tempDiff = enc.simularDiferenca( doceGanhador );
				if( tempDiff > diferencaGanhadora ) {
					encontroGanhador = enc;
					diferencaGanhadora = tempDiff;
				}
			}
			
			encontroGanhador.adicionarDoce( doceGanhador );
			encontrosAReceberDoce.remove( encontroGanhador );
			docesDisponiveis.remove( doceGanhador );
		}
		
		// Distribui doces - maximiza diferenças		
		ArrayList <Doce> docesQueFaltam = new ArrayList<Doce>();  
		ArrayList <Encontro> encontrosARemover = new ArrayList<Encontro>();  
		
		encontrosAReceberDoce = new ArrayList<Encontro>(encontrosTemporarios);
		
		while( encontrosAReceberDoce.size() > 0 && docesDisponiveis.size() > 0 ) {
			encontroGanhador = null;
			doceGanhador = null;
			diferencaGanhadora = 0;
			encontrosARemover.clear();
			
			doceGanhador = Cesta.melhorDoce( docesDisponiveis );

			for( Encontro enc : encontrosAReceberDoce ) {
				
				docesQueFaltam = enc.docesQueFaltam( docesDisponiveis );			
				
				if( docesQueFaltam.size() <= 0 ) {
					// Tira 
					encontrosARemover.add( encontroGanhador );
				} else if( docesQueFaltam.contains( doceGanhador ) ) {	
					// Calcula o ganho de dar o melhor doce possivel para este encontro
					//double tempDiff =  enc.custoOriginal - enc.calcularCustoEncontro( doceGanhador );
					double tempDiff = enc.simularDiferenca( doceGanhador );
					
					if( encontroGanhador == null || tempDiff > diferencaGanhadora  ) {
						encontroGanhador = enc;
						diferencaGanhadora = tempDiff;
					}
				}
			}
			
			// Remove os encontros que nao podem mais receber doce algum
			encontrosAReceberDoce.removeAll( encontrosARemover );
			
			// Checa se a iteracao seguinte é possivel
			if( encontroGanhador == null ) {
				break;
			}			
			
			//System.out.println("Encontro: " + this.encontros.lastIndexOf( encontroGanhador ) + " recebeu doce: " + doceGanhador.tipo + " | " + doceGanhador.fator );
			
			// Atualiza as listas
			encontroGanhador.adicionarDoce( doceGanhador );
			docesDisponiveis.remove( doceGanhador );
		}
		
		// Permutações entre as soluções
		AlgoritmoGuloso1_aux3_trocas( encontrosTemporarios );	
		
		// Checa se a solução nova é melhor do que a anterior
		for( Encontro enc : encontrosTemporarios ) {
			custoTotalFinal += enc.calcularCustoEncontro(null);		
		}
		if( custoTotalOriginal > custoTotalFinal ) {
			// Solução melhor, substitui encontros originais
			for( Encontro encVelho : encontros ) {
				for( Encontro encNovo : encontrosTemporarios ) {
					if( encVelho.custoOriginal == encNovo.custoOriginal ) {
						encVelho.doces = encNovo.doces;
					}
				}
			}
		}
	}

	private void AlgoritmoGuloso1_aux3_trocas( ArrayList<Encontro> listaEncontros ) {
		// Permutações entre as soluções
		
		Encontro encontroTrocaA;
		Encontro encontroTrocaB;
		Doce doceTrocaA;
		Doce doceTrocaB;
		
		double custoOrigemOriginal;	
		double custoTestadoOriginal;
		
		double custoSomadoOriginal;
		double custoSomadoTroca;
		
		int haveFlag;
		
		for( int i = 0 ; i < 2 ; i++ ) {
			for( Encontro encOrigem : listaEncontros ) {
				custoOrigemOriginal = encOrigem.calcularCustoEncontro( null );
				
				ArrayList<Doce> listaDeDocesOriginais = new ArrayList<Doce>( encOrigem.doces );
				for( Doce docOrigem : listaDeDocesOriginais ) { 
					encontroTrocaA = null;
					encontroTrocaB = null;
					doceTrocaA = null;
					doceTrocaB = null;
					
					for( Encontro encTestado : listaEncontros ) {
						if( encOrigem == encTestado ) {
							continue;
						}
						custoTestadoOriginal = encTestado.calcularCustoEncontro( null );
						custoSomadoOriginal = custoOrigemOriginal + custoTestadoOriginal;
						
						haveFlag = 0;
						ArrayList<Doce> listaDeDocesTestados = new ArrayList<Doce>( encTestado.doces );
						
						for( Doce docTestado : listaDeDocesTestados ) {
							if( docTestado.tipo == docOrigem.tipo ) {
								haveFlag = 1;
							} else {		
								custoSomadoTroca = encOrigem.calcularCustoEncontro( docTestado , docOrigem );
								custoSomadoTroca += encTestado.calcularCustoEncontro( docOrigem , docTestado );
								
								if( custoSomadoOriginal > custoSomadoTroca ) {
									//System.out.println("ACHEI: " + custoSomadoOriginal + " | " + custoSomadoTroca );
									
									// Debug apenas: Evitar doces repetidos
									int haveFlag2 = 0;
									for( Doce dc : encOrigem.doces ) {
										if( dc.tipo == docTestado.tipo ) {
											haveFlag2 = 1;
										}
									}
									for( Doce dc : encTestado.doces ) {
										if( dc.tipo == docOrigem.tipo ) {
											haveFlag2 = 1;
										}
									}
										
									// Troca efetiva
									if( haveFlag2 == 0 ) {
										encontroTrocaA = encOrigem;
										encontroTrocaB = encTestado;
										
										doceTrocaA = docOrigem;
										doceTrocaB = docTestado;
									}
								}
							}
						}
						
						// Testa doando o doce ( sem trocar por nada )
						if( haveFlag == 0 ) {
							custoSomadoTroca = encOrigem.calcularCustoEncontro( null , docOrigem );
							custoSomadoTroca += encTestado.calcularCustoEncontro( docOrigem , null );
							
							if( custoSomadoOriginal > custoSomadoTroca ) {
								//System.out.println("ACHEI: " + custoSomadoOriginal + " | " + custoSomadoTroca );
								encontroTrocaA = encOrigem;
								encontroTrocaB = encTestado;
								
								doceTrocaA = docOrigem;
								doceTrocaB = null;
							}
						}
					}
					
					// Faz a troca de doces
					if( encontroTrocaB != null ) {
						//System.out.println("ACHEI: " + encontroTrocaA.custoOriginal + ">" + doceTrocaA.fator + " | " + encontroTrocaB.custoOriginal + ">" + doceTrocaB.fator );
						encontroTrocaA.doces.remove(doceTrocaA);
						encontroTrocaB.doces.add(doceTrocaA);
						
						if( doceTrocaB != null ) {
							encontroTrocaB.doces.remove(doceTrocaB);
							encontroTrocaA.doces.add(doceTrocaB);
						}
					}
				}
			}
		}
	}
	
	private void AlgoritmoForcaBruta1(int qtd) {
		// Lucas, desenvolve o teu força bruta no AlgoritmoForcaBruta2
		// Esse meu desenvolvimento será experimental
		
		// Prepara lista de doces disponiveis
		ArrayList<Doce> docesDisponiveis = new ArrayList<Doce>();
		for( PunhadoDeDoce punDoces : this.cesta.inventarioOriginal ) {
			int j;
			for( j = 0 ; j < punDoces.quantidade ; j++) {
				docesDisponiveis.add( punDoces );
			}
		}
		
		// Separa o doce da vovo
		this.doceDaVovo = Cesta.piorDoce( docesDisponiveis );
		docesDisponiveis.remove(doceDaVovo);
		
		// Combinação A: Possiveis doces que um encontro pode ter
		// Estado: FINALIZADO e CHECADO
		ArrayList<ArrayList<Doce>> combDocesPorEncontro = new ArrayList<ArrayList<Doce>>();
		AlgoritmoForcaBruta1_Aux1( combDocesPorEncontro , this.cesta.inventarioOriginal , new ArrayList<Doce>() , 0 );
		
		// Combinação B: Define como as combinações A podem ficar distribuidas em todos os encontros
		// Neste momento, a ordem do encontro ainda não faz diferença
		// Estado: EM ANDAMENTO
		
		// Combinação C: Define como as combinações B podem ficar distribuidas considerando a ordem dos encontros
		// Estado: EM ANDAMENTO
		
		/*
		// DEBUG: Imprimir essa bosta
		for( ArrayList<Doce> ListaDoce : combDocesPorEncontro ) {
			System.out.print("\n");
			for( Doce doc : ListaDoce ) {
				System.out.print( doc.tipo + " | " );
			}
		}
		System.out.print("\n");
		*/
		
	}
	
	private void AlgoritmoForcaBruta1_Aux1( ArrayList<ArrayList<Doce>> listaDoces , ArrayList<PunhadoDeDoce> inventario ,  ArrayList<Doce> ultimaLista , int pos ) {
		if( pos >= inventario.size() ) {
			return;
		}
		
		for( int i = pos ; i < inventario.size() ; i ++ ) {
			ArrayList<Doce> novaLista = new ArrayList<Doce>(ultimaLista);
			novaLista.add( inventario.get(i) );
			listaDoces.add( novaLista );
			
			this.AlgoritmoForcaBruta1_Aux1( listaDoces , inventario , novaLista , (i+1) );
		}
		
	}
	
	private void AlgoritmoForcaBruta2(int qtd) {
		// Algoritmo de força bruta do Lucas
	}
	
	public double ObterCustoEncontro( int numeroDoEncontro ) {
		return this.encontros.get( numeroDoEncontro - 1 ).calcularCustoEncontro(null);
	}
	
	public void ImprimirEncontros() {
		int count = 0;
		double custoTotal = 0;
		double custoTotalOriginal = 0;
		double custoTemp = 0;
		
		System.out.println("Encontros: ");
		
		Encontro enc;
		
		for( int i = 0 ; i < encontrosCalculados ; i++ )  {
			enc = this.encontros.get(i);
		
			custoTemp = enc.calcularCustoEncontro(null);
			custoTotal += custoTemp;
			custoTotalOriginal += enc.custoOriginal;
			System.out.print( "\n\nEncontro: " + count + "\tCusto: " + custoTemp + " / " + enc.custoOriginal + "\nDoces:\t");
			for( Doce doc : enc.doces ) {
				
				System.out.printf( "%d(%.1f) | " , doc.tipo , doc.fator );
			}
			count++;
		}
		System.out.println("\nCusto Total: " + custoTotal + " / " + custoTotalOriginal);
		System.out.println("\n\n");
	}
}
	