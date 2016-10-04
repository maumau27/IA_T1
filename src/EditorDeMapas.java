import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

class TileSelector implements MouseListener {
	private int type;
	public JLabel label;
	public JLabel select;
	
	public ArrayList<TileSelector> group;
	
	public TileSelector( ImageIcon ico , int type ,  ArrayList<TileSelector> group ) {
		this.type = type;
		this.group = group;
		
		label = new JLabel();
		label.setIcon( ico );
		label.setBounds( 0, 0, ico.getIconWidth(),  ico.getIconHeight());
		
		select = new JLabel();
		select.setIcon( JanelaPrincipal.iconSel );
		select.setBounds( 0, 0, JanelaPrincipal.iconSel.getIconWidth(),  JanelaPrincipal.iconSel.getIconHeight());
		select.setVisible(false );

		label.add(select);

		label.addMouseListener( this );
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if( this.select.isVisible() ) {
			this.select.setVisible( false );
			JanelaPrincipal.paintType = -1;
		} else {
			for( TileSelector ts : this.group ) {
				ts.select.setVisible(false);
			}
			
			this.select.setVisible( true );
			JanelaPrincipal.paintType = this.type;	
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
}

class Tile implements MouseListener {
	public JLabel label;
	public JLabel select;
	public JLabel path;
	
	public int type;
	
	public Celula celula;
	
	public Tile( Celula celula ) {
		this.type = celula.terreno;
		this.celula = celula;
		
		label = new JLabel();
		path = new JLabel();
		select = new JLabel();
		
		select.setIcon( JanelaPrincipal.iconSel );
		select.setBounds( 0, 0, JanelaPrincipal.iconSel.getIconWidth(),  JanelaPrincipal.iconSel.getIconHeight());
		select.setVisible(false);
		
		path.setIcon( JanelaPrincipal.iconPathMiddle );
		path.setBounds( 0, 0, JanelaPrincipal.iconPathGood.getIconWidth(),  JanelaPrincipal.iconPathGood.getIconHeight());
		path.setVisible(false);
		
		label.add(select);
		label.add(path);
		
		this.setIcon();
		
		this.getComponent().addMouseListener( this );
	}
	
	private void setIcon() {
		ImageIcon icon;
		switch( this.type ) {
			case 2:
				// Galho
				icon = JanelaPrincipal.iconG;
				break;
				
			case 3:
				// Floresta
				icon = JanelaPrincipal.iconD;
				break;
				
			case 4:
				// Clareira
				icon = JanelaPrincipal.iconC;
				break;
				
			case 5:
				// Inicio ( vila )
				icon = JanelaPrincipal.iconI;
				break;
				
			case 6:
				// Fim ( casa da Vovo )
				icon = JanelaPrincipal.iconF;
				break;				
				
			default:
				icon = JanelaPrincipal.iconP;
				break;
		}
		
		this.label.setIcon(icon);
	}
	
	public void setPathIcon( int stage ) {
		if( stage < 0 ) {
			path.setVisible(false);
			return;
		}
		switch( stage ) {
			case 0:
				path.setIcon( JanelaPrincipal.iconPathBad );
				break;
			case 1:
				path.setIcon( JanelaPrincipal.iconPathMiddle );
				break;
			case 2:
				path.setIcon( JanelaPrincipal.iconPathGood );
				break;
		}
		
		path.setVisible(true);
	}
	
	public Component getComponent() {
		return this.label;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if( JanelaPrincipal.paintType >= 0 ) {
			this.type = JanelaPrincipal.paintType;
		} else {
			this.type = this.type + 1;
			
			if( this.type > 3 ) {
				this.type = 1;
			}
		}
		
		this.setIcon();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if( JanelaPrincipal.selectedTile != null ) {
			JanelaPrincipal.selectedTile.select.setVisible( false );
		}
		JanelaPrincipal.selectedTile = this;
		this.select.setVisible(true);
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}

class JanelaPrincipal extends JFrame {
	public Container mainPane;
	public Container mapPane;
	
	public static JButton btnRunCont;
	public static JTextArea labelRelatorio;
	public static JSlider sliderSpeed;
	
	static public ImageIcon iconD;
	static public ImageIcon iconP;
	static public ImageIcon iconG;
	static public ImageIcon iconC;
	static public ImageIcon iconI;
	static public ImageIcon iconF;	
	static public ImageIcon iconSel;
	
	static public ImageIcon iconPathGood;
	static public ImageIcon iconPathBad;
	static public ImageIcon iconPathMiddle;
	
	static public ArrayList<Tile> tiles;
	static public ArrayList<ArrayList<Tile>> tilesByCoord;
	static public Tile selectedTile;
	
	static public int paintType = -1;
	
	
	public JanelaPrincipal( Mapa map , int sx , int sy , int gx , int gy ) {
        setTitle("Chapeuzinho Vermelho");
        setSize(sx, sy);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        tilesByCoord = new ArrayList<ArrayList<Tile>>();
        
        String defaultPath = "src/imagens/";
        iconD = new ImageIcon( defaultPath + "editor_D.jpg" );
        iconP = new ImageIcon( defaultPath + "editor_P.jpg" );
        iconG = new ImageIcon( defaultPath + "editor_G.jpg" );
        iconC = new ImageIcon( defaultPath + "editor_C.jpg" );
        iconI = new ImageIcon( defaultPath + "editor_I.jpg" );
        iconF = new ImageIcon( defaultPath + "editor_F.jpg" );
        iconSel = new ImageIcon( defaultPath + "editor_SEL.png" );
        
        iconPathGood = new ImageIcon( defaultPath + "editor_CAM_GOD.png" );
        iconPathBad = new ImageIcon( defaultPath + "editor_CAM_BAD.png" );
        iconPathMiddle = new ImageIcon( defaultPath + "editor_CAM_MID.png" );
        
        mainPane = getContentPane();
        mainPane.setLayout( new BorderLayout() );
        
        // Options Pane
        JPanel optPane = new JPanel();
        optPane.setLayout( new FlowLayout() );
        optPane.setPreferredSize( new Dimension( 200 , 100 ) );
        optPane.setBackground( new Color(0,0,0));
        mainPane.add( optPane , BorderLayout.LINE_START );
 
        // Painel do mapa - dimensoes reais
        mapPane = new JPanel();
        mapPane.setLayout( null );
        mapPane.setPreferredSize( new Dimension( sx , sy ));        
        
        // Painel do mapa - area visivel
        JScrollPane mapSPane = new JScrollPane( mapPane );
        mapSPane.setPreferredSize( new Dimension( 300 , 300 ) );
        mapSPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        mainPane.add( mapSPane , BorderLayout.CENTER );
        
        // Populando parte de opcoes	  
        Font font1 = new Font( "Arial" , Font.BOLD , 20);
        Font font2 = new Font( "Arial" , Font.BOLD , 18);
        Font font3 = new Font( "Arial" , Font.BOLD , 14);
        
	        JLabel tileAlgoModeTitle = new JLabel("Executar" , SwingConstants.CENTER);
	        tileAlgoModeTitle.setFont(font1);
	        tileAlgoModeTitle.setForeground( new Color( 255 ,255 ,255 ));
	        tileAlgoModeTitle.setPreferredSize( new Dimension(190,40 ));
	        optPane.add( tileAlgoModeTitle );
        
	        // Botao para resolver o algoritmo so na interface 
	        JButton btnRunText = new JButton("Textual");
	        btnRunText.setFont(font2);
	        btnRunText.setPreferredSize( new Dimension( 190 , 40) );
	        btnRunText.addActionListener(new ActionListener() { 
	            public void actionPerformed(ActionEvent e) { 
	            	EditorDeMapas.runText();
	            } 
	        });
	        optPane.add( btnRunText );
        
	        // Botao para resolver o algoritmo de forma simples
	        JButton btnRunTech = new JButton("Tecnico");
	        btnRunTech.setFont(font2);
	        btnRunTech.setPreferredSize( new Dimension( 190 , 40) );
	        btnRunTech.addActionListener(new ActionListener() { 
	            public void actionPerformed(ActionEvent e) { 
	            	EditorDeMapas.runTech();
	            } 
	        });
	        optPane.add( btnRunTech );
	        
	        // Botao para resolver o algoritmo da forma animada
	        JButton btnRunAnimation = new JButton("Animado");
	        btnRunAnimation.setFont(font2);
	        btnRunAnimation.setPreferredSize( new Dimension( 190 , 40) );
	        btnRunAnimation.addActionListener(new ActionListener() { 
	            public void actionPerformed(ActionEvent e) { 
	            	EditorDeMapas.runAnimated();
	            } 
	        });
	        optPane.add( btnRunAnimation );
	        
	        // Editor
	        JLabel tileSelectorTitle = new JLabel("Terreno a Pintar" , SwingConstants.CENTER);
	        tileSelectorTitle.setFont(font1);
	        tileSelectorTitle.setForeground( new Color( 255 ,255 ,255 ));
	        tileSelectorTitle.setPreferredSize( new Dimension(190,40 ));
	        optPane.add( tileSelectorTitle );
	        
	        ArrayList<TileSelector> group = new ArrayList<TileSelector>();
	        
	        TileSelector selector1 = new TileSelector( JanelaPrincipal.iconP , 1 , group );
	        optPane.add( selector1.label );
	        group.add( selector1 );
	        
	        TileSelector selector2 = new TileSelector( JanelaPrincipal.iconG , 2 , group );
	        optPane.add( selector2.label );
	        group.add( selector2 );
	        
	        TileSelector selector3 = new TileSelector( JanelaPrincipal.iconD , 3 , group );
	        optPane.add( selector3.label );
	        group.add( selector3 );
	        
	        TileSelector selector4 = new TileSelector( JanelaPrincipal.iconC , 4 , group );
	        optPane.add( selector4.label );
	        group.add( selector4 );
	        
	        TileSelector selector5 = new TileSelector( JanelaPrincipal.iconI , 5 , group );
	        optPane.add( selector5.label );
	        group.add( selector5 );
	        
	        TileSelector selector6 = new TileSelector( JanelaPrincipal.iconF , 6 , group );
	        optPane.add( selector6.label );
	        group.add( selector6 );
	        
	        JLabel separator = new JLabel("" , SwingConstants.CENTER);
	        separator.setFont(font1);
	        separator.setForeground( new Color( 255 ,255 ,255 ));
	        separator.setPreferredSize( new Dimension(190,20 ));
	        optPane.add( separator );
	        
	    sliderSpeed = new JSlider(JSlider.HORIZONTAL, 1 , 100 , 10 );
	    sliderSpeed.setPreferredSize( new Dimension( 190 , 40) );
        optPane.add( sliderSpeed );
        
        // Continuar iteracao
        btnRunCont = new JButton("Proxima It");
        btnRunCont.setFont(font2);
        btnRunCont.setPreferredSize( new Dimension( 190 , 40) );
        btnRunCont.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
            	EditorDeMapas.runTech_cont();
            } 
        });
        btnRunCont.setVisible(false);
        optPane.add( btnRunCont );
	        
        // Relatorio
        EditorDeMapas.janelaPrincipal.labelRelatorio = new JTextArea("Relatorio" );
        labelRelatorio.setFont(font3);
        labelRelatorio.setForeground( new Color( 255 ,255 ,255 ));
        labelRelatorio.setPreferredSize( new Dimension(190,500 ));
        labelRelatorio.setOpaque(false);
        optPane.add( labelRelatorio );
	        
        // Popula o mapa
        tiles = new ArrayList<Tile>();
        for( int y = 0 ; y < gx-1 ; y++ ) {
        	ArrayList<Tile> line = new ArrayList<Tile>();
        	JanelaPrincipal.tilesByCoord.add( line ); 
        	
        	for( int x = 0 ; x < gy-1 ; x++ ) {
                Tile tile = new Tile( map.ObterCelula(x, y) );
                if( tile.celula == null ) {
                	continue;
                }
                
                line.add( tile );
                tiles.add(tile);
                
                mapPane.add( tile.label );
                tile.getComponent().setBounds( x * EditorDeMapas.tileSizeX , y * EditorDeMapas.tileSizeY  , EditorDeMapas.tileSizeX , EditorDeMapas.tileSizeY );
        	}
        }  
        
        // 
        
        this.pack();
	}
	
