import javax.swing.*;


/* Cette classe décrit le mouvement des deux modules de jeu au fur et à mesure que le jeu se déroule
 * On utilise les méthodes de la classe CcaracteristiquesModuleDeJeu pour calculer la position au fur et à mesure du jeu
 */
public class TrajectoireModuleJeu extends Thread {
	private JeuCaracteristiques caracteristiquesDeJeu;
	private CaracteristiquesModuleDeJeu caracteristiquesDuModule;
	private ProjectileCaracteristiquesPosition projectileCaracteristiques;
	private CanvasInterface dessin;
	int numeroModule; //Pour savoir sur quel module on travail
	boolean mouvement;
	private JLabel accelerationModuleValue, vitesseProjectileValue;
	private Boolean quoiChanger;
	
	public TrajectoireModuleJeu(JeuCaracteristiques caracteristiquesDeJeu, CaracteristiquesModuleDeJeu caracteristiquesDuModule, CanvasInterface dessin, int i, JLabel accelerationModuleValue, JLabel vitesseProjectileValue, Boolean quoiChanger){
		this.caracteristiquesDeJeu = caracteristiquesDeJeu;
		this.caracteristiquesDuModule = caracteristiquesDuModule;
		this.dessin = dessin;
		numeroModule = i;
		mouvement = true;
		this.accelerationModuleValue = accelerationModuleValue;
		this.vitesseProjectileValue = vitesseProjectileValue;
		this.quoiChanger = quoiChanger;
	}
	
	public void run(){
		int i=0;
		VitesseAdapteur vitesseParticule;
		while(mouvement){
			if((i%800000 == 0)){
				//On crée ce programme afin que le vecteur vitesse suive le module de jeu
				//dessin.addMouseMotionListener(vitesseParticule);
				//dessin.addMouseListener(vitesseParticule);
				caracteristiquesDuModule.changementPosition(caracteristiquesDeJeu);
				dessin.modifierCaracteristiquesModuleDeJeu (caracteristiquesDuModule, numeroModule);
				dessin.repaint();
				//dessin.removeMouseMotionListener(vitesseParticule);
				//dessin.removeMouseListener(vitesseParticule);
			}
			i++;
		}
	}
	
	
}
