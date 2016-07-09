package expression.modele;

import java.io.Serializable;

import expression.*;

abstract public class Modele implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 672300605549655095L;
	
	
	abstract public Expression genererExpression();
	abstract public String toString();
	
	protected int nbParametres = 0;
	protected int nbParametresMin=0;
	protected boolean nbParametresModifiable=false;
	
	
	public String getNom(){
		return "Inconnu";
	}
	
	public int getNbParametres(){
		return nbParametres;
	}	
	public int addParametre(){
		// ajoute un parametre et renvoie le numero du parametre ajouté
		nbParametres+=1;
		return nbParametres-1;
	}
	public int supprParametre(){
		// Supprime un parametre et renvoie le numero du parametre supprimé
		nbParametres-=1;
		return nbParametres;
	}
	public boolean canAddParametre(){
		return nbParametresModifiable;
	}
	public boolean canSupprParametre(){
		return nbParametresModifiable && nbParametres>nbParametresMin;
	}
}