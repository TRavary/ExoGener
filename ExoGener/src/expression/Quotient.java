package expression;

import java.util.ArrayList;

import outils.Pair;

public class Quotient extends Operateur {

	public Quotient(Expression op1,Expression op2){
		priorite = Expression.prioriteProduit;
		symbole = "/";
		operandes = new ArrayList<>();
		operandes.add(op1);
		operandes.add(op2);
	}
	
	@Override
	public String toString(String destination) {
		StringBuilder result = new StringBuilder();
		
		if(destination.equals(Expression.destinationLatex)){
			result.append("\\frac{");
			result.append(operandes.get(0).toString(destination));
			result.append("}{");
			result.append(operandes.get(1).toString(destination));
			result.append("}");
		}
		else
		{
			Expression op0 = operandes.get(0);
			if(op0.priorite<this.priorite){
				result.append("(");
				result.append(op0.toString(destination));
				result.append(")");
			}
			else{
				result.append(op0.toString(destination));
			}
			
			Expression op1 = operandes.get(1);
			result.append(symbole);
			if(op1.priorite<=this.priorite)
			{
				result.append("(");
				result.append(op1.toString(destination));
				result.append(")");
			}
			else{
				result.append(op1.toString(destination));
			}
			
		}
		return result.toString();
	}
	
	public static void test(){
		Entier deux = new Entier(2);
		Entier m1 = new Entier(-1);
		Variable x = new Variable("x");
		
		System.out.println("Test quotient :");
		System.out.print("2/2 -> ");
		System.out.println(new Quotient(deux,deux));
		
		System.out.print("-1/(-1) -> ");
		System.out.println(new Quotient(m1,m1));
		
		System.out.print("((-1)/(-1))/(-1) -> ");
		System.out.println(new Quotient(new Quotient(m1,m1),m1));
		
		System.out.print("(-1)/((-1)/(-1)) -> ");
		System.out.println(new Quotient(m1,new Quotient(m1,m1)));
		
		System.out.print("((-1)+(-1))/((-1)*(-1)) -> ");
		System.out.println(new Quotient(new Somme(m1,m1),new Produit(m1,m1)));
		
		System.out.print("((-1)^(-1))/Racine(-1) -> ");
		System.out.println(new Quotient(new Puissance(m1,m1),new Racine(m1)));
		
		
		System.out.print("parenthese(x+x)/oppose(x-x) -> ");
		System.out.println(new Quotient(new Parenthese(new Somme(x,x)),new Oppose(new Somme(x,new Oppose(x)))));
		
		System.out.println("");
	}

	@Override
	public Expression Remplacer(ArrayList<Pair<String, Expression>> liste) {
		return new Quotient(operandes.get(0).Remplacer(liste),operandes.get(1).Remplacer(liste));
	}
}

