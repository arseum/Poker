
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

    public Carte[] getMain() {
        return main;
    }

    public int getBlinde() {
        return blinde;
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

    public String afficheSesCarte(){
        return "";
    }

    public void estMort() {this.estVivant = false;}

    /**
     * fonction qui gere l'IHM pour une parole d'un joueur
     * cette version ne prend pas en compte le fait que le joueur peut suivre en fesant un tapis
     * @return false si le joueur suit ou se chouche et true si il decide de relancer (on doit alors refaire un tour de table)
     * @param minimumMise le nombre de jeton minimum a misé
     */
    public boolean parle(int minimumMise){

        int choix;

        affichageDebutTour();

        System.out.println(pseudo + " que voulez-vous faire?");
        System.out.println("1. Se coucher");
        System.out.println("2. Suivre pour " + minimumMise + " jetons"); //il faudrait rajouter le nombre de jetons que le joueur doit rajouter pour suivre
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
        }else if (choix == 2){

        }

        return true;

    }

    /**
     * petit affichage stylé pour annoncé le debut d'un tour
     */
    private void affichageDebutTour(){
        int nbEspaceEnMoin = 7 + pseudo.length();

        ClearConsole();
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
