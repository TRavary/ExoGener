package expression.modele;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import interfacegraphique.FenetrePrincipale;

public class ModeleManager {
	FenetrePrincipale FP;
	ArrayList<String> nomModeles = new ArrayList<>();
	Map<String,Class<? extends Modele>> baseModeles = new HashMap<>();
	String PATH = "modeles";
	
	public void init(FenetrePrincipale FP){
		this.FP = FP;
		
		File path = new File(PATH);
		if(!(path.exists() && path.isDirectory())){path.mkdir();}
		
		addBaseModele("Variables et Nombres/Variable",MVariable.class);
		addBaseModele("Variables et Nombres/Entier",MEntier.class);
		addBaseModele("Variables et Nombres/Entier Aleatoire",MEntierAlea.class);
		addBaseModele("Operations/Somme",MSomme.class);
		addBaseModele("Operations/Produit",MProduit.class);
		addBaseModele("Operations/Quotient",MQuotient.class);
		addBaseModele("Operations/Puissance",MPuissance.class);
		addBaseModele("Operations/Opposé",MOppose.class);
		addBaseModele("Operations/Racine carrée",MRacine.class);
		addBaseModele("Divers/Parenthèses",MParenthese.class);
		addBaseModele("Divers/Liste",MListe.class);
		addBaseModele("Divers/Egalite", MEgalite.class);
		
		importModelesFrom(new File(PATH));
	}
	
	private void importModelesFrom(File repertoire){
		if(repertoire.isDirectory()){
            File[] list = repertoire.listFiles();
            if (list != null){
                for ( int i = 0; i < list.length; i++) {
                        // Appel récursif sur les sous-répertoires
                        importModelesFrom( list[i]);
                } 
            } else {
            	System.err.println(repertoire + " : Erreur de lecture.");
            }
		}
		else{
			nomModeles.add(repertoire.getPath());
		}
	}
	
	public void addBaseModele(String nomModele,Class<? extends Modele> classe){
		baseModeles.put(PATH+File.separator+nomModele,classe);
		nomModeles.add(PATH+File.separator+nomModele);
	}

	private Map<String,JMenuItem> createMenu(JMenu menu){
		Map<String,JMenuItem> nomModele2item = new HashMap<>();
		
		Map<String,JMenu> rep2menu = new HashMap<>();   // table de correspondance "nom complet du repertoire -> menu correspondant"
		rep2menu.put(PATH, menu);
		for(String nomModele : nomModeles){
			File fmodele = new File(nomModele);
			
			// cree les menus correspondants aux repertoires non encore rencontrés
			File repParents = fmodele.getParentFile();
			while(repParents!=null && !repParents.getPath().equals(PATH) && !rep2menu.containsKey(repParents.getPath()) ){
				rep2menu.put(repParents.getPath(), new JMenu(repParents.getName()));
				repParents = repParents.getParentFile();
			}
			// (re)link les menus entre eux
			repParents = fmodele.getParentFile();
			
			while(!repParents.getPath().equals(PATH) ){
				if(repParents.getParent() != null){
					JMenu curMenu = rep2menu.get(repParents.getPath());
					JMenu parentMenu = rep2menu.get(repParents.getParent());
					parentMenu.add(curMenu);
				}
				repParents = repParents.getParentFile();
			}
			repParents = fmodele.getParentFile();
			
			JMenuItem itemModele = new JMenuItem(fmodele.getName());
			itemModele.setName(nomModele);
			
			nomModele2item.put(nomModele, itemModele);
			
			if(repParents == null){
				menu.add(itemModele);
			}
			else{
				rep2menu.get(repParents.getPath()).add(itemModele);
			}
		}
		return nomModele2item;		
	}
	
	
	public JMenu createMenuCreerModele(){
		JMenu menuCreer = new JMenu("Creer modele");
		Map<String,JMenuItem> nomModele2item = createMenu(menuCreer);
		
		Iterator<String> iterNom = nomModele2item.keySet().iterator();
		while(iterNom.hasNext()){
			String nomModele = iterNom.next();
			JMenuItem itemModele = nomModele2item.get(nomModele);
			itemModele.addActionListener(new ActionListener(){
				@Override public void actionPerformed(ActionEvent e) {	
					String nomModele = ((JMenuItem)e.getSource()).getName();
					FP.getModeleFactory().addModele(newModele(nomModele));
				}
				
			});
		}
		return menuCreer;
	}
	
	public JMenu createMenuCreerExercice(){
		JMenu menuCreer = new JMenu("Creer exercice");
		Map<String,JMenuItem> nomModele2item = createMenu(menuCreer);
		
		Iterator<String> iterNom = nomModele2item.keySet().iterator();
		while(iterNom.hasNext()){
			String nomModele = iterNom.next();
			JMenuItem itemModele = nomModele2item.get(nomModele);
			itemModele.addActionListener(new ActionListener(){
				@Override public void actionPerformed(ActionEvent e) {	
					String nomModele = ((JMenuItem)e.getSource()).getName();
					FP.getFeuilleFactory().addExercice(newModele(nomModele));
				}
			});
		}
		return menuCreer;
	}
	
	
	
	public void inserer(Modele modele, String nomModele) throws Exception{
		File fmodele = new File(PATH+File.separator+nomModele);
		
		if(fmodele.exists() || nomModeles.contains(nomModele)){
			throw new Exception("Ce nom existe dejà");
		}
		
		// cree les nouveaux sous repertoires
		File dir = fmodele.getParentFile();
		if(dir!=null && !dir.exists()){dir.mkdirs();}
		
		
		ObjectOutputStream oos = null;
		try {
			final FileOutputStream fichier = new FileOutputStream(fmodele);
			oos = new ObjectOutputStream(fichier);
			oos.writeObject(modele);
		} catch (final java.io.IOException e1) {e1.printStackTrace();}
		finally {
			try {
				if (oos != null) {
					oos.flush();
					oos.close();
				}
			} catch (final IOException ex) {ex.printStackTrace();}
		}
		nomModeles.add(PATH+File.separator+nomModele);
	}
	
	public Modele newModele(String nomModele){
		if(baseModeles.containsKey(nomModele)){
			try {
				return baseModeles.get(nomModele).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		else{
			ObjectInputStream ois = null;
		    try {
		    	File fmodele = new File(nomModele);
		    	final FileInputStream fichier = new FileInputStream(fmodele);
		    	ois = new ObjectInputStream(fichier);
		    	Modele modele = (Modele) ois.readObject();
		    	((MArbre)modele).setNom(nomModele);
		    	return modele;
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
		}
		return null;
	}
	
	
	
}
