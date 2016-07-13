package expression;

import java.util.ArrayList;

import outils.Pair;

public class Egalite extends Operateur {

	public Egalite(Expression op1, Expression op2){
		priorite = Expression.prioriteEgalite;
		symbole = "=";
		operandes = new ArrayList<>();
		operandes.add(op1);
		operandes.add(op2);
	}
	
	@Override
	public String toString(String destination) {
		StringBuilder result = new StringBuilder();
		
		Expression op0 = operandes.get(0);
		if(op0.priorite<=this.priorite){
			result.append("(");
			result.append(op0.toString(destination));
			result.append(")");
		}
		else{
			result.append(op0.toString(destination));
		}
		
		Expression op1 = operandes.get(1);
		result.append(symbole);
		if(destination.equals(Expression.destinationLatex)){
			result.append("{");
			result.append(op1.toString(destination));
			result.append("}");
		}
		else if(op1.priorite<=this.priorite)
		{
			result.append("(");
			result.append(op1.toString(destination));
			result.append(")");
		}
		else{
			result.append(op1.toString(destination));
		}
		
		return result.toString();
	}

	@Override
	public Expression Remplacer(ArrayList<Pair<String, Expression>> liste) {
		return new Egalite(operandes.get(0).Remplacer(liste),operandes.get(1).Remplacer(liste));
	}
}
