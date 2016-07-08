package interfacegraphique;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import expression.modele.*;

@SuppressWarnings("serial")
public class JVariable extends JLabel {
	int id;
	int idModele;
	boolean activeSelection = false;
	
	static String itemNewVariable = "-> Nouvelle Variable";
	
	public void init(int id,int idModele){
		this.id = id;
		this.idModele = idModele;
		this.setText(String.valueOf(getVariable()));
		setHorizontalAlignment(SwingConstants.CENTER);
		if(id ==0){
			setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.black));}
		else{
			setBorder(BorderFactory.createMatteBorder(1,1,0,0,Color.black));}
		addMouseListener(new ClicListener());
		addMouseListener(new PopupListener());
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setText(String.valueOf(getVariable()));
		setHorizontalAlignment(SwingConstants.CENTER);
		if(getVariable() == getModeleFactory().variableSelect){
			g.drawRect(2,2, getWidth()-4, getHeight()-4);
		}
	}
	
	
	public JModeleFactory getModeleFactory(){
		return (JModeleFactory)SwingUtilities.getAncestorOfClass(JModeleFactory.class, this);
	}
	public Modele getModele(){
		return getModeleFactory().modelePrincipal.getModele(idModele);
	}
	public int getVariable(){
		return getModele().getVariableLibre(id);
	}
	
	
	public String getItemName(int variable){
		return "-> ".concat(String.valueOf(variable));
	}	
	private JPopupMenu createPopup(){
		JPopupMenu popup = new JPopupMenu();
		ArrayList<Integer> variablesLibres = getModeleFactory().modelePrincipal.getVariablesLibres();
		
		JMenuItem itemNew = new JMenuItem(itemNewVariable);
		itemNew.addActionListener(new ChangeVariableListener());
		popup.add(itemNew);
		
		for(int i =0;i<variablesLibres.size();i++){
			int variable = variablesLibres.get(i);
			if(variable != getVariable()){
				JMenuItem item = new JMenuItem(getItemName(variable));
				item.addActionListener(new ChangeVariableListener());
				popup.add(item);
			}
		}
		
		return popup;
	}
	class PopupListener extends MouseAdapter {
	    public void mousePressed(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    public void mouseReleased(MouseEvent e) {
	    	maybeShowPopup(e);
	    }

	    private void maybeShowPopup(MouseEvent e) {
	        if (e.isPopupTrigger()) {
	        
	        	createPopup().show(e.getComponent(),
	                       e.getX(), e.getY());
	        }
	    }
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
            	((JModeleFactory)getModeleFactory()).selectVariable(getVariable());
            	
            }
            activeSelection = false;
        }           
	}
	class ChangeVariableListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
	    	String item = ((JMenuItem)e.getSource()).getText();
	    	if(item.equals(itemNewVariable)){
	    		getModeleFactory().changeVariable(idModele, id, getModeleFactory().modelePrincipal.getNewVariable());
	    	}
	    	else{
	    		ArrayList<Integer> variablesLibres = getModeleFactory().modelePrincipal.getVariablesLibres();
	    		
	    		for(int i =0;i<variablesLibres.size();i++){
	    			int variable = variablesLibres.get(i);
	    			if(getItemName(variable).equals(item)){
	    				getModeleFactory().changeVariable(idModele, id, variable);
	    			}
	    		}
	    	}
	    	repaint();
	    }
	  }
}
