import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Partie implements Constante {

    // -1 = non defini
    private int petiteBlindeActuelle;
    private int grosseBlindeActuelle; //la groose blinde est toujours egale a 2X la petite blinde
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
        indexHautPaquet = -1;
    }

    public void startPartie(){

        String pseudo;
        Scanner in = new Scanner(System.in).useDelimiter("\n");

        //init des joueurs
        for (int i = 0 ; i < joueurs.length ; i++) {
            System.out.println("joueur n°"+ (i+1) + " entrez votre pseudo :");
            pseudo = in.next();
            joueurs[i] = new Joueur(pseudo,jetonPourPartieNormal);
        }

        //init des blindes
        petiteBlindeActuelle = 5;
        grosseBlindeActuelle = 10;

        //init paquet
        creerPaquet();

        //boucle de jeu
        while (!aUnGagnant()){
            manche();
            //mettre a jour les blinde ici si besoin
        }

    }

    public Carte[] getPaquet() {return paquet;  }

    public Joueur[] getJoueurs() {
        return joueurs;
    }

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

    /**
     * fonction qui parcours la liste de joueur et verifie si il y a un gaganat dans la partie
     * @return true si il y a plus que un joueur en vie
     */
    private boolean aUnGagnant(){
        int nbdeTrue = 0;
        for (Joueur joueur : joueurs)
            if (joueur.isEstVivant())
                nbdeTrue++;

        return nbdeTrue == 1;
    }

    /**
     * methode qui permet le deroulement d'une manche
     */
    private void manche(){

        //init
        pot = 0;
        minimumMise = grosseBlindeActuelle;
        compteurTour = 0;
        indexHautPaquet = 51;
        ArrayList<Joueur> fileJoueur = new ArrayList<>(); //FIFO - on push a la fin et on recupére a l'index 0

        //mise en place de la fifo + recuperation des blindes
        int i = 0;boolean krakzi = false;
        while (fileJoueur.size() != joueurs.length){
            if (joueurs[i].getBlinde() == 2){
                krakzi = true;
            }

            if (krakzi) {
                pot += joueurs[i].poseJeton(petiteBlindeActuelle);
                fileJoueur.add(joueurs[i]);
            }

            i++;
            if (i == joueurs.length)
                i = 0;
        }
        //a partir d'ici il faut veillez a ce que le joueur a qui c'est le tour de parler est a la tête de la FIFO

        //debut
        melangerCarte();
        distribuerCarte();
        //boucle de parole


    }

    public void melangerCarte(){

        Carte[] newPaquet = new Carte[52];
        int placer = 0;
        int index;

        while (placer < 52){
            index = (int) (Math.random() * 52);
            if (newPaquet[index] == null){
                newPaquet[index] = paquet[placer];
                placer++;
            }
        }

        paquet = newPaquet;

    }

    private Carte piocheHautDuPaquet(){
        Carte carte = paquet[indexHautPaquet];
        paquet[indexHautPaquet] = null;
        indexHautPaquet--;
        return carte;
    }

    public void distribuerCarte(){

        for (int t = 1 ; t <= 2 ; t++){
            System.out.println(t);
            for (Joueur j : joueurs) {
                j.recoitCarte(piocheHautDuPaquet());
            }
        }

    }

}