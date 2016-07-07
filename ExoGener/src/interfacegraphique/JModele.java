package interfacegraphique;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import expression.modele.Modele;

@SuppressWarnings("serial")
public class JModele extends JPanel {
	int idModele;
	JLabel label = new JLabel();
	int dX;
	int dY;
	boolean activeSelection = false;
	
	JPanel panelVariables = new JPanel();
	ArrayList<JVariable> jvariables = new ArrayList<>();
	
	JPopupMenu popup = new JPopupMenu();
	
	public void init(int idModele,int x,int y){
		this.idModele = idModele;
		this.setSize(90,60);
		
		setLayout(new BorderLayout());
		this.add(label,BorderLayout.CENTER);
		panelVariables.setLayout(new GridLayout());
		this.add(panelVariables,BorderLayout.PAGE_END);
		
		setBorder(BorderFactory.createLineBorder(Color.black));
		this.setLocation(x, y);
		this.setVisible(true);
		
		label.setText(getModele().getNom().concat(String.valueOf(idModele)));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(label);
		
		int nbVar = getModele().getNbVariablesLibres();
		if(nbVar !=0){
			for(int idVar = 0;idVar<nbVar;idVar++){
				JVariable jvar = new JVariable();
				jvariables.add(jvar);
				panelVariables.add(jvar);
				jvar.init(idVar,idModele);
			}
		}
		addMouseListener(new clicListener());
		addMouseMotionListener(new deplacementListener());
	}
	
	public JMenu createModifierMenu(){
		JMenu menu = new JMenu();
		
		
		return menu;
	}
	
	public Point getAncre(){
		Point ancre = new Point();
		ancre.x = this.getX()+this.getWidth()/2;
		ancre.y = this.getY();
		return ancre;
	}
	
	public Point getAncreVariable(int idVar){
		Point ancre = new Point();
		int nbVar = getModele().getNbVariablesLibres();
		ancre.x = this.getX()+(2*idVar+1)*getWidth()/(2*nbVar);
		ancre.y = this.getY()+this.getHeight();
		return ancre;
	}
	
	
	
	public Modele getModele(){
		JModeleFactory parent = (JModeleFactory) getParent();
		return parent.modelePrincipal.getModele(idModele);
	}
	
	public void paintComponent(Graphics g)
	{		
		super.paintComponent(g);
		this.setSize(90,60);
		//label.setSize(80, 40);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		/*
		int h = getHeight()-1;
		int w = getWidth()-1;
		hVariable = 3*h/4;
		g.drawRect(0, 0, w,h);
		int nbVar = getModele().getNbVariablesLibres();
		int variableSelect = ((JModeleFactory)getParent()).variableSelect;
		if(nbVar !=0){
			g.drawLine(0, hVariable, w,hVariable);
			
			
			g.drawString(String.valueOf(getModele().getVariableLibre(0)), 5, h-3);
			if(variableSelect==getModele().getVariableLibre(0))
			{
				g.drawRect(2, hVariable+2, w/nbVar-4, h-hVariable-4);
			}
			
			
			for(int i = 1;i<nbVar;i++){
				g.drawLine(i*w/nbVar, hVariable, i*w/nbVar, h);
				g.drawString(String.valueOf(getModele().getVariableLibre(i)), i*w/nbVar+5, h-3);
			
				if(variableSelect==getModele().getVariableLibre(i))
				{
					g.drawRect(i*w/nbVar+2, hVariable+2, w/nbVar-4, h-hVariable-4);
				}
			}
		}
		*/
	}
	
	
	class clicListener extends MouseAdapter{
		@Override 
		public void mousePressed(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)){
            	Point p = e.getLocationOnScreen();
            	dX = getX()-p.x;
				dY = getY()-p.y;
				
				activeSelection = true;
				
			}
	    }

        @Override
        public void mouseReleased(MouseEvent e){
            if(activeSelection){
            	((JModeleFactory)getParent()).selectModele(idModele);
            }
        }           
	}    
	
	class deplacementListener extends MouseAdapter{
        @Override
        public void mouseDragged(MouseEvent e){
            Point p = new Point(e.getLocationOnScreen());
            setLocation(
            		Integer.min(Integer.max(0,p.x+dX),getParent().getWidth()-getWidth()),
            		Integer.min(Integer.max(0,p.y+dY),getParent().getHeight()-getHeight())
            		);
            
            activeSelection = false;
            getParent().repaint();
        }	
	}
	
	
	class PopupListener extends MouseMotionAdapter {
	    public void mousePressed(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    public void mouseReleased(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    private void maybeShowPopup(MouseEvent e) {
	        if (e.isPopupTrigger()) {
	        	popup.show(e.getComponent(),
	                       e.getX(), e.getY());
	            
	        }
	    }
	}
}
