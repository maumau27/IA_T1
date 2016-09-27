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
	
	public double calcularCustoEncontro( Doce doc ) {
		double fator = 0;
		
		if( doc != null ) {
			fator = doc.fator;
		} 
		
		for( Doce doceEncontro : this.doces ) {
			fator = fator + doceEncontro.fator;
		}
		
		if( fator <= 0.01 ) {
			fator = 1;
		}
		
		return ( this.custoOriginal / fator );
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
	private ArrayList<Doce> doces = new ArrayList<Doce>();
	private Encontros encontrosCalculados[];
	private ArrayList<Double> encontrosCustosOriginais = new ArrayList<Double>();

	
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
				
		// Variaveis dos proximos passos
		ArrayList<Encontro> encontrosAReceberDoce;
		Encontro encontroGanhador; 
		double diferencaGanhadora;
		Doce doceGanhador;
		
		// Distribui doces minimos - minimiza diferenças
		
		encontrosAReceberDoce = new ArrayList<Encontro>();
		
		for( int i = 0 ; i < qtd ; i++ ) {
			encontrosAReceberDoce.add( this.encontros.get(i) );
		}
		
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
		
		//this.ImprimirEncontros();
		
		// Distribui doces - maximiza diferenças
		encontrosAReceberDoce = new ArrayList<Encontro>();
		
		for( int i = 0 ; i < qtd ; i++ ) {
			encontrosAReceberDoce.add( this.encontros.get(i) );
		}
		
		ArrayList <Doce> docesQueFaltam = new ArrayList<Doce>();  
		ArrayList <Encontro> encontrosARemover = new ArrayList<Encontro>();  
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
		
		for( Encontro enc : this.encontros ) {
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
	