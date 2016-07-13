package interfacegraphique;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import FeuilleExercice.Exercice;

public class JExercice extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2069988936123039376L;
	
	Exercice exercice;
	JLabel label = new JLabel();
	int marge = 1;
	
	public JExercice(Exercice exercice){
		this.exercice = exercice;
		
		add(label);
		label.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(marge, marge, marge, marge),
						BorderFactory.createLineBorder(Color.black))
				);
	}
	
	public void init(){
		updateText();
	}
	
	public JFeuilleFactory getJFeuilleFactory(){
		return (JFeuilleFactory)SwingUtilities.getAncestorOfClass(JFeuilleFactory.class, this);
	}
	
	public void updateText(){
		label.setText(String.format(
				"<html><div align=left><b><u>Exercice %d :</b></u> %s<br>"
				+ "\tModele : %s<br>"
				+ "\t%d items sur %d colonnes</div></html>",
				getJFeuilleFactory().getNumero(exercice),
				exercice.getConsigne(),
				exercice.getModele().getNom(),
				exercice.getNbItems(),
				exercice.getNbCol()));
	}		
}
