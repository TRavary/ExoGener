package expression.modele;

import java.util.ArrayList;

import expression.Expression;
import expression.Variable;

public class MVariable extends Modele{
	String nomVariable;
	
	public MVariable(String nomVariable){
		this.nomVariable = nomVariable;
		this.variablesLibres = new ArrayList<>();
		this.nbVarModifiable=false;
		this.minVariables=0;
	}
	
	public String getNom(){
		return nomVariable;
	}

	@Override
	public Expression genererExpression() {
		return new Variable(nomVariable);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("MVariable(");
		result.append(nomVariable);
		result.append(")");
		return result.toString();
	}
}
