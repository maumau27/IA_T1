import java.util.ArrayList;

public class jogo {

	public static void main(String[] args) {
		System.out.println( "DEBUG: START" );
		
		// TODO Auto-generated method stub
		Mapa map = new Mapa();
		Encontros enc = new Encontros();
		AeController a = new AeController(map);
		
		enc.CalcularEncontros(10);
		
		//System.out.println( "Custo do Encontro 1: " + enc.ObterCustoEncontro(1) +"\n\n" );
		
		enc.ImprimirEncontros(true);
		
		// Da um quantidade definida de passos
		for( int i = 0 ; i < 50 ; i ++ ) {
			a.DarPasso( 1 , true );
			//a.VoltarPasso();
			//System.out.println("Celula atual : (" + a.ObterCelulaAtual().ObterCelula().x + "," + a.ObterCelulaAtual().ObterCelula().y + ")" );
		}
		
		// Roda ate o final
		while( a.ObterUltimoEstado() != EstadoDeParada.CHEGOU_MELHORCASO ) {
			while( a.ObterUltimoEstado() == EstadoDeParada.NAOCHEGOU ) {
				a.DarPasso( 1 , true );
				//System.out.println("Celula atual : (" + a.ObterCelulaAtual().ObterCelula().x + "," + a.ObterCelulaAtual().ObterCelula().y + ")" );
			}
			
			// Verifica custo total
			System.out.println("\nRELATORIO\n");
			System.out.println("\tCusto Total:\t\t" + a.ObterCustoTotal() );
			System.out.println("\tEncontros:\t\t" + a.ObterCelulaAtual().ObterClareirasPassadas() );
			System.out.println("\tCusto do Caminho:\t" + a.ObterCustoCaminho() );
			System.out.println("\tCusto dos Encontros:\t" + a.ObterCustoEncontros() );
			enc.ImprimirEncontros(false , "\t");
		}
	}
}
