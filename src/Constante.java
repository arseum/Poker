import java.util.Scanner;

public interface Constante {

    String PIQUE = "pique";
    String CARREAU = "carreau";
    String TREFLE = "trefle";
    String COEUR = "coeur";

    int PIQUE_V = 1;
    int CARREAU_V = 2;
    int TREFLE_V = 4;
    int COEUR_V = 8;

    int NON_DETERMINER = 0;
    int NADA = 2;
    int PAIRE = 3;
    int BRELAN = 7;
    int QUINTE = 8;
    int FLUSH = 9;
    int CARRE = 11;

    int PARTIE_CLASSIQUE = 1;
    int PARTI_SPECIALE = 2;

    int jetonPourPartieNormal = 2000;
    int nbEspace = 43;
    Scanner in = new Scanner(System.in).useDelimiter("\n");

}
