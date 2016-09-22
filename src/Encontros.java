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
				this.AlgoritmoGuloso( qtd );
				break;
			default:
				break;
		}
	}
	
	private void AlgoritmoGuloso( int qtd ) {	
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
			doceGanhador = Cesta.piorDoce(docesDisponiveis);
			
			encontroGanhador = encontrosAReceberDoce.get(0);
			diferencaGanhadora = encontroGanhador.simularDiferenca( doceGanhador );

			for( Encontro enc : encontrosAReceberDoce ) {
				double tempDiff = enc.simularDiferenca( doceGanhador);
				if( tempDiff < diferencaGanhadora ) {
					encontroGanhador = enc;
					diferencaGanhadora = tempDiff;
				}
			}
			
			encontroGanhador.adicionarDoce( doceGanhador );
			encontrosAReceberDoce.remove( encontroGanhador );
			docesDisponiveis.remove( doceGanhador );
		}
		
		// Distribui doces - maximiza diferenças
		encontrosAReceberDoce = new ArrayList<Encontro>();
		
		for( int i = 0 ; i < qtd ; i++ ) {
			encontrosAReceberDoce.add( this.encontros.get(i) );
		}
		
		ArrayList <Doce> docesQueFaltam = new ArrayList<Doce>();  
		ArrayList <Encontro> encontrosARemover = new ArrayList<Encontro>();  
		while( encontrosAReceberDoce.size() > 0 ) {
			encontroGanhador = null;
			doceGanhador = null;
			diferencaGanhadora = 0;
			encontrosARemover.clear();

			for( Encontro enc : encontrosAReceberDoce ) {
				docesQueFaltam = enc.docesQueFaltam( docesDisponiveis );

				if( docesQueFaltam.size() <= 0 ) {
					// Tira 
					encontrosARemover.add( encontroGanhador );
					//encontrosAReceberDoce.remove( encontroGanhador );	
				} else {
					// Calcula o ganho de dar o melhor doce possivel para este encontro
					doceGanhador = Cesta.melhorDoce(docesQueFaltam);
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
			
			// Atualiza as listas
			encontroGanhador.adicionarDoce( doceGanhador );
			docesDisponiveis.remove( doceGanhador );
		}
	}
	
	private void AlgoritmoForcaBruta1(int qtd) {
		// Lucas, desenvolve o teu força bruta no AlgoritmoForcaBruta2
		// Esse meu desenvolvimento será experimental
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
				System.out.print( doc.tipo + " | ");
			}
			count++;
		}
		System.out.println("\nCusto Total: " + custoTotal + " / " + custoTotalOriginal);
		System.out.println("\n\n");
	}
}
	