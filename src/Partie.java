import java.util.ArrayList;
import java.util.Scanner;

public class Partie implements Constante {

    // -1 = non defini
    private int petiteBlindeActuelle;
    private int grosseBlindeActuelle;
    private int compteurTour;
    private int pot;
    private int minimumMise;
    private int indexHautPaquet; // index de la prochaine carte a distribuer/retourner/cramer
    private Carte[] paquet;
    private Joueur[] joueurs;
    private Carte[] riviere;


    public Partie(int nombreJoueur) {
        paquet = new Carte[52];
        joueurs = new Joueur[nombreJoueur];
        riviere = new Carte[5];
        minimumMise = -1;
        grosseBlindeActuelle = -1;
        petiteBlindeActuelle = -1;
        compteurTour = -1;
        pot = -1;
    }

    public void startPartie(){

        String pseudo;
        Scanner in = new Scanner(System.in).useDelimiter("\n");

        for (int i = 0 ; i < joueurs.length ; i++) {
            System.out.println("joueur n°"+ (i+1) + " entrez votre pseudo :");
        }

    /**
     * methode qui permet le deroulement d'une manche
     */
    private void manche(){

        //init
        pot = 0;
        minimumMise = grosseBlindeActuelle;
        compteurTour = 0;
        indexHautPaquet = 52;
        melangerCarte();
        distribuerCarte();


    }

    public Carte[] getPaquet() {        return paquet;    }

    public void creerPaquet(){
        String[] couleurs = {COEUR, CARREAU, TREFLE, PIQUE};
        int cpt = 0;

        for (int i = 1; i < 14; i++) {
            for (int j = 0; j < 4; j++){
                paquet[cpt] = new Carte(i,couleurs[j]);
                cpt++;
            }
        }
    }

    private Carte piocheHautDuPaquet(){
        Carte carte = paquet[indexHautPaquet];
        paquet[indexHautPaquet] = null;
        indexHautPaquet--;
        return carte;
    }

    private void distribuerCarte(){

        for (int t = 1 ; t <= 2 ; t++){
            for (Joueur j : joueurs) {
                j.recoiCarte(piocheHautDuPaquet());
            }
        }

    }

}
