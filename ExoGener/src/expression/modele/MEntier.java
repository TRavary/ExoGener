package expression.modele;

public class MEntier extends MEntierAlea {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1009477968584244448L;
 
	public MEntier(int valeur){
		super(valeur, valeur,true);
	}
	
	public int getValeur(){
		return min;
	}
	
	@Override
	public String getNom() {
		return String.valueOf(getValeur());
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("MEntier(");
		result.append(min);
		result.append(")");
		return result.toString();
	}
}
