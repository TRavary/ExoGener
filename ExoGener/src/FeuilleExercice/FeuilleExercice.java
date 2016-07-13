package FeuilleExercice;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FeuilleExercice {
	
	String titre = "Feuille d'exercice";
	File fichierEnTete = new File("Tex/Entete/EnTete.txt");
	ArrayList<Exercice> exercices = new ArrayList<>();

	public boolean addExercice(Exercice exo) {
		return exercices.add(exo);
	}
	public void addExercice(int index, Exercice exo) {
		exercices.add(index,exo);
	}
	public void clear() {
		exercices.clear();
	}
	public Exercice remove(int index) {
		return exercices.remove(index);
	}
	public int getNbExercices() {
		return exercices.size();
	}
	public Exercice getExercice(int index){
		return exercices.get(index);
	}
	
	public int getNumero(Exercice exo){
		return exercices.indexOf(exo);
	}
	
	public String genererLatex() throws IOException{
		StringBuilder result = new StringBuilder();
		result.append(getEnTeteLatex());
		result.append(getTitreLatex());
		
		for(int i=0;i<getNbExercices();i++) {
			result.append(getExerciceLatex(i));
		}
		
		result.append("\\end{document}\n");
		return result.toString();
	}
	
	private String getEnTeteLatex() throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(fichierEnTete.getPath()));
		return new String(encoded, Charset.defaultCharset());
	}
	
	
	private String getTitreLatex(){
		StringBuilder result = new StringBuilder();
		result.append("\\bigskip\n");
		result.append("\\begin{center}\\begin{Huge}"+titre+"\\end{Huge}\\end{center}\n");
		result.append("\\bigskip\n\n");
		return result.toString();
	}
	
	private String getExerciceLatex(int index){
		Exercice exo = getExercice(index);
		StringBuilder result = new StringBuilder();
		result.append("\\begin{exo}\n");
		result.append(exo.getConsigne());
		result.append("\\end{exo}\n");
		result.append("\\begin{tasks}("+String.valueOf(exo.getNbCol())+")\n");
		for(int i =0;i<exo.getNbItems();i++){
			result.append(String.format("\\task $%s$\n",exo.getItem()));
		}
		result.append("\\end{tasks}\n\n");
		
		return result.toString();
	}
}
