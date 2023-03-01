public class Carte implements Constante {
    private int valeur;
    private String couleur;

    public Carte(int valeur, String couleur){ this.couleur = couleur;  this.valeur = valeur; }

    @Override
    public String toString() {
        return  "╔══════════╗\n" +
                "║          ║\n" +
                "║    "+ valeur +"    ║\n " +
                "║   "+ couleur + "  ║\n "+
                "║          ║\n "+
                "╚══════════╝" ;
    }
    //╚ ╔  ═══║╗╝
}
