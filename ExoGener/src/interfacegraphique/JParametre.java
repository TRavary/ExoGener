package interfacegraphique;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import expression.modele.*;

@SuppressWarnings("serial")
public class JParametre extends JLabel {
	int numeroModele;
	int idModele;
	boolean activeSelection = false;
	
	static String itemNewParametre = "-> Nouveau parametre";
	
	public void init(int numeroModele,int idModele){
		this.numeroModele = numeroModele;
		this.idModele = idModele;
		this.setText(String.valueOf(getParametre()));
		setHorizontalAlignment(SwingConstants.CENTER);
		if(numeroModele ==0){
			setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.black));}
		else{
			setBorder(BorderFactory.createMatteBorder(1,1,0,0,Color.black));}
		addMouseListener(new ClicListener());
		addMouseListener(new PopupListener());
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(!isLien()){
			this.setText(String.valueOf(getParametre()));
			setHorizontalAlignment(SwingConstants.CENTER);
		}
		else{
			this.setText(" ");
		}
		
		
		if(numeroModele == getModeleFactory().numeroSelect && idModele == getModeleFactory().idModeleSelect){
			g.drawRect(2,2, getWidth()-4, getHeight()-4);
		}
	}
		
	
	
	public JModeleFactory getModeleFactory(){
		return (JModeleFactory)SwingUtilities.getAncestorOfClass(JModeleFactory.class, this);
	}
	public Modele getModele(){
		return getModeleFactory().modelePrincipal.getModele(idModele);
	}
	
	public boolean isLien(){
		return getModeleFactory().modelePrincipal.isLien(idModele, numeroModele);
	}
	public int getIdLien(){
		return getModeleFactory().modelePrincipal.getIdLien(idModele, numeroModele);
	}
	public int getParametre(){
		return getModeleFactory().modelePrincipal.getParametre(idModele,numeroModele);
	}
	
	
	

	
	
	
	
	class ClicListener extends MouseAdapter{
		@Override 
		public void mousePressed(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)){
            	activeSelection = true;
			}
	    }

        @Override
        public void mouseReleased(MouseEvent e){
            if(activeSelection){
            	getModeleFactory().selectParametre(idModele, numeroModele);
            	
            }
            activeSelection = false;
        }           
	}
	
	public String getItemName(int variable){
		return "-> ".concat(String.valueOf(variable));
	}	
	
	private JPopupMenu createPopup(){
		// Lancé uniquement si ce parametre n'est pas lié
		JPopupMenu popup = new JPopupMenu();
		
		if(!getModeleFactory().modelePrincipal.isLastOccurenceOf(getParametre())){
			JMenuItem item = new JMenuItem("Dissocier");
			item.addActionListener(new DissocierListener());
			popup.add(item);
		}
		
		JMenu menuFusion = new JMenu("Fusionner avec ");
		for(int numero=0;numero<getModeleFactory().modelePrincipal.getNbParametres();numero++){
			if(numero!=getParametre()){
				JMenuItem item = new JMenuItem(getItemName(numero));
				item.addActionListener(new FusionListener());
				menuFusion.add(item);
			}
		}
		popup.add(menuFusion);
		
		JMenu menuEchanger = new JMenu("EChanger avec ");
		for(int numero=0;numero<getModeleFactory().modelePrincipal.getNbParametres();numero++){
			if(numero!=getParametre()){
				JMenuItem item = new JMenuItem(getItemName(numero));
				item.addActionListener(new EchangerListener());
				menuEchanger.add(item);
			}
		}
		popup.add(menuEchanger);
		
		return popup;
	}
	
	class FusionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String item = ((JMenuItem)e.getSource()).getText();
			for(int numero=0;numero<getModeleFactory().modelePrincipal.getNbParametres();numero++){
				
				if(item.equals(getItemName(numero))){
					getModeleFactory().FusionnerParametre(idModele, numeroModele, numero);
				}
			}
		}
	}
	
	class DissocierListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			getModeleFactory().DissocierParametre(idModele, numeroModele);
		}
	}
	
	class EchangerListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String item = ((JMenuItem)e.getSource()).getText();
			for(int numero=0;numero<getModeleFactory().modelePrincipal.getNbParametres();numero++){
				
				if(item.equals(getItemName(numero))){
					getModeleFactory().EchangerParametre(idModele, numeroModele, numero);
				}
			}
		}
	}
	
	
	class PopupListener extends MouseAdapter {
	    public void mousePressed(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    public void mouseReleased(MouseEvent e) {
	    	maybeShowPopup(e);
	    }

	    private void maybeShowPopup(MouseEvent e) {
	        if (e.isPopupTrigger() && !isLien()) {
	        
	        	createPopup().show(e.getComponent(),
	                       e.getX(), e.getY());
	        }
	    }
	}

}
