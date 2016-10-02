import javax.swing.JFrame;


public class Tela extends JFrame{
	
	public Tela(){
		super("IA");
		setVisible(true);
		setSize(1024, 1024);
		getContentPane().add(InterfaceGrafica.getInterfaceGrafica());
		
	}

}
