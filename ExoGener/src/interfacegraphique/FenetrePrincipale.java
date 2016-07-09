package interfacegraphique;


import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;



@SuppressWarnings("serial")
public class FenetrePrincipale extends JFrame {
	JModeleFactory modeleFactory = new JModeleFactory();
	JMenuBar menuBar = new JMenuBar();
	
	JMenu menuOutils = new JMenu("Outils");
	JMenu menuCreer = new JMenu("Creer");
	
	public FenetrePrincipale(){
	    this.setTitle("ExoGener");
	    this.setSize(400, 500);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
	    this.setVisible(true);
	    this.setContentPane(modeleFactory);	    
	    
	    
	    menuOutils.add(modeleFactory.itemGenererStandard);
	    menuOutils.add(modeleFactory.itemGenererLatex);
	    menuOutils.add(modeleFactory.itemAfficherLatex);
	    
	    
	    menuBar.add(menuOutils);
	    menuCreer = modeleFactory.createMenuCreer();
	    menuBar.add(menuCreer);
	    this.setJMenuBar(menuBar);

	    
	    modeleFactory.init();

	    this.setVisible(true);
 
	}
}