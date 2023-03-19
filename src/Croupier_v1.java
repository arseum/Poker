import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * class qui permet d'implementer les algorithme qui determine le vainqueur
 * ceci n'est pas un objet,toute les fonctions seront alors statiques
 */
public class Croupier_v1 implements Constante {

    public static ArrayList<ArrayList<Integer>> quinteMem = new ArrayList<>();
    public static HashMap<Carte[], Integer> Memcombi = new HashMap<>();
    public static long tempsDebut;
    public static long tempsFin;//System.out.println("temps = " + ((tempsFin - tempsDebut) / 1000)  );

    public static void determineGagnat_v1(Carte[] flop, Carte[][] mainJoueur) throws CloneNotSupportedException {

        int nbJoueur = mainJoueur.length;
        int scoreMax = NON_DETERMINER;
        int score;
        int index;
        int max;
        Carte[][] maintemp = new Carte[nbJoueur][7];
        ArrayList<Carte[]> mainGagnante = new ArrayList<>();
        ArrayList<Carte[]> mainPerdante = new ArrayList<>();

        //innit
        for (int i = 0; i < nbJoueur; i++) {
            System.arraycopy(flop, 0, maintemp[i], 0, 5);
            System.arraycopy(mainJoueur[i], 0, maintemp[i], 5, 2);
        }

        debug(maintemp);

        for (int i = 0; i < nbJoueur; i++) {
            score = determineValueMain(maintemp[i]);
            if (score > scoreMax) {
                scoreMax = score;
                mainGagnante.clear();
                mainGagnante.add(maintemp[i]);
            } else if (score == scoreMax)
                mainGagnante.add(maintemp[i]);
        }

        if (mainGagnante.size() != 1) {
            if (scoreMax == QUINTE || scoreMax == FLUSH || scoreMax == (QUINTE + FLUSH)) {
                max = max(mainGagnante, 0);
                for (Carte[] m : mainGagnante)
                    if (m[0].getValeur() != max)
                        mainPerdante.add(m);
                for (Carte[] m : mainPerdante)
                    mainGagnante.remove(m);
            } else {
                index = 0;
                while (index != 5) {
                    max = max(mainGagnante, index);
                    for (Carte[] m : mainGagnante)
                        if (m[index].getValeur() != max)
                            mainPerdante.add(m);
                    for (Carte[] m : mainPerdante)
                        mainGagnante.remove(m);
                    index++;
                }
            }
        }

        debug(maintemp);

        System.out.println("les main gagnante sont : ");
        debug(mainGagnante);


    }

    /**
     * utilise le principe de programmation dynamique
     * il se trouve que c'est efficace mais pas forcement necessaire (le temp de calcul etant ridicule on peut s'en passer)
     */
    public static int determineValueMain(Carte[] main) throws CloneNotSupportedException {

        if (Memcombi.containsKey(main)) {
            System.out.println("youppi!!!!");
            return Memcombi.get(main);
        }

        int value;

        value = containtFlush(main);
        value += containtQuinte(main);

        if (value != NON_DETERMINER)
            return value;

        value = containMultiples(main);

        if (value != NON_DETERMINER)
            return value;

        //pas de combinaison il faut donc garder les 5 meilleur carte et les trier

        return NADA;
    }

    /**
     * si la main contient une flush, la fonction met la carte la plus haute a l'index 0 et met la couleur dans le debut de la main
     */
    public static int containtFlush(Carte[] main) {

        ArrayList<String> blackList = new ArrayList<>();
        int nbCarteMemeCouleur;
        int carteRestante = 7;
        int index = 0;
        int indexCarteMax = 0;
        String couleurRechercher;

        //si la boucle est bien gerer on ne devrait pas a avoir a gerer les index out of range
        while (index < 7 && carteRestante >= 5) {
            if (!blackList.contains(main[index].getCouleur())) {
                nbCarteMemeCouleur = 0;
                couleurRechercher = main[index].getCouleur();
                for (Carte carte : main) {
                    if (Objects.equals(carte.getCouleur(), couleurRechercher))
                        nbCarteMemeCouleur++;
                }
                if (nbCarteMemeCouleur >= 5) {

                    index = 0;
                    for (int i = 0; i < main.length; i++) {
                        if (Objects.equals(main[i].getCouleur(), couleurRechercher)) {
                            if (i != index)
                                swapCarteIndex(main, index, i);
                            index++;
                        }
                    }
                    index = 1;
                    while (Objects.equals(main[index].getCouleur(), couleurRechercher)) {
                        if (main[indexCarteMax].getValeur() < main[index].getValeur())
                            indexCarteMax = index;
                        index++;
                    }
                    swapCarteIndex(main, indexCarteMax, 0);
                    Memcombi.put(main, FLUSH);
                    return FLUSH;
                } else {
                    carteRestante -= nbCarteMemeCouleur;
                    blackList.add(main[index].getCouleur());
                }
            }
            index++;
        }

        return NON_DETERMINER;

    }

