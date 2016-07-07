package expression.modele;

import java.util.ArrayList;

public class MManager extends MArbre {
	
	public MManager(){
		
	}
	
	public Modele getModele(int idModele){
		return modeles.get(idModele);
	}
	
	public int getLastIdModele(){
		return modeles.size()-1;
	}
	
	public ArrayList<Integer> getVariablesLibres(){
		return variablesLibres;
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
	
	public void changeVariable(int idModele,int idVar, int variable){
		int ancienneVariable = modeles.get(idModele).getVariableLibre(idVar);
		modeles.get(idModele).changeVariable(idVar, variable);
		
		boolean noMore = true;
		for(int i=0;i<modeles.size();i++){
			Modele m = modeles.get(i);
			for(int j=0;j<m.getNbVariablesLibres();j++){
				if(m.getVariableLibre(j)==ancienneVariable){
					noMore = false;
				}
			}
		}
		
		if(noMore){
			variablesLibres.remove(variablesLibres.indexOf(ancienneVariable));
		}
		if(!variablesLibres.contains(variable)){
			variablesLibres.add(variable);
		}
		
	}
	
}
