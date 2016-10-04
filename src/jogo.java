import java.util.ArrayList;

public class jogo {
	
	public static Mapa map; 
	public static Encontros enc;
	public static AeController a;

	public static void main(String[] args) {
		System.out.println( "DEBUG: START" );
		
		jogo.incializacao();
		jogo.editorDeMapas();
		//jogo.iniciaAlgoritmo();
		//jogo.rodaAlgoritmo();
	}

	public static void incializacao() {
		// TODO Auto-generated method stub
		map = new Mapa();
	}

	public static void editorDeMapas() {
		EditorDeMapas.iniciar(map);
	}
	
	public static void iniciaAlgoritmo() {
		enc = new Encontros();
		a = new AeController(map);
	}
	
	public static void rodaAlgoritmo() {
		// Da um quantidade definida de passos
		for( int i = 0 ; i < 50 ; i ++ ) {
			a.DarPasso( 1 , true );
			//a.VoltarPasso();
			//System.out.println("Celula atual : (" + a.ObterCelulaAtual().ObterCelula().x + "," + a.ObterCelulaAtual().ObterCelula().y + ")" );
		}
		
		// Roda ate o final
		while( a.ObterUltimoEstado() != EstadoDeParada.CHEGOU_MELHORCASO ) {
			a.DarPasso( 1 , true );
			
			while( a.ObterUltimoEstado() == EstadoDeParada.NAOCHEGOU ) {
				a.DarPasso( 1 , true );
				//System.out.println("Celula atual : (" + a.ObterCelulaAtual().ObterCelula().x + "," + a.ObterCelulaAtual().ObterCelula().y + ")" );
			}
			
			// Verifica custo total
			System.out.println("\nRELATORIO\n");
			System.out.println("\tCusto Total:\t\t" + a.ObterCustoTotal() );
			System.out.println("\tEncontros:\t\t" + a.ObterCelulaAtual().ObterClareirasPassadas() + " / " + a.ObterEncontrosEsperados() );
			System.out.println("\tCusto do Caminho:\t" + a.ObterCustoCaminho() );
			System.out.println("\tCusto dos Encontros:\t" + a.ObterCustoEncontros() );
			a.ImprimirEncontros(false , "\t");
			System.out.print("\n");
		}
	}
	
	public static void imprimirEncontros() {
		enc.ImprimirEncontros(true);
	}
}
