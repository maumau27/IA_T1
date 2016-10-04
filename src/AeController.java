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
	private EstadoDeParada ultimoEstado;
	
	public AeController(Mapa floresta){
		this.floresta = floresta;
		this.ponto_inicial = floresta.ObterInicio();
		System.out.println("(" + ponto_inicial.x + "," + ponto_inicial.y + ")");
		this.ponto_final = floresta.ObterFim();
		this.clareiras_esperadas = this.floresta.ObterQuantidadeClareiras();
		this.encontros = new Encontros();
	
		this.Inicializar();
	}
	
	private void Inicializar(){
		this.encontros.CalcularEncontros( this.clareiras_esperadas );
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
	
	public EstadoDeParada DarPasso(){
		return DarPasso(0);
	}
	
	public EstadoDeParada DarPasso(int n_passos){
		return DarPasso( n_passos , false );
	}
	
	public EstadoDeParada DarPasso(int n_passos , boolean paraSeChegouAoFinal ){
		if( this.ultimoEstado == EstadoDeParada.CHEGOU_PODEMELHORAR ) {
			this.RodarNovamente();
		} else if ( this.ultimoEstado == EstadoDeParada.CHEGOU_MELHORCASO ) {
			return this.ultimoEstado;
		}
		
		for (int i = 0; i < n_passos; i++) {
			this.ultimoEstado = this.a_estrela.DarPasso();
			if( this.ultimoEstado == EstadoDeParada.CHEGOU_MELHORCASO || this.ultimoEstado == EstadoDeParada.CHEGOU_PODEMELHORAR ){
				if( paraSeChegouAoFinal == true ) {
					return this.ultimoEstado;
				} else {
					if( this.ultimoEstado == EstadoDeParada.CHEGOU_PODEMELHORAR ) {
						this.RodarNovamente();
					} else {
						return this.ultimoEstado;
					}
				}
			}
		}
		return EstadoDeParada.NAOCHEGOU;
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
	
	public EstadoDeParada ObterUltimoEstado() {
		return this.ultimoEstado;
	}
	
	public double ObterCustoTotal( ) {
		return this.a_estrela.ObterCustoTotal();
	}
	
	public double ObterCustoEncontros( ) {
		return this.a_estrela.ObterCustoEncontros();
	}
	
	public double ObterCustoCaminho( ) {
		return this.a_estrela.ObterCustoCaminho();
	}	

	public int ObterEncontrosEsperados() {
		return a_estrela.ObterEncontrosEsperados();
	}	
	
	public void ImprimirEncontros( boolean detalhado , String prefixo ) {
		this.encontros.ImprimirEncontros( detalhado , prefixo );
	}
	
}
