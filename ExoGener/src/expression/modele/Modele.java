package expression.modele;
import java.util.ArrayList;

import expression.*;

abstract public class Modele {
	abstract public Expression genererExpression();
	abstract public String toString();
	
	
	ArrayList<Integer> variablesLibres;
	int minVariables;
	boolean nbVarModifiable;

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
	
	public void deleteVariable(){
		variablesLibres.remove(variablesLibres.size()-1);
	}

}

