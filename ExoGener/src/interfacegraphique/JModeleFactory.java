package interfacegraphique;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import expression.modele.MEntier;
import expression.modele.MListe;
import expression.modele.MManager;
import expression.modele.MProduit;
import expression.modele.MQuotient;
import expression.modele.MSomme;
import expression.modele.Modele;

@SuppressWarnings("serial")
public class JModeleFactory extends JPanel {
	
	MManager modelePrincipal;
	ArrayList<JModele> jmodeles;
	int variableSelect = -1;

	static String creerEntier = "Entier";
	static String creerSomme = "Somme";
	static String creerProduit = "Produit";
	static String creerQuotient = "Quotient";
	static String creerListe = "Liste";

	JMenuItem itemGenererStandard = new JMenuItem("Generer Expression");
	JMenuItem itemGenererLatex = new JMenuItem("Generer Latex");

	
	
	JPopupMenu popupModeleFactory = new JPopupMenu();
	int popupX = 10;
	int popupY = 10;
	
	public JModeleFactory(){
		modelePrincipal = new MManager();
		jmodeles = new ArrayList<>();
		
	}
	
	public void init(){
		setLayout(null);
	    itemGenererStandard.addActionListener(new GenererStandardListener());
	    itemGenererLatex.addActionListener(new GenererLatexListener());
	
	    popupModeleFactory.add(createMenuCreer());
	    addMouseListener(new PopupListener());
	}
	
	public JMenu createMenuCreer(){
		JMenu menu = new JMenu("Creer");

		JMenuItem itemEntier = new JMenuItem(JModeleFactory.creerEntier);
		JMenuItem itemSomme = new JMenuItem(JModeleFactory.creerSomme);
		JMenuItem itemProduit = new JMenuItem(JModeleFactory.creerProduit);
		JMenuItem itemQuotient = new JMenuItem(JModeleFactory.creerQuotient);
		JMenuItem itemListe = new JMenuItem(JModeleFactory.creerListe);
		
		CreerListener CL = new CreerListener();
	    itemEntier.addActionListener(CL);
	    itemSomme.addActionListener(CL);
	    itemProduit.addActionListener(CL);
	    itemQuotient.addActionListener(CL);
	    itemListe.addActionListener(CL);
	    
	    menu.add(itemEntier);
	    menu.add(itemSomme);
	    menu.add(itemProduit);
	    menu.add(itemQuotient);
	    menu.add(itemListe);
	    
		
		return menu;
	}
	
	public void selectModele(int idModele){
		if(variableSelect != -1){
			modelePrincipal.creerLien(variableSelect, idModele);
			variableSelect = -1;
			repaint();
		}
	}
	
	public void selectVariable(int variable){
		variableSelect = variable;
		repaint();
	}
	
	public void addModele(Modele nouveauModele)
	{
		modelePrincipal.addModele(nouveauModele);
		int idNouveauModele = modelePrincipal.getLastIdModele();
		JModele modele = new JModele();
		this.add(modele);
        jmodeles.add(modele);
        modele.init(idNouveauModele,popupX,popupY);
        popupX = 10;
        popupY = 10;   
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2= (Graphics2D)g.create() ; //profiter de Java2D et ne pas modifier g
		
		
		for(int idModele=0;idModele<jmodeles.size();idModele++){
			Modele modele = modelePrincipal.getModele(idModele);
			for(int idVar = 0;idVar<modele.getNbVariablesLibres();idVar++){
				int variable = modele.getVariableLibre(idVar);
				if(modelePrincipal.isLien(variable)){
					int idLien = modelePrincipal.getIdLien(variable);
					Point source = jmodeles.get(idModele).getAncreVariable(idVar);
					Point destination = jmodeles.get(idLien).getAncre();
					g2.draw(new Line2D.Double(source.x, source.y, destination.x, destination.y));
				}
			}
		}
		g2.dispose() ;
	}
	
	public void creerLien(int variable,int idModele){
		modelePrincipal.creerLien(variable, idModele);
		repaint();
	}
	
	class CreerListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
	    	String item = ((JMenuItem)e.getSource()).getText();
	    	if(item.equals(creerEntier)){
	    		addModele(new MEntier(1,9));}
	    	else if(item.equals(creerSomme)){
	    		addModele(new MSomme());}
	    	else if(item.equals(creerProduit)){
	    		addModele(new MProduit());}
	    	else if(item.equals(creerQuotient)){
	    		addModele(new MQuotient());}
	    	else if(item.equals(creerListe)){
	    		addModele(new MListe());}
	    }    

	  }
	
	class GenererStandardListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.out.println(modelePrincipal.genererExpression().toString("Standard"));
		}
	}
	
	class GenererLatexListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.out.println(modelePrincipal.genererExpression().toString("Latex"));
		}
	}
	
	
	class PopupListener extends MouseAdapter {
	    public void mousePressed(MouseEvent e) {
	    	variableSelect = -1;
	        repaint();
	        maybeShowPopup(e);
	    }

	    public void mouseReleased(MouseEvent e) {
	    	maybeShowPopup(e);
	    }

	    private void maybeShowPopup(MouseEvent e) {
	        if (e.isPopupTrigger()) {
	        	popupX = e.getX();
	        	popupY = e.getY();

	        	popupModeleFactory.show(e.getComponent(),
	                       e.getX(), e.getY());
	        }
	    }
	}
	
	
	
	
}
