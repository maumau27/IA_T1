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
		
		enc.ImprimirEncontros();
		
		// Da um quantidade definida de passos
		for( int i = 0 ; i < 50 ; i ++ ) {
			a.DarPasso(1);
			//a.VoltarPasso();
			System.out.println("Celula atual : (" + a.ObterCelulaAtual().ObterCelula().x + "," + a.ObterCelulaAtual().ObterCelula().y + ")" );
		}
		
		// Roda ate o final
		while( a.DarPasso(1) != true ) {
			System.out.println("Celula atual : (" + a.ObterCelulaAtual().ObterCelula().x + "," + a.ObterCelulaAtual().ObterCelula().y + ")" );
		}
		
	}
}
