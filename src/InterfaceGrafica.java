import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class InterfaceGrafica extends JPanel{

	private Graphics2D g2d;
	private static InterfaceGrafica inst = null;
	private BufferedImage image[] = new BufferedImage[6];
	private BufferedImage movimento[] = new BufferedImage[12];
	private int i = 0;
	private int localAtualx;
	private int localAtualy;
	private boolean inicio = true;
	
	private InterfaceGrafica(){
		
		try{
			image[0] = ImageIO.read(getClass().getResource("/imagens/caminho.png"));
			image[1] = ImageIO.read(getClass().getResource("/imagens/Grass_Type2.jpg"));
			image[2] = ImageIO.read(getClass().getResource("/imagens/arvore.jpg"));
			image[3] = ImageIO.read(getClass().getResource("/imagens/lobo2.png"));
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
        Timer timer = new Timer(150,animate);
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
		//if(localAtualy)
		movimentaNorte();
		
	}
	
	private void desenhaMatriz(){
		
		int i = 0;
		int j = 0;
		int valor = 0;
		int terreno;
		
		Mapa mapa = new Mapa();
		
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
						inicio = false;
					}
					
				}
			}
			
		}
		
	}
	
	private void movimentaSul(){
		g2d.drawImage(movimento[(localAtualy%3)], localAtualx, localAtualy, 23, 23, null);
		localAtualy+=4;
	}
	
	private void movimentaNorte(){
		g2d.drawImage(movimento[6 + (localAtualy%3)], localAtualx, localAtualy, 23, 23, null);
		localAtualy-=4;
	}
	
	private void movimentaLeste(){
		g2d.drawImage(movimento[9 + (localAtualx%3)], localAtualx, localAtualy, 23, 23, null);
		localAtualx+=4;
	}
	
	private void movimentaOeste(){
		g2d.drawImage(movimento[3 + (localAtualx%3)], localAtualx, localAtualy, 23, 23, null);
		localAtualx-=4;
	}
	
	
	
	
}
