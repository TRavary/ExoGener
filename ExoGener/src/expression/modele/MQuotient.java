package expression.modele;

import expression.Expression;
import expression.Quotient;
import expression.Parametre;

public class MQuotient extends Modele {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7411730826746091543L;

	public MQuotient(){
		nbParametres = 2;
		nbParametresMin=2;
		nbParametresModifiable=true;
	}
	
	public String getNom(){
		return "Quotient";
	}
	@Override
	public Expression genererExpression() {
		return new Quotient(new Parametre(0),new Parametre(1));
	}

	@Override
	public String toString() {
		return "MQuotient";
	}

}
