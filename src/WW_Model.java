import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Iterator;

public class WW_Model extends Observable{
	
	private TreeSet<String> vocaboli;
	private TreeSet<String> subVocaboli;
	private String parolaCorrente;
	private WW_Casella[][] griglia;
	private char[] vocali= {'a','a','a','a','e','e','e','e','i','i','i','o','o','o','u'};
	private char[] consonanti= {'b','b','c','c','c','c','d','d','d','d','f','f','g','g','g','h','h','l','l','l','l','l','m','m','m','n','n','n','n','n','j','k','p','p','p','q','q','r','r','r','r','r','s','s','s','s','t','t','t','t','v','v','v','w','x','y','z','z'};
	private ArrayList <WW_Casella> caselleSelezionate= new ArrayList<WW_Casella>();
	private int punti;
	private int paroleTrovate;
	
	// 			ordine lettere in base a frequenza: 	eaionlrtscdupmvghfbqz ò à ù ì é ê Ö ykwxj
	
	public WW_Model(){	
		vocaboli= new TreeSet<String>();
		subVocaboli=new TreeSet<String>();
		loadVocaboli();
		punti=0;
		paroleTrovate=0;
		parolaCorrente="";
		griglia=new WW_Casella[7][7];
		for (int r=0; r<7; r++){
			for (int c=0; c<7; c++){
				griglia[r][c]= new WW_Casella(r, c, letteraRandom());
			}
		}
		
		for(int i=0;i<14;i++){
			for(int j=0;j<7;j++){
				if(i%2!=j%2){
					System.out.printf("%c ",griglia[i/2][j].getLettera());
				}
				else System.out.printf("  ");
			}
			System.out.println();
		}

	}
	
	public void selezionaCasella(int r, int c){
		System.out.println("si è cercato di selezionare la casella: "+r+" "+c+", lettera: "+ griglia[r][c].getLettera());
		if(griglia[r][c].isSelezionata()){
			System.out.println("la casella era già selezionata");
			if (caselleSelezionate.get(caselleSelezionate.size()-1).compareTo(r, c)){
				System.out.println("la casella era l'ultima selezionata");
				caselleSelezionate.remove(caselleSelezionate.size()-1);
				griglia[r][c].seleziona_deselezionaCasella();
				if (parolaCorrente.length()>1) {
					parolaCorrente=parolaCorrente.substring(0, parolaCorrente.length()-1);
					aggiornaSubVocaboli();
				}
				else {
					parolaCorrente="";
					aggiornaSubVocaboli();
				}
				System.out.println("parola corrente: "+parolaCorrente);
			}
			else System.out.println("mossa ignorata");
		}
		else {
			System.out.println("la casella non era ancora selezionata");
			if (isAdiacente(r,c)){
				parolaCorrente=parolaCorrente+griglia[r][c].getLettera();
				aggiornaSubVocaboli();
				if (subVocaboli.size()>=1){
					System.out.println("la parola esiste, mossa effettuata");
					caselleSelezionate.add(griglia[r][c]);
					griglia[r][c].seleziona_deselezionaCasella();
					System.out.println("parola corrente: "+parolaCorrente);
				}
				else {
					System.out.println("non esiste un parola che inizi per '"+parolaCorrente+"', inizio nuova parola");
					for (int i=0; i<caselleSelezionate.size(); i++){
						caselleSelezionate.get(i).seleziona_deselezionaCasella();
					}
					caselleSelezionate.clear();
					caselleSelezionate.add(griglia[r][c]);
					griglia[r][c].seleziona_deselezionaCasella();
					parolaCorrente=""+griglia[r][c].getLettera();
					aggiornaSubVocaboli();
					System.out.println("parola corrente: "+parolaCorrente);
				}
			}
			else {
				System.out.println("la casella non è adiacente, inizio nuova parola");
				for (int i=0; i<caselleSelezionate.size(); i++){
					caselleSelezionate.get(i).seleziona_deselezionaCasella();
				}
				caselleSelezionate.clear();
				caselleSelezionate.add(griglia[r][c]);
				griglia[r][c].seleziona_deselezionaCasella();
				parolaCorrente=""+griglia[r][c].getLettera();
				aggiornaSubVocaboli();
				System.out.println("parola corrente: "+parolaCorrente);
			}
		}
		setChanged();
		notifyObservers();
	}
	
