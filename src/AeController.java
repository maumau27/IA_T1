import java.awt.datatransfer.FlavorTable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class AeController {
	private A_estrela a_estrela;
	private Encontros encontros;
	private int clareiras_esperadas;
	private Celula ponto_inicial;
	private Celula ponto_final;
	private Mapa floresta;
	
	public AeController(Mapa floresta){
		this.floresta = floresta;
		this.ponto_inicial = floresta.ObterInicio();
		this.ponto_final = floresta.ObterFim();
		this.clareiras_esperadas = this.floresta.ObterQuantidadeClareiras();
		this.Inicializar();
	}
	
	private void Inicializar(){
		this.encontros = new Encontros();
		this.encontros.CalcularEncontros(this.clareiras_esperadas);
		this.a_estrela = new A_estrela(this.clareiras_esperadas, this.ponto_inicial, this.ponto_final, this.floresta, this.encontros);
	}
	
	private void RodarNovamente(){
		if(this.a_estrela.ObterClareirasPassadas() < this.clareiras_esperadas){
			this.clareiras_esperadas = this.a_estrela.ObterClareirasPassadas();
			this.Inicializar();
		}
		else{
			this.TerminarAlgoritimo();
		}
	}
	
	private void TerminarAlgoritimo(){
		
	}
	
	public void DarPasso(){
		int fim = this.a_estrela.DarPasso();
		if(fim == 1){
			this.RodarNovamente();
		}
	}
	
	public void DarPasso(int n_passos){
		for (int i = 0; i < n_passos; i++) {
			int fim = this.a_estrela.DarPasso();
			if(fim == 1){
				this.RodarNovamente();
			}
		}
	}
	
	public void VoltarPasso(){
		this.a_estrela.VoltarPasso();
	}
	
	public void VoltarPasso(int n_passos){
		for (int i = 0; i < n_passos; i++) {
			this.a_estrela.VoltarPasso();
		}
	}
	
	public Corrente_Celula ObterCelulaAtual(){
		return this.a_estrela.ObterCelulaAtual();
	}
	
	public ArrayList<Corrente_Celula> ObterCaminho(){
		return this.a_estrela.ObterCaminho();
	}
	
	public ArrayList<Corrente_Celula> ObterCaminhoPlanejado(){
		return this.a_estrela.ObterCaminhoPlanejado();
	}

}
