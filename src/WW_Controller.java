import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;


public class WW_Controller implements MouseListener{

	private WW_Model m;
	private WW_View v;
	
	public WW_Controller(WW_Model m, WW_View v) {
		this.m=m;
		this.v=v;
	}

	@Override
	public void mouseClicked(MouseEvent mc) {
		if (mc.getButton()==MouseEvent.BUTTON3){
			m.esplodiParola();
		}
		else {
			Component comp=mc.getComponent();
			if (! (comp instanceof JLabel)) return;
			Point p = comp.getLocation();
			int r,c;
			c=(p.x*7)/350;
			if (c%2==0) r=((p.y-25)*7)/350;
			else r=(p.y*7)/350;
			m.selezionaCasella(r, c);
		}
	}

	@Override
	public void mouseEntered(MouseEvent me) {
		Component comp=me.getComponent();
		if (! (comp instanceof JLabel)) return;
		comp.setBackground(Color.orange);
	}

	@Override
	public void mouseExited(MouseEvent me) {
		Component comp=me.getComponent();
		if (! (comp instanceof JLabel)) return;
		Point p = comp.getLocation();
		int r,c;
		c=(p.x*7)/350;
		if (c%2==0) r=((p.y-25)*7)/350;
		else r=(p.y*7)/350;
		if (!m.getCasella(r, c).isSelezionata()) v.getLabel(r, c).setBackground(null);
		else v.getLabel(r, c).setBackground(Color.yellow);
	}

	@Override
	public void mousePressed(MouseEvent me) {
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	}
}
