package interfacegraphique;


import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import expression.modele.ModeleManager;



@SuppressWarnings("serial")
public class FenetrePrincipale extends JFrame {
	JModeleFactory modeleFactory = new JModeleFactory();
	ModeleManager modeleManager = new ModeleManager();
	JMenuBar menuBar = new JMenuBar();
	
	JMenu menuOutils = new JMenu("Outils");
	
	public FenetrePrincipale(){
	    this.setTitle("ExoGener");
	    this.setSize(400, 500);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
	    this.setVisible(true);
	    this.setContentPane(modeleFactory);	    
	    
	    modeleManager.init(modeleFactory);
	    modeleFactory.init(modeleManager);
	    
	    menuOutils.add(modeleFactory.itemGenererStandard);
	    menuOutils.add(modeleFactory.itemGenererLatex);
	    menuOutils.add(modeleFactory.itemAfficherLatex);
	    
	    
	    menuBar.add(menuOutils);
	    this.setJMenuBar(menuBar);

	    

	    this.setVisible(true);
 
	}
}