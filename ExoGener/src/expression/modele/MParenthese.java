package expression.modele;

import expression.Expression;
import expression.Parenthese;
import expression.Parametre;

public class MParenthese extends Modele {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5407984520999757886L;

	public MParenthese(){
		nbParametres = 1;
		nbParametresMin=1;
		nbParametresModifiable=false;
	}
	
	public String getNom(){
		return "Parentheses";
	}
	
	@Override
	public Expression genererExpression() {
		return new Parenthese(new Parametre(0));
	}

	@Override
	public String toString() {
		return "MParentheses()";
	}

}
