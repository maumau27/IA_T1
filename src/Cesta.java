import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Cesta{
	
	private class Doce {
		public String 	nome;
		public int 		quantidadeTotal;
		public int 		quantidadeAtual;
		public float 	fator;
		
		public Doce( String nome , int quantidadeTotal , float fator ) {
			this.nome = nome;
			this.quantidadeTotal = quantidadeTotal;
			this.quantidadeAtual = quantidadeTotal;
			this.fator = fator;
		}
	}
	
	ArrayList<Doce> doces = new ArrayList<Doce>();
	
	
	public Cesta() {
		this("cesta.txt");
	}
	
	public Cesta(String arquivo) {
		 CarregarArquivo(arquivo);
	}
	
	public void CarregarArquivo(String arquivo) {
	    Scanner file;
	    file = null;
	    
	    try {
	        file = new Scanner(Paths.get(arquivo));
	    } catch (FileNotFoundException ex) {
	    	System.out.print("Arquivo "+arquivo+" Nao Encontrado!\n");
	        //Logger.getLogger(Skills.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (IOException e) {
	    	System.out.println("Abertura de "+arquivo+" falhou!");
			e.printStackTrace();
		}
	    
	    // Limpando ambiente
	    doces.clear();
	    
	    String elementos[];
	    char chr;
	    int x = 0 , y = 0;
	    while( file.hasNext() ) {
	    	elementos = file.nextLine().split(";");
	    	doces.add( new Doce( elementos[2] , Integer.parseInt(elementos[0]) , Float.parseFloat(elementos[1]) ) );
	    }
	    
	    file.close();
	}
	
	public void DefinirEncontros( int qtd ) {
		
	}
	
	public float ObterCustoEncontro( int numeroDoEncontro ) {
		return 10;
	}
}
