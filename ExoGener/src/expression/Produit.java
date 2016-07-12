package expression;

import java.util.ArrayList;

import outils.Pair;

public class Produit extends Operateur {
	
	public Produit(Expression op1, Expression op2, Expression... ops){
		priorite = Expression.prioriteProduit;
		symbole = "*";
		operandes = new ArrayList<>();
		operandes.add(op1);
		operandes.add(op2);
		for(int i=0;i<ops.length;i++){operandes.add(ops[i]);}
	}

	public Produit(ArrayList<Expression> ops)
	{
		priorite = Expression.prioriteProduit;
		symbole = "*";
		operandes = new ArrayList<>();
		for(int i=0;i<ops.size();i++){operandes.add(ops.get(i));}
	}

	@Override
	public String toString(String destination){
		if(destination.equals(Expression.destinationLatex)){
			symbole ="\\times ";
			
		}
		else {
			symbole ="*";
		}
		return super.toString(destination);
	}
	
	
	
	public static void test(){
		Entier deux = new Entier(2);
		Entier m1 = new Entier(-1);
		Variable x = new Variable("x");
		
		System.out.println("Test produit :");
		System.out.print("2*2 -> ");
		System.out.println(new Produit(deux,deux));
		
		System.out.print("-1*(-1) -> ");
		System.out.println(new Produit(m1,m1));
		
		System.out.print("-1*(-1)*(-1)*(-1)*(-1) -> ");
		System.out.println(new Produit(m1,m1,m1,m1,m1));
		
		System.out.print("((-1)+(-1))*((-1)*(-1))*((-1)/(-1))*((-1)^(-1))*Racine(-1) -> ");
		System.out.println(new Produit(new Somme(m1,m1),new Produit(m1,m1),new Quotient(m1,m1),new Puissance(m1,m1),new Racine(m1)));
		
		System.out.print("parenthese(x+x)*oppose(x*oppose(x)) -> ");
		System.out.println(new Produit(new Parenthese(new Somme(x,x)),new Oppose(new Produit(x,new Oppose(x)))));
		
		System.out.println("");
	}

	@Override
	public Expression Remplacer(ArrayList<Pair<String, Expression>> liste) {
		ArrayList<Expression> newOps = new ArrayList<>();
		for(int i = 0;i<nbTermes();i++){
			newOps.add(operandes.get(i).Remplacer(liste));
		}
		return new Produit(newOps);
	}
}
