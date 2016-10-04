import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class InterfaceGrafica extends JPanel{

	private Graphics2D g2d;
	private static InterfaceGrafica inst = null;
	private BufferedImage image[] = new BufferedImage[6];
	private BufferedImage movimento[] = new BufferedImage[12];
	private int localAtualx;
	private int localAtualy;
	private boolean inicio = true;
	Mapa mapa = jogo.map;
	private int localDesejadoy;
	private int localDesejadox;
	public boolean linhas = false;
	private int g = 0;
	private int h = 0;
	private int tempo = 0;
	
	public static ArrayList<ArrayList<Line2D.Double>> lst;
	
	private InterfaceGrafica(){
		
		try{
			image[0] = ImageIO.read(getClass().getResource("/imagens/caminho.png"));
			image[1] = ImageIO.read(getClass().getResource("/imagens/Grass_Type2.jpg"));
			image[2] = ImageIO.read(getClass().getResource("/imagens/arvore.jpg"));
			//image[3] = ImageIO.read(getClass().getResource("/imagens/lobo2.png"));
			image[3] = ImageIO.read(getClass().getResource("/imagens/editor_C.jpg"));
			image[5] = ImageIO.read(getClass().getResource("/imagens/casaavo.png"));
			movimento[0] = ImageIO.read(getClass().getResource("/imagens/personagem1.png"));
			movimento[1] = ImageIO.read(getClass().getResource("/imagens/personagem2.png"));
			movimento[2] = ImageIO.read(getClass().getResource("/imagens/personagem9.png"));
			movimento[3] = ImageIO.read(getClass().getResource("/imagens/personagem3.png"));
			movimento[4] = ImageIO.read(getClass().getResource("/imagens/personagem4.png"));
			movimento[5] = ImageIO.read(getClass().getResource("/imagens/personagem10.png"));
			movimento[6] = ImageIO.read(getClass().getResource("/imagens/personagem5.png"));
			movimento[7] = ImageIO.read(getClass().getResource("/imagens/personagem6.png"));
			movimento[8] = ImageIO.read(getClass().getResource("/imagens/personagem11.png"));
			movimento[9] = ImageIO.read(getClass().getResource("/imagens/personagem7.png"));
			movimento[10] = ImageIO.read(getClass().getResource("/imagens/personagem8.png"));
			movimento[11] = ImageIO.read(getClass().getResource("/imagens/personagem12.png"));
		} catch (Exception e){
			System.out.println("Nao foi possivel ler imagem: " + e.getMessage());
		}
		
		ActionListener animate = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				repaint();				
			}
        };
        Timer timer = new Timer(tempo,animate);
        timer.start();
		
	}
	
	
	public static InterfaceGrafica getInterfaceGrafica(){
		if(inst == null){
			inst = new InterfaceGrafica();
		}
		return inst;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		g2d = (Graphics2D)g;
		
		
		desenhaMatriz();
		
		if(linhas == false){

			if(localAtualy > localDesejadoy)
				movimentaNorte();
			else if(localAtualy < localDesejadoy)
				movimentaSul();
			else if (localAtualx < localDesejadox)
				movimentaLeste();
			else if (localAtualx > localDesejadox)
				movimentaOeste();
			else if(localAtualx == localDesejadox && localAtualy == localDesejadoy)
				jogo.setaProx();
			
			if(jogo.i == -1){
				jogo.printaLinhas();
				
			}
		} else{

			//System.out.println("oi");
			tempo = 50;
			ativaPrintLinhas();
			h++;
		}
		
	}
	
	public void ativaPrintLinhas(){
		if(g >= lst.size())
			return;
		ArrayList<Line2D.Double> l = lst.get(g);
		if(h == l.size()){
			h = 0;
			g++;
			repaint();
			return;
		}
		for (int q = 0; q <= h; q++){
			Stroke strokeHour = new BasicStroke(4f);
			g2d.setStroke(strokeHour);
			g2d.draw(l.get(q));
		}
	}
	
	
	private void desenhaMatriz(){
		
		int i = 0;
		int j = 0;
		int valor = 0;
		int terreno;
		
		
		
		for (i = 0; i < 41; i++){
			
			for (j = 0; j < 41; j++){
				
				terreno = mapa.ObterTerreno(i, j);
				if(terreno > 0 && terreno < 4){
					g2d.drawImage(image[terreno - 1], 23*i, 23*j, null);
					
				}
				if(terreno == 4){
					g2d.drawImage(image[0], 23*i, 23*j, null);
					g2d.drawImage(image[terreno - 1], 23*i, 23*j, 23, 23, null);
				}
				if(terreno == 6){ //PRINTA A CASA DA VOVO
					g2d.drawImage(image[0], 23*i, 23*j, null);
					g2d.drawImage(image[terreno - 1], 23*i, 23*j,23, 23, null);
				}
				if(terreno == 5){ //INICIO
					g2d.drawImage(image[0], 23*i, 23*j, null);
					if (inicio){
						localAtualx = 23*i;
						localAtualy = 23*j;
						localDesejadox = localAtualx;
						localDesejadoy = localAtualy;
						//System.out.println("x: "+i+" y: "+j);
						//System.out.println("x inicio: " + mapa.ObterInicio().x + " y inicio: " + mapa.ObterInicio().y);
						inicio = false;
					}
					
				}
			}
			
		}
		
	}
	
	public void vertical(int valor){
		localDesejadoy += valor*23;
	}
	
	public void horizontal(int valor){
		localDesejadox += valor*23;
	}
	
	private void movimentaSul(){
		g2d.drawImage(movimento[(localAtualy%3)], localAtualx, localAtualy, 23, 23, null);
		localAtualy+=1;
	}
	
	private void movimentaNorte(){
		g2d.drawImage(movimento[6 + (localAtualy%3)], localAtualx, localAtualy, 23, 23, null);
		localAtualy-=1;
	}
	
	private void movimentaLeste(){
		g2d.drawImage(movimento[9 + (localAtualx%3)], localAtualx, localAtualy, 23, 23, null);
		localAtualx+=1;
	}
	
	private void movimentaOeste(){
		g2d.drawImage(movimento[3 + (localAtualx%3)], localAtualx, localAtualy, 23, 23, null);
		localAtualx-=1;
	}
	
	
	
	
}