	public boolean esplodiParola(){
		if (subVocaboli.contains(parolaCorrente)){
			System.out.println("la parola è completa, aggiorno stato");
			punti=punti+((int)(Math.pow(parolaCorrente.length()-1, 2)));
			if (parolaCorrente.length()>1) paroleTrovate++;
			spostaCaselle();
			parolaCorrente="";
			caselleSelezionate.clear();
			aggiornaSubVocaboli();
			System.out.println("nuovi punti: "+punti+". parole trovate: "+paroleTrovate);
			for(int i=0;i<14;i++){
				for(int j=0;j<7;j++){
					if(i%2!=j%2){
						System.out.printf("%c ",griglia[i/2][j].getLettera());
					}
					else System.out.printf("  ");
				}
				System.out.println();
			}
			setChanged();
			notifyObservers();
			return true;
		}
		else {
			System.out.println("la parola non è completa! mossa ignorata");
			return false;
		}
		
	}
	
	private void spostaCaselle() {
		for (int i=0; i<parolaCorrente.length(); i++){
			int r =caselleSelezionate.get(i).getR();
			int c= caselleSelezionate.get(i).getC();
			System.out.println("si analizza la casella "+r+" "+c);
			if ( r+1==7 || !griglia[r+1][c].isSelezionata()){
				int fattore=1;
				while (r-fattore>=0 && griglia[r-fattore][c].isSelezionata()){
					fattore++;
				}
				System.out.println("fattore :"+fattore);
				for (int j=r; j-fattore>=0; j--){
					char ca=griglia[j-fattore][c].getLettera();
					griglia[j][c]=new WW_Casella(j,c,ca);
					System.out.println("la casella "+j+" "+c+" viene sostituita dalla casella "+(j-fattore)+" "+c);
				}
				for (int k=0; k<fattore; k++){
					griglia[k][c]= new WW_Casella(k,c, letteraRandom());
					System.out.println("la casella "+k+" "+c+" viene sostituita da una nuova casella");
				}
			}
			else System.out.println("la casella sottostante è selezionata, salto");
		}
		
	}

	private boolean isAdiacente(int x, int y) {
		if (caselleSelezionate.isEmpty()==true){
			return true;
		}
		else { 
			int i=caselleSelezionate.get(caselleSelezionate.size()-1).getR();
			int j=caselleSelezionate.get(caselleSelezionate.size()-1).getC();
		
			if (Math.abs(y-j)==0){
				if (Math.abs(x-i)==1) {
					System.out.println("la casella è adiacente");
					return true;
				}
				else return false;
			}
			else if (Math.abs(y-j)==1){
				if (y%2==0){
					if ( (x-i)==0 || (x-i)==-1) {
						System.out.println("la casella è adiacente");
						return true;
					}
					else return false;
				}
				else {
					if ((x-i)==0 || (x-i)==1) {
						System.out.println("la casella è adiacente");
						return true;
					}
					else return false;
				}
			}
			else return false;
		}

	}
	
	private void aggiornaSubVocaboli() {
		subVocaboli.clear();
		if (parolaCorrente.length()>0){
			Iterator<String> it = vocaboli.iterator();
			String temp= "";
			while (temp!=vocaboli.ceiling(parolaCorrente)){
				if (it.hasNext())temp=it.next();
			}
			//System.out.println("nuovo subVocabolario");
			while (temp!=vocaboli.ceiling(parolaCorrente+"zzz")){
				subVocaboli.add(temp);
				//System.out.println(temp);
				if (it.hasNext())temp=it.next();
				else break;
			}
			//if (temp!=null) subVocaboli.add(temp);
		}
	}

	private char letteraRandom() {
		if ((int)(Math.random()*3)>0){
			return consonanti[(int) (Math.random()*(consonanti.length))];	
		}
		else return vocali[(int) (Math.random()*(vocali.length))];	
	}

	private void loadVocaboli(){
		Scanner s;
		int i=0;
		try {
			s = new Scanner(new FileReader("words.italian.txt"));
			while (s.hasNextLine()){
				String vocabolo= s.nextLine();
				if (vocabolo!=null || vocabolo!=""){
					i++;
					vocaboli.add(vocabolo);
				}
			}
			s.close();
			System.out.println("griglia caricata");
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		}
	}
	
	public int getPunti(){
		return punti;
	}
	public int getParoleTrovate(){
		return paroleTrovate;
	}
	
	public WW_Casella getCasella(int r,int c){
		return griglia[r][c];
	}

	public Object[] getSubVocabolario(){
		return subVocaboli.toArray();
	}
	
	public int getCaselleSelezionate_Size(){
		return caselleSelezionate.size();
	}
	
	public WW_Casella getCasellaSelezionata(int i){
		return caselleSelezionate.get(i);
	}
}