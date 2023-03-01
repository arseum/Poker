public class Partie {

    // -1 = non defini
    private int petiteBlindeActuelle;
    private int grosseBlindeActuelle;
    private int compteurTour;
    private int pot;
    private int minimumMise;
    private Carte[] paquet;
    private Joueur[] joueurs;
    private Carte[] riviere;


    public Partie(int nombreJouer) {
        paquet = new Carte[52];
        joueurs = new Joueur[nombreJouer];
        riviere = new Carte[5];
        minimumMise = -1;
        grosseBlindeActuelle = -1;
        petiteBlindeActuelle = -1;
        compteurTour = -1;
        pot = -1;
    }

    public

}
