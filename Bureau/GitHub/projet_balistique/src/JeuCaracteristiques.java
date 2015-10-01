// Cette classe a pour but de mémoriser les données de jeu.
// Ainsi, une instance de cette classe contiendra toutes les données relatives au type de jeu
// i.e. le nombre de joueurs, la position de l'eau, le niveau de jeu etc.
// Les constructeurs sont crées de telle manière qu'il soit possible de créer un gameplay
// sur demande, mais aussi de manière automatique et aléatoire.
// Pour plus amples informations, voir l'avant-projet.



class JeuCaracteristiques {
	
	// Le pas pour intégrer les équations différentielles du mouvement
	public final double DT; 
	public static double g = 9.81;

	// Le niveau de jeu sera choisi parmis 3 (simple, moyen, expert)
	// Le nombre de joueurs sera soit 1 (seul contre ordi), soit à deux contre l'ordi, 
	// soit à deux en réseau.
	enum NiveauJeu {
		FACILE, INTERMEDIAIRE, EXPERT
	}
	private NiveauJeu niveauJeu;
	enum NombreJoueurs {
		SEUL, DUO_ORDI, DUO_RESEAU
	}
	private NombreJoueurs nombreJoueurs;
	
	
	//Niveau de l'eau pour le gameplay. Si pas d'eau, on prend la référence -1
	private int hauteurInterfaceEau;
	
	//On crée les epsilons relatifs au type de jeu:
	private int epsVent, epsFrottements;
	
	//Pour le vent, on le schématise comme une force F algébrique selon x.
	private double ventX, ventY;
	
	//On crée deux types de constructeurs:
	//  - un constructeur aléatoire
	//  - un constructeur sur demande
	
	public JeuCaracteristiques(int niveauJeu, int nombreJoueurs, int hauteurInterfaceEau){
		DT = 0.005;
		switch(niveauJeu){
			case 1:
				this.niveauJeu = NiveauJeu.FACILE;
				break;
			
			case 2:
				this.niveauJeu = NiveauJeu.INTERMEDIAIRE;
				break;
				
			case 3:
				this.niveauJeu = NiveauJeu.EXPERT;
				break;
				
			default:
				//Il faudra lancer une exception ici pour montrer que le jeu voulu n'est pas correct
		}
		
		switch(nombreJoueurs){
		case 1:
			this.nombreJoueurs = NombreJoueurs.SEUL;
			epsVent = 0;
			epsFrottements = 0;
			break;
		
		case 2:
			this.nombreJoueurs = NombreJoueurs.DUO_ORDI;
			epsFrottements = 1;
			epsVent = 1;
			break;
			
		case 3:
			this.nombreJoueurs = NombreJoueurs.DUO_RESEAU;
			epsFrottements = 1;
			epsVent = 1;
			break;
			
		default:
			//Il faudra lancer une exception ici pour montrer que le jeu voulu n'est pas correct
		}
		
		this.hauteurInterfaceEau = hauteurInterfaceEau;
		ventX = 500;
		ventY = -200;// Valeur à changer pour créer un système qui "crée" aléatoirement le vent
	}
	
	public JeuCaracteristiques(NiveauJeu niveauDeJeu, int nombreJoueurs, int hauteurEau){
		this(1,nombreJoueurs,hauteurEau);
		niveauJeu = niveauDeJeu;
	}
	
	public JeuCaracteristiques(){
		this(0,1,100);
	}
	
	// On a besoin de methodes donnant les différentes données:
	public int getHauteurInterfaceEau(){
		return hauteurInterfaceEau;
	}
	
	public NiveauJeu getNiveauJeu(){
		return niveauJeu;
	}
	
	public NombreJoueurs getNombreJoueurs(){
		return nombreJoueurs;
	}
	
	public int getEpsVent(){
		return epsVent;
	}
	
	public int getEpsFrottements(){
		return epsFrottements;
	}
	
	public double getVentX(){
		return ventX;
	}
	
	public double getVentY(){
		return ventY;
	}
	
	/*Pour savoir dans les classes ProjectileCaracteristiquesPosition et CaracteristiquesModuleDeJeu
	quel type de frottements adopter, il faut d'abord savoir si oui ou non nous nous trouvons dans l'eau.
	La methode suivante le permet */
	public boolean estDansEau(double y){
		return (y < hauteurInterfaceEau);
	}
	
	public void changerNombreJoueurs(int i){
		switch(i){
		case 1:
			this.nombreJoueurs = NombreJoueurs.SEUL;
			epsVent = 0;
			epsFrottements = 0;
			break;
		
		case 2:
			this.nombreJoueurs = NombreJoueurs.DUO_ORDI;
			epsFrottements = 1;
			epsVent = 1;
			break;
			
		case 3:
			this.nombreJoueurs = NombreJoueurs.DUO_RESEAU;
			epsFrottements = 1;
			epsVent = 1;
			break;
			
		default:
			//Il faudra lancer une exception ici pour montrer que le jeu voulu n'est pas correct
		}
	}
		
	public void changerNiveau(int i){
		switch(i){
		case 1:
			this.niveauJeu = NiveauJeu.FACILE;
			break;
		
		case 2:
			this.niveauJeu = NiveauJeu.INTERMEDIAIRE;
			break;
			
		case 3:
			this.niveauJeu = NiveauJeu.EXPERT;
			break;
			
		default:
			//Il faudra lancer une exception ici pour montrer que le jeu voulu n'est pas correct
		}
	}

}
