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

    public void estMort() {this.estVivant = false;  }
}
