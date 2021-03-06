package expression.modele;

public class MEntier extends MEntierAlea {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1009477968584244448L;
 
	public MEntier(int valeur){
		super(valeur, valeur,true);
	}
	
	public MEntier(){
		this(0);
	}
	
	public int getValeur(){
		return min;
	}
	
	public void setValeur(int valeur){
		this.min = valeur;
		this.max = valeur;
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
