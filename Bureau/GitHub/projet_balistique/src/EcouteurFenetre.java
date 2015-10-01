import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class EcouteurFenetre implements WindowListener{
	
	public EcouteurFenetre(){
	}

	public void windowClosing(WindowEvent w) {
		fenetreEnglobante.dispose(); // libération des ressources utilisées par la frame
		System.exit(0); // sort du programme (0 indique à l’OS que c’est une fin normale, sans "erreur")
	}
	
	public void windowActivated(WindowEvent e){
	}
	
	public void windowDeactivated(WindowEvent e){		
	}
	
	public void windowOpened(WindowEvent e){
	}
	
	public void windowIconified(WindowEvent e){
	}
	
	public void windowDeiconified(WindowEvent e){
	}
	
	public void windowClosed(WindowEvent e){
	}
}
