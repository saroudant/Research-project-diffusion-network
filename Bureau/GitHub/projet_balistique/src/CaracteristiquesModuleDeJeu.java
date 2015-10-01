/* Pour jouer, le joueur a besoin d'un module de jeu qui représentera soit un tank (jeu sur terre),
 * soit un hélicoptère (jeu en l'air), soit un sous-marin (jeu sous l'eau). Selon le type de
 * module de jeu, on enverra le projectile avec des propriétés différentes.
 * Cette classe caractérise ces modules
 * 
 *  On considèrera que 1px = 10 m
 */

enum TypeModule{
		/* Valeurs à changer!!!
		 * Il faut conserver cependant l'idée que plus un outil est gros et plus il peut envoyer
		 * un projectile avec une puissance élevée */
		TANK("tank"), 
		HELICOPTERE("hélicoptère"),
		SOUS_MARIN("sous-marin");
		
		private String nomModule;
		private double energieCinetiqueEnvoi;
		private double demiLongueur, demiHauteur;
		public double volume, masse;

		private TypeModule (String nomModule){
			
			this.nomModule = nomModule;
			
			if((this.nomModule).equals("tank")){
				energieCinetiqueEnvoi = 15000;
				demiLongueur = 30;
				demiHauteur = 5;
				volume = 3;
				masse = 10*1000*1000;
			}
			else if((this.nomModule).equals("hélicoptère")){
				energieCinetiqueEnvoi = 12000;
				demiLongueur = 46;
				demiHauteur = 38;
				volume = 10;
				masse = 3*1000*1000;
			}
			else if((this.nomModule).equals("sous-marin")){
				energieCinetiqueEnvoi = 20000;
				demiLongueur = 100;
				demiHauteur = 10;
				volume = 100;
				masse = 100*1000*1000;
			}
		}
		
		public String getNomModule(){
			return nomModule;
		}
		public double getEnergieCinetiqueEnvoie(){
			return energieCinetiqueEnvoi;
		}
		public double getHauteur(){
			return demiHauteur;
		}
		public double getLongueur(){
			return demiLongueur;
		}
	}

class CaracteristiquesModuleDeJeu {
	//On donne un nom au module
	
	TypeModule typeDuModule;
	
	//On lui donne les caractéristiques de position via les attributs suivants(position du centre d'inertie):
	private double x,y;
	private double vitesseX, vitesseY;
	private double accelerationX, accelerationY;
	public ComportementPassif passivite;
	
	
	/* Il y a deux types de comportements pour un module de jeu:
	 * 	* un comportement passif, quand on se fait tirer dessus
	 * 	* un comportement actif, quand on tire sur l'autre
	 * Les deux classes internes suivantes traduisent ce fait 
	 */
	
	class ComportementPassif{
		public double energieResistance; //Mesure l'énergie cinétique encore à fournir pour détruire la cible
		private boolean estDetruit;
		
		public ComportementPassif(){
			energieResistance = 200;
			estDetruit = false;
		}
		
		public void detruire(){
			estDetruit = true;
			energieResistance = 0;
		}
		
		public void reconstruire(){
			estDetruit = false;
		}
		
		
		public double getEnergieResistance(){
			return energieResistance;
		}
		
		public boolean getEstDetruit(){
			return estDetruit;
		}
		
	}
	
	/* On créera un module au début de jeu pour savoir d'où part le module. Cela explique la signature.
	 * Il partira toujours avec une vitesse nulle à la création du jeu (pas d'intérêt à partir avec une vitesse)
	 */
	public CaracteristiquesModuleDeJeu(TypeModule typeDuModule, double x, double y){
		this.typeDuModule = typeDuModule;
		this.x = x;
		this.y = y;
		//Les vitesses sont divisées par 10 également (pour rendre le jeu plus plausible)
		this.vitesseX = 10;
		this.vitesseY= -15;
		accelerationX = 0;
		accelerationY = 0;
		passivite = new ComportementPassif();
	}
	
	public CaracteristiquesModuleDeJeu(){
		this(TypeModule.HELICOPTERE,45,200);
	}
	
	//Fonction qui regarde, pour un projectile donné, si ce-dernier touche le module
	public boolean getTouched(ProjectileCaracteristiquesPosition projectileCaracteristiques){
		return ((projectileCaracteristiques.getX() >= (x)) && (projectileCaracteristiques.getX() <= (x + 2*typeDuModule.getLongueur()))
				&& (projectileCaracteristiques.getY() >= y ) && (projectileCaracteristiques.getY() <= (y + 2*typeDuModule.getHauteur())));
	}
	
