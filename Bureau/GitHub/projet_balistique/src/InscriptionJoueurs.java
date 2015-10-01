import java.awt.*;
import java.awt.event.*;

/* Ecouteur du bouton d'inscription des joueurs
 * Cette classe inscrit les deux joueurs afin de permettre au gameplay de commencer.
 */
public class InscriptionJoueurs implements ActionListener{
	private JeuCaracteristiques caracteristiquesDeJeu;
	private CaracteristiquesModuleDeJeu[] caracteristiquesJoueurs;
	private Choice[] choixJoueurs;
	private String[] caseSelected;
	private CanvasInterface dessin;
	
	public InscriptionJoueurs(JeuCaracteristiques caracteristiquesDeJeu,CanvasInterface dessin, CaracteristiquesModuleDeJeu caracteristiquesJoueurUn, CaracteristiquesModuleDeJeu caracteristiquesJoueurDeux, Choice choixJoueurUn, Choice choixJoueurDeux){
		this.caracteristiquesDeJeu = caracteristiquesDeJeu;
		caracteristiquesJoueurs = new CaracteristiquesModuleDeJeu[2];
		caracteristiquesJoueurs[0] = caracteristiquesJoueurUn;
		caracteristiquesJoueurs[1] = caracteristiquesJoueurDeux;
		choixJoueurs = new Choice[2];
		choixJoueurs[0] = choixJoueurUn;
		choixJoueurs[1] = choixJoueurDeux;
		caseSelected = new String[2];
		this.dessin = dessin;
	}
	
	public void actionPerformed(ActionEvent e){
		int i;
		int positionAbscisse;
		for(i=0; i<=1; i++){
			switch(i){
			case 0:
				positionAbscisse = 20;
				break;
				
			case 1:
				positionAbscisse = 280;
				break;
			
			default:
				positionAbscisse = 0;
			}
			caseSelected[i] = choixJoueurs[i].getSelectedItem();
			if(caseSelected[i].equals("Sous-marin")){
				caracteristiquesJoueurs[i].changerType(TypeModule.SOUS_MARIN);
				caracteristiquesJoueurs[i].changerPosition(positionAbscisse,600);
			}
			else if(caseSelected[i].equals("Hélicoptère")){
				caracteristiquesJoueurs[i].changerType(TypeModule.HELICOPTERE);
				caracteristiquesJoueurs[i].changerPosition(positionAbscisse, 30);
			}
			else if(caseSelected[i].equals("Tank")){
				caracteristiquesJoueurs[i].changerType(TypeModule.TANK);
				caracteristiquesJoueurs[i].changerPosition(positionAbscisse,650);
			}
			dessin.repaint();
		}
	}
}
