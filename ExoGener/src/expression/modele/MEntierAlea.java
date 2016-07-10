package expression.modele;

import expression.Entier;
import expression.Expression;

public class MEntierAlea extends Modele {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6165352218745831426L;
	
	int min;
	int max;
	boolean avecZero;
	
	public MEntierAlea(int min,int max,boolean avecZero){
		nbParametres = 0;
		nbParametresMin=0;
		nbParametresModifiable=false;
		this.min = min;
		this.max = max;
		this.avecZero = avecZero;
	}
	
	
	public MEntierAlea(int min,int max){
		this(min,max,false);
	}
	
	public MEntierAlea(){
		this(1,9);
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
		result.append("MEntierAlea(");
		result.append(min);
		result.append(",");
		result.append(max);
		result.append(",");
		result.append(avecZero);
		result.append(")");
		return result.toString();
	}
}