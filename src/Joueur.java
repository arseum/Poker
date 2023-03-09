
public class Joueur implements Constante{

    private String pseudo;
    private int totalJeton;
    private int blinde; //0 = pas de blinde, 1 = petite blinde, 2 = grosse blinde
    private boolean estCouche;
    private boolean estVivant;
    private Carte[] main;
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

    public boolean isEstVivant() {
        return estVivant;
    }

    public boolean isEstCouche() {
        return estCouche;
    }

    public void setBlinde(int blinde) {
        this.blinde = blinde;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setEstVivant(boolean estVivant) {
        this.estVivant = estVivant;
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

    public int getJetonSurTable() {
        return jetonSurTable;
    }

    public void setEstCouche(boolean estCouche) {
        this.estCouche = estCouche;
    }

    /**
     * actuellement utilisé pour recupérer les blindes
     * ne prend pas en compte le fait que le joueur peut etre amener au tapis par la blindes
     */
    public void poseBlinde(int petiteBlinde){
        int t = petiteBlinde * blinde;
        totalJeton -= t;
        jetonSurTable = t;
        System.out.println("\n"+pseudo+" pose " + t + " jetons pour la blinde.\n"); //debug
    }

    public void recoitCarte(Carte carte){
        if (main[0] == null || main[1] == null) {
            if (main[0] == null)
                main[0] = carte;
            else
                main[1] = carte;
        }else
            System.err.println("Erreur! " + pseudo + " a deja 2 cartes en main!");
    }

    public int donneJetonPoser(){
        int tampon = jetonSurTable;
        jetonSurTable = 0;
        return tampon;
    }

    public void afficheCarte(Carte[] cartes){

        if(cartes.length != 2)
            System.out.println("cartes du flop : ");
        else
            System.out.println("cartes de " + pseudo + " :");

        for (Carte c : cartes)
            System.out.println(c);

    }

    public void estMort() {this.estVivant = false;}

    /**
     * fonction qui gere l'IHM pour une parole d'un joueur
     * cette version ne prend pas en compte le fait que le joueur peut suivre en fesant un tapis
     * @return false si le joueur suit ou se chouche et true si il decide de relancer (on doit alors refaire un tour de table)
     * @param minimumMise le nombre de jeton minimum a misé
     */
    public boolean parle(int minimumMise,Carte[] flop){

        int choix;
        boolean relance;

        affichageDebutTour();

        //affichage du flop + de l'etat des autres joueur + du pot/mises sur la table
        //version beta:
        afficheCarte(main);
        if (flop.length != 0)
            afficheCarte(flop);

        System.out.println(pseudo + " que voulez-vous faire?");
        System.out.println("1. Se coucher");
        System.out.println("2. Suivre pour " + (minimumMise-jetonSurTable) + " jetons");
        System.out.println("3. Relancer\n");

        System.out.print("Votre choix : ");
        choix = in.nextInt();

        while (choix < 1 || choix > 3){
            System.out.print("Resseyer : ");
            choix = in.nextInt();
        }

        if (choix == 1){
            estCouche = true;
            //simulation que le joueur jette ses cartes dans la defausse
            main[0] = null;
            main[1] = null;
            relance = false;
        }else if (choix == 2){
            jetonSurTable += minimumMise-jetonSurTable;
            totalJeton -= minimumMise-jetonSurTable;
            relance = false;
        }else{
            System.out.println("\nEntrez le montant");
            System.out.println("votre choix doit etre compris entre " + (minimumMise*2) + " jetons et " + totalJeton + " jetons");
            System.out.print("votre choix : ");
            choix = in.nextInt();
            while (choix < (minimumMise*2) || choix > totalJeton){
                System.out.print("Resseyer : ");
                choix = in.nextInt();
            }
            jetonSurTable += choix - jetonSurTable;
            totalJeton -= choix - jetonSurTable;
            relance = true;
        }

        return relance;

    }

    /**
     * petit affichage stylé pour annoncé le debut d'un tour
     */
    private void affichageDebutTour(){
        int nbEspaceEnMoin = 7 + pseudo.length();

        ClearConsole(); // totalement useless
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); //pour ne pas voire ce que le joueur precedant a fait
        System.out.println(" -----------------------------------------------------------");
        for (int i = 0 ; i < 2 ; i++) {
            System.out.print("|");
            for (int j = 0; j < 15; j++)
                System.out.print("\t");
            System.out.println("|");
        }

        System.out.print("|");
        for(int i = 0; i < 5 ; i++) {
            System.out.print("\t");
            nbEspaceEnMoin++;
        }
        System.out.print("Tour de " + pseudo );
        for (int i = nbEspaceEnMoin; i <= nbEspace; i++)
            System.out.print(" ");
        System.out.println("|");

        for (int i = 0 ; i < 2 ; i++) {
            System.out.print("|");
            for (int j = 0; j < 15; j++)
                System.out.print("\t");
            System.out.println("|");
        }
        System.out.println(" ----------------------------------------------------------- \n\n\n");
        System.out.println("tapper entrer lorsque vous etes prêt.");
        in.next();
    }
}
