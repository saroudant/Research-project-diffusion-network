import javax.swing.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.*;

/*L'objectif de cette classe est de créer un module de dessin de l'application; le tout dans un objectif
 * de générer un code propre et lisible.
 */
@SuppressWarnings("serial")

//Chercher à transformer avec JPanel
// cf. TP10 dernier exo
class CanvasInterface extends JPanel {
	
	public void setSize(){
		setMinimumSize(new Dimension(900,650));
	}
	
	public JeuCaracteristiques caracteristiquesDeJeu;
	public ProjectileCaracteristiquesPosition caracteristiquesProjectile;
	public CaracteristiquesModuleDeJeu[] caracteristiquesJoueurs;
	
	public CanvasInterface(JeuCaracteristiques caracteristiquesDeJeu, ProjectileCaracteristiquesPosition caracteristiquesProjectile, CaracteristiquesModuleDeJeu caracteristiquesJoueurUn, CaracteristiquesModuleDeJeu caracteristiquesJoueurDeux){
		this.caracteristiquesDeJeu = caracteristiquesDeJeu;
		this.caracteristiquesProjectile = caracteristiquesProjectile;
		caracteristiquesJoueurs = new CaracteristiquesModuleDeJeu[2];
		caracteristiquesJoueurs[0] =  caracteristiquesJoueurUn;
		this.caracteristiquesJoueurs[1] = caracteristiquesJoueurDeux;
	}
	
	public void paint(Graphics g){
		/*On créer une fenêtre de jeu de 600 sur 600px. Cette quantité est contenue dans la varibale
		tailleFenetre 
		int tailleFenetre = 600;*/
		
		//Le fond de la fenêtre de chois sera en blanc
		g.setColor(Color.white);
		g.fillRect(0, 0, 650, 1200);
		
		/* On dessine la fenêtre de jeu en sachant que, du bas vers le haut:
		 * 		- La terre occupe 10px et est peinte en marron
		 * 		- Si eau il y a, elle occupe autant de pixel qu'elle est haute; si il n'y a que de l'eau
		 * alors la fenêtre entière sera bleue.
		 * 		- Ensuite il y a intégralement de l'air.
		 */
		Color couleurFondIntermediaire;
		// La terre
		couleurFondIntermediaire = new Color(94,64,1);
		g.setColor(couleurFondIntermediaire);
		g.fillRect(0, 640, 1200, 10);
		// L'eau
		if(caracteristiquesDeJeu.getHauteurInterfaceEau() >= 0){
			couleurFondIntermediaire = new Color(57,101,167);
			g.setColor(couleurFondIntermediaire);
			g.fillRect(0, 640-caracteristiquesDeJeu.getHauteurInterfaceEau(), 1200, caracteristiquesDeJeu.getHauteurInterfaceEau());
		}
		//L'air
		if(caracteristiquesDeJeu.getHauteurInterfaceEau() <= 590){
			couleurFondIntermediaire = new Color(206,249,252);
			g.setColor(couleurFondIntermediaire);
			g.fillRect(0, 0, 1200, (640 - caracteristiquesDeJeu.getHauteurInterfaceEau()));
		}
		
		/* Il faut peindre les différents joueurs: chercher des images intéressantes et pas trop
		 * grandes pour pouvoir les dessiner. Pour ce qui est du projectile on  le dessine comme
		 * un cercle de rayon 3px
		 */
		int i;
		try{
			Image destruction = ImageIO.read(new File("explosion.gif"));
			for(i=0; i<=1; i++){
				if((caracteristiquesJoueurs[i].getModule() == TypeModule.SOUS_MARIN)){
					Image sousMarinDroite = ImageIO.read(new File("sous-marin_vers_droite.gif")); 
					g.drawImage(sousMarinDroite, (int) caracteristiquesJoueurs[i].getX(), (int) caracteristiquesJoueurs[i].getY(), this);
				}
				else if(caracteristiquesJoueurs[i].getModule() == TypeModule.HELICOPTERE){
					Image helicoptereDroite = ImageIO.read(new File("helico_vers_droite.gif"));
					g.drawImage(helicoptereDroite,(int) caracteristiquesJoueurs[i].getX(), (int) caracteristiquesJoueurs[i].getY(), this);
				}
				else if(caracteristiquesJoueurs[i].getModule() == TypeModule.TANK){
					Image tankDroite = ImageIO.read(new File("tank_vers_droite.gif"));
					g.drawImage(tankDroite,(int) caracteristiquesJoueurs[i].getX(), (int) caracteristiquesJoueurs[i].getY(), this);
				}
				
				if(caracteristiquesJoueurs[i].passivite.getEstDetruit()){
					g.drawImage(destruction,  (int) caracteristiquesJoueurs[i].getX(), (int) caracteristiquesJoueurs[i].getY(), this);
					
				}
			}
		}catch(IOException exc) {
			exc.printStackTrace();
		}
		g.setColor(Color.black);
		g.fillOval((int) Math.ceil(caracteristiquesProjectile.getX()),(int) Math.ceil(caracteristiquesProjectile.getY()),6,6);
		
		
	}

	
	public void modifierCaracteristiquesJeu(JeuCaracteristiques caracteristiquesDeJeu){
		this.caracteristiquesDeJeu = caracteristiquesDeJeu;	
	}
	
	public void modifierCaracteristiquesProjectile(ProjectileCaracteristiquesPosition caracteristiquesProjectile){
		this.caracteristiquesProjectile = caracteristiquesProjectile;
	}
	
	public void modifierCaracteristiquesModuleDeJeu(CaracteristiquesModuleDeJeu moduleCaracteristiques, int i){
		this.caracteristiquesJoueurs[i] = moduleCaracteristiques;
	}
}
