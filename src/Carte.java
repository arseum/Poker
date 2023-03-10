public class Carte implements Cloneable{
    private int valeur;
    private String couleur;

    public Carte(int valeur, String couleur){ this.couleur = couleur;  this.valeur = valeur; }

    public int getValeur() {
        return valeur;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    @Override
    public String toString() {

        String valeur;

        valeur = "   " + this.valeur + "   ";

        if (this.valeur < 10 && this.valeur>1)
            valeur = "    " + this.valeur + "   ";
        else if (this.valeur == 10)
            valeur = "   " + this.valeur + "   ";
        else if(valeur.equals("   11   "))
            valeur = "valet de";
        else if (valeur.equals("   12   "))
            valeur = "reine de";
        else if (valeur.equals("   1   "))
            valeur = "  as de ";
        else if (valeur.equals("   13   "))
            valeur = " roi de ";

        String couleur;

        couleur = "   " + this.couleur + "  ";

        if (this.couleur.equals("trefle")){
            couleur = "  " + this.couleur + "  ";}
        else if (this.couleur.equals("carreau")) {
            couleur = "  " + this.couleur + " ";}

            return  "╔══════════╗\n" +
                    "║          ║\n" +
                    "║ " + valeur + " ║\n" +
                    "║          ║\n"+
                    "║"+ couleur + "║\n"+
                    "║          ║\n"+
                    "╚══════════╝" ;
        }

    public String affichangeLeger(){
        return valeur + " de " + couleur;
    }

    @Override
    public Carte clone() throws CloneNotSupportedException {
        return  (Carte)super.clone();
    }

}
    //╚ ╔  ═══║╗╝
