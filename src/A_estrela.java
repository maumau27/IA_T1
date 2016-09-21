import java.awt.datatransfer.FlavorTable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class A_estrela{

	private int clareiras_esperadas;
	private int clareiras_passadas;
	private Celula ponto_inicial;
	private Celula ponto_final;
	private Mapa floresta;
	private Corrente_Celula ccelula_atual;
	private ArrayList<Corrente_Celula> ccelulas_planejadas;
	private ArrayList<Corrente_Celula> ccaminho;

	public A_estrela(int clareiras_esperadas, Celula ponto_inicial, Celula ponto_final, Mapa floresta){
		this.clareiras_esperadas = clareiras_esperadas;
		this.clareiras_passadas = 0;
		this.ponto_inicial = ponto_inicial;
		this.ponto_final = ponto_final;
		this.floresta = floresta;
		this.ccelula_atual = null;
		this.ccelulas_planejadas = new ArrayList<Corrente_Celula>();
		this.ccelulas_planejadas.add(new Corrente_Celula(ponto_inicial, Heuristica(ponto_inicial)));
	}
	
	private void Iterar(){
		this.ccelula_atual = this.ccelulas_planejadas.get(0);
		this.ccelulas_planejadas.remove(this.ccelula_atual);
		this.ccaminho.add(this.ccelula_atual);
		
		if(this.ccelula_atual.ObterCelula().custo == -1)
			this.clareiras_passadas++;

		ArrayList<Celula> celulas_vizinhas = this.floresta.ObterVizinhos(this.ccelula_atual.ObterCelula());
		
		for (Celula celula : celulas_vizinhas) {
			this.ccelulas_planejadas.add(new Corrente_Celula(celula, this.ccelula_atual, Heuristica(celula)));
		}
		
		//sort celulas_planejadas
		this.ccelulas_planejadas.sort(Corrente_Celula.Comparar_Custo);
	}
	
	private void DesIterar(){
		ArrayList<Celula> celulas_vizinhas = this.floresta.ObterVizinhos(this.ccelula_atual.ObterCelula());
		
		for (Corrente_Celula elemento : this.ccelulas_planejadas) {
			if(celulas_vizinhas.contains(elemento.ObterCelula()) && elemento.ObterPai() == this.ccelula_atual){
				this.ccelulas_planejadas.remove(elemento);
			}
		}
		
		this.ccelulas_planejadas.add(this.ccelula_atual);
		this.ccaminho.remove(this.ccelula_atual);
		
		this.ccelula_atual = this.ccelula_atual.ObterPai();
		
		//sort celulas_planejadas
		this.ccelulas_planejadas.sort(Corrente_Celula.Comparar_Custo);
	}
	
	public int DarPasso(){
		if(this.ccelula_atual.ObterCelula().x == this.ponto_final.x && this.ccelula_atual.ObterCelula().y == ponto_final.y){
			return 1;
		}
		else{
			this.Iterar();
			return 0;
		}
	}
	
	public int DarPasso(int n_passos){
		for (int i = 0; i < n_passos; i++) {	
			if(this.ccelula_atual.ObterCelula().x == this.ponto_final.x && this.ccelula_atual.ObterCelula().y == ponto_final.y){
				return 1;
			}
			else{
				this.Iterar();
				
			}
		}
		return 0;
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
	
	public int VoltarPasso(int n_passos){
		for (int i = 0; i < n_passos; i++) {
			if(this.ccelula_atual == null){
				return -1;
			}
			else if(this.ccelula_atual.ObterCelula().x == this.ponto_inicial.x && this.ccelula_atual.ObterCelula().y == ponto_inicial.y){
				return 1;
			}
			else{
				this.DesIterar();
				
			}
		}
		return 0;
	}
	
	public Corrente_Celula ObterCelulaAtual(){
		return this.ccelula_atual;
	}
	
	public ArrayList<Corrente_Celula> ObterCaminho(){
		return this.ccaminho;
	}
	
	public int ObterClareirasPassadas(){
		return this.clareiras_passadas;
	}
	
	public ArrayList<Corrente_Celula> ObterCaminhoPlanejado(){
		return this.ccelulas_planejadas;
	}
	
	public float Heuristica(Celula casa_inicial) {
		return (casa_inicial.x - this.ponto_final.x) + (casa_inicial.y - this.ponto_final.y);
	}
}
