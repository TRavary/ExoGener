package expression.modele;

import java.util.ArrayList;

import expression.Entier;
import expression.Expression;

public class MEntier extends Modele {
	int min;
	int max;
	boolean avecZero;
	
	public MEntier(int min,int max,boolean avecZero){
		this.min = min;
		this.max = max;
		this.avecZero = avecZero;
		this.variablesLibres = new ArrayList<>();
		this.nbVarModifiable=false;
		this.minVariables=0;
	}
	
	
	public MEntier(int min,int max){
		this.min = min;
		this.max = max;
		this.avecZero = false;
		variablesLibres = new ArrayList<>();
	}
	
	public String getNom(){
		StringBuilder result = new StringBuilder();
		result.append("Entier(");
		result.append(min);
		result.append(",");
		result.append(max);
		result.append(")");
		return result.toString();
	}
	
	@Override
	public Expression genererExpression() {
		int valeur =0;
		if(avecZero || min>0 || max<0){
			valeur = (int)( Math.random()*(max+1 - min))+min;
		}
		else{
			valeur = (int)( Math.random()*(max+1-(min+1)))+min+1;
			if(valeur == 0){valeur = min;}
		}
		return new Entier(valeur);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("MEntier(");
		result.append(min);
		result.append(",");
		result.append(max);
		result.append(",");
		result.append(avecZero);
		result.append(")");
		return result.toString();
	}
}
