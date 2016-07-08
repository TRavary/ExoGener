import expression.Entier;
import expression.Expression;
import expression.Produit;

public class test {
	
	public test(){
		
	}
	
	public void affiche(Expression e)
	{
		if(e.getClass() == Entier.class)
			affiche((Entier) e);
		else if(e.getClass() == Produit.class)
			affiche((Produit) e);
	}
	
	public void affiche(Entier entier){
		System.out.println("Je suis un entier");
	}
	
	public void affiche(Produit produit){
		System.out.println("Je suis un produit");
	}
}