	public void resetPath() {
		this.mapPane.setVisible(false);
		for( Tile t : tiles ) {
			t.setPathIcon(-1);
		}
		this.mapPane.setVisible(true);
	}
}

public class EditorDeMapas {
	private static Mapa mapa;
	
	public static int tileSizeX;
	public static int tileSizeY;
	public static int mapOffsetX;
	public static int mapOffsetY;
	
	public static JanelaPrincipal janelaPrincipal;
	
	public static Timer timer;
	public static int animState;
	
	
	static void iniciar(Mapa mp) {
		mapa = mp;

		tileSizeX = 23;
		tileSizeY = 23;
		mapOffsetX = 0;
		mapOffsetY = 0;
		
		int mapSx[] = mp.ObterTamanho();
		
        EventQueue.invokeLater(() -> {
        	janelaPrincipal = new JanelaPrincipal( mp , mapSx[0]*tileSizeX  ,  mapSx[1]*tileSizeY , mapSx[0] , mapSx[1] );
        	janelaPrincipal.setVisible(true);
        });
 
	}
	
	public static void salvar() {
		for( Tile til : janelaPrincipal.tiles ) {
			til.celula.terreno = til.type;
			til.celula.custo = (float)mapa.obterCustoTerreno( til.type );
;		}
	}
	
