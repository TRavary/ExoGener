package interfacegraphique;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import expression.modele.MArbre;
import expression.modele.MEntier;
import expression.modele.MListe;
import expression.modele.MOppose;
import expression.modele.MParenthese;
import expression.modele.MProduit;
import expression.modele.MPuissance;
import expression.modele.MQuotient;
import expression.modele.MRacine;
import expression.modele.MSomme;
import expression.modele.MVariable;
import expression.modele.Modele;

@SuppressWarnings("serial")
public class JModeleFactory extends JPanel {
	
	MArbre modelePrincipal = new MArbre();;
	HashMap<Integer,JModele> id2jmodele = new HashMap<>();;
	int variableSelect = -1;


	static String creerVariable = "Variable";
	static String creerEntier = "Entier";
	static String creerSomme = "Somme";
	static String creerProduit = "Produit";
	static String creerQuotient = "Quotient";
	static String creerPuissance = "Puissance";
	static String creerOppose = "Opposé";
	static String creerRacine = "Racine carrée";
	static String creerParenthese = "Parenthèses";
	static String creerListe = "Liste";


	JMenuItem itemGenererStandard = new JMenuItem("Generer Expression");
	JMenuItem itemGenererLatex = new JMenuItem("Generer Latex");

	JPopupMenu popupModeleFactory = new JPopupMenu();
	
	int popupX = 10;
	int popupY = 10;
	
	public void init(){
		setLayout(null);
	    itemGenererStandard.addActionListener(new GenererStandardListener());
	    itemGenererLatex.addActionListener(new GenererLatexListener());
	
	    popupModeleFactory.add(createMenuCreer());
	    addMouseListener(new PopupListener());
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2= (Graphics2D)g.create();
		
		Iterator<Integer> iterIdModele = id2jmodele.keySet().iterator();
		while(iterIdModele.hasNext()){
			int idModele = iterIdModele.next();
			Modele modele = modelePrincipal.getModele(idModele);
			for(int idVar = 0;idVar<modele.getNbVariablesLibres();idVar++){
				int variable = modele.getVariableLibre(idVar);
				if(modelePrincipal.isLien(variable)){
					int idLien = modelePrincipal.getIdLien(variable);
					Point source = id2jmodele.get(idModele).getAncreVariable(idVar);
					Point destination = id2jmodele.get(idLien).getAncre();
					g2.draw(new Line2D.Double(source.x, source.y, destination.x, destination.y));
				}
			}
		}
		g2.dispose() ;
	}
	
	public JMenu createMenuCreer(){
		JMenu menu = new JMenu("Creer");

		JMenuItem itemEntier = new JMenuItem(JModeleFactory.creerEntier);
		JMenuItem itemSomme = new JMenuItem(JModeleFactory.creerSomme);
		JMenuItem itemProduit = new JMenuItem(JModeleFactory.creerProduit);
		JMenuItem itemQuotient = new JMenuItem(JModeleFactory.creerQuotient);
		JMenuItem itemListe = new JMenuItem(JModeleFactory.creerListe);
		JMenuItem itemPuissance = new JMenuItem(JModeleFactory.creerPuissance);
		JMenuItem itemOppose = new JMenuItem(JModeleFactory.creerOppose);
		JMenuItem itemRacine = new JMenuItem(JModeleFactory.creerRacine);
		JMenuItem itemParenthese = new JMenuItem(JModeleFactory.creerParenthese);
		JMenuItem itemVariable = new JMenuItem(JModeleFactory.creerVariable);
		
		
		CreerListener CL = new CreerListener();
	    itemEntier.addActionListener(CL);
	    itemSomme.addActionListener(CL);
	    itemProduit.addActionListener(CL);
	    itemQuotient.addActionListener(CL);
	    itemListe.addActionListener(CL);
	    itemPuissance.addActionListener(CL);
	    itemOppose.addActionListener(CL);
	    itemRacine.addActionListener(CL);
	    itemParenthese.addActionListener(CL);
	    itemVariable.addActionListener(CL);
	    
	    
	    menu.add(itemVariable);
	    menu.add(itemEntier);
	    menu.add(itemSomme);
	    menu.add(itemProduit);
	    menu.add(itemQuotient);
	    menu.add(itemListe);
	    menu.add(itemPuissance);
	    menu.add(itemOppose);
	    menu.add(itemRacine);
	    menu.add(itemParenthese);
	    
		return menu;
	}
	
	public void selectModele(int idModele){
		if(variableSelect != -1){
			if(modelePrincipal.getVariableLinkedTo(idModele) ==-1){
				modelePrincipal.addLien(variableSelect, idModele);
			}
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
		JModele jmodele = new JModele();
		this.add(jmodele);
        id2jmodele.put(idNouveauModele, jmodele);
        jmodele.init(idNouveauModele,popupX,popupY);
        popupX = 10;
        popupY = 10;   
	}
	public void supprModele(int idModele){
		modelePrincipal.supprModele(idModele);
		remove(id2jmodele.get(idModele));
		id2jmodele.remove(idModele);
		validate();
		repaint();
	}
	
	public void addVariableTo(int idModele){
		modelePrincipal.addVariableTo(idModele);
		id2jmodele.get(idModele).addVariable();
		repaint();
	}
	public void supprVariableTo(int idModele){
		modelePrincipal.supprVariableTo(idModele);
		id2jmodele.get(idModele).supprVariable();
		validate();
		repaint();
	}
	public void changeVariable(int idModele, int idVar, int newVariable) {
		modelePrincipal.changeVariable(idModele,idVar, newVariable);
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
	    	else if(item.equals(creerPuissance)){
	    		addModele(new MPuissance());}
	    	else if(item.equals(creerOppose)){
	    		addModele(new MOppose());}
	    	else if(item.equals(creerParenthese)){
	    		addModele(new MParenthese());}
	    	else if(item.equals(creerRacine)){
	    		addModele(new MRacine());}
	    	else if(item.equals(creerVariable)){
	    		addModele(new MVariable("x"));}
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
