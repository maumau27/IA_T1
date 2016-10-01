import java.util.Comparator;
public class Corrente_Celula{
	
	private int clareiras_passadas;
	private Celula celula_atual;
	private double custo_acumulado;//custo do caminho da cedula inicial ate a atual
	private double custo_total;//custo acumulado + heristica(Geometria do táxi)
	private Corrente_Celula celula_pai;
	public boolean flag_folha;
	
	public Corrente_Celula(Celula celula_atual, Corrente_Celula celula_pai, double heuristica, Encontros encontro){
		this.flag_folha = true;
		this.celula_atual = celula_atual;
		this.celula_pai = celula_pai;
		this.clareiras_passadas = this.celula_pai.ObterClareirasPassadas();
		if(celula_atual.custo < 0)
			this.clareiras_passadas++;
		if(celula_atual.custo != -1)
			this.custo_acumulado = celula_pai.custo_acumulado + celula_atual.custo;
		else
			this.custo_acumulado = celula_pai.custo_acumulado + encontro.ObterCustoEncontro(this.clareiras_passadas);
		this.custo_total = this.custo_acumulado + heuristica;
	}
	
	public Corrente_Celula(Celula celula_atual, double heuristica){
		this.celula_atual = celula_atual;
		this.celula_pai = null;
		this.custo_acumulado = 0;
		this.clareiras_passadas = 0;
		this.custo_total = this.custo_acumulado + heuristica;
		this.flag_folha = true;
	}
	
	public Corrente_Celula ObterPai(){
		return this.celula_pai;
	}
	
	public Celula ObterCelula(){
		return this.celula_atual;
	}
	
	public double ObterCustoAcumulado(){
		return this.custo_acumulado;
	}
	
	public double ObterCustoTotal(){
		return this.custo_total;
	}
	
	public boolean HeFolha(){
		return this.flag_folha;
	}
	
	public void TornarFolha(){
		this.flag_folha = true;
	}
	
	public void DesTornarFolha(){
		this.flag_folha = false;
	}
	
	public int ObterClareirasPassadas(){
		return this.clareiras_passadas;
	}
	
	public static Comparator<Corrente_Celula> Comparar_Custo = new Comparator<Corrente_Celula>() {
		public int compare(Corrente_Celula c1, Corrente_Celula c2){
			double custo_c1 = c1.ObterCustoTotal();
			double custo_c2 = c2.ObterCustoTotal();
			
			return (int)(custo_c1-custo_c2);
		}
	};
} 