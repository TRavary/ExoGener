package expression.modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import expression.Expression;
import expression.VariableLibre;
import outils.Pair;

public class MArbre extends Modele {
	
	ArrayList<Modele> modeles;
	HashMap<Integer,Integer> variable2idModele;
	int idRacine;
	
	public MArbre(){
		variablesLibres = new ArrayList<>();
		modeles = new ArrayList<>();
		variable2idModele = new HashMap<>();
		idRacine = 0;
	}
	
	
	public int getNewVariable(){
		int newVariable = 0;
		while(variablesLibres.contains(Integer.valueOf(newVariable)))
		{
			newVariable+=1;
		}
		return newVariable;
	}
	
	
	
	public void addVariableTo(int idModele)
	{
		int newVariable = getNewVariable();
		modeles.get(idModele).addVariable(newVariable);
		variablesLibres.add(newVariable);
	}
	
	public void addModele(Modele nouveauModele){
		int minModele = 0;
		int maxModele = 0;
		if(nouveauModele.variablesLibres.size() != 0){
			minModele = Collections.min(nouveauModele.variablesLibres);
			maxModele = Collections.max(nouveauModele.variablesLibres);
		}
		int maxArbre = -1;
		if(variablesLibres.size()!=0){
			maxArbre = Collections.max(variablesLibres);
		}
		
		int decalage = Integer.max(maxModele, maxArbre)+1-minModele;
		if(minModele>maxArbre){decalage = 0;}
		for(int i=0;i<nouveauModele.variablesLibres.size();i++)
		{
			int newVar = nouveauModele.variablesLibres.get(i)+decalage;
			nouveauModele.changeVariable(i,newVar);
			variablesLibres.add(newVar);
		}
		modeles.add(nouveauModele);
	}
	
	
	public void creerLien(int variable,int idModele)
	{
		variable2idModele.put(variable, idModele);
	}
	
	public void supprimerLien(int variable)
	{
		variable2idModele.remove(variable);
	}
	
	@Override
	public Expression genererExpression() {
		HashMap<Integer,Expression> variable2Expression = new HashMap<>();
		Stack<Integer> pile = new Stack<>();
		
		Expression racine = modeles.get(idRacine).genererExpression();
		int var = racine.getNextVariableLibre(-1);
		while(var>=0)
		{
			if(variable2idModele.containsKey(var))
			{
				pile.push(var);
			}
			var = racine.getNextVariableLibre(var);
		}
		
		while(!pile.empty())
		{
			int curVar = pile.pop();
			if(!variable2Expression.containsKey(curVar)){
				Expression curExpression = modeles.get(variable2idModele.get(curVar)).genererExpression();
				variable2Expression.put(curVar, curExpression);
				pile.push(curVar);
				var = curExpression.getNextVariableLibre(-1);
				while(var>=0){
					if(variable2idModele.containsKey(var) && !variable2Expression.containsKey(var)){
						pile.push(var);
					}
					var = curExpression.getNextVariableLibre(var);
				}
			}
			else{
				Expression curExpression = variable2Expression.get(curVar);
				ArrayList<Pair<String,Expression>> liste = new ArrayList<>();
				var = curExpression.getNextVariableLibre(-1);
				while(var>=0)
				{
					if(variable2idModele.containsKey(var) && variable2Expression.containsKey(var))
					{
						liste.add(Pair.of(VariableLibre.getNom(var), variable2Expression.get(var)));
					}
					var = curExpression.getNextVariableLibre(var);
				}
				variable2Expression.put(curVar,curExpression.Remplacer(liste));
			}
		}
		
		
		ArrayList<Pair<String,Expression>> liste = new ArrayList<>();
		var = racine.getNextVariableLibre(-1);
		while(var>=0)
		{
			if(variable2idModele.containsKey(var) && variable2Expression.containsKey(var))
			{
				liste.add(Pair.of(VariableLibre.getNom(var), variable2Expression.get(var)));
			}
			var = racine.getNextVariableLibre(var);
		}
		
		return racine.Remplacer(liste);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("MArbre(\n");
		for(int i=0;i<modeles.size();i++)
		{
			result.append(i);
			result.append(":");
			result.append(modeles.get(i).toString());
			result.append("\n");
		}
		
		Iterator<Integer> iter=variable2idModele.keySet().iterator();
		while(iter.hasNext())
		{
			int i = iter.next();
			result.append("%");
			result.append(i);
			result.append("->");
			result.append(variable2idModele.get(i));
			result.append("\n");
		}
		result.append(")");
		return result.toString();
	}
}
