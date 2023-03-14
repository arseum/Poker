import java.util.ArrayList;
import java.util.Objects;

/**
 * class qui permet d'implementer les algorithme qui determine le vainqueur
 * ceci n'est pas un objet,toute les fonctions seront alors statiques
 */
public class Croupier_v1 implements Constante {

    public static ArrayList<ArrayList<Integer>> quinteMem = new ArrayList<>();

    /**
     * premiere version pensée par arsene
     * utilisation des paradigmes de diviser pour mieux regner, programmation dynamique, recursivité
     */
    public static void determineGagnat_v1(Carte[] flop, Carte[][] mainJoueur) throws CloneNotSupportedException {

        int nbJoueur = mainJoueur.length;
        int[] valueMain = new int[nbJoueur];
        Carte[][] mainFinal = new Carte[nbJoueur][5];
        Carte[][] maintemp = new Carte[nbJoueur][7];

        //innit
        for (int i = 0 ; i < nbJoueur ; i++){
            for (int j = 0 ; j < 5 ; j++)
                maintemp[i][j] = flop[j];
            for (int j = 0 ; j < 2 ; j++)
                maintemp[i][j+5] = mainJoueur[i][j];
        }

        debug(maintemp);

        for (Carte[] main : maintemp) {
            debug(main);
            System.out.println("test containtFlush = " + containtFlush(main));
            System.out.println("test containtQuinte = " + containtQuinteDynamique(main));
            System.out.println("test containtQuinte = " + containtQuinteDynamique(main));
        }

        System.out.println();
        System.out.println(quinteMem);

        //etape 1 : savoir la value de chaque main


    }

    /**
     * @param main main de 7 cartes
     * @return 0 si ne contient pas de flush sinon renvoie 9
     * amelioration : la fonction devrait mettre la flush dans les 5er case de main
     */
    public static int containtFlush(Carte[] main){

        ArrayList<String> blackList = new ArrayList<>();
        int nbCarteMemeCouleur;
        int carteRestante = 7;
        int index = 0;
        String couleurRechercher;

        //si la boucle est bien gerer on ne devrait pas a avoir a gerer les index out of range
        while (index < 7 && carteRestante >= 5){
            if (!blackList.contains(main[index].getCouleur())){
                nbCarteMemeCouleur = 0;
                couleurRechercher = main[index].getCouleur();
                for (Carte carte : main){
                    if (Objects.equals(carte.getCouleur(), couleurRechercher))
                        nbCarteMemeCouleur++;
                }
                if (nbCarteMemeCouleur == 5) {
                    //amelioration ici
                    return 9;
                }else{
                    carteRestante -= nbCarteMemeCouleur;
                    blackList.add(main[index].getCouleur());
                }
            }
            index++;
        }

        return 0;

    }

    public static int containtQuinteDynamique(Carte[] main) throws CloneNotSupportedException {

        boolean estUneQuinte = false;
        int index = 0;

        while (!estUneQuinte && index < quinteMem.size()){
            if (containtAll(main,quinteMem.get(0))) {
                System.out.println("youpi!");
                return 8;
            }
            else
                index++;
        }

        return containtQuinte(main);

    }

    /**
     * @param main main de 7 cartes / 5 cartes (on comprendra alors que le joueur a une flush)
     * @return 0 si ne contient pas de quinte sinon renvoie 8
     * amelioration : -la fonction devrait faire en sorte de sauvegarder l'information de la valeur de la meilleur carte
     *                 - les list devrait stocket les valeur des carte et non les cartes !
     */
    public static int containtQuinte(Carte[] main) throws CloneNotSupportedException {

        ArrayList<Integer> fifoMainTrier = new ArrayList<>();
        ArrayList<Integer> suite = new ArrayList<>();
        int nbCarte = main.length;
        int indexMinCarte;
        int nbCarteSuccessive;
        int nbCarteRestante;
        int index;
        int indexSuivant;
        Carte[] copieMain; //il faut faire attention a ne pas modifier la main du joueur

        copieMain = cloneTabDeCarte(main);

        //on prepare la fifo trié
        for (int i = 0 ; i < nbCarte ; i++){
            indexMinCarte = indexMinCarte(copieMain);
            copieMain[indexMinCarte].setValeur(100);
            fifoMainTrier.add(main[indexMinCarte].getValeur());
            if (main[indexMinCarte].getValeur() == 14){
                copieMain[indexMinCarte].setValeur(0);
                fifoMainTrier.add(0,copieMain[indexMinCarte].getValeur());
                copieMain[indexMinCarte].setValeur(100);
                //un as est donc stocker 2 fois
                //une fois en tant que 14
                //et une fois en tant que 1
            }
        }

        //la fifo est prete on peut derouler l'algorithme
        //si la boucle est bien gerer on ne devrait pas a avoir a gerer les index out of range
        index = 0;
        nbCarteRestante = fifoMainTrier.size();
        while (index < fifoMainTrier.size() && nbCarteRestante >= 5){
            indexSuivant = index+1;
            nbCarteSuccessive = 1;
            while (fifoMainTrier.get(indexSuivant) == fifoMainTrier.get(indexSuivant-1) + 1){
                indexSuivant++;
                nbCarteSuccessive++;
            }
            if (nbCarteSuccessive >= 5){
                for (int i = 5 ; i >= 1; i--)
                    suite.add(fifoMainTrier.get(indexSuivant-i));
                quinteMem.add(suite);
                return 8;
            }else {
                index = indexSuivant;
                nbCarteRestante -= nbCarteSuccessive;
            }
        }

        return 0;
    }


    public static int indexMinCarte(Carte[] tabDeCarte){

        int valeurMin = tabDeCarte[0].getValeur();
        int indexMin = 0;

        for (int index = 1 ; index < tabDeCarte.length ; index++){
            if (tabDeCarte[index].getValeur() < valeurMin){
                valeurMin = tabDeCarte[index].getValeur();
                indexMin = index;
            }
        }

        return indexMin;
    }

    public static Carte[] cloneTabDeCarte(Carte[] tabDeCarte) throws CloneNotSupportedException {

        Carte[] new_tab = new Carte[tabDeCarte.length];

        for (int i = 0 ; i < tabDeCarte.length ; i++){
            new_tab[i] = tabDeCarte[i].clone();
        }

        return new_tab;

    }


    /**
     * @param main de 7 cartes
     * @param suite de 5 int
     * @return vrai si la main contient la suite
     */
    public static boolean containtAll(Carte[] main, ArrayList<Integer> suite){
        boolean ok;
        for (Integer integer : suite){
            ok = false;
            for (Carte carte : main)
                if (carte.getValeur() == integer)
                    ok = true;
            if (!ok)
                return false;
        }
        return true;

    }

    public static void debug(Carte[][] mainJoueur){
        for (int i = 0 ; i < mainJoueur.length ; i++){
            System.out.print("main j°" + (i+1) + " : ");
            debug(mainJoueur[i]);
        }
        System.out.println();

    }

    public static void debug(Carte[] mainJoueur){
        System.out.print("[");
        for (int j = 0 ; j < mainJoueur.length ; j++)
            System.out.print(mainJoueur[j].affichangeLeger() + (( j == mainJoueur.length - 1) ? "" : " , "));
        System.out.println("]");
    }

}
