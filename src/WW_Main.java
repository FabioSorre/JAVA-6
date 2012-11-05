import java.awt.EventQueue;

public class WW_Main {

	public WW_Main(){
		WW_View v = new WW_View(new WW_Model());
	}
		
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
				
			@Override
			public void run() {
				new WW_Main();
			}
		});
	}
}
