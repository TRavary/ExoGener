package expression.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import expression.Expression;
import expression.Variable;
import expression.Parametre;
import outils.Pair;

public class MArbre extends Modele {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5455761430262498679L;
	
	private HashMap<Integer,Modele> id2modele = new HashMap<>();
	private HashMap<Integer,ArrayList<EtatParametre>> parametres = new HashMap<>();
	// "parametres.get(idModele)" : liste des etats de chaque parametre associé aux modele de l'arbre
	 
	
	private int idRacine = -1;
	private int lastIdModele = -1;
	
	public MArbre(){
		nbParametres = 0;
		nbParametresMin=0;
		nbParametresModifiable=false;
	}
	
	public String getNom(){
		return "Arbre";
	}
	
	public void setRacine(int idModele){
		idRacine = idModele;
	}
	public int getLastIdModele(){
		return lastIdModele;
	}
	public int getRacine(){
		return idRacine;
	}
	
	public Modele getModele(int idModele){
		return id2modele.get(idModele);
	}
	private int getNewIdModele(){
		int id = 0;
		while(id2modele.containsKey(id)){id++;}
		return id;
	}
	
	private int getNewIdParametre(){
		return nbParametres;
	}
	
	public void addModele(Modele nouveauModele){
		int idModele = getNewIdModele();
		parametres.put(idModele, new ArrayList<>());
		for(int i=0;i<nouveauModele.getNbParametres();i++){
			addNewParametre(idModele);
		}
		
		if(id2modele.size()==0){setRacine(0);}
		id2modele.put(idModele,nouveauModele);
		lastIdModele = idModele;
	}
	
	private void addNewParametre(int idModele){
		parametres.get(idModele).add(new EtatParametre(getNewIdParametre(),false));
		nbParametres+=1;
	}
	
	public void addParametreTo(int idModele)
	{
		getModele(idModele).addParametre();
		addNewParametre(idModele);
	}
	
	
	public void supprParametreTo(int idModele)
	{
		int numeroParametre = getModele(idModele).supprParametre();
		EtatParametre etat = parametres.get(idModele).get(numeroParametre);
		if(!etat.isLien && isLastOccurenceOf(etat.getNumero())){
			supprTrou(etat.getNumero());
			nbParametres-=1;
		}
		parametres.get(idModele).remove(numeroParametre);
	}
	
	public boolean isLastOccurenceOf(int numero){
		int nb = 0;
		Iterator<Integer> iter = parametres.keySet().iterator();
		while(iter.hasNext()){
			int idModele = iter.next();
			for(int i=0;i<parametres.get(idModele).size();i++){
				EtatParametre e =parametres.get(idModele).get(i);
				if(!e.isLien && e.getNumero()==numero){
					nb+=1;
				}
			}
		}
		return nb ==1;
	}
	
	private void supprTrou(int numero){
		Iterator<Integer> iter = parametres.keySet().iterator();
		while(iter.hasNext()){
			int idModele = iter.next();
			for(int i=0;i<parametres.get(idModele).size();i++){
				EtatParametre e =parametres.get(idModele).get(i);
				if(!e.isLien && e.getNumero()>numero){
					e.setNumero(e.getNumero()-1);
				}
				
			}
		}
	}

	public void supprModele(int idModele) {
		Iterator<Integer> iter = parametres.keySet().iterator();
		while(iter.hasNext()){
			int idM = iter.next();
			for(int i=0;i<parametres.get(idM).size();i++){
				EtatParametre e =parametres.get(idM).get(i);
				if(e.isLien && e.getIdLien()==idModele){
					e.setNumero(getNewIdParametre());
					nbParametres+=1;
				}
				
			}
		}
		
		
		while(getModele(idModele).getNbParametres()>0){
			this.supprParametreTo(idModele);}
		
		
		id2modele.remove(idModele);
		
		if(idModele == idRacine){
			if(id2modele.size() ==0){idRacine=-1;}
			else{idRacine = id2modele.keySet().iterator().next();}
		}
	}
	
	
	public void addLien(int idModeleSource,int numeroModele,int idModeleDestination){
		EtatParametre etat = parametres.get(idModeleSource).get(numeroModele);
		if(!etat.isLien && isLastOccurenceOf(etat.getNumero())){
			supprTrou(etat.getNumero());
			nbParametres-=1;
		}
		etat.setLien(idModeleDestination);
	}
	
	
	
	public void supprLien(int idModeleSource,int numeroModele,int idModeleDestination){
		EtatParametre etat = parametres.get(idModeleSource).get(numeroModele);
		etat.setLien(getNewIdParametre());
	}

	
	public void FusionnerParametre(int idModele,int numeroModele, int nouveauNumero){
		EtatParametre etat = parametres.get(idModele).get(numeroModele);
		if(!etat.isLien && isLastOccurenceOf(etat.getNumero())){
			supprTrou(etat.getNumero());
			nbParametres-=1;
		}
		etat.setNumero(nouveauNumero);
	}
	
