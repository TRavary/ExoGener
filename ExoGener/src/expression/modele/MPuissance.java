package expression.modele;

import expression.Expression;
import expression.Puissance;
import expression.Parametre;

public class MPuissance extends Modele {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5766280602780787930L;

	public MPuissance(){
		nbParametres = 2;
		nbParametresMin=2;
		nbParametresModifiable=false;
	}
	
	public String getNom(){
		return "Puissance";
	}
	
	@Override
	public Expression genererExpression() {
		return new Puissance(new Parametre(0),new Parametre(1));
	}

	@Override
	public String toString() {
		return "MPuissance()";
	}

}
