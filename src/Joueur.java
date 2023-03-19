import java.util.ArrayList;
import java.util.Objects;

public class Joueur implements Constante {

    private final String pseudo;
    private int totalJeton;
    private int blinde; //0 = pas de blinde, 1 = petite blinde, 2 = grosse blinde
    private boolean estCouche;
    private boolean estVivant;
    private final Carte[] main;
    private int jetonSurTable; //les jetons qui sont devant le joueur mais qui ne sont pas encore dans le pot

    public Joueur(String pseudo, int totalJeton) {
        this.pseudo = pseudo;
        this.totalJeton = totalJeton;
        blinde = 0;
        estCouche = false;
        main = new Carte[2];
        estVivant = true;
        jetonSurTable = 0;
    }

    @Override
    public String toString() {
        return pseudo;
    }

    public boolean isEstVivant() {
        return estVivant;
    }

    public void setEstVivant(boolean estVivant) {
        this.estVivant = estVivant;
    }

    public boolean isEstCouche() {
        return estCouche;
    }

    public void setEstCouche(boolean estCouche) {
        this.estCouche = estCouche;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void defausseSesCartes() {
        main[0] = null;
        main[1] = null;
    }

    public int getTotalJeton() {
        return totalJeton;
    }

    public Carte[] getMain() {
        return main;
    }

    public int getBlinde() {
        return blinde;
    }

    public void setBlinde(int blinde) {
        this.blinde = blinde;
    }

    public int getJetonSurTable() {
        return jetonSurTable;
    }

    /**
     * actuellement utilisé pour recupérer les blindes
     * ne prend pas en compte le fait que le joueur peut etre amener au tapis par la blindes
     */
    public void poseBlinde(int petiteBlinde) {
        int t = petiteBlinde * blinde;
        totalJeton -= t;
        jetonSurTable = t;
        System.out.println("\n" + pseudo + " pose " + t + " jetons pour la blinde.\n"); //debug
    }

    public void recoitCarte(Carte carte) {
        if (main[0] == null || main[1] == null) {
            if (main[0] == null)
                main[0] = carte;
            else
                main[1] = carte;
        } else
            System.err.println("Erreur! " + pseudo + " a deja 2 cartes en main!");
    }

    /**
     * permet d'affciher un rappel pour que le joueur sache si il est la grosse blinde/petite/rien
     */
    public void afficheLetatBlinde() {
        if (blinde != 0)
            System.out.println("\nRappel : vous etes la " + ((blinde == 1) ? "petite" : "grosse") + " blinde");
        else
            System.out.println("\nRappel : vous n'avez aucune blinde pour cette manche");

    }

    public int donneJetonPoser() {
        int tampon = jetonSurTable;
        jetonSurTable = 0;
        return tampon;
    }

    public void afficheCarte(Carte[] cartes) {

        if (cartes.length != 2)
            System.out.println("\ncartes du flop : ");
        else
            System.out.println("\ncartes de " + pseudo + " :");

        for (Carte c : cartes)
            System.out.println(c);

    }

    public void estMort() {
        this.estVivant = false;
    }

    /**
     * fonction qui gere l'IHM pour une parole d'un joueur
     * cette version ne prend pas en compte le fait que le joueur peut suivre en faisant un tapis
     *
     * @param minimumMise le nombre de jetons minimum à miser
     * @return false si le joueur suit ou se chouche et true s'il décide de relancer (on doit alors refaire un tour de table)
     */
    public boolean parle(int minimumMise, Carte[] flop, int grosseBlinde, ArrayList<Joueur> joueurEnVie) {

        int choix;
        boolean relance, checkTrue = false;


        affichageDebutTour();

        //affichage du flop + de l'etat des autres joueur + du pot/mises sur la table
        //version beta:
        afficheCarte(main);
        if (flop[0] != null)
            afficheCarte(flop);
        else
            afficheLetatBlinde();

        System.out.println("\nvous avez " + jetonSurTable + " jetons deja mis sur la table");
        System.out.println("Total de jetons actuel : " + totalJeton + " jetons");
        System.out.println("file des tour de parole = " + joueurEnVie + "\n");
        System.out.println(pseudo + " que voulez-vous faire?");
        if (minimumMise != 0) {
            System.out.println("1. Se coucher");
            System.out.println("2. Suivre " + minimumMise + " jetons pour " + (minimumMise - jetonSurTable) + " jetons");
            System.out.println("3. Relancer\n");

            choix = demandeSaisieChoix(1, 3);
        } else {
            System.out.println("1. check");
            System.out.println("2. relancer (minimum " + grosseBlinde + " jetons)");

            choix = demandeSaisieChoix(1, 2);

            if (choix == 1) {
                checkTrue = true;
            }
            choix++; //pour correspondre au ifelse qui suit
            minimumMise = grosseBlinde / 2; //de meme

        }


        if (choix == 1) { //se couche
            estCouche = true;
            defausseSesCartes();
            relance = false;
        } else if (checkTrue) {
            System.out.println("vous Checkez");
            relance = false;

        } else if (choix == 2) { //suit le minimum mise
            totalJeton -= minimumMise - jetonSurTable;
            jetonSurTable += minimumMise - jetonSurTable;
            relance = false;
        } else { // relance
            System.out.println("\nEntrez le montant");
            System.out.println("votre choix doit etre compris entre " + (minimumMise * 2) + " jetons et " + totalJeton + " jetons");

            choix = demandeSaisieChoix(minimumMise * 2, totalJeton);
            totalJeton -= choix - jetonSurTable;
            jetonSurTable += choix - jetonSurTable;
            relance = true;
        }

        return relance;

    }

    /**
     * permet de demander la saisie d'un int en fonction d'un min et d'un max
     * pour eviter la redondance.
     */
    private int demandeSaisieChoix(int min, int max) {
        int choix;

        System.out.print("Votre choix : ");
        choix = in.nextInt();

        while (choix < min || choix > max) {
            System.out.print("Réessayez : ");
            choix = in.nextInt();
        }

        return choix;
    }

    /**
     * petit affichage stylé pour annoncé le debut d'un tour
     */
    private void affichageDebutTour() {
        int nbEspaceEnMoin = 7 + pseudo.length();
        String saisie;

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); //pour ne pas voire ce que le joueur precedant a fait
        System.out.println(" -----------------------------------------------------------");
        for (int i = 0; i < 2; i++) {
            System.out.print("|");
            for (int j = 0; j < 15; j++)
                System.out.print("\t");
            System.out.println("|");
        }

        System.out.print("|");
        for (int i = 0; i < 5; i++) {
            System.out.print("\t");
            nbEspaceEnMoin++;
        }
        System.out.print("Tour de " + pseudo);
        for (int i = nbEspaceEnMoin; i <= nbEspace; i++)
            System.out.print(" ");
        System.out.println("|");

        for (int i = 0; i < 2; i++) {
            System.out.print("|");
            for (int j = 0; j < 15; j++)
                System.out.print("\t");
            System.out.println("|");
        }
        System.out.println(" ----------------------------------------------------------- \n\n\n");

        do {
            System.out.println("tappez ok lorsque vous etes prêt.");
            saisie = in.next();
        } while (!Objects.equals(saisie, "ok"));


    }
}
