package interfacegraphique;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

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
	JLabel label;
	int dX;
	int dY;
	int hVariable;
	boolean activeSelection = false;
	
	JPopupMenu popup = new JPopupMenu();
	
	public JModele(int idModele){
		this.idModele = idModele;
		setLayout(null);
	}
	
	public JMenu createModifierMenu(){
		JMenu menu = new JMenu();
		
		//toto tata
		
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
	
	public void init(int x,int y){
		this.setSize(90,60);
		this.setLocation(x, y);
		this.setVisible(true);
		hVariable = 60;
		label = new JLabel();
		label.setSize(80, 40);
		label.setLocation(5, 5);
		
		label.setText(getModele().getNom().concat(String.valueOf(idModele)));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(label);
		
		addMouseListener(new MouseAdapter()
        {
            @Override public void mousePressed(MouseEvent e) {	
                if(SwingUtilities.isLeftMouseButton(e)){
                	Point p = e.getLocationOnScreen();
                	dX = getX()-p.x;
					dY = getY()-p.y;
					
					activeSelection = true;
					
				}
		    }
 
            @Override public void mouseReleased(MouseEvent e){
                
                if(activeSelection){
                	int variableSelect = -1;
                	if(getModele().getNbVariablesLibres()>0){
                		if(e.getY()>hVariable){
		            		int idVar = e.getX()*getModele().getNbVariablesLibres()/getWidth();
		            		variableSelect = getModele().getVariableLibre(idVar);
		            	}
                	}
                	((JModeleFactory)getParent()).select(idModele, variableSelect);
                	
                }
            }           
        });
 
        addMouseMotionListener(new MouseMotionAdapter()
        {                     
            @Override public void mouseDragged(MouseEvent e){
                Point p = new Point(e.getLocationOnScreen());
                setLocation(
                		Integer.min(Integer.max(0,p.x+dX),getParent().getWidth()-getWidth()),
                		Integer.min(Integer.max(0,p.y+dY),getParent().getHeight()-getHeight())
                		);
                
                activeSelection = false;
                getParent().repaint();
            }	
        });
	}
	
	public Modele getModele(){
		JModeleFactory parent = (JModeleFactory) getParent();
		return parent.modelePrincipal.getModele(idModele);
	}
	
	public void paintComponent(Graphics g)
	{		
		super.paintComponent(g);
		this.setSize(90,60);
		label.setSize(80, 40);
		label.setHorizontalAlignment(SwingConstants.CENTER);
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
	        	popup.show(e.getComponent(),
	                       e.getX(), e.getY());
	            
	        }
	    }
	}
}
