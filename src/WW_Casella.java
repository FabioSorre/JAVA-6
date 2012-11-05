
public class WW_Casella {
	
	private char lettera;
	private boolean selezionata;
	private int r,c;
	
	public WW_Casella(int riga, int colonna, char carattere){
		lettera=carattere;
		selezionata=false;
		r=riga;
		c=colonna;
	}
	
	public char getLettera(){
		return lettera;
	}
	
	public boolean isSelezionata(){
		if (selezionata) return true;
		else return false;
	}
	public void seleziona_deselezionaCasella(){
		if (!selezionata) selezionata=true;
		else selezionata = false;
	}
	public boolean compareTo (int riga, int colonna){
		if (riga==r && colonna==c) return true;
		else return false;
	}

	public int getR() {
		return r;
	}

	public int getC() {
		return c;
	}


}