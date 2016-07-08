package interfacegraphique;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
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
				addVariable();
			}
		}
		addMouseListener(new clicListener());
		addMouseMotionListener(new deplacementListener());
		addMouseListener(new PopupListener());
	}
	public void paintComponent(Graphics g)
	{		
		super.paintComponent(g);
		this.setSize(90,60);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		if(idModele == getModeleFactory().modelePrincipal.idRacine){
			setBorder(BorderFactory.createLineBorder(Color.black,2,true));
		}
		else{
			setBorder(BorderFactory.createLineBorder(Color.black));
		}
	}
	
	
	public void addVariable(){
		JVariable jvar = new JVariable();
		jvariables.add(jvar);
		panelVariables.add(jvar);
		jvar.init(jvariables.size()-1,idModele);
	}
	public void supprVariable(){
		panelVariables.remove(jvariables.get(jvariables.size()-1));
		jvariables.remove(jvariables.size()-1);
	}
	
	
	public JModeleFactory getModeleFactory(){
		return (JModeleFactory)SwingUtilities.getAncestorOfClass(JModeleFactory.class, this);
	}	
	public Modele getModele(){
		return getModeleFactory().modelePrincipal.getModele(idModele);
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

	public JPopupMenu createPopupMenu(){
		JPopupMenu menu = new JPopupMenu();
		
		if(idModele != getModeleFactory().modelePrincipal.idRacine){
			JMenuItem itemRacine = new JMenuItem("definir operation principale");
			itemRacine.addActionListener(new ActionListener(){
				@Override public void actionPerformed(ActionEvent e) {
					getModeleFactory().modelePrincipal.setRacine(idModele);}
			});
			menu.add(itemRacine);
		}

		if(getModele().nbVarModifiable){
			JMenuItem itemAddVar = new JMenuItem("Ajouter variable libre");
			itemAddVar.addActionListener(new ActionListener(){
				@Override public void actionPerformed(ActionEvent e) {
					getModeleFactory().addVariableTo(idModele);}
			});
			menu.add(itemAddVar);
			if(getModele().getNbVariablesLibres()>getModele().minVariables){
				JMenuItem itemSupprVar = new JMenuItem("Supprimer variable libre");
				itemSupprVar.addActionListener(new ActionListener(){
					@Override public void actionPerformed(ActionEvent e) {
						getModeleFactory().supprVariableTo(idModele);}
				});
				menu.add(itemSupprVar);
			}
		}
		
		JMenuItem supprModele = new JMenuItem("Supprimer modele");
		supprModele.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e) {
				getModeleFactory().supprModele(idModele);}
			});
		menu.add(supprModele);
		return menu;
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
            	getModeleFactory().selectModele(idModele);
            }
        }           
	}    
	class deplacementListener extends MouseAdapter{
        @Override
        public void mouseDragged(MouseEvent e){
        	if(SwingUtilities.isLeftMouseButton(e)){
	            Point p = new Point(e.getLocationOnScreen());
	            setLocation(
	            		Integer.min(Integer.max(0,p.x+dX),getParent().getWidth()-getWidth()),
	            		Integer.min(Integer.max(0,p.y+dY),getParent().getHeight()-getHeight())
	            		);
        	}
            
            activeSelection = false;
            getParent().repaint();
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
	        	createPopupMenu().show(e.getComponent(),
	                       e.getX(), e.getY());
	            
	        }
	    }
	}
}
