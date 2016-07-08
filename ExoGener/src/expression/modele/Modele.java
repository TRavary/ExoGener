package expression.modele;
import java.util.ArrayList;

import expression.*;

abstract public class Modele {
	abstract public Expression genererExpression();
	abstract public String toString();
	
	
	ArrayList<Integer> variablesLibres;
	public int minVariables=0;
	public boolean nbVarModifiable=false;

	public String getNom(){
		return "Inconnu";
	}
	
	public void changeVariable(int indice, int nouvelleVariable){
		variablesLibres.set(indice, nouvelleVariable);
	}
	

	public int getNbVariablesLibres(){
		return variablesLibres.size();
	}
	
	public int getVariableLibre(int idVar){
		return variablesLibres.get(idVar);
	}
	
	public void addVariable(int nouvelleVariable){
		variablesLibres.add(nouvelleVariable);
	}
	
	public int supprVariable(){
		int variableSupprimee = variablesLibres.get(getNbVariablesLibres()-1);
		variablesLibres.remove(variablesLibres.size()-1);
		return variableSupprimee;
	}

}

