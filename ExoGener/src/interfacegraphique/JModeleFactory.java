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
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import expression.modele.MArbre;
import expression.modele.Modele;
import expression.modele.ModeleManager;

@SuppressWarnings("serial")
public class JModeleFactory extends JPanel {
	
	MArbre modelePrincipal = new MArbre();
	ModeleManager modeleManager;
	HashMap<Integer,JModele> id2jmodele = new HashMap<>();;
	int idModeleSelect = -1;
	int numeroSelect = -1;
	
	JMenuItem itemGenererStandard = new JMenuItem("Generer Expression");
	JMenuItem itemGenererLatex = new JMenuItem("Generer Latex");
	JMenuItem itemAfficherLatex = new JMenuItem("Afficher arbre");

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
		updateAllComponents();
	}
	
	public void init(ModeleManager modeleManager){
		this.modeleManager = modeleManager;
		setLayout(null);
		itemGenererStandard.addActionListener(new GenererStandardListener());
	    itemGenererLatex.addActionListener(new GenererLatexListener());
	    itemAfficherLatex.addActionListener(new AfficherListener());
	    itemEnregistrer.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e) {
				MArbre modele = modelePrincipal;
				
				String nomModele = JOptionPane.showInputDialog(null, "Entrez un nom de modele :", "Sauvegarder modele", JOptionPane.QUESTION_MESSAGE);
				
				try {
					modeleManager.inserer(modele, nomModele);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				resetModelePrincipal();
				modele.setNom(nomModele);
				addModele(modele);
				updateAllComponents();
			}
	    });
	    
	    addMouseListener(new PopupListener());
	}
	
	public JPopupMenu getPopupMenu(){
		JPopupMenu popup = new JPopupMenu();
		popup.add(modeleManager.createMenuCreerModele());
		popup.add(itemEnregistrer);
		return popup;
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

	        	getPopupMenu().show(e.getComponent(),
	                       e.getX(), e.getY());
	        }
	    }
	}
}
