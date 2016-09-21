import java.util.ArrayList;

public class jogo {

	public static void main(String[] args) {
		System.out.println( "DEBUG: START" );
		
		// TODO Auto-generated method stub
		Mapa map = new Mapa();
		Encontros enc = new Encontros();
		
		enc.CalcularEncontros(10);
		
		System.out.println( "Custo do Encontro 1: " + enc.ObterCustoEncontro(1) +"\n\n" );
		
		enc.ImprimirEncontros();
	
	}
}
