import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class LancerJeu implements ActionListener {
	
	private JFormattedTextField hauteurEau, nbreJoueurs;
	private JeuCaracteristiques caracteristiquesDeJeu;
	private ProjectileCaracteristiquesPosition caracteristiquesProjectile;
	private CaracteristiquesModuleDeJeu caracteristiquesJoueurUn, caracteristiquesJoueurDeux;
	public CanvasInterface dessin;
	private Frame fenetreEnglobante;
	//private InterfaceJeu interfaceDeJeu;
	
	public LancerJeu(Frame fenetre, JFormattedTextField hauteurEau, JFormattedTextField nbreJoueurs, JeuCaracteristiques caracteristiquesDeJeu, CanvasInterface dessin, ProjectileCaracteristiquesPosition caracteristiquesProjectile){
	
		this.fenetreEnglobante = fenetre;
		caracteristiquesJoueurUn = new CaracteristiquesModuleDeJeu();
		this.hauteurEau = hauteurEau;
		this.caracteristiquesDeJeu = caracteristiquesDeJeu; 
		this.caracteristiquesProjectile = caracteristiquesProjectile;
		this.dessin = dessin;
	}
	
	public void actionPerformed(ActionEvent e){
		caracteristiquesDeJeu = new JeuCaracteristiques(caracteristiquesDeJeu.getNiveauJeu(),  1 /*(int) (Integer.parseInt(nbreJoueurs.getText()))*/, (int) (Integer.parseInt(hauteurEau.getText())));
		//dessin = new CanvasInterface(caracteristiquesDeJeu, caracteristiquesProjectile, caracteristiquesJoueurUn);
		dessin.modifierCaracteristiquesJeu(caracteristiquesDeJeu);
		dessin.repaint();
	}
}
