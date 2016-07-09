package interfacegraphique;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import expression.modele.MArbre;
import expression.modele.MEntier;
import expression.modele.MEntierAlea;
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
	int idModeleSelect = -1;
	int numeroSelect = -1;


	static String creerVariable = "Variable";
	static String creerEntier = "Entier";
	static String creerEntierAlea = "EntierAlea";
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
	JMenuItem itemAfficherLatex = new JMenuItem("Afficher arbre");

	JPopupMenu popupModeleFactory = new JPopupMenu();
	JMenuItem itemEnregistrer = new JMenuItem("Enregistrer");
	JMenuItem itemCharger = new JMenuItem("Charger");
	
	int popupX = 10;
	int popupY = 10;
	
	public void resetModelePrincipal(){
		Iterator<Integer> iter = id2jmodele.keySet().iterator();
		while(iter.hasNext())
		{
			int idModele = iter.next();
			remove(id2jmodele.get(idModele));
			id2jmodele.remove(idModele);
			iter = id2jmodele.keySet().iterator();
		}
		modelePrincipal = new MArbre();
		id2jmodele = new HashMap<>();
		idModeleSelect = -1;
		numeroSelect = -1;
	}
	
	public void init(){
		setLayout(null);
		itemGenererStandard.addActionListener(new GenererStandardListener());
	    itemGenererLatex.addActionListener(new GenererLatexListener());
	    itemAfficherLatex.addActionListener(new AfficherListener());
	
	    popupModeleFactory.add(createMenuCreer());
	    
	    itemEnregistrer.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e) {
				MArbre modele = modelePrincipal;
				resetModelePrincipal();
				ObjectOutputStream oos = null;

				try {
				  final FileOutputStream fichier = new FileOutputStream("test.mod");
				  oos = new ObjectOutputStream(fichier);
				  oos.writeObject(modele);
				} catch (final java.io.IOException e1) {
				  e1.printStackTrace();
				} finally {
				  try {
				    if (oos != null) {
				      oos.flush();
				      oos.close();
				    }
				  } catch (final IOException ex) {
				    ex.printStackTrace();
				  }
				}
				
				addModele(modele);
				updateAllComponents();
			}
	    });
	    
	    
	    itemCharger.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e) {
    		    ObjectInputStream ois = null;

    		    try {
			      final FileInputStream fichier = new FileInputStream("test.mod");
			      ois = new ObjectInputStream(fichier);
				  Modele modele = (Modele) ois.readObject(); 
				  addModele(modele);
    		    } catch (final java.io.IOException e1) {
    		      e1.printStackTrace();
    		    } catch (final ClassNotFoundException e1) {
    		      e1.printStackTrace();
    		    } finally {
    		      try {
    		        if (ois != null) {
    		          ois.close();
    		        }
    		      } catch (final IOException ex) {
    		        ex.printStackTrace();
    		      }
    		    }
	    	 
				updateAllComponents();
			}
	    });
	    
	    popupModeleFactory.add(itemEnregistrer);
	    popupModeleFactory.add(itemCharger);
	    
	    addMouseListener(new PopupListener());
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2= (Graphics2D)g.create();
		
		Iterator<Integer> iterIdModele = id2jmodele.keySet().iterator();
		while(iterIdModele.hasNext()){
			int idModele = iterIdModele.next();
			Modele modele = modelePrincipal.getModele(idModele);
			for(int p = 0;p<modele.getNbParametres();p++){
				if(modelePrincipal.isLien(idModele,p)){
					int idLien = modelePrincipal.getIdLien(idModele,p);
					Point source = id2jmodele.get(idModele).getAncreParametre(p);
					Point destination = id2jmodele.get(idLien).getAncre();
					g2.draw(new Line2D.Double(source.x, source.y, destination.x, destination.y));
				}
			}
		}
		g2.dispose() ;
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
		updateAllComponents();
	}
	
	
	public void selectModele(int idModele){
		if(numeroSelect != -1){
			modelePrincipal.addLien(idModeleSelect, numeroSelect, idModele);
			idModeleSelect = -1;
			numeroSelect = -1;
			updateAllComponents();
		}
	}
	
	
	
	public void selectParametre(int idModele,int numero){
		idModeleSelect = idModele;
		numeroSelect = numero;
		updateAllComponents();
	}
	
	public void updateAllComponents(){
		Iterator<Integer> iterIdModele = id2jmodele.keySet().iterator();
		while(iterIdModele.hasNext()){
			int idModele = iterIdModele.next();
			JModele jmodele = id2jmodele.get(idModele);
			jmodele.update();
		}
		validate();
		repaint();
	}
	
	
	public void addParametreTo(int idModele){
		modelePrincipal.addParametreTo(idModele);
		updateAllComponents();
	}
	
	public void supprParametreTo(int idModele){
		modelePrincipal.supprParametreTo(idModele);
		updateAllComponents();
	}
	
	public void FusionnerParametre(int idModele, int numeroModele, int nouveauNumero) {
		modelePrincipal.FusionnerParametre(idModele, numeroModele, nouveauNumero);
	}
	
	public void EchangerParametre(int idModele, int numeroModele, int nouveauNumero) {
		modelePrincipal.EchangerParametre(idModele, numeroModele, nouveauNumero);
	}
	
	public void DissocierParametre(int idModele,int numeroModele){
		modelePrincipal.DissocierParametre(idModele,numeroModele);
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
	class AfficherListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.out.println(modelePrincipal.toString());
		}
	}
	
	
	
	class PopupListener extends MouseAdapter {
	    public void mousePressed(MouseEvent e) {
	    	idModeleSelect = -1;
	    	numeroSelect = -1;
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
	
	
	
	public JMenu createMenuCreer(){
		JMenu menu = new JMenu("Creer");

		JMenuItem itemEntier = new JMenuItem(JModeleFactory.creerEntier);
		JMenuItem itemEntierAlea = new JMenuItem(JModeleFactory.creerEntierAlea);
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
	    itemEntierAlea.addActionListener(CL);
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
	    menu.add(itemEntierAlea);
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
	
	
	
	class CreerListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
	    	String item = ((JMenuItem)e.getSource()).getText();
	    	if(item.equals(creerEntier)){
	    		String str = JOptionPane.showInputDialog(null, "Entrez une valeur :", "Creation d'un entier", JOptionPane.QUESTION_MESSAGE);
	    	    try{
	    	    	int entier=Integer.parseInt(str);
	    	    	addModele(new MEntier(entier));
	    	    }
	    	    catch(Exception except)
	    	    {
	    	    	System.out.println("Exception is "+e);
	    	    }
	    	}
	    	if(item.equals(creerEntierAlea)){
	    		addModele(new MEntierAlea(1,9));}
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
}
