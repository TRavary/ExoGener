package expression;

import java.util.ArrayList;
import outils.Pair;

public abstract class Expression {
	protected int priorite;
	protected ArrayList<Expression> operandes;
	public int nbTermes(){
		return operandes.size();
	}
	
	static int prioriteParenthese = 10;
	static int prioritePuissance = 7;
	static int prioriteProduit = 5;
	static int prioriteSomme = 1;
	static int prioriteEgalite = 0;
	/* 10 -> Terme () fonctions
	 * 9  ->
	 * 8  -> 
	 * 7  -> ^
	 * 6  ->
	 * 5  -> * / oppose
	 * 4  -> 
	 * 3  -> 
	 * 2  -> 
	 * 1  -> +  
	 * 0  -> =
	 */
	
	public static void test(){
		Somme.test();
		Produit.test();
		Quotient.test();
	}
	
	public abstract String toString(String destination);
	public static String destinationLatex = "Latex";
	public static String destinationStandard = "Standard";

	@Override 
	public String toString()
	{
		return toString(destinationStandard);
	}
	
	public boolean isVariable(String nom){
		for(int i=0;i<operandes.size();i++){
			if(operandes.get(i).isVariable(nom)){return true;}
		}
		return false;
	}
	
	public int getNextVariableLibre(int curVar)
	{
		int minVar = -1;
		for(int i=0;i<operandes.size();i++){
			int operandeVar = operandes.get(i).getNextVariableLibre(curVar);
			if(operandeVar > curVar){
				if(minVar <= curVar){minVar = operandeVar;}
				else{minVar = Integer.min(minVar, operandeVar);}
			}
		}
		return minVar;
	}
	
	abstract public Expression Remplacer(ArrayList<Pair<String,Expression>> liste);
	
	
	
	public Expression Remplacer(@SuppressWarnings("unchecked") Pair<String,Expression>... args){
		ArrayList<Pair<String,Expression>> liste= new ArrayList<>();
		for(int i=0;i<args.length;i++){
			liste.add(args[i]);
		}
		return Remplacer(liste);
	}
}