	public void EchangerParametre(int idModele,int numeroModele,int nouveauNumero){
		EtatParametre etat = parametres.get(idModele).get(numeroModele);
		int numero = etat.getNumero();
		Iterator<Integer> iter=id2modele.keySet().iterator();
		while(iter.hasNext())
		{
			int idM = iter.next();
			for(int n = 0;n<getModele(idM).getNbParametres();n++){
				etat =parametres.get(idM).get(n); 
				if(!etat.isLien){
					if(etat.getNumero()==numero){
						etat.setNumero(nouveauNumero);
					}
					else if(etat.getNumero()==nouveauNumero){
						etat.setNumero(numero);
					}
				}
			}
		}
	}
	
	public void DissocierParametre(int idModele, int numeroModele) {
		EtatParametre etat = parametres.get(idModele).get(numeroModele);
		etat.setNumero(getNewIdParametre());
		nbParametres+=1;
	}
	
	
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("MArbre(\n");
		Iterator<Integer> iter=id2modele.keySet().iterator();
		while(iter.hasNext())
		{
			int idModele = iter.next();
			result.append(String.format("\t%d : %s\n", idModele,getModele(idModele)));
			for(int numero = 0;numero<getModele(idModele).getNbParametres();numero++){
				if(parametres.get(idModele).get(numero).isLien){
					result.append(String.format("\t\tLien vers %d\n",parametres.get(idModele).get(numero).getIdLien()));
				}
				else{
					result.append(String.format("\t\tParametre numero %d\n",parametres.get(idModele).get(numero).getNumero()));
				}
			}
		}
		
		result.append(")");
		return result.toString();
	}

	private class EtatParametre implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 3290015981381153478L;
		private boolean isLien = false;
		private int id;
		
		EtatParametre(int id,boolean isLien){
			this.id = id;
			this.isLien = isLien;
		}
		
		int getIdLien(){
			return this.id;
		}
		
		int getNumero(){
			return this.id;
		}
		void setNumero(int numero){
			this.isLien = false;
			this.id = numero;
		}
		void setLien(int idModele){
			this.isLien = true;
			this.id = idModele;
		}
	}
	
	@Override
	public Expression genererExpression() {
		if(id2modele.size() == 0){return new Variable("ExpressionVide");}
		
		HashMap<Integer,Expression> idModele2Expression = new HashMap<>();
		Stack<Integer> pile = new Stack<>();
		
		pile.push(idRacine);
		while(!pile.empty())
		{
			int idModele = pile.pop();
			
			if(!idModele2Expression.containsKey(idModele))
			{
				pile.push(idModele);
				for(int numero=0;numero<getModele(idModele).getNbParametres();numero++){
					EtatParametre etat = parametres.get(idModele).get(numero);
					if(etat.isLien){
						pile.push(etat.getIdLien());
					}
				}
				idModele2Expression.put(idModele,getModele(idModele).genererExpression());
			}
			else{
				Expression expression = idModele2Expression.get(idModele);
				ArrayList<Pair<String,Expression>> liste = new ArrayList<>();
				for(int numero=0;numero<getModele(idModele).getNbParametres();numero++){
					EtatParametre etat = parametres.get(idModele).get(numero);
					if(etat.isLien){
						liste.add(Pair.of(Parametre.getString(numero),idModele2Expression.get(etat.getIdLien())));
					}
					else
					{
						liste.add(Pair.of(Parametre.getString(numero),new Variable(getNomParametreLibre(etat.getNumero()))));
					}
				}
				idModele2Expression.put(idModele, expression.Remplacer(liste));
			}
		}
		
		Expression expression = idModele2Expression.get(idRacine);
		ArrayList<Pair<String,Expression>> liste = new ArrayList<>();
		for(int p = 0;p<getNbParametres();p++){
			liste.add(Pair.of(getNomParametreLibre(p), new Parametre(p)));
		}
		return expression.Remplacer(liste);
		
	}
	
	public String getNomParametreLibre(int numero){
		return "$$".concat(String.valueOf(numero));
	}
	
	
	public boolean isLien(int idModele, int numeroParametre){
		return parametres.get(idModele).get(numeroParametre).isLien;
	}
	
	public int getIdLien(int idModele,int numeroParametre){
		return parametres.get(idModele).get(numeroParametre).getIdLien();
	}
	
	public int getParametre(int idModele,int numeroParametre){
		return parametres.get(idModele).get(numeroParametre).getNumero();
	}

	
}
