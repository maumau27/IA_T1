import java.awt.datatransfer.FlavorTable;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;


public class A_estrela{

	private int clareiras_esperadas;
	private Celula ponto_inicial;
	private Celula ponto_final;
	private Mapa floresta;
	private Corrente_Celula ccelula_atual;
	private ArrayList<Corrente_Celula> ccelulas_planejadas;
	private ArrayList<Corrente_Celula> ccaminho;
	private Encontros encontros;

	public A_estrela(int clareiras_esperadas, Celula ponto_inicial, Celula ponto_final, Mapa floresta, Encontros encontros){
		this.clareiras_esperadas = clareiras_esperadas;
		this.ponto_inicial = ponto_inicial;
		this.ponto_final = ponto_final;
		this.floresta = floresta;
		this.ccelula_atual = null;
		this.ccelulas_planejadas = new ArrayList<Corrente_Celula>();
		this.ccaminho = new ArrayList<Corrente_Celula>();
		this.ccelulas_planejadas.add(new Corrente_Celula(ponto_inicial, Heuristica(ponto_inicial)));
		this.encontros = encontros;
		Iterar();
	}
	
	private Corrente_Celula ObterCelulaMaisProxima( ArrayList<Corrente_Celula> lista ) {
		Corrente_Celula melhorCelula = lista.get(0);
		double menorCusto = melhorCelula.ObterCustoTotal();
		
		for( Corrente_Celula cor : lista ) {
			if( cor.ObterCustoTotal() < menorCusto ) {
				menorCusto = cor.ObterCustoTotal();
				melhorCelula = cor;
			}
		}
		
		return melhorCelula;
	}
	
	private void Iterar(){
		this.ccelula_atual = this.ObterCelulaMaisProxima(ccelulas_planejadas);//da um passo
		this.ccelulas_planejadas.remove(ccelula_atual);//remove celula usada da lista de celulas planejadas
		this.ccaminho.add(this.ccelula_atual);//e coloca na de passadas
		if(this.ccelula_atual.ObterPai() != null)
			this.ccelula_atual.ObterPai().DesTornarFolha();//faz com que o pai da celula atual pare de ser folha

		ArrayList<Celula> celulas_vizinhas = this.floresta.ObterVizinhos(this.ccelula_atual.ObterCelula());//pega as casas vizinhas

		for (Celula celula : celulas_vizinhas) {//para cada casa vizinha
			int addFlag = 1;
			
			// Checa se a celula vizinha ja esta cadastrada na lista dos passados ( caminho ) 
			for( Corrente_Celula cor : ccaminho ) {
				if( cor.ObterCelula().x == celula.x && cor.ObterCelula().y == celula.y ) {
					addFlag = 0;
					//break;
				}
			}
			if( addFlag == 0 ) {
				continue;
			}			
			
			Corrente_Celula nova_celula = new Corrente_Celula(celula, this.ccelula_atual, Heuristica(celula), this.encontros);
			// Checa se a celula vizinha ja esta cadastrada na propria lista de celulas planejadas
			for( Corrente_Celula cor : ccelulas_planejadas ) {
				if( cor.ObterCelula().x == celula.x && cor.ObterCelula().y == celula.y ) {
					if(nova_celula.ObterCustoTotal() < cor.ObterCustoTotal()){
						this.ccelulas_planejadas.remove(cor);
						this.ccelulas_planejadas.add(nova_celula);
					}
					addFlag = 0;
					break;
				}
			}
			if( addFlag == 0 ) {
				continue;
			}
			
			this.ccelulas_planejadas.add(nova_celula);//adiciona na lista de celulas planejadas
		}
	}
	
	private void DesIterar(){
		ArrayList<Celula> celulas_vizinhas = this.floresta.ObterVizinhos(this.ccelula_atual.ObterCelula());//obtem os vizinhos
		
		for (Corrente_Celula elemento : this.ccelulas_planejadas) {//para cada celula planejada
			if(celulas_vizinhas.contains(elemento.ObterCelula()) && elemento.ObterPai() == this.ccelula_atual){//se ela estiver na lista de celulas vizinhas
				this.ccelulas_planejadas.remove(elemento);//remove a celula planejada
			}
		}
		
		this.ccelulas_planejadas.add(this.ccelula_atual);//coloca a celula atua nas planejadas
		this.ccaminho.remove(this.ccelula_atual);//remove do caminho
		
		this.ccelula_atual = this.ccelula_atual.ObterPai();//torna a celula atual seu pai
		this.ccelula_atual.TornarFolha();//torna a celula atual uma folha
		
		this.ccelulas_planejadas.sort(Corrente_Celula.Comparar_Custo);//organiza a lista de celula planejadas, para a melhor celula ficar na frente
	}
	
	public EstadoDeParada DarPasso(){
		this.Iterar();
		
		if(this.ccelula_atual.ObterCelula().x == this.ponto_final.x && this.ccelula_atual.ObterCelula().y == ponto_final.y){
			if( this.ccelula_atual.ObterClareirasPassadas() < this.clareiras_esperadas ) {
				return EstadoDeParada.CHEGOU_PODEMELHORAR;
			} else {
				return EstadoDeParada.CHEGOU_MELHORCASO;
			}
		}
		else{
			return EstadoDeParada.NAOCHEGOU;
		}
	}
	
	public int VoltarPasso(){
		if(this.ccelula_atual == null){
			return -1;
		}
		else if(this.ccelula_atual.ObterCelula().x == this.ponto_inicial.x && this.ccelula_atual.ObterCelula().y == ponto_inicial.y){
			return 1;
		}
		else{
			this.DesIterar();
			return 0;
		}
	}
	
	public Corrente_Celula ObterCelulaAtual(){
		return this.ccelula_atual;
	}
	
	public ArrayList<Corrente_Celula> ObterCaminho(){
		return this.ccaminho;
	}
	
	public int ObterClareirasPassadas(){
		return this.ccelula_atual.ObterClareirasPassadas();
	}
	
	public ArrayList<Corrente_Celula> ObterCaminhoPlanejado(){
		return this.ccelulas_planejadas;
	}
	
	public double Heuristica(Celula casa_inicial) {
		return Math.abs(casa_inicial.x - this.ponto_final.x) + Math.abs(casa_inicial.y - this.ponto_final.y);
	}
	
	public double ObterCustoTotal( ) {
		return this.ObterCelulaAtual().ObterCustoAcumulado();
	}
	
	public double ObterCustoEncontros( ) {
		double custoAcc = 0;
		
		for( int i = 0 ; i < this.ObterCelulaAtual().ObterClareirasPassadas() ; i++ ) {
			custoAcc += this.encontros.ObterCustoEncontro(i+1);
		}
		
		return custoAcc;
	}
	
	public double ObterCustoCaminho( ) {
		return ( this.ObterCustoTotal() - this.ObterCustoEncontros() );
	}
	
	public int ObterEncontrosEsperados() {
		return this.clareiras_esperadas;
	}
}
