package interfacegraphique;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import expression.Expression;
import expression.modele.MArbre;
import expression.modele.Modele;
import outils.Pdf2Image;

@SuppressWarnings("serial")
public class JModeleFactory extends JPanel {
	
	MArbre modelePrincipal = new MArbre();
	FenetrePrincipale FP;
	
	HashMap<Integer,JModele> id2jmodele = new HashMap<>();;
	int idModeleSelect = -1;
	int numeroSelect = -1;
	
	JMenuItem itemGenererStandard = new JMenuItem("Generer Expression");
	JMenuItem itemGenererLatex = new JMenuItem("Generer Latex");
	JMenuItem itemAfficherLatex = new JMenuItem("Afficher arbre");
	
	JLabel exempleExpression = new JLabel();

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
	
	public void init(FenetrePrincipale FP){
		this.FP = FP;
		setLayout(null);
		itemGenererStandard.addActionListener(new GenererStandardListener());
	    itemGenererLatex.addActionListener(new GenererLatexListener());
	    itemAfficherLatex.addActionListener(new AfficherListener());
	    
	    exempleExpression.setLocation(0,0);
	    exempleExpression.setSize(300,100);
	    exempleExpression.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.black));
	    exempleExpression.setBackground(Color.white);
	    exempleExpression.setOpaque(true);
	    exempleExpression.setAlignmentX(CENTER_ALIGNMENT);
	    exempleExpression.addMouseListener(new MouseAdapter(){
	    	@Override public void mousePressed(MouseEvent e) {
	            if(SwingUtilities.isLeftMouseButton(e)){
	            	updateExemple();
	            }
		    }
	    });
	    add(exempleExpression);
	    
	    addMouseListener(new PopupListener());
	}
	
	public void associer(){
		MArbre modele = modelePrincipal;
		resetModelePrincipal();
		modele.setNom("Nouveau Modele");
		addModele(modele);
	}
	
	public JPopupMenu createPopupMenu(){
		JPopupMenu popup = new JPopupMenu();
		
		// SOUS MENU : Creer
		popup.add(FP.getModeleManager().createMenuCreerModele());

		// ITEM : Enregistrer
		JMenuItem itemEnregistrer = new JMenuItem("Enregistrer");
		itemEnregistrer.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e) {
				String nomModele = JOptionPane.showInputDialog(null, "Entrez un nom de modele :", "Sauvegarder modele", JOptionPane.QUESTION_MESSAGE);
				try {
					FP.getModeleManager().inserer(modelePrincipal, nomModele);
				} catch (Exception e1) {
					e1.printStackTrace();
				}				
			}
	    });
		popup.add(itemEnregistrer);

		// ITEM : ASSOCIER
		JMenuItem itemAssocier = new JMenuItem("Associer");
		itemAssocier.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e) {
				associer();}
		});
		popup.add(itemAssocier);
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
		int idNouveauModele = modelePrincipal.addModele(nouveauModele);
		addJModele(idNouveauModele);
	}
	
	private void addJModele(int idNouveauModele){
		JModele jmodele = new JModele();
		this.add(jmodele);
        id2jmodele.put(idNouveauModele, jmodele);
        jmodele.init(idNouveauModele,popupX,popupY);
        popupX = 10;
        popupY = 10;
	}
	public void supprModele(int idModele){
		modelePrincipal.supprModele(idModele);
		supprJModele(idModele);
	}
	
	private void supprJModele(int idModele){
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

	        	createPopupMenu().show(e.getComponent(),
	                       e.getX(), e.getY());
	        }
	    }
	}
	
	public void dissocier(int idModele) {
		ArrayList<Integer> nouvellesId = modelePrincipal.dissocier(idModele);
		for(int id : nouvellesId){
			addJModele(id);
		}
		supprJModele(idModele);
	}
	
	public void updateExemple(){
		String TEMP_DIRECTORY = "temp";
        String TEMP_TEX_FILE_NAME = "Exemple"; // for New22.tex
        String exemple = modelePrincipal.genererExpression().toString(Expression.destinationLatex);
        // 1. Prepare the .tex file
        String newLineWithSeparation = System.getProperty("line.separator")+System.getProperty("line.separator");
        String math = "";
        math += "\\documentclass[border=0.50001bp,convert={convertexe={imgconvert},outext=.png}]{standalone}" + newLineWithSeparation;
        math += "\\usepackage{amsfonts}" + newLineWithSeparation;
        math += "\\usepackage{amsmath}" + newLineWithSeparation;
        math += "\\begin{document}" + newLineWithSeparation;
        math += "\\begin{Huge}" + newLineWithSeparation;
        math += "$"+exemple+"$"+ newLineWithSeparation;
        math += "\\end{Huge}" + newLineWithSeparation;
        math += "\\end{document}";

        // 2. Create the .tex file
        FileWriter writer = null;
        try {
            writer = new FileWriter(TEMP_DIRECTORY + "/" + TEMP_TEX_FILE_NAME + ".tex", false);
            writer.write(math, 0, math.length());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // 3. Execute LaTeX from command line  to generate pdf
        ProcessBuilder pb = new ProcessBuilder("pdflatex", "-shell-escape", TEMP_TEX_FILE_NAME + ".tex");
        pb.directory(new File(TEMP_DIRECTORY));
        try {
            Process p = pb.start();
            StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), false);
            StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream(), false);
            new Thread(fluxSortie).start();
            new Thread(fluxErreur).start();
            p.waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

        // 4. Convert pdf to png
        Pdf2Image.main(new String[] {TEMP_DIRECTORY + "/" +TEMP_TEX_FILE_NAME+".pdf",TEMP_DIRECTORY + "/" +TEMP_TEX_FILE_NAME});
        
        // 5. Display picture
        ImageIcon img=new ImageIcon(TEMP_DIRECTORY + "/" + TEMP_TEX_FILE_NAME + ".png");
        img.getImage().flush();
        exempleExpression.setIcon(img);
        updateAllComponents();
        
        /*
        // 6. Delete files
         for (File file : (new File(TEMP_DIRECTORY).listFiles())) {
         
            if (file.getName().startsWith(TEMP_TEX_FILE_NAME )) {
                file.delete();
            }
        }
        */
        
	}
	
	class StreamPrinter implements Runnable {

	    // Source: http://labs.excilys.com/2012/06/26/runtime-exec-pour-les-nuls-et-processbuilder/
	    private final InputStream inputStream;

	    private boolean print;

	    StreamPrinter(InputStream inputStream, boolean print) {
	        this.inputStream = inputStream;
	        this.print = print;
	    }

	    private BufferedReader getBufferedReader(InputStream is) {
	        return new BufferedReader(new InputStreamReader(is));
	    }

	    @Override
	    public void run() {
	        BufferedReader br = getBufferedReader(inputStream);
	        String ligne = "";
	        try {
	            while ((ligne = br.readLine()) != null) {
	                if (print) {
	                    System.out.println(ligne);
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
}
