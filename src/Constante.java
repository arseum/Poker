import java.util.Scanner;

public interface Constante {

    final String PIQUE = "pique";
    final String CARREAU = "carreau";
    final String TREFLE = "trefle";
    final String COEUR = "coeur";

    final int jetonPourPartieNormal = 2000;
    final int nbEspace = 43;
    final Scanner in = new Scanner(System.in).useDelimiter("\n");

}
