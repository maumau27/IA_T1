import java.util.Comparator;
public class Corrente_Celula{
		public Celula celula_atual;
		public float custo_acumulado;//custo do caminho da cedula inicial ate a atual
		public float custo_total;//custo acumulado + heristica(Geometria do táxi)
		public Corrente_Celula celula_pai;
		
		public Corrente_Celula(Celula celula_atual, Corrente_Celula celula_pai, float heuristica){
			this.celula_atual = celula_atual;
			this.celula_pai = celula_pai;
			this.custo_acumulado = celula_pai.custo_acumulado + celula_atual.custo;
			this.custo_total = this.custo_acumulado + heuristica;
		}
		
		public Corrente_Celula(Celula celula_atual, float heuristica){
			this.celula_atual = celula_atual;
			this.celula_pai = null;
			this.custo_acumulado = 0;
			this.custo_total = this.custo_acumulado + heuristica;
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
		
		public static Comparator<Corrente_Celula> Comparar_Custo = new Comparator<Corrente_Celula>() {
			public int compare(Corrente_Celula c1, Corrente_Celula c2){
				float custo_c1 = c1.ObterCustoTotal();
				float custo_c2 = c2.ObterCustoTotal();
				
				return (int)(custo_c1-custo_c2);
			}
		};
	} 