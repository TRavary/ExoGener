package expression.modele;

import java.util.ArrayList;

import expression.Expression;
import expression.Produit;
import expression.Parametre;

public class MProduit extends Modele {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3467384633779516960L;

	public MProduit(){
		nbParametres = 2;
		nbParametresMin=2;
		nbParametresModifiable=true;
	}
	
	public String getNom(){
		return "Produit";
	}
	
	@Override
	public Expression genererExpression() {
		ArrayList<Expression> variables = new ArrayList<>();
		for(int i=0;i<nbParametres;i++){
			variables.add(new Parametre(i));
		}
		return new Produit(variables);
	}

	@Override
	public String toString() {
		return String.format("MSomme(%d)", nbParametres);
	}

}
