
public class Test implements Constante {

    public static void main(String[] args) throws CloneNotSupportedException {

        Carte[][] mainJoueur = new Carte[3][2];
        Carte[] flop = new Carte[5];

        Carte carte = new Carte(5,PIQUE);
        Carte carte1 = new Carte(5,TREFLE);

        Carte carte2 = new Carte(12,COEUR);
        Carte carte3 = new Carte(2,PIQUE);

        Carte carte4 = new Carte(4,CARREAU);
        Carte carte5 = new Carte(8,CARREAU);

        Carte flop0 = new Carte(12,TREFLE);
        Carte flop1 = new Carte(2,COEUR);
        Carte flop2 = new Carte(5,CARREAU);
        Carte flop3 = new Carte(6,CARREAU);
        Carte flop4 = new Carte(7,CARREAU);

        mainJoueur[0][0] = carte;
        mainJoueur[0][1] = carte1;

        mainJoueur[1][0] = carte2;
        mainJoueur[1][1] = carte3;

        mainJoueur[2][0] = carte4;
        mainJoueur[2][1] = carte5;

        flop[0] = flop0;
        flop[1] = flop1;
        flop[2] = flop2;
        flop[3] = flop3;
        flop[4] = flop4;


        Croupier_v1.determineGagnat_v1(flop,mainJoueur);

    }

}
