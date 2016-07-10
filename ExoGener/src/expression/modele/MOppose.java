package expression.modele;

import expression.Expression;
import expression.Oppose;
import expression.Parametre;

public class MOppose extends Modele {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6183778465165381696L;

	public MOppose(){
		nbParametres = 1;
		nbParametresMin=1;
		nbParametresModifiable=false;
	}
	public String getNom(){
		return "Oppose";
	}
	@Override
	public Expression genererExpression() {
		return new Oppose(new Parametre(0));
	}

	@Override
	public String toString() {
		return "MOppose()";
	}

}