	public static void pintarCaminho() {
		ArrayList<Corrente_Celula> corrente;
		Tile tile;
		Celula cel;
		Corrente_Celula cc;
		
		janelaPrincipal.resetPath();
		
		janelaPrincipal.mapPane.setVisible(false);
		
		corrente= jogo.a.ObterCaminhoPlanejado();
		for( Corrente_Celula cCelula : corrente ) {
			cel = cCelula.ObterCelula();
			tile = JanelaPrincipal.tilesByCoord.get( cel.y ).get( cel.x );
			tile.setPathIcon(0);
			
		}
		
		corrente= jogo.a.ObterCaminho();
		for( Corrente_Celula cCelula : corrente ) {
			cel = cCelula.ObterCelula();
			tile = JanelaPrincipal.tilesByCoord.get( cel.y ).get( cel.x );
			tile.setPathIcon(1);	
		}
		
		cc = jogo.a.ObterCelulaAtual();
		while( cc.ObterPai() != null ) {
			cel = cc.ObterCelula();
			tile = JanelaPrincipal.tilesByCoord.get( cel.y ).get( cel.x );
			tile.setPathIcon(2);
			cc = cc.ObterPai();
		}
		
		janelaPrincipal.mapPane.setVisible(true);
		
	}
	
	
	public static void runText() {
    	EditorDeMapas.salvar();
		jogo.iniciaAlgoritmo();
		jogo.rodaAlgoritmo();
	}
	
	
	public static void runTech() {
		EditorDeMapas.salvar();
		jogo.iniciaAlgoritmo();
		
		//jogo.rodaAlgoritmo();
		/*
		while( jogo.a.ObterUltimoEstado() != EstadoDeParada.CHEGOU_MELHORCASO ) {
			jogo.rodaPassoAlgoritmo(1);
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			EditorDeMapas.pintarCaminho();
		}
		*/
		
        EditorDeMapas.runTech_timer();
	}

