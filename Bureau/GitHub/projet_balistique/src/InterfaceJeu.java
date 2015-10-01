/* Cette classe permettra de g�rer toute l'interface jeu. Elle utilisera les classes d�j� cr�es.
 * L'interface graphique de r�f�rence est celle de l'avant-projet, m�me si je m'en suis
 * quelque peu d�solidaris� dans mon code.
 */


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.awt.GridLayout; 
import javax.imageio.ImageIO;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;
import javax.swing.*;
import java.lang.*;


@SuppressWarnings("serial")
public class InterfaceJeu extends JFrame implements ActionListener, WindowListener{

	static final long serialVersionUID=1L;
	
	//On cr�e les diff�rents boutons utiles pour l'IHM
	private BannierePresentation banniere;
	/*Panel qui contient tous les boutons, panels etc. Tout ce qui n'est pas le dessin On d�fini ensuite le tableau de boolean avec un seul des trois vrai.
	 * Si le premier est vrai, alors on affiche les changements de param�tre de l'eau
	 * Si le deuxieme est vrai, on affiche le choix du type de jeu
	 * Enfin, on lance le jeu! */
	private boolean[] affichageChoix = new boolean[3];
	private JPanel boutonsChoix;
	//Boutons utiles dans la phase d'inscription des donn�es
	private JPanel boutonsDroite;
	public CanvasInterface dessinJeu;
	private JButton lancerInscription;
	private ButtonGroup typeJeu, difficulteJeu;
	private JRadioButtonMenuItem[] menusTypeJeu, menusDifficulteJeu;
	private JPanel panelMenusTypeJeu, panelMenusDifficulteJeu;
	private JLabel labelMenusTypeJeu, labelMenusDifficulteJeu;
	private JFormattedTextField nbreJoueurs, hauteurEau;
	private JLabel labelNbreJoueurs, labelHauteurEau;
	private JPanel panelNbreJoueurs, panelHauteurEau;
	//Boutons utiles pour l'inscription des joueurs
	private JPanel boutonsInscription;
	private JLabel premierJoueur, deuxiemeJoueur;
	private Choice choixPremierJoueur, choixDeuxiemeJoueur;
	private JButton lancerJeu;
	//Boutons et outils utiles dans la phase de jeu
	private JButton changerPosition, lancerProjectile;
	private boolean quoiChanger; //true si on modifie la vitesse, false si on modifie l'acc�l�ration du module de jeu.
	private boolean typeModifications, changerTypeDeChangement; //true si on change les caract�ristiques de jeu, faux sinon => on l'initie � true
	private JPanel changerChoix;
	private JPanel typeDeChangement;
	private JLabel vitesseInitialeProjectile, accelerationModuleJeu;
	public JLabel vitesseInitialeProjectileValue, accelerationModuleJeuValue;
	private JLabel energieCinetiqueOpposant, energieCinetiqueOpposantValue;
	private JButton changerCaracteristiquesJeu;
	//Caracteristiques du jeu
	private JeuCaracteristiques caracteristiquesDeJeu;
	private ProjectileCaracteristiquesPosition caracteristiquesProjectile;
	private CaracteristiquesModuleDeJeu caracteristiquesJoueurUn, caracteristiquesJoueurDeux;
	//Quelques �couteurs
	private VitesseAdapteur vitesseParticule;
	
	public static void main (String args[]){
		InterfaceJeu interfaceDeJeu = new InterfaceJeu();
		//On veut que la fen�tre occupe tout l'�cran:
		Dimension dimensionEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int hauteurEcran = (int)dimensionEcran.getHeight();
		int largeurEcran  = (int)dimensionEcran.getWidth();
		interfaceDeJeu.setMinimumSize(new Dimension(1350,650));
		interfaceDeJeu.setSize(new Dimension(largeurEcran,hauteurEcran));
		interfaceDeJeu.affiche();
		interfaceDeJeu.pack();
	}
	
