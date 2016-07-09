package expression.modele;

import expression.Expression;
import expression.Parametre;


public class MListe extends Modele {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1985105007296786983L;

	public MListe(){
		nbParametres = 2;
		nbParametresMin=2;
		nbParametresModifiable=true;
	}
	public String getNom(){
		return "Liste";
	}
	@Override
	public Expression genererExpression() {
		double seuil = Math.random();
		double u = 0;
		for(int i=0;i<nbParametres;i++){
			u+=1./nbParametres;
			if(u>seuil)
			{
				return new Parametre(i);
			}
		}
		return new Parametre(nbParametres-1);
	}

	@Override
	public String toString() {
		return String.format("MListe(%d)",nbParametres);
	}
}