	/*On considère le cas où un projectile vient heurter le module de jeu
	 * Dans ce cas, on soustrait au module de jeu l'énergie cinétique du projectile.
	 * Si il se trouve que l'énergie de résistance est inférieure à 0 après soustraction, cela signifie que le projectile a touché sa cible et l'a détruite.
	 * Dans ce cas, on met cette énergie à 0.
	 */
	public void reduireEnergieResistance(ProjectileCaracteristiquesPosition caracteristiquesProjectile){
		passivite.energieResistance -= caracteristiquesProjectile.energieCinetique();
		if(passivite.energieResistance <= 0){
			passivite.energieResistance = 0;
			detruire();
		}
	}
	public void detruire(){
		vitesseX = 0;
		vitesseY = 0;
		passivite.detruire();
	}
	
	//Fonction de retour des variables;
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
	public TypeModule getModule(){
		return typeDuModule;
	}
	
	public void changerAccelerationX(double accelerationX){
		this.accelerationX = accelerationX;
	}
	public void changerAccelerationY(double accelerationY){
		this.accelerationY = accelerationY;
	}
	public void changerType(TypeModule type){
		this.typeDuModule = type;
		if(typeDuModule == TypeModule.TANK){
			y = 540;
		}
		this.vitesseX = 0;
		this.vitesseY= 0;
		accelerationX = 0;
		accelerationY = 0;
		passivite.reconstruire();
	}
	public void changerPosition(double x, double y){
		this.x = x;
		this.y = y;
	}

	
	//On passe de t à t+dt dans les équations du mouvement
	public void changementPosition(JeuCaracteristiques caracteristiquesDeJeu){
		/*On gagne juste en lisibilité avec dt; je suis conscient que cela nous fait perdre
		les règles élementaires d'écriture en Java, mais je pensais que c'était mieux ainsi.
		*/
		double dt;
		dt = caracteristiquesDeJeu.DT;
		
		/*On détermine si on est dans l'eau ou pas: pour ce faire on met 0 à une variable de
		type epsilon avec 0 si dans l'air, 1 si dans l'eau */
		int epsEau;
		if(caracteristiquesDeJeu.estDansEau(y)){
			epsEau = 1;
		}
		else{
			epsEau = 0;
		}
		
		x += dt * vitesseX;
		vitesseX += dt * accelerationX;
		accelerationX = (- (1-epsEau)*(caracteristiquesDeJeu.getEpsFrottements() * vitesseX + caracteristiquesDeJeu.getEpsVent() * caracteristiquesDeJeu.getVentX()) - epsEau * 0.5 * vitesseX * vitesseX)/10;
	
		y += dt * vitesseY;
		vitesseY += dt * accelerationY;
		accelerationY = (JeuCaracteristiques.g - JeuCaracteristiques.g * typeDuModule.volume * 1000 / typeDuModule.masse + (1-epsEau)*(caracteristiquesDeJeu.getEpsFrottements() * vitesseY + caracteristiquesDeJeu.getEpsVent() * caracteristiquesDeJeu.getVentY()) - epsEau * 0.5 * vitesseY * vitesseY)/10;
	
		if(y > 640 - 10*typeDuModule.getHauteur()){
			y = 639 - 10*typeDuModule.getHauteur();
			vitesseY = 0;
			accelerationY = 0;
		}
		
		//Il y a des aberrations à éviter: que le tank ne s'envole, que l'hélico plonge ou que le sous-marin finisse dans les airs!
		if(typeDuModule.equals(TypeModule.TANK )){
			accelerationY = 0;
			vitesseY = 0;
		}
		else if(typeDuModule.equals(TypeModule.SOUS_MARIN) && (y < 328 - caracteristiquesDeJeu.getHauteurInterfaceEau())){
			y = 328 - caracteristiquesDeJeu.getHauteurInterfaceEau();
			vitesseY = 0;
			accelerationY = 0;		
		}
		else if(typeDuModule.equals(TypeModule.HELICOPTERE) && (y > 328 - caracteristiquesDeJeu.getHauteurInterfaceEau())){
			y = 328 - caracteristiquesDeJeu.getHauteurInterfaceEau();
			vitesseY = 0;
			accelerationY = 0;	
		}
	}
}
