public class Carte implements Cloneable, Constante {
    private int valeur;
    private final String couleur;
    private int valeur_couleur;

    public Carte(int valeur, String couleur) {
        this.couleur = couleur;
        this.valeur = valeur;
        switch (couleur) {
            case TREFLE -> valeur_couleur = TREFLE_V;
            case CARREAU -> valeur_couleur = CARREAU_V;
            case COEUR -> valeur_couleur = COEUR_V;
            case PIQUE -> valeur_couleur = PIQUE_V;
        }
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public int getValeur_couleur() {
        return valeur_couleur;
    }

    public String getCouleur() {
        return couleur;
    }

    @Override
    public String toString() {

        String valeur;

        valeur = "   " + this.valeur + "   ";

        if (this.valeur < 10 && this.valeur > 1)
            valeur = "    " + this.valeur + "   ";
        else if (this.valeur == 10)
            valeur = "   " + this.valeur + "   ";
        else if (valeur.equals("   11   "))
            valeur = "valet de";
        else if (valeur.equals("   12   "))
            valeur = "reine de";
        else if (valeur.equals("   14   "))
            valeur = "  as de ";
        else if (valeur.equals("   13   "))
            valeur = " roi de ";

        String couleur;

        couleur = "   " + this.couleur + "  ";

        if (this.couleur.equals(TREFLE)) {
            couleur = "  " + this.couleur + "  ";
        } else if (this.couleur.equals(CARREAU)) {
            couleur = "  " + this.couleur + " ";
        }

        return "╔══════════╗\n" +
                "║          ║\n" +
                "║ " + valeur + " ║\n" +
                "║          ║\n" +
                "║" + couleur + "║\n" +
                "║          ║\n" +
                "╚══════════╝";
    }

    public String affichangeLeger() {
        return valeur + " de " + couleur;
    }

    @Override
    public Carte clone() throws CloneNotSupportedException {
        return (Carte) super.clone();
    }

}
//╚ ╔  ═══║╗╝
