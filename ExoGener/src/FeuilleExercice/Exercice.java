package FeuilleExercice;

import expression.modele.Modele;

public class Exercice {
	Modele modele;
	private int nbItems;
	private int nbCol;
	String consigne = "Je suis la consigne";
	
	public Exercice(String consigne, Modele modele,int nbItems,int nbCol){
		this.consigne = consigne;
		this.modele = modele;
		this.setNbItems(nbItems);
		this.setNbCol(nbCol);
	}
	
	public String getConsigne(){
		return consigne;
	}
	
	public String getItem(){
		return modele.genererExpression().toString("Latex");
	}
	
	public Modele getModele(){
		return modele;
	}

	public int getNbItems() {
		return nbItems;
	}

	public void setNbItems(int nbItems) {
		this.nbItems = nbItems;
	}

	public int getNbCol() {
		return nbCol;
	}

	public void setNbCol(int nbCol) {
		this.nbCol = nbCol;
	}
}
