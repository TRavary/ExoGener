package expression.modele;

import expression.Expression;
import expression.Variable;

public class MVariable extends Modele{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8495831299701953522L;
	String nomVariable;
	
	public MVariable(String nomVariable){
		nbParametres = 0;
		nbParametresMin=0;
		nbParametresModifiable=false;
		this.nomVariable = nomVariable;
	}
	
	public MVariable(){
		this("x");
	}
	
	public String getNom(){
		return nomVariable;
	}
	
	public void setNom(String nom){
		nomVariable = nom;
	}

	@Override
	public Expression genererExpression() {
		return new Variable(nomVariable);
	}

	@Override
	public String toString() {
		return String.format("MVariable(%s)",nomVariable);
	}
}
