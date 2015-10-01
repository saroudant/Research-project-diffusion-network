import javax.swing.*;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/* Cette classe est d'une importance minime: elle ne contient que le canvas relatif au dessin de la bannière en tout début de jeu
 * On l'instancie dans InterfaceJeu et après son affichage (qui ne dure que quelques secondes), on commence le jeu.
 */
@SuppressWarnings("serial")
class BannierePresentation extends JPanel {
	int index;
	
	public BannierePresentation(){	
		index = 4;
	}
	
	public void paint(Graphics g){
		try{
			Image banniere = ImageIO.read(new File("banniere.jpg")); 
			g.drawImage(banniere,0,0, this);
			System.out.println("Ouais!");
		}catch(IOException exc) {
			exc.printStackTrace();
		}
		System.out.println("Pourquoi pas...");
	}
}
