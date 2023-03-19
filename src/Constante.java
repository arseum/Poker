import java.util.Scanner;

public interface Constante {

    final String PIQUE = "pique";
    final String CARREAU = "carreau";
    final String TREFLE = "trefle";
    final String COEUR = "coeur";

    final int PIQUE_V = 1;
    final int CARREAU_V = 2;
    final int TREFLE_V = 4;
    final int COEUR_V = 8;

    final int NON_DETERMINER = 0;
    final int NADA = 2;
    final int PAIRE = 3;
    final int BRELAN = 7;
    final int QUINTE = 8;
    final int FLUSH = 9;
    final int CARRE = 11;

    final int jetonPourPartieNormal = 2000;
    final int nbEspace = 43;
    final Scanner in = new Scanner(System.in).useDelimiter("\n");

}