	public InterfaceJeu(){
		
			
		//On cr�e les diff�rents param�tre de jeu, initialis�s aux valeurs par d�faut
		caracteristiquesDeJeu = new JeuCaracteristiques();
		caracteristiquesProjectile = new ProjectileCaracteristiquesPosition();
		caracteristiquesJoueurUn = new CaracteristiquesModuleDeJeu();
		caracteristiquesJoueurDeux = new CaracteristiquesModuleDeJeu();
		caracteristiquesJoueurDeux.changerPosition(500,200);
		typeModifications = true;
		
		
		//Pour la banni�re:
				banniere = new BannierePresentation();
				banniere.setSize(new Dimension(500,450));
				this.add(banniere);
				try{
					Thread.sleep(1000);
				}catch(InterruptedException e){
				}
				this.remove(banniere);
				
		//On cr�e les diff�rents outils
		boutonsChoix = new JPanel();
		boutonsDroite = new JPanel();
		dessinJeu = new CanvasInterface(caracteristiquesDeJeu, caracteristiquesProjectile, caracteristiquesJoueurUn, caracteristiquesJoueurDeux);
		lancerInscription = new JButton("Inscription des joueurs");
		panelNbreJoueurs = new JPanel();
		labelNbreJoueurs = new JLabel("Nombre de joueurs (1 ou 2)");
		nbreJoueurs = new JFormattedTextField(java.text.NumberFormat.getIntegerInstance());
		MaskFormatter masqueHauteurEau = new MaskFormatter();
		masqueHauteurEau.setValidCharacters("0123456789");
		panelHauteurEau = new JPanel();
		labelHauteurEau = new JLabel("Hauteur de l'eau (entre 0 et 850)");
		hauteurEau = new JFormattedTextField(java.text.NumberFormat.getIntegerInstance());
		
		
		//Cr�ation des barres de d�filement
		//Regarder les Headless execeptions pour les menubar!
		typeJeu = new ButtonGroup();
		difficulteJeu = new ButtonGroup();
		//On cr�e les diff�rents items du menu
		panelMenusTypeJeu = new JPanel();
		labelMenusTypeJeu = new JLabel("Type de jeu");
		menusTypeJeu = new JRadioButtonMenuItem[3];
		menusTypeJeu[0] = new JRadioButtonMenuItem("VS Ordinateur");
		//menusTypeJeu[1] = new JRadioButtonMenuItem("VS Ami sur m�me ordinateur");
		panelMenusDifficulteJeu = new JPanel();
		labelMenusDifficulteJeu = new JLabel("Difficulte du jeu");
		menusDifficulteJeu = new JRadioButtonMenuItem[3];
		menusDifficulteJeu[0] = new JRadioButtonMenuItem("Facile");
		menusDifficulteJeu[1] = new JRadioButtonMenuItem("Interm�diaire");
		menusDifficulteJeu[2] = new JRadioButtonMenuItem("Difficile");
		//On les rentre
		int i;
		for(i=0; i<=2; i++){
				typeJeu.add(menusTypeJeu[i]);
				difficulteJeu.add(menusDifficulteJeu[i]);
		}
		//Cr�ation des outils n�cessaires � l'inscription des joueurs
		boutonsInscription = new JPanel();
		premierJoueur = new JLabel ("Caract�ristiques du premier joueur");
		deuxiemeJoueur = new JLabel ("Caract�ristiques du deuxi�me joueur");
		choixPremierJoueur = new Choice();
		choixDeuxiemeJoueur = new Choice();
		lancerJeu = new JButton("Lancer le Jeu!");
		
		//Pour les outils de changements
		changerPosition = new JButton("Changer la position");
		lancerProjectile = new JButton("Lancer le projectile");
		changerChoix = new JPanel();
		typeDeChangement = new JPanel();
		vitesseInitialeProjectile = new JLabel("Vitesse du projectile: \n");
		accelerationModuleJeu = new JLabel("Acceleration du module de jeu:\n");
		vitesseInitialeProjectileValue = new JLabel("(,)");
		accelerationModuleJeuValue = new JLabel("(,)");
		energieCinetiqueOpposant = new JLabel("EnergieCin�tique");
		energieCinetiqueOpposantValue = new JLabel(caracteristiquesJoueurDeux.passivite.getEnergieResistance()+" J");
		changerCaracteristiquesJeu = new JButton("Changer les caracteristiques De Jeu");
		quoiChanger = true;

		/* Pour le positionnement du dessin et des boutons, on a divis� la fen�tre en deux:
		 * - le dessin � gauche qui va prendre les 3/4 de l'espace
		 * - les boutons � droite qui vont prendre 1/4
		 * On utilise un GridBagLayout afin de bien positionner comme on l'entend: ce GridBagLayout poss�de 4 cases horizontalement et une verticalement
		 */
		this.setLayout(new GridBagLayout());
		GridBagConstraints positionnementGBC = new GridBagConstraints();
		positionnementGBC.gridx = 0;
		positionnementGBC.gridy = 0;
		//On ajoute le dessin en premi�re case
		dessinJeu.setPreferredSize(new Dimension(900,650));
		positionnementGBC.gridheight = 1;
	    positionnementGBC.gridwidth = 2;
		this.add(dessinJeu, positionnementGBC);
		//On ajoute les boutons en deuxi�me case
		boutonsChoix.setPreferredSize(new Dimension(450,650));
		positionnementGBC.gridx = 2;
		positionnementGBC.gridwidth = 1;
		this.add(boutonsChoix, positionnementGBC);
		//On utilise un GridLayout pour que ce soit comme on le veut: le dessin � gauche et les boutons � droite
		boutonsChoix.setLayout(new GridLayout(3,1));
		boutonsDroite.setLayout(new GridLayout(5,1));
		changerChoix.setLayout(new GridLayout(7,1));
		typeDeChangement.setLayout(new GridLayout(2,1));
			
		
		//On ajoute les �l�ments aux diff�rents cont�naires
		
		//El�ments g�n�riques, toujours pr�sents
		dessinJeu.setSize();
		this.add(dessinJeu,0);
		
		//On ajoute l'�l�ment de droite
		this.add(boutonsChoix,1);
		
		//El�ments pour cr�er le syst�me de jeu
		boutonsDroite.add(panelMenusDifficulteJeu);
		panelMenusDifficulteJeu.setLayout(new GridLayout(4,1));
		panelMenusDifficulteJeu.setMinimumSize(new Dimension(50,100));
		panelMenusDifficulteJeu.add(labelMenusDifficulteJeu,0);
		for(i=0; i<=2; i++){
			panelMenusDifficulteJeu.add(menusDifficulteJeu[i],i+1);
		}
		boutonsDroite.add(panelMenusTypeJeu);
		panelMenusTypeJeu.setLayout(new GridLayout(2,2));
		panelMenusTypeJeu.add(labelMenusTypeJeu);
		panelMenusTypeJeu.add(menusTypeJeu[0],0);
		boutonsDroite.add(panelNbreJoueurs);
		panelNbreJoueurs.setLayout(new GridLayout(2,2));
		panelNbreJoueurs.add(labelNbreJoueurs);
		panelNbreJoueurs.add(nbreJoueurs);
		nbreJoueurs.setSize(50,10);
		boutonsDroite.add(panelHauteurEau);
		panelHauteurEau.setLayout(new GridLayout(2,1));
		panelHauteurEau.add(labelHauteurEau);
		panelHauteurEau.add(hauteurEau);
		boutonsDroite.add(lancerInscription);
		
		//El�ments pour inscrire les joueurs
		boutonsInscription.setLayout(new GridLayout(3,1));
		boutonsInscription.setMinimumSize(new Dimension(100,250));
		boutonsInscription.add(premierJoueur);
		boutonsInscription.add(deuxiemeJoueur);
		boutonsInscription.add(choixPremierJoueur);
		boutonsInscription.add(choixDeuxiemeJoueur);
		boutonsInscription.add(lancerJeu);
		
		//El�ments pour changer le style de jeu
		typeDeChangement.add(changerPosition);
		typeDeChangement.add(lancerProjectile);
		changerChoix.setLayout(new GridLayout(8,0));
		changerChoix.add(typeDeChangement,0);
		changerChoix.add(vitesseInitialeProjectile,1);
		changerChoix.add(vitesseInitialeProjectileValue,2);
		changerChoix.add(accelerationModuleJeu,3);
		changerChoix.add(accelerationModuleJeuValue,4);
		changerChoix.add(changerCaracteristiquesJeu,5);
		changerChoix.add(energieCinetiqueOpposant,6);
		changerChoix.add(energieCinetiqueOpposantValue,7);
		boutonsChoix.add(boutonsDroite);
		
		//On cr�e les �couteurs
		//Pour lancer l'inscription des joueurs:
		LancerJeu lancerJeuSoumettre;
		lancerJeuSoumettre = new LancerJeu(this, hauteurEau, nbreJoueurs, caracteristiquesDeJeu, dessinJeu, caracteristiquesProjectile);
		lancerInscription.addActionListener(lancerJeuSoumettre);
		lancerInscription.addActionListener(this);
		
		//Pour l'inscription des joueurs
		lancerJeu.addActionListener(this);
		lancerJeu.addActionListener(new InscriptionJoueurs(caracteristiquesDeJeu, dessinJeu, caracteristiquesJoueurUn, caracteristiquesJoueurDeux, choixPremierJoueur, choixDeuxiemeJoueur));
		
		
		//Pour changer les reglage du jeu:
		changerCaracteristiquesJeu.addActionListener(this);
		for(i=0; i<=2; i++){
			if(i<=0){
				menusTypeJeu[i].addActionListener(this);
				menusDifficulteJeu[i].addActionListener(this);
			}
			else{
				menusDifficulteJeu[i].addActionListener(this);
			}
		}
		//Pour le choix de l'objet que l'on fait bouger
		changerPosition.addActionListener(this);
		lancerProjectile.addActionListener(this);
		//Pour le r�glage de la vitesse
		vitesseParticule = new VitesseAdapteur(dessinJeu, caracteristiquesProjectile, quoiChanger, vitesseInitialeProjectileValue, accelerationModuleJeuValue, caracteristiquesDeJeu, caracteristiquesJoueurUn, caracteristiquesJoueurDeux, energieCinetiqueOpposantValue);
		dessinJeu.addMouseMotionListener(vitesseParticule);
		dessinJeu.addMouseListener(vitesseParticule);
		
		//On ajoute l'�coute de la fen�tre pour la fermer
		this.addWindowListener(this);
		
		//On ajoute les threads pour la trajectoire des deux modules de jeu
		TrajectoireModuleJeu[] trajectoireModules;
		trajectoireModules = new TrajectoireModuleJeu[2];
		trajectoireModules[0] = new TrajectoireModuleJeu(caracteristiquesDeJeu, caracteristiquesJoueurUn, dessinJeu, 0, accelerationModuleJeuValue, vitesseInitialeProjectileValue, quoiChanger);
		trajectoireModules[1] = new TrajectoireModuleJeu(caracteristiquesDeJeu, caracteristiquesJoueurDeux, dessinJeu, 1, accelerationModuleJeuValue, vitesseInitialeProjectileValue, quoiChanger);
		trajectoireModules[0].start();
		trajectoireModules[1].start();
	}

	
	public void changerDessin( CanvasInterface dessinJeu){
		this.dessinJeu = dessinJeu;
	}
	public void changerNbreJoueurs(JFormattedTextField nbreJoueurs){
		this.nbreJoueurs = nbreJoueurs;
	}
	public void changerHauteurEau(JFormattedTextField hauteurEau){
		this.hauteurEau = hauteurEau;
	}
	public void changerCaracteristiquesJeu(JeuCaracteristiques caracteristiquesDeJeu){
		this.caracteristiquesDeJeu = caracteristiquesDeJeu;
	}
	public void changerCaracteristiquesProjectile(ProjectileCaracteristiquesPosition caracteristiquesProjectile){
		this.caracteristiquesProjectile = caracteristiquesProjectile;
	}
	
