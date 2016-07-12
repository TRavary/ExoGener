package expression;

import java.util.ArrayList;

import outils.Pair;

public class Parametre extends Terme {
	int numero;

	public Parametre(int numero)
	{
		this.numero = numero;
		this.operandes = new ArrayList<>();
		priorite=Expression.prioriteParenthese;
	}
	
	public static String getString(int numero){
		String result = "param";
		result = result.concat(String.valueOf(numero));
		return result;
	}

	
	public static int getNumero(String nom){
		return Integer.valueOf(nom.substring(1));
	}
	
	@Override
	public String toString(String destination) {
		return Parametre.getString(numero);
	}

	@Override
	public Expression Remplacer(ArrayList<Pair<String, Expression>> liste) {
		for(int i=0;i<liste.size();i++)
		{
			if(getString(numero).equals(liste.get(i).left))
			{
				return liste.get(i).right;
			}
		}
		return new Parametre(numero);
	}
	
	@Override
	public boolean isVariable(String nom){
		if(this.toString().equals(nom)){return true;}
		return false;
	}
	
	@Override
	public int getNextVariableLibre(int curVar){
		if(numero>curVar){return numero;}
		return -1;
	}
}
