package expression.modele;

import expression.Expression;
import expression.Racine;
import expression.Parametre;

public class MRacine extends Modele {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8285554625111318703L;

	public MRacine(){
		nbParametres = 1;
		nbParametresMin=1;
		nbParametresModifiable=false;
	}

	public String getNom(){
		return "Racine carrée";
	}
	
	@Override
	public Expression genererExpression() {
		return new Racine(new Parametre(0));
	}

	@Override
	public String toString() {
		return "MRacine()";
	}

}
