import java.awt.datatransfer.FlavorTable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class A_estrela {
	private class Corrente_Celula{
		public Celula celula_atual;
		public float custo_acumulado;//custo do caminho da cedula inicial ate a atual
		public float custo_total;//custo acumulado + heristica(Geometria do táxi)
		public Corrente_Celula celula_pai;
		
		public Corrente_Celula(Celula celula_atual, Corrente_Celula celula_pai){
			this.celula_atual = celula_atual;
			this.celula_pai = celula_pai;
			this.custo_acumulado = celula_pai.custo_acumulado + celula_atual.ObterCusto();
			this.custo_total = this.custo_acumulado + Heuristica(this.celula_atual);
		}
		
		public Corrente_Celula(Celula celula_atual){
			this.celula_atual = celula_atual;
			this.celula_pai = null;
			this.custo_acumulado = 0;
			this.custo_total = this.custo_acumulado + Heuristica(this.celula_atual);
		}
	}
	
	private int clareiras_esperadas;
	private int clareiras_passadas;
	private Celula ponto_inicial;
	private Celula ponto_final;
	private Mapa floresta;
	private Corrente_Celula celula_atual;
	private ArrayList<Corrente_Celula> celulas_planejadas;
	private ArrayList<Corrente_Celula> caminho;

	public A_estrela(int clareiras_esperadas, Celula ponto_inicial, Celula ponto_final, Mapa floresta){
		this.clareiras_esperadas = clareiras_esperadas;
		this.clareiras_passadas = 0;
		this.ponto_inicial = ponto_inicial;
		this.ponto_final = ponto_final;
		this.floresta = floresta;
		this.celula_atual = null;
		this.celulas_planejadas = new ArrayList<Corrente_Celula>();
		this.celulas_planejadas.add(new Corrente_Celula(ponto_inicial));
	}
	
	public void Iterar(){
		
		ArrayList<Celula> celulas_vizinhas = this.floresta.ObterVizinhos(ponto_inicial);
		
		for (Celula celula : celulas_vizinhas) {
			this.celulas_planejadas.add(new Corrente_Celula(celula, this.celula_atual));
		}
	}
	
	private float Heuristica(Celula casa_inicial) {
		return (casa_inicial.x - this.ponto_final.x) + (casa_inicial.y - this.ponto_final.y);
	}
}
