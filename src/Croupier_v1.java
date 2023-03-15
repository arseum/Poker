import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * class qui permet d'implementer les algorithme qui determine le vainqueur
 * ceci n'est pas un objet,toute les fonctions seront alors statiques
 */
public class Croupier_v1 implements Constante {

    public static ArrayList<ArrayList<Integer>> quinteMem = new ArrayList<>();
    public static HashMap<Carte[],Integer> Memcombi = new HashMap<>();
    public static long tempsDebut;
    public static long tempsFin;//System.out.println("temps = " + ((tempsFin - tempsDebut) / 1000)  );

    public static void determineGagnat_v1(Carte[] flop, Carte[][] mainJoueur) throws CloneNotSupportedException {

        int nbJoueur = mainJoueur.length;
        int[] valueMain = new int[nbJoueur];
        int scoreMax = 0;
        int score;
        Carte[][] mainFinal = new Carte[nbJoueur][5];
        Carte[][] maintemp = new Carte[nbJoueur][7];
        ArrayList<Carte[]> mainGagnante = new ArrayList<>();

        //innit
        for (int i = 0 ; i < nbJoueur ; i++){
            for (int j = 0 ; j < 5 ; j++)
                maintemp[i][j] = flop[j];
            for (int j = 0 ; j < 2 ; j++)
                maintemp[i][j+5] = mainJoueur[i][j];
        }

        debug(maintemp);

        for (int i = 0 ; i < nbJoueur ; i++) {
            score = determineValueMain(maintemp[i]);
            valueMain[i] = score;
            if(score > scoreMax) {
                scoreMax = score;
                mainGagnante.clear();
                mainGagnante.add(maintemp[i]);
            }else if(score == scoreMax)
                mainGagnante.add(maintemp[i]);
        }

        System.out.println("les main gangate sont : " );
        debug(mainGagnante);

        //etape 1 : savoir la value de chaque main


    }

    /**
     * utilise le principe de programmation dynamique
     * il se trouve que c'est efficace mais pas forcement necessaire (le temp de calcul etant ridicule on peut s'en passer)
     */
    public static int determineValueMain(Carte[] main) throws CloneNotSupportedException {

        if (Memcombi.containsKey(main)) {
            System.out.println("youppi!");
            return Memcombi.get(main);
        }

        int value;

        value = containtFlush(main);
        value += containtQuinte(main);

        if (value != 0)
            return value;

        value = containMultiples(main);

        if (value != 0)
            return value;

        //pas de combinaison il faut donc garder les 5 meilleur carte et les trier

        return 2;
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
                    Memcombi.put(main,9);
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

    /**
     * version dynamique de la detection de quinte
     * il se trouve qu'elle est moins efficace que la classique...
     */
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
     *                 - les list devraientt stocker les valeur des carte et non les cartes !
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

                if (Memcombi.containsKey(main))
                    Memcombi.replace(main,Memcombi.get(main)+8);
                else
                    Memcombi.put(main,8);

                return 8;
            }else {
                index = indexSuivant;
                nbCarteRestante -= nbCarteSuccessive;
            }
        }

        return 0;
    }

    /**
     * prototype voir si je peux gerer toutes les questions de pluricité de valeurs dans la meme methode/ Résolu normalement tout va bien
     *
     * @param main main de 7cartes
     * @return 3 pour une paire/ 6 pour double paire / 7 pour un brelan / 11 pour le carré / voir 10 pour full
     */
    public static int containMultiples(Carte[] main){ // TODO gérer tous les cas ou ya 2 paires-un brelan/deux brelans etc, choisir le meilleur pr chaque cas

         int nbDeRep;
         boolean existeDansList;
         ArrayList<Integer> vals =new ArrayList<>() ;
         ArrayList<Integer> paires =new ArrayList<>() ;
         ArrayList<Integer> brelans =new ArrayList<>() ;

        for (Carte carte:main) {  // met toutes les valeurs distinctes de la main dans val
            existeDansList=false;
            for (int val:vals ) {
                if (carte.getValeur() == val){
                    existeDansList=true;
                    break;
                }
            }
            if (!existeDansList)
                vals.add(carte.getValeur());
        }

        for (int val:vals) { // compte pour chaque valeur distincte le nombre de rep

            nbDeRep =0;
            for (Carte carte:main) {
                if (carte.getValeur() == val){
                    nbDeRep++;
                }
            }
            switch (nbDeRep) { // entre les valeurs qui se repetent dans la liste correspondante, s'il y a un carré on le dit insyant car peut y avoir rien de plus interressant (a voir si ya 2 carrés)
                case 2 -> paires.add(val);
                case 3 -> brelans.add(val);
                case 4 -> {
                    Memcombi.put(main,11);
                    return 11; }
                default -> {}
            }
        }
        if (brelans.size()==1 && paires.size()>0){  // en fonction des rep quil y a dans la main, renvoie le bon, "score"
            Memcombi.put(main,10);
            return 10;
        }else if (brelans.size()>0){
            Memcombi.put(main,7);
            return 7;
        }else if (paires.size() >1 ){
            Memcombi.put(main,6);
            return 6;
        }else if (paires.size()==1) {
            Memcombi.put(main,3);
            return 3;
        }else return 0; //0 pour non determiner, c plus simple pour l'algo
        //plus simple avec un dictionnaire je pense
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

    public static void debug(ArrayList<Carte[]> mains){
        for (int i = 0 ; i < mains.size() ; i++){
            System.out.print("main j°" + (i+1) + " : ");
            debug(mains.get(i));
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
