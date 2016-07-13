package interfacegraphique;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import FeuilleExercice.Exercice;
import FeuilleExercice.FeuilleExercice;
import expression.modele.Modele;

public class JFeuilleFactory extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 937994771943125986L;
	
	FenetrePrincipale FP;
	
	FeuilleExercice feuilleExercice = new FeuilleExercice();
	ArrayList<JExercice> jexercices = new ArrayList<>();
	
	JLabel panelTitre = new JLabel("",JLabel.CENTER);
	
	int defaultNbItems = 6;
	int defaultNbCol = 3;

	public void init(FenetrePrincipale FP){
		this.FP = FP;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Font font = new Font("Arial",Font.BOLD,16);
		panelTitre.setFont(font);
		panelTitre.setText("<html><div style='text-align: center;'><u>Feuille d'exercice</html>");
		panelTitre.setAlignmentX(JFeuilleFactory.CENTER_ALIGNMENT);
		panelTitre.setAlignmentY(JFeuilleFactory.TOP_ALIGNMENT);
		add(panelTitre);
		addMouseListener(new PopupListener());
	}
	public void addExercice(Modele modele){
		String consigne = JOptionPane.showInputDialog(null,"Entrez la consigne de l'exercice :","Ajouter exercice",JOptionPane.QUESTION_MESSAGE);
		Exercice exo = new Exercice(consigne, modele, defaultNbItems, defaultNbCol);
		feuilleExercice.addExercice(exo);
		JExercice jexo = new JExercice(exo);
		jexo.setMaximumSize(new Dimension(10000,60));
		jexercices.add(jexo);
		add(jexo);
		jexo.init();
	}
	
	public int getNumero(Exercice exo){
		return feuilleExercice.getNumero(exo)+1;
	}
	
	public JPopupMenu createPopupMenu(){
		JPopupMenu popup = new JPopupMenu();
		JMenu menuCreerExercice = FP.getModeleManager().createMenuCreerExercice();
		popup.add(menuCreerExercice);
		return popup;
	}
	
	class PopupListener extends MouseAdapter {
	    public void mousePressed(MouseEvent e) {
	    	repaint();
	        maybeShowPopup(e);
	    }

	    public void mouseReleased(MouseEvent e) {
	    	maybeShowPopup(e);
	    }

	    private void maybeShowPopup(MouseEvent e) {
	        if (e.isPopupTrigger()) {
	        	createPopupMenu().show(e.getComponent(),
	                       e.getX(), e.getY());
	        }
	    }
	}
}