    public static int containtFlush_v2(Carte[] main) {

        int[] index = new int[5];
        int j;
        int non1, non2;

        for (non2 = main.length - 1; non2 > 0; non2--) {
            for (non1 = 0; non1 < non2; non1++) {
                j = 0;
                for (int i = 0; i < main.length; i++) {
                    if (i != non2 && i != non1) {
                        index[j] = i;
                        j++;
                    }
                }
                if (main[index[0]].getValeur_couleur() == (main[index[1]].getValeur_couleur() | main[index[2]].getValeur_couleur() | main[index[3]].getValeur_couleur() | main[index[4]].getValeur_couleur())) {
                    return FLUSH;
                }
            }
        }
        return NON_DETERMINER;
    }

    /**
     * @param main main de 7 cartes
     *             la fonction met la carte la plus haute a l'index 0
     *             attention: elle ne met pas la suite dans les 5er case de main car c'est inutile
     */
    public static int containtQuinte(Carte[] main) throws CloneNotSupportedException {

        ArrayList<Integer> fifoMainTrier = new ArrayList<>();
        int nbCarte = main.length;
        int indexMinCarte;
        int nbCarteSuccessive;
        int nbCarteRestante;
        int index;
        int indexSuivant;
        int valMax;
        Carte[] copieMain; //il faut faire attention a ne pas modifier la main du joueur

        copieMain = cloneTabDeCarte(main);

        //on prepare la fifo trié
        for (int i = 0; i < nbCarte; i++) {
            indexMinCarte = indexMinCarte(copieMain);
            copieMain[indexMinCarte].setValeur(100);
            if (!fifoMainTrier.contains(main[indexMinCarte].getValeur()))
                fifoMainTrier.add(main[indexMinCarte].getValeur());
            if (main[indexMinCarte].getValeur() == 14) {
                copieMain[indexMinCarte].setValeur(0);
                fifoMainTrier.add(0, copieMain[indexMinCarte].getValeur());
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
        while (index < fifoMainTrier.size() && nbCarteRestante >= 5) {
            indexSuivant = index + 1;
            nbCarteSuccessive = 1;
            while (fifoMainTrier.get(indexSuivant) == fifoMainTrier.get(indexSuivant - 1) + 1) {
                indexSuivant++;
                nbCarteSuccessive++;
            }
            if (nbCarteSuccessive >= 5) {

                if (Memcombi.containsKey(main))
                    Memcombi.put(main, QUINTE + FLUSH);
                else
                    Memcombi.put(main, QUINTE);

                valMax = fifoMainTrier.get(indexSuivant - 1);
                index = 0;
                while (main[index].getValeur() != valMax)
                    index++;
                swapCarteIndex(main, 0, index);
                return QUINTE;
            } else {
                index = indexSuivant;
                nbCarteRestante -= nbCarteSuccessive;
            }
        }

        return NON_DETERMINER;
    }

    /**
     * prototype voir si je peux gerer toutes les questions de pluricité de valeurs dans la meme methode/ Résolu normalement tout va bien
     *
     * @param main main de 7cartes
     */
    public static int containMultiples(Carte[] main) {

        int variable;
        int nbDeRep;
        ArrayList<Integer> vals = new ArrayList<>();
        ArrayList<Integer> paires = new ArrayList<>();
        ArrayList<Integer> brelans = new ArrayList<>();

        for (Carte carte : main) {  // met toutes les valeurs distinctes de la main dans val
            if (!vals.contains(carte.getValeur()))
                vals.add(carte.getValeur());
        }

        for (int val : vals) { // compte pour chaque valeur distincte le nombre de rep

            nbDeRep = 0;
            for (Carte carte : main) {
                if (carte.getValeur() == val) {
                    nbDeRep++;
                }
            }
            switch (nbDeRep) { // entre les valeurs qui se repetent dans la liste correspondante, s'il y a un carré on le dit insyant car peut y avoir rien de plus interressant (a voir si ya 2 carrés)
                case 2 -> {
                    if (paires.size() != 0 && val > paires.get(0))
                        paires.add(0, val);
                    else
                        paires.add(val);
                }
                case 3 -> {
                    if (brelans.size() != 0 && val > brelans.get(0))
                        brelans.add(0, val);
                    else
                        brelans.add(val);
                }

                case 4 -> {
                    remonteCarteIndex(main, 0, val);
                    variable = max(main, 4);
                    remonteCarteIndex(main, 4, variable);
                    Memcombi.put(main, CARRE);
                    return CARRE;
                }
            }
        }
        if (brelans.size() == 1 && paires.size() > 0) {  // en fonction des rep quil y a dans la main, renvoie le bon, "score"
            Memcombi.put(main, (PAIRE + BRELAN));
            variable = remonteCarteIndex(main, 0, brelans.get(0));
            remonteCarteIndex(main, variable, paires.get(0));
            Memcombi.put(main, (PAIRE + BRELAN));
            return (PAIRE + BRELAN);
        } else if (brelans.size() > 0) {
            Memcombi.put(main, BRELAN);
            remonteCarteIndex(main, 0, brelans.get(0));
            variable = max(main, 3);
            remonteCarteIndex(main, 3, variable);
            variable = max(main, 4);
            remonteCarteIndex(main, 4, variable);
            Memcombi.put(main, BRELAN);
            return BRELAN;
        } else if (paires.size() > 1) {
            Memcombi.put(main, (PAIRE + PAIRE));
            remonteCarteIndex(main, 0, paires.get(0));
            remonteCarteIndex(main, 2, paires.get(1));
            variable = max(main, 4);
            remonteCarteIndex(main, 4, variable);
            Memcombi.put(main, (PAIRE + PAIRE));
            return (PAIRE + PAIRE);
        } else if (paires.size() == 1) {
            Memcombi.put(main, PAIRE);
            remonteCarteIndex(main, 0, paires.get(0));
            variable = max(main, 2);
            remonteCarteIndex(main, 2, variable);
            variable = max(main, 3);
            remonteCarteIndex(main, 3, variable);
            variable = max(main, 4);
            remonteCarteIndex(main, 4, variable);
            Memcombi.put(main, PAIRE);
            return PAIRE;
        } else return NON_DETERMINER; //0 pour non determiner, c plus simple pour l'algo
    }


    public static int indexMinCarte(Carte[] tabDeCarte) {

        int valeurMin = tabDeCarte[0].getValeur();
        int indexMin = 0;

        for (int index = 1; index < tabDeCarte.length; index++) {
            if (tabDeCarte[index].getValeur() < valeurMin) {
                valeurMin = tabDeCarte[index].getValeur();
                indexMin = index;
            }
        }

        return indexMin;
    }

    public static Carte[] cloneTabDeCarte(Carte[] tabDeCarte) throws CloneNotSupportedException {

        Carte[] new_tab = new Carte[tabDeCarte.length];

        for (int i = 0; i < tabDeCarte.length; i++) {
            new_tab[i] = tabDeCarte[i].clone();
        }

        return new_tab;

    }


    /**
     * @param main  de 7 cartes
     * @param suite de 5 int
     * @return vrai si la main contient la suite
     */
    public static boolean containtAll(Carte[] main, ArrayList<Integer> suite) {
        boolean ok;
        for (Integer integer : suite) {
            ok = false;
            for (Carte carte : main)
                if (carte.getValeur() == integer) {
                    ok = true;
                    break;
                }
            if (!ok)
                return false;
        }
        return true;

    }

    public static int remonteCarteIndex(Carte[] main, int aPartirDe, int valeurCarte) {

        for (int i = 0; i < main.length; i++) {
            if (main[i].getValeur() == valeurCarte) {
                swapCarteIndex(main, aPartirDe, i);
                aPartirDe++;
            }
        }
        return aPartirDe;

    }

    public static int max(Carte[] main, int indexMin) {
        int max = main[indexMin].getValeur();
        for (int i = indexMin + 1; i < main.length; i++)
            if (main[i].getValeur() > max)
                max = main[i].getValeur();
        return max;
    }

    public static int max(ArrayList<Carte[]> mains, int indexCarte) {
        int max = mains.get(0)[indexCarte].getValeur();
        for (int i = 1; i < mains.size(); i++)
            if (mains.get(i)[indexCarte].getValeur() > max)
                max = mains.get(i)[indexCarte].getValeur();
        return max;
    }

    public static void swapCarteIndex(Carte[] main, int index1, int index2) {
        Carte pivot;
        pivot = main[index1];
        main[index1] = main[index2];
        main[index2] = pivot;
    }


    public static void debug(Carte[][] mainJoueur) {
        System.out.println();
        for (int i = 0; i < mainJoueur.length; i++) {
            System.out.print("main j°" + (i + 1) + " : ");
            debug(mainJoueur[i]);
        }
        System.out.println();

    }

    public static void debug(ArrayList<Carte[]> mains) {
        System.out.println();
        for (int i = 0; i < mains.size(); i++) {
            System.out.print("main j°" + (i + 1) + " : ");
            debug(mains.get(i));
        }
        System.out.println();

    }

    public static void debug(Carte[] mainJoueur) {
        System.out.print("[");
        for (int j = 0; j < mainJoueur.length; j++)
            System.out.print(mainJoueur[j].affichangeLeger() + ((j == mainJoueur.length - 1) ? "" : " , "));
        System.out.println("]");
    }

}
