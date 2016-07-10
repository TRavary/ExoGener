package expression.modele;

import java.util.ArrayList;

import expression.Expression;
import expression.Somme;
import expression.Parametre;

public class MSomme extends Modele {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7615269343944771247L;

	public MSomme(){
		nbParametres = 2;
		nbParametresMin=2;
		nbParametresModifiable=true;
	}
	
	public String getNom(){
		return "Somme";
	}
	
	@Override
	public Expression genererExpression() {
		ArrayList<Expression> variables = new ArrayList<>();
		for(int i=0;i<nbParametres;i++){
			variables.add(new Parametre(i));
		}
		return new Somme(variables);
	}

	@Override
	public String toString() {
		return String.format("MSomme(%d)", nbParametres);
	}

}
