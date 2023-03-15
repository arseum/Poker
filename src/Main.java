import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {

        int nombreJ;
        Scanner in = new Scanner(System.in).useDelimiter("\n");
        Partie partie;

        do {
            System.out.print("Entrez le nombre de joueur : ");
            nombreJ = in.nextInt();
        }while (nombreJ < 2 || nombreJ > 6);

        partie = new Partie(nombreJ);
        partie.startPartie();

    }
}
