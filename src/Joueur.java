public class Joueur {

    private String pseudo;
    private int totalJeton;
    private int blinde; //0 = pas de blinde, 1 = petite blinde, 2 = grosse blinde
    private boolean estCouche;
    private boolean estVivant;
    private Carte[] main;

    public Joueur(String pseudo, int totalJeton) {
        this.pseudo = pseudo;
        this.totalJeton = totalJeton;
        blinde = 0;
        estCouche = false;
        main = new Carte[2];
        estVivant = true;
    }

    public boolean isEstVivant() {
        return estVivant;
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
     * peut etre renomé par 'deposeBlinde' si n'est pas utilisé par l'avenir
     */
    public int poseJeton(int petiteBlinde){
        int t = petiteBlinde * blinde;
        totalJeton -= t;
        System.out.println("\n"+pseudo+" pose " + t + " jetons pour la blinde.\n"); //debug
        return t;
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

    public String afficheSesCarte(){
        return "";
    }

}
