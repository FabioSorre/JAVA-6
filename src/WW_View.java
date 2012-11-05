import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

public class WW_View implements Observer{
	
	private JFrame f;
	private JScrollPane sp;
	private JList suggerimenti;
	private WW_Model m;
	private WW_Controller ctrl;
	private JPanel pdx;
	private JPanel psx;
	private JLabel l[][];
	private JLabel punti;
	private JLabel paroleTrovate;
	private JPanel ptop;
	
	public WW_View(WW_Model m){
		f= new JFrame("WordWorm");
		f.setLayout(new BorderLayout());
		f.setPreferredSize(new Dimension(605,500));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocation(500, 300);
		JOptionPane.showMessageDialog(f, "Vuoi iniziare una nuova partita?");
		this.m=m;
		this.m.addObserver(this);
		ctrl = new WW_Controller(m, this);
		this.suggerimenti= new JList(m.getSubVocabolario());
		sp= new JScrollPane(this.suggerimenti);
		sp.setPreferredSize(new Dimension(225,370));
		psx= new JPanel();
		psx.add(sp);
		f.add(psx, BorderLayout.WEST);
		pdx=new JPanel();
		pdx.setLayout(null);
		l=new JLabel[7][7];
		for (int r=0; r<7; r++){
			for (int c=0; c<7; c++){
				l[r][c]=new JLabel(""+m.getCasella(r, c).getLettera(), JLabel.CENTER);
				if (c%2==0) l[r][c].setBounds(50*(c%7), (50*(r%7))+25, 50, 50);
				else l[r][c].setBounds(50*(c%7), (50*(r%7)), 50, 50);
				l[r][c].setBorder(LineBorder.createGrayLineBorder());
				l[r][c].setOpaque(true);
				l[r][c].addMouseListener(this.ctrl);
				l[r][c].setFont(l[r][c].getFont().deriveFont(40.f));
				pdx.add(l[r][c]);
			}
		}
		pdx.setPreferredSize(new Dimension(355, 375));
		f.add(pdx, BorderLayout.EAST);
		
		
		ptop=new JPanel(new BorderLayout());
		punti=new JLabel("Punti: "+m.getPunti());
		punti.setFont(punti.getFont().deriveFont(40.f));
		punti.setHorizontalAlignment(JLabel.LEFT);
		ptop.add(punti, BorderLayout.NORTH);
		paroleTrovate=new JLabel("Parole trovate: "+m.getParoleTrovate());
		paroleTrovate.setFont(punti.getFont().deriveFont(25.f));
		paroleTrovate.setHorizontalAlignment(JLabel.LEFT);
		ptop.add(paroleTrovate, BorderLayout.SOUTH);
		f.add(ptop, BorderLayout.NORTH);
		
		f.pack();
		f.setResizable(false);
		f.setVisible(true);
	}
	
	public JLabel getLabel(int r, int c){
		return l[r][c];
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		punti.setText("Punti: "+m.getPunti());
		paroleTrovate.setText("Parole trovate: "+m.getParoleTrovate());
		for (int r=0; r<7; r++){
			for (int c=0; c<7; c++){
				l[r][c].setText(""+m.getCasella(r,c).getLettera());
				if (m.getCasella(r, c).isSelezionata()) l[r][c].setBackground(Color.yellow);
				else l[r][c].setBackground(null);
			}
		}
		for (int i=0; i<m.getCaselleSelezionate_Size(); i++){
			int r = m.getCasellaSelezionata(i).getR();
			int c = m.getCasellaSelezionata(i).getC();
		}
		suggerimenti.setListData(m.getSubVocabolario());		
		System.out.println("aggiornato");
	}
}