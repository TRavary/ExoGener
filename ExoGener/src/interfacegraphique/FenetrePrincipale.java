package interfacegraphique;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import expression.modele.MArbre;
import expression.modele.Modele;
import expression.modele.ModeleManager;
import outils.DeepCopy;



@SuppressWarnings("serial")
public class FenetrePrincipale extends JFrame {
	private JFeuilleFactory feuilleFactory = new JFeuilleFactory();
	private JModeleFactory modeleFactory = new JModeleFactory();
	JSplitPane jsp = new JSplitPane();
	
	private ModeleManager modeleManager = new ModeleManager();
	
	String feuillePATH = "tex/feuilleExercice/";
		
	JMenuBar menuBar = new JMenuBar();
	JMenu menuOutils = new JMenu("Outils");
	JMenu menuFeuille = new JMenu("Feuille d'exercice");
	
	public FenetrePrincipale(){
	    this.setTitle("ExoGener");
	    this.setSize(800, 600);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
	    this.setVisible(true);
	    this.setContentPane(jsp);	    
	    
	    jsp.setDividerLocation(160);
	    jsp.setLeftComponent(feuilleFactory);
	    jsp.setRightComponent(getModeleFactory());
	    
	    getModeleManager().init(this);
	    getModeleFactory().init(this);
	    getFeuilleFactory().init(this);
	    
	    menuOutils.add(getModeleFactory().itemGenererStandard);
	    menuOutils.add(getModeleFactory().itemGenererLatex);
	    menuOutils.add(getModeleFactory().itemAfficherLatex);
	    
	    
	    
	    JMenuItem itemAjouterExercice = new JMenuItem("Ajouter un exercice");
	    itemAjouterExercice.addActionListener(new ActionListener(){
	    	@Override public void actionPerformed(ActionEvent arg0) {
				Modele modele = (Modele) DeepCopy.of(modeleFactory.modelePrincipal);
				((MArbre)modele).setNom("Pas de nom");
				feuilleFactory.addExercice(modele);
	    	}
	    });
	    menuFeuille.add(itemAjouterExercice);
	    
	    JMenuItem itemGenererLatex = new JMenuItem("Generer le .tex");
	    itemGenererLatex.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e) {
				String nomFichier = JOptionPane.showInputDialog(null,"Entrez le nom du fichier","Generer le .tex",JOptionPane.QUESTION_MESSAGE);
				genererLatex(nomFichier);
			}
	    	
	    });
	    menuFeuille.add(itemGenererLatex);
	    
	    
	    JMenuItem itemGenererPDF = new JMenuItem("Generer le .pdf");
	    itemGenererPDF.addActionListener(new ActionListener(){
	    	@Override public void actionPerformed(ActionEvent e) {
	    		String nomFichier = JOptionPane.showInputDialog(null,"Entrez le nom du fichier","Generer le .pdf",JOptionPane.QUESTION_MESSAGE);
	    		genererLatex(nomFichier);
	    		latex2pdf(nomFichier);
	    		ouvrirPDF(nomFichier);
	    	}
	    	
	    });
	    menuFeuille.add(itemGenererPDF);
	    
	    JMenuItem itemOuvrirPDF = new JMenuItem("Ouvrir le .pdf");
	    itemOuvrirPDF.addActionListener(new ActionListener(){
	    	@Override public void actionPerformed(ActionEvent e) {
	    		String nomFichier = JOptionPane.showInputDialog(null,"Entrez le nom du fichier","Ouvrir .pdf",JOptionPane.QUESTION_MESSAGE);
	    		ouvrirPDF(nomFichier);
	    	}
	    	
	    });
	    menuFeuille.add(itemOuvrirPDF);
	    
	    JMenuItem itemLaTotale = new JMenuItem("La Totale !");
	    itemLaTotale.addActionListener(new ActionListener(){
	    	@Override public void actionPerformed(ActionEvent e) {
	    		String nomFichier = JOptionPane.showInputDialog(null,"Entrez le nom du fichier","La Totale",JOptionPane.QUESTION_MESSAGE);
	    		genererLatex(nomFichier);
	    		latex2pdf(nomFichier);
	    		ouvrirPDF(nomFichier);
	    	}
	    	
	    });
	    menuFeuille.add(itemLaTotale);
	    
	    
	    menuBar.add(menuOutils);
	    menuBar.add(menuFeuille);
	    this.setJMenuBar(menuBar);

	    this.setVisible(true);
	}
	
	
	public JFeuilleFactory getFeuilleFactory() {
		return feuilleFactory;
	}


	void genererLatex(String nomFichier){
		String latex = "";
		try {
			latex = feuilleFactory.feuilleExercice.genererLatex();
		} catch (IOException e1) {e1.printStackTrace();}
		try {
			PrintWriter out = new PrintWriter(feuillePATH+nomFichier+".tex");
			out.println(latex);
			out.close();
		} catch (FileNotFoundException e1) {e1.printStackTrace();}
	}
	
	void latex2pdf(String nomFichier){
		Runtime runtime = Runtime.getRuntime();
		final Process process;
		try {
			//process = new ProcessBuilder("pdflatex","-synctex=1 -interaction=nonstopmode "+feuillePATH+nomFichier+".tex").start();
			process = runtime.exec(new String[] {"pdflatex",
					"-interaction=nonstopmode",
					"-aux-directory",feuillePATH+"Auxiliaires",
					"-output-directory",feuillePATH,
					feuillePATH+nomFichier+".tex"});
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		// Consommation de la sortie standard de l'application externe dans un Thread separe
		new Thread() {
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String line = "";
					try {
						while((line = reader.readLine()) != null) {
							// Traitement du flux de sortie de l'application si besoin est
							System.out.println(line);
						}
					} finally {
						reader.close();
					}
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}.start();
		
		// Consommation de la sortie d'erreur de l'application externe dans un Thread separe
		new Thread() {
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					String line = "";
					try {
						while((line = reader.readLine()) != null) {
							// Traitement du flux d'erreur de l'application si besoin est
							System.out.println(line);
						}
					} finally {
						reader.close();
					}
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}.start();
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void ouvrirPDF(String nomFichier){
		final Process process;
		try {
			process = new ProcessBuilder("C:/Program Files (x86)/Adobe/Reader 10.0/Reader/AcroRd32.exe", feuillePATH+nomFichier+".pdf").start();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		// Consommation de la sortie standard de l'application externe dans un Thread separe
		new Thread() {
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String line = "";
					try {
						while((line = reader.readLine()) != null) {
							// Traitement du flux de sortie de l'application si besoin est
							System.out.println(line);
						}
					} finally {
						reader.close();
					}
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}.start();
		
		// Consommation de la sortie d'erreur de l'application externe dans un Thread separe
		new Thread() {
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					String line = "";
					try {
						while((line = reader.readLine()) != null) {
							// Traitement du flux d'erreur de l'application si besoin est
							System.out.println(line);
						}
					} finally {
						reader.close();
					}
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}.start();
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public JModeleFactory getModeleFactory() {
		return modeleFactory;
	}

	public ModeleManager getModeleManager() {
		return modeleManager;
	}
}




