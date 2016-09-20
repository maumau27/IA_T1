import java.io.*;
import java.nio.file.Paths;
import java.util.*;


public class Mapa {
	private ArrayList<ArrayList<Celula>> celulas = new ArrayList<ArrayList<Celula>>(); 
	private Celula inicio = null;
	private Celula fim = null;
	private int[] dimensoes = new int[2];
	
	
	public Mapa(String arquivo) {
		 CarregarArquivo(arquivo);
	}
	
	public Mapa() {
		this("mapa.txt");
	}
	
	public void CarregarArquivo(String arquivo) {
	    Scanner file;
	    file = null;
	    
	    try {
	        file = new Scanner(Paths.get(arquivo));
	    } catch (FileNotFoundException ex) {
	    	System.out.println("Arquivo "+arquivo+" Nao Encontrado!");
	        //Logger.getLogger(Skills.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (IOException e) {
	    	System.out.println("Abertura de "+arquivo+" falhou!");
			e.printStackTrace();
		}
	    
	    // Limpando lista
	    dimensoes[0] = 0;
	    dimensoes[1] = 0;
	    inicio = null;
	    fim = null;	    
	    celulas.clear();

	    String line;
	    char chr;
	    int x = 0 , y = 0;
	    while( file.hasNext() ) {
	    	celulas.add(new ArrayList<Celula>());
	    	y = celulas.size() - 1;
	    	
	    	line = file.nextLine();
	        for ( x = 0; x < line.length(); x++) {
	        	chr = line.charAt(x);

	        	
	        	if ( chr == '.') {
	        		celulas.get(y).add( new Celula(x,y,1,1) );
	        	} else if ( chr == 'G') {
	        		celulas.get(y).add( new Celula(x,y,2,5) );
	        	} else if ( chr == 'D') {
	        		celulas.get(y).add( new Celula(x,y,3,200) );
	        	} else if ( chr == 'C') {
	        		celulas.get(y).add( new Celula(x,y,4,-1) );
	        	} else if ( chr == 'I') {
	        		celulas.get(y).add( new Celula(x,y,5,0) );
	        		inicio = new Celula(x,y,5,0);
	        	} else if ( chr == 'F') {
	        		celulas.get(y).add( new Celula(x,y,6,0) );
	        		fim = new Celula(x,y,5,0);
	        	} else {
	        		// Todo: Throw
	        	}	
	        	//System.out.print( celulas.get(y).get(x).terreno );
	        }
	        dimensoes[0] = x + 1;
	        // System.out.print("\n");     
	    }	   
	    dimensoes[1] = y + 1;
	    file.close();   
	}
	
	public boolean PertenceAoMapa( int x , int y ) {
		if ( ((x+1) > dimensoes[0]) || ((y+1) > dimensoes[1]) || x < 0 || y < 0 ) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean PertenceAoMapa( Celula cel ) {
		if( ObterCelula( cel.x , cel.y ) == cel ) {
			return true;
		} else {
			return false;
		}
	}	
	
	public Celula ObterCelula( int x , int y ) {
		if( this.PertenceAoMapa( x , y ) ) {
			return  ObterCelula( x,y );
		} else {
			return null;
		}
	}

	public int ObterTerreno( int x , int y ) {
		if( this.PertenceAoMapa( x , y ) ) {
			return celulas.get(y).get(x).terreno;
		} else {
			return -1;
		}
	}

	public float ObterTempo( int x , int y ) {
		if( this.PertenceAoMapa( x , y ) ) {
			return celulas.get(y).get(x).custo;
		} else {
			return -1;
		}
	}	
	
	public Celula ObterInicio() {
		return  this.inicio;
	}
	
	public Celula ObterFim() {
		return  this.fim;
	}	
	
	public int[] ObterTamanho() {
		return dimensoes;
	}
	
	public ArrayList<Celula> ObterVizinhos( Celula cel ) {
		ArrayList<Celula> vizinhos = new ArrayList<Celula>();
		Celula tempCel;
		
		tempCel = ObterCelula(cel.x + 1,cel.y);
		if( tempCel != null ) {
			vizinhos.add( tempCel );
		}
		
		tempCel = ObterCelula(cel.x - 1,cel.y);
		if( tempCel != null ) {
			vizinhos.add( tempCel );
		}	
		
		tempCel = ObterCelula(cel.x ,cel.y + 1);
		if( tempCel != null ) {
			vizinhos.add( tempCel );
		}	
		
		tempCel = ObterCelula(cel.x ,cel.y - 1);
		if( tempCel != null ) {
			vizinhos.add( tempCel );
		}			
		
		return vizinhos;
	}
	
}
