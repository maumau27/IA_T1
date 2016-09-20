import java.awt.datatransfer.FlavorTable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class A_estrela{
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
		
		public Corrente_Celula ObterPai(){
			return this.celula_pai;
		}
		
		public Celula ObterCelula(){
			return this.celula_atual;
		}
		
		public float ObterCustoAcumulado(){
			return this.custo_acumulado;
		}
		
		public float ObterCustoTotal(){
			return this.custo_total;
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
	
	private void Iterar(){
		this.celula_atual = this.celulas_planejadas.get(0);
		this.celulas_planejadas.remove(this.celula_atual);
		this.caminho.add(this.celula_atual);
		
		if(this.celula_atual.ObterCelula().custo == -1)
			this.clareiras_passadas++;

		ArrayList<Celula> celulas_vizinhas = this.floresta.ObterVizinhos(this.celula_atual.ObterCelula());
		
		for (Celula celula : celulas_vizinhas) {
			this.celulas_planejadas.add(new Corrente_Celula(celula, this.celula_atual));
		}
		
		//sort celulas_planejadas
	}
	
	private void DesIterar(){
		ArrayList<Celula> celulas_vizinhas = this.floresta.ObterVizinhos(this.celula_atual.ObterCelula());
		
		for (Corrente_Celula elemento : this.celulas_planejadas) {
			if(celulas_vizinhas.contains(elemento.ObterCelula()) && elemento.ObterPai() == this.celula_atual){
				this.celulas_planejadas.remove(elemento);
			}
		}
		
		this.celulas_planejadas.add(this.celula_atual);
		this.caminho.remove(this.celula_atual);
		
		this.celula_atual = this.celula_atual.ObterPai();
		
		//sort celulas_planejadas
	}
	
	public void DarUmPasso(){
		this.Iterar();
	}
	
	public void VoltarUmPasso(){
		this.DesIterar();
	}
	
	public Corrente_Celula ObterCelulaAtual(){
		return this.celula_atual;
	}
	
	public ArrayList<Corrente_Celula> ObterCaminho(){
		return this.caminho;
	}
	
	public ArrayList<Corrente_Celula> ObterCaminhoPlanejado(){
		return this.celulas_planejadas;
	}
	
	public float Heuristica(Celula casa_inicial) {
		return (casa_inicial.x - this.ponto_final.x) + (casa_inicial.y - this.ponto_final.y);
	}
}
