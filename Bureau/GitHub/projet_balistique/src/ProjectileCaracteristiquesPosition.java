/* Cette classe a pour but de représenter les caractéristiques du projectile.
 * Le but est que cette classe contienne toutes les informations nécessaires au calcul de
 * la position, et tous les outils de calcul.
 * On y disposera également une méthode permettant de savoir si une cible est touchée.
 * 
 * Afin de rendre le jeu jouable, on considère que 1px = 10m. Ainsi, le jeu sera réaliste.
 */

class ProjectileCaracteristiquesPosition {
	//Il faut avoir des données cinétiques que l'on trouve ci-dessous
	private double x,y;
	private double vitesseX, vitesseY;
	private double accelerationX, accelerationY;
	
	//La masse est une constante qui ne variera pas au cours du temps.
	final double MASSE = 100*1000; //en g
	final double VOLUME = 1; // en m^3
	
	//On passe de t à t+dt dans les équations du mouvement
	public void changementPosition(JeuCaracteristiques caracteristiquesDeJeu){
		/*On gagne juste en lisibilité avec dt; je suis conscient que cela nous fait perdre
		les règles élementaires d'écriture en Java, mais je pensais que c'était mieux ainsi.
		*/
		double dt;
		dt = caracteristiquesDeJeu.DT;
		
		/*On détermine si on est dans l'eau ou pas: pour ce faire on met 0 à une variable de
		type epsilon avec 0 si dans l'air, 1 si dans l'eau */
		double epsEau;
		if(caracteristiquesDeJeu.estDansEau(y)){
			epsEau = 1;
		}
		else{
			epsEau = 0;
		}
		
		x += dt * vitesseX;
		vitesseX += dt * accelerationX;
		accelerationX = (- (1-epsEau)*(caracteristiquesDeJeu.getEpsFrottements() * vitesseX + caracteristiquesDeJeu.getEpsVent() * caracteristiquesDeJeu.getVentX()) - epsEau * 0.5 * vitesseX * vitesseX) / 10;
	
		y += dt * vitesseY;
		vitesseY += dt * accelerationY;
		accelerationY =  (JeuCaracteristiques.g - JeuCaracteristiques.g * VOLUME * 1000 / (MASSE)  - (1-epsEau)*(caracteristiquesDeJeu.getEpsFrottements() * vitesseY/ MASSE + caracteristiquesDeJeu.getEpsVent() * caracteristiquesDeJeu.getVentY())- epsEau * 0.5 * vitesseY * vitesseY)/10;
	
		if(y<=10){
			y = 10;
			vitesseY = 0;
			accelerationY = 0;
		}
	}
	
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public double getVitesseX(){
		return vitesseX;
	}
	public double getVitesseY(){
		return vitesseY;
	}
	public double getAccelerationX(){
		return accelerationX;
	}
	public double getAccelerationY(){
		return accelerationY;
	}
	
	
	public void changerVitesseX(int newVitesseX){
		this.vitesseX = newVitesseX;
	}
	public void changerVitesseY(int newVitesseY){
		this.vitesseY = newVitesseY;
	}
	
	public void changerPosition(CaracteristiquesModuleDeJeu module){
		x = module.getX();
		y = module.getY();
	}
	
	public void changerPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public ProjectileCaracteristiquesPosition(double x, double y, double vitesseX, double vitesseY){
		this.x = x;
		this.y = y;
		this.vitesseX = vitesseX;
		this.vitesseY = vitesseY;
	}
	
	public ProjectileCaracteristiquesPosition(){
		this(-5,0,0,0);
	}
	
	public void renew(CaracteristiquesModuleDeJeu moduleDeJeu){
		x = moduleDeJeu.getX();
		y = moduleDeJeu.getY();
	}
	
	public double energieCinetique(){
		return 1/2*MASSE*(vitesseX*vitesseX + vitesseY*vitesseY);
	}
}
