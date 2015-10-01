import javax.swing.*;


/* L'objectif de ce thread est de réaliser l'intégration pas à pas de la trajectoire du projectile et de dessiner cette trajectoire.
 * Il a également pour objectif de voir si le projectile rencontre une cible sur sa route
 */
class TrajectoireProjectile extends Thread {
	//On a besoin de dessiner sur le canvas de l'interface et on a besoin des coordonnés du projectile pour créer la trajectoire
	public CanvasInterface dessin;
	private ProjectileCaracteristiquesPosition caracteristiquesProjectile;
	private JeuCaracteristiques caracteristiquesDeJeu;
	private CaracteristiquesModuleDeJeu[] modulesDeJeu;
	private JLabel energieCinetiqueValue;
	
	
	public TrajectoireProjectile(CanvasInterface dessin, ProjectileCaracteristiquesPosition caracteristiquesProjectile, JeuCaracteristiques caracteristiquesDeJeu, CaracteristiquesModuleDeJeu moduleUn, CaracteristiquesModuleDeJeu moduleDeux, JLabel energieCinetiqueValue){
		this.dessin = dessin;
		this.caracteristiquesProjectile = caracteristiquesProjectile;
		this.caracteristiquesDeJeu = caracteristiquesDeJeu;
		modulesDeJeu = new CaracteristiquesModuleDeJeu[2];
		modulesDeJeu[0] = moduleUn;
		modulesDeJeu[1] = moduleDeux;
		this.energieCinetiqueValue = energieCinetiqueValue;
	}
	
	public void run(){
		int i = 0;

		//Le projectile n'a d'intérêt que lorsqu'il est dans la fenêtre (pour toucher un opposant), ou lorsqu'il n'a pas encore touché un opposant
		while(caracteristiquesProjectile.getX() <= 1000 && caracteristiquesProjectile.getX() >= -20 && caracteristiquesProjectile.getY() <= 1400 && caracteristiquesProjectile.getY() >= -20 && !modulesDeJeu[1].getTouched(caracteristiquesProjectile)){
			caracteristiquesProjectile.changementPosition(caracteristiquesDeJeu);
			dessin.modifierCaracteristiquesProjectile(caracteristiquesProjectile);
			dessin.repaint();
			try{
				sleep(1);
			}
			catch(InterruptedException e){	
				for(i=0; i<=10; i++){					
				}
			}
		}
		if(modulesDeJeu[1].getTouched(caracteristiquesProjectile)){
			//modulesDeJeu[i].reduireEnergieResistance(caracteristiquesProjectile);
			caracteristiquesProjectile.changerPosition(-100,0);
			modulesDeJeu[1].detruire();
			energieCinetiqueValue.setText(modulesDeJeu[i].passivite.getEnergieResistance()+" J");
		}
		dessin.modifierCaracteristiquesProjectile(caracteristiquesProjectile);
		dessin.repaint();
	}
}
