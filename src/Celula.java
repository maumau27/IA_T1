
public class Celula {
	public int terreno;
	public float custo;
	public int x;
	public int y;
	
	public Celula(int x , int y , int terreno , float custo) {
		this.x = x;
		this.y = y;
		this.terreno = terreno;
		this.custo = custo;
	}
	
	public Celula(int x , int y , int terreno , double custo) {
		this( x , y , terreno , (float)custo );
	}	
	
	public Celula( int x , int y ) {
		this(x, y, 1, 0);
	}
}
