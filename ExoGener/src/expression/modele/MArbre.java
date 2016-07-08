package expression.modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import expression.Expression;
import expression.Variable;
import expression.VariableLibre;
import outils.Pair;

public class MArbre extends Modele {
	
	HashMap<Integer,Modele> id2modele;
	HashMap<Integer,Integer> variable2idModele;
	public int idRacine = -1;
	int lastIdModele = -1;
	
	public MArbre(){
		variablesLibres = new ArrayList<>();
		id2modele = new HashMap<>();
		variable2idModele = new HashMap<>();
		this.nbVarModifiable=false;
		this.minVariables=0;
	}
	
	public void setRacine(int idModele){
		idRacine = idModele;
	}
	
	
	public int getLastIdModele(){
		return lastIdModele;
	}
	public int getNewVariable(){
		int newVariable = 0;
		while(variablesLibres.contains(Integer.valueOf(newVariable)))
		{
			newVariable+=1;
		}
		return newVariable;
	}
	public int getNewIdModele(){
		int id = 0;
		while(id2modele.containsKey(id)){id++;}
		return id;
	}
	public int getVariableLinkedTo(int idModele){
		Iterator<Integer> iter= variable2idModele.keySet().iterator();
		while(iter.hasNext()){
			int variable = iter.next();
			if(variable2idModele.get(variable)==idModele){
				return variable;
			}
		}
		return -1;
	}
	public Modele getModele(int idModele){
		return id2modele.get(idModele);
	}
	public ArrayList<Integer> getVariablesLibres(){
		return variablesLibres;
	}
	
	
	
	public void addVariableTo(int idModele)
	{
		int newVariable = getNewVariable();
		id2modele.get(idModele).addVariable(newVariable);
		variablesLibres.add(newVariable);
	}
	public void supprVariableTo(int idModele)
	{
		int variable = id2modele.get(idModele).supprVariable();
		
		boolean noMore = true;
		Iterator<Integer> iter=id2modele.keySet().iterator();
		while(iter.hasNext())
		{
			int i = iter.next();
			Modele m = id2modele.get(i);
			for(int j=0;j<m.getNbVariablesLibres();j++){
				if(m.getVariableLibre(j)==variable){
					noMore = false;
				}
			}
		}
		
		if(noMore){
			variablesLibres.remove(variablesLibres.indexOf(variable));
		}
	}
	public void changeVariable(int idModele,int idVar, int variable){
		int ancienneVariable = id2modele.get(idModele).getVariableLibre(idVar);
		id2modele.get(idModele).changeVariable(idVar, variable);
		
		// Verification de la suppression de la derniere occurence de l'ancienne variable
		boolean noMore = true;
		Iterator<Integer> iter=id2modele.keySet().iterator();
		while(iter.hasNext())
		{
			int i = iter.next();
			Modele m = id2modele.get(i);
			for(int j=0;j<m.getNbVariablesLibres();j++){
				if(m.getVariableLibre(j)==ancienneVariable){
					noMore = false;
				}
			}
		}
		
		// Suppression du lien de l'ancienne si c'était la derniere
		// Suppression de l'ancienne de variablesLibres si c'était la derniere
		if(noMore){
			variable2idModele.remove(ancienneVariable);
			variablesLibres.remove(variablesLibres.indexOf(ancienneVariable));
		}
		
		// Ajout de la nouvelle dans variablesLibres
		if(!variablesLibres.contains(variable)){
			variablesLibres.add(variable);
		}
		
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
		
		if(id2modele.size()==0){
			setRacine(0);
		}
		lastIdModele = getNewIdModele();
		id2modele.put(lastIdModele,nouveauModele);
	}
	public void supprModele(int idModele) {
		id2modele.remove(idModele);
		int variable = getVariableLinkedTo(idModele);
		if(variable != -1){variable2idModele.remove(variable);}
		
		if(idModele == idRacine){
			if(id2modele.size() ==0){idRacine=-1;}
			else{idRacine = id2modele.keySet().iterator().next();}
		}
	}
	
	
	public void addLien(int variable,int idModele)
	{
		variable2idModele.put(variable, idModele);
	}
	public void supprLien(int variable)
	{
		variable2idModele.remove(variable);
	}
	public boolean isLien(int variable){
		return variable2idModele.containsKey(variable);
	}
	public int getIdLien(int variable){
		if(isLien(variable)){
			return variable2idModele.get(variable);
		}
		return -1;
	}
	
	
	@Override
	public Expression genererExpression() {
		if(id2modele.size() == 0){return new Variable("ExpressionVide");}
		HashMap<Integer,Expression> variable2Expression = new HashMap<>();
		Stack<Integer> pile = new Stack<>();
		
		Expression racine = id2modele.get(idRacine).genererExpression();
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
				Expression curExpression = id2modele.get(variable2idModele.get(curVar)).genererExpression();
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
		Iterator<Integer> iter=id2modele.keySet().iterator();
		while(iter.hasNext())
		{
			int i = iter.next();
			result.append(i);
			result.append(":");
			result.append(id2modele.get(i).toString());
			result.append("\n");
		}
		
		iter=variable2idModele.keySet().iterator();
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