	public static void runTech_timer() {
		ActionListener animate = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EditorDeMapas.runTech_rec();	
			}
        };	
        EditorDeMapas.timer = new Timer( 100 , animate );
        EditorDeMapas.timer.start();	
	}	
	
	public static void runTech_rec() {
		if( jogo.a.ObterUltimoEstado() == EstadoDeParada.NAOCHEGOU ) {
			EditorDeMapas.animState = 0;
		}
		
		if( jogo.a.ObterUltimoEstado() != EstadoDeParada.NAOCHEGOU ) {
			EditorDeMapas.janelaPrincipal.labelRelatorio.setText("aaa");
			EditorDeMapas.janelaPrincipal.labelRelatorio.setText( jogo.obterStringEstado() );
		}
		
		if( jogo.a.ObterUltimoEstado() == EstadoDeParada.CHEGOU_MELHORCASO && EditorDeMapas.animState == 0 ) {
			EditorDeMapas.animState = 1;
			EditorDeMapas.janelaPrincipal.btnRunCont.setVisible(false);
			return;
		}

		if( jogo.a.ObterUltimoEstado() == EstadoDeParada.CHEGOU_PODEMELHORAR && EditorDeMapas.animState == 0 ) {
			EditorDeMapas.animState = 1;
			EditorDeMapas.janelaPrincipal.btnRunCont.setVisible(true);
			return;
		}	
		
		if( EditorDeMapas.animState == 0  || EditorDeMapas.animState == 3 ) {	
			int steps = EditorDeMapas.janelaPrincipal.sliderSpeed.getValue();
			if( steps > 95 ) {
				steps = 100000;
			}
			
			jogo.rodaPassoAlgoritmo(steps);
			EditorDeMapas.pintarCaminho();
		}
	}
	

	public static void runTech_cont() {
		EditorDeMapas.animState = 3;
		EditorDeMapas.runTech_timer();
	}

	public static void runAnimated() {
    	EditorDeMapas.salvar();
		jogo.iniciaAlgoritmo();
		jogo.rodaAlgoritmo();
		
		int mapSx[] = EditorDeMapas.mapa.ObterTamanho();
		
		InterfaceGrafica IG = InterfaceGrafica.getInterfaceGrafica();
		IG.setPreferredSize( new Dimension(100,100));
		IG.setBounds( 0 , 0 , mapSx[0]*EditorDeMapas.tileSizeX , mapSx[1]*EditorDeMapas.tileSizeY);
		
		EditorDeMapas.janelaPrincipal.mapPane.removeAll();
		EditorDeMapas.janelaPrincipal.mapPane.add(IG);
		EditorDeMapas.janelaPrincipal.mapPane.repaint();
		
		jogo.iniciaAnimacao();
	}

}
