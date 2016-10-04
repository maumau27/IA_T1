import java.awt.geom.Line2D;
import java.util.ArrayList;

public class jogo {
	
	public static Mapa map; 
	public static Encontros enc;
	public static AeController a;

	static Corrente_Celula corrente_celula;
	static Tela tela;;
	static ArrayList<Ponto> lst = new ArrayList<>();
	static ArrayList<ArrayList<Ponto>> lst2 = new ArrayList<>();	
	static ArrayList<ArrayList<Line2D.Double>> arrayLinhas = new ArrayList<>();
	static int i = 0;
	
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
	
	public static void iniciaAnimacao() {
		tela = new Tela();
		corrente_celula = a.ObterCelulaAtual();
		recursao(corrente_celula);
		
		ArrayList<Corrente_Celula> z = a.ObterCaminhoPlanejado();
		for (Corrente_Celula cel : z){
			ArrayList<Ponto> lst3 = new ArrayList<Ponto>();
			lst2.add(lst3);
			recursao2(cel, lst3);
		}
		criaLinhas();
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
	
	static void recursao(Corrente_Celula corrente_celula){
		if(corrente_celula.ObterPai() == null){
			lst.add(new Ponto(corrente_celula.ObterCelula().x, corrente_celula.ObterCelula().y));
			return;
		}
		recursao(corrente_celula.ObterPai());
		lst.add(new Ponto(corrente_celula.ObterCelula().x, corrente_celula.ObterCelula().y));
	}
	
	static void recursao2(Corrente_Celula corrente_celula, ArrayList<Ponto> lst3){
		if(corrente_celula.ObterPai() == null){
			lst3.add(new Ponto(corrente_celula.ObterCelula().x, corrente_celula.ObterCelula().y));
			return;
		}
		recursao2(corrente_celula.ObterPai(), lst3);
		lst3.add(new Ponto(corrente_celula.ObterCelula().x, corrente_celula.ObterCelula().y));
	}
	
	static void criaLinhas(){
		for(ArrayList<Ponto> Ap : lst2){
			ArrayList<Line2D.Double> linhas = new ArrayList<>();
			arrayLinhas.add(linhas);
			for(int i = 0; i + 1 < Ap.size(); i++){
				linhas.add(new Line2D.Double((Ap.get(i).x*23) + 12, (Ap.get(i).y*23)+12, (Ap.get(i+1).x*23)+12, (Ap.get(i+1).y*23)+12));
			}
		}
	}
	
	static void setaProx(){
		
		if(i == -1)
			return;
		
		if(lst.size() == 0)
			return;
		
		Ponto a = lst.get(i);
		Ponto b = lst.get(i+1);
		
		if(a.x > b.x)
			InterfaceGrafica.getInterfaceGrafica().horizontal(-1);
		else if (a.x < b.x)
			InterfaceGrafica.getInterfaceGrafica().horizontal(1);
		else if (a.y > b.y)
			InterfaceGrafica.getInterfaceGrafica().vertical(-1);
		else if (a.y < b.y)
			InterfaceGrafica.getInterfaceGrafica().vertical(1);
		i++;
		
		if(b.x == map.ObterFim().x && b.y == map.ObterFim().y)
			i = -1;
	}
	
	static void printaLinhas(){
		InterfaceGrafica.lst = arrayLinhas;
		InterfaceGrafica.getInterfaceGrafica().linhas = true;
	}
}
