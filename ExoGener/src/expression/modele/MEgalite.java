package expression.modele;

import expression.Egalite;
import expression.Expression;
import expression.Parametre;

public class MEgalite extends Modele {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5766280602780787930L;

	public MEgalite(){
		nbParametres = 2;
		nbParametresMin=2;
		nbParametresModifiable=false;
	}
	
	public String getNom(){
		return "Egalite";
	}
	
	@Override
	public Expression genererExpression() {
		return new Egalite(new Parametre(0),new Parametre(1));
	}

	@Override
	public String toString() {
		return "MEgalite()";
	}

}