	//Pour obtenir les attributs depuis une autre classe
	public Frame getFenetre(){
		return this;
	}
	public CanvasInterface getDessin(){
		return dessinJeu;
	}
	public JFormattedTextField getNbreJoueurs(){
		return nbreJoueurs;
	}
	public JFormattedTextField getHauteurEau(){
		return hauteurEau;
	}
	public JeuCaracteristiques getCaracteristiquesJeu(){
		return caracteristiquesDeJeu;
	}
	public ProjectileCaracteristiquesPosition getCaracteristiquesProjectile(){
		return caracteristiquesProjectile;
	}
	public CaracteristiquesModuleDeJeu getCaracteristiquesJoueur(){
		return caracteristiquesJoueurUn;
	}
	
	
	public void affiche(){
	    this.setTitle("Le jeu de la balistique");
	    this.setPreferredSize(new Dimension(1000,820));
	    this.pack();
	    this.setVisible(true);
	}
	
	public void actionPerformed (ActionEvent e){
		if(e.getSource() == lancerJeu){
			boutonsChoix.removeAll();
			boutonsChoix.add(changerChoix); 
			boutonsChoix.validate();
			
		}
		
		if(e.getSource() == lancerInscription){
			boutonsChoix.removeAll();
			boutonsChoix.add(boutonsInscription);
			boutonsChoix.validate();
			choixPremierJoueur.removeAll();
			choixDeuxiemeJoueur.removeAll();
			if(menusTypeJeu[0].isSelected()){
				boutonsInscription.remove(choixDeuxiemeJoueur);
				boutonsInscription.remove(deuxiemeJoueur);
			}
			if(Integer.parseInt(hauteurEau.getText()) <= 0){
				choixPremierJoueur.add("Tank"); choixDeuxiemeJoueur.add("Tank");
				choixPremierJoueur.add("H�licopt�re"); choixDeuxiemeJoueur.add("Helicopt�re");
				repaint();
			}
			else if(Integer.parseInt(hauteurEau.getText()) >= 800){
				choixPremierJoueur.add("Sous-marin"); choixDeuxiemeJoueur.add("Sous-marin");
				repaint();
			}
			else{
				choixPremierJoueur.add("Sous-marin"); choixDeuxiemeJoueur.add("Sous-marin");
				choixPremierJoueur.add("H�licopt�re"); choixDeuxiemeJoueur.add("Helicopt�re");
				repaint();
			}
			
		}
		
		if(e.getSource() == changerCaracteristiquesJeu){
			boutonsChoix.removeAll();
			boutonsChoix.add(boutonsDroite);
			boutonsChoix.validate();
		}
		
		if(e.getSource() == changerPosition){
			quoiChanger = true;
			vitesseParticule.changerChoix(true);
		}
		
		if(e.getSource() == lancerProjectile){
			quoiChanger = false;
			vitesseParticule.changerChoix(false);
		}
		
		int i;
		for(i=0; i<=2; i++){
			if(e.getSource() == menusTypeJeu[i] && menusTypeJeu[i].isSelected() && i<=1){
				caracteristiquesDeJeu.changerNiveau(i);
			}
			if(e.getSource() == menusDifficulteJeu[i] && menusDifficulteJeu[i].isSelected()){
				caracteristiquesDeJeu.changerNombreJoueurs(i);
			}
		}
	}
	
	public void windowClosing(WindowEvent w) {
		this.dispose(); // lib�ration des ressources utilis�es par la frame
		System.exit(0); // sort du programme (0 indique � l�OS que c�est une fin normale, sans "erreur")
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
