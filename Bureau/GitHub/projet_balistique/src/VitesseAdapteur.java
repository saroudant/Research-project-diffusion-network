import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/* Lorsqu'un joueur veut lancer un projectile, il a besoin de régler la vitesse. La vitesse ici sera un vecteur de deux dimensions.
 * Afin de régler ce vecteur, il bougera sur le plan et la vitesse apparaîtra alors.
 * Lorsqu'un joueur est heureux de la vitesse qui s'affiche, il clique et ce sera bon, le projectile sera  lancé
 */
class VitesseAdapteur implements MouseListener, MouseMotionListener{
	public CanvasInterface dessin;
	private ProjectileCaracteristiquesPosition projectileCaracteristiques;
	private boolean quoiChanger; // Vaut true si on change la vitesse du projectile, false si on change l'accélération du module
	private JLabel vitesse, acceleration;
	private JeuCaracteristiques caracteristiquesJeu;
	private CaracteristiquesModuleDeJeu[] modulesDeJeu;
	private int indexModule;
	private JLabel energieCinetiqueValue;
	
	public VitesseAdapteur(CanvasInterface dessin, ProjectileCaracteristiquesPosition projectileCaracteristiques, boolean quoiChanger, JLabel vitesse, JLabel acceleration, JeuCaracteristiques caracteristiquesJeu, CaracteristiquesModuleDeJeu moduleUn, CaracteristiquesModuleDeJeu moduleDeux, JLabel energieCinetiqueValue){
		this.dessin = dessin;
		this.quoiChanger = quoiChanger;
		this.vitesse = vitesse;
		this.acceleration = acceleration;
		this.caracteristiquesJeu = caracteristiquesJeu;
		this.projectileCaracteristiques = projectileCaracteristiques;
		modulesDeJeu = new CaracteristiquesModuleDeJeu[2];
		modulesDeJeu[0] = moduleUn;
		modulesDeJeu[1] = moduleDeux;
		indexModule = 0; // Par référence, on bouge celui du joueur 1 (toujours joué)
		this.energieCinetiqueValue = energieCinetiqueValue;
	}
	
	//On veut pouvoir changer des valeurs depuis d'autres classes, d'où les méthodes suivantes:
	public void changerChoix(boolean bool){
		quoiChanger = bool;
	}
	
	public void changerModule(){
		switch(indexModule){
		case 0:
			indexModule = 1;
			break;
			
		case 1:
			indexModule = 0;
			break;
		
		default:
		}
	}
	
	public void mouseMoved(MouseEvent e){
		//On cherche la position de la souris
		dessin.repaint();
		Point positionSouris;
		positionSouris = e.getPoint();
		
		Graphics g = dessin.getGraphics();
		g.drawLine((int) modulesDeJeu[indexModule].getX(), (int) modulesDeJeu[indexModule].getY(), positionSouris.x, positionSouris.y);
		int i;
		for(i=0; i<= 100; i++){
			positionSouris = e.getPoint();
			g.drawLine((int) modulesDeJeu[indexModule].getX(), (int) modulesDeJeu[indexModule].getY(), positionSouris.x, positionSouris.y);
		}
		
	}
	
	public void mouseClicked(MouseEvent e){
		//On cherche la position de la souris
		projectileCaracteristiques.renew(modulesDeJeu[indexModule]);
		Point positionSouris;
		positionSouris = e.getPoint();
						
		if(quoiChanger){
			projectileCaracteristiques.changerVitesseX((positionSouris.x - (int) modulesDeJeu[indexModule].getX())/10);
			projectileCaracteristiques.changerVitesseY((positionSouris.y - (int) modulesDeJeu[indexModule].getY())/10);
			projectileCaracteristiques.changerPosition(modulesDeJeu[indexModule]);
			TrajectoireProjectile trajectoire = new TrajectoireProjectile(dessin, projectileCaracteristiques, caracteristiquesJeu, modulesDeJeu[0], modulesDeJeu[1], energieCinetiqueValue);
			vitesse.setText("("+projectileCaracteristiques.getVitesseX()+","+projectileCaracteristiques.getVitesseY()+") en m/s");
			trajectoire.start();
		}
		else{
			modulesDeJeu[indexModule].changerAccelerationX((positionSouris.x - (int) modulesDeJeu[indexModule].getX())/10);
			modulesDeJeu[indexModule].changerAccelerationY((positionSouris.y - (int) modulesDeJeu[indexModule].getY())/10);
			acceleration.setText("("+ modulesDeJeu[indexModule].getAccelerationX() +","+ modulesDeJeu[indexModule].getAccelerationY() +") en m/s");
		}
		
	}
	
	public void mouseDragged (MouseEvent e){
		
	}
	
	public void mousePressed(MouseEvent e){
		
	}
	
	public void mouseExited(MouseEvent e){
		
	}
	
	public void mouseEntered(MouseEvent e){
		
	}
	
	public void mouseReleased(MouseEvent e){
		
	}
}
