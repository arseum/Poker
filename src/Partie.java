import java.util.ArrayList;
import java.util.Timer;


public class Partie implements Constante {

    // -1 = non defini
    private int petiteBlindeActuelle;
    private int grosseBlindeActuelle; //la groose blinde est toujours egale a 2X la petite blinde
    private int compteurTour;
    private int pot;
    private int minimumMise;
    private int indexHautPaquet; // index de la prochaine carte a distribuer/retourner/cramer
    private Carte[] paquet;
    private Joueur[] joueurs;
    private Carte[] riviere;


    public Partie(int nombreJoueur) {
        paquet = new Carte[52];
        joueurs = new Joueur[nombreJoueur];
        riviere = new Carte[5];
        minimumMise = -1;
        grosseBlindeActuelle = -1;
        petiteBlindeActuelle = -1;
        compteurTour = -1;
        pot = -1;
        indexHautPaquet = -1;
    }

    public void startPartie() throws CloneNotSupportedException {

        String pseudo;
        int mancheNumero=1;
        Timer timerGeneral;


        //init des joueurs
        for (int i = 0 ; i < joueurs.length ; i++) {
            System.out.println("joueur n°"+ (i+1) + " entrez votre pseudo :");
            pseudo = in.next();
            joueurs[i] = new Joueur(pseudo,jetonPourPartieNormal);
            System.out.println("joueur " + pseudo + " a bien été créé avec un total de " + jetonPourPartieNormal + " jetons! gl hf");
        }

        //init des blindes
        petiteBlindeActuelle = 5;
        grosseBlindeActuelle = 10;
        tireAuSortBlinde();

        timerGeneral= new Timer("timerGeneral");

        //timerGeneral.scheduleAtFixedRate(,600000 ,600000 );
        //jpensait que ct bien pr faire une tache tt les x temps mais peux pas executer de methodes avec
        // dcp jsp jme suis arrété al


        //boucle de jeu
        while (!aUnGagnant()){
            manche();
            remiseEnEtat();
            System.out.println(timerGeneral);
            mancheNumero++; // sa sert a quoi ?

            //mettre a jour es blinde ici si besoin
        }

        timerGeneral.cancel();
    }


    public Carte[] getPaquet() {return paquet;}

    public Joueur[] getJoueurs() {
        return joueurs;
    }

    private boolean auMoin2JoueurPasCoucher(){
        int nbJoueurPasCoucher = 0;
        for (Joueur j : joueurs)
            if (!j.isEstCouche())
                nbJoueurPasCoucher++;
        return nbJoueurPasCoucher >= 2;
    }

    public void creerPaquet(){
        String[] couleurs = {COEUR, CARREAU, TREFLE, PIQUE};
        int cpt = 0;

        for (int i = 2; i <= 14; i++) {
            for (int j = 0; j < 4; j++){
                paquet[cpt] = new Carte(i,couleurs[j]);
                cpt++;
            }
        }
    }

    /**
     * fonction qui parcours la liste de joueur et verifie si il y a un gagnant dans la partie
     * @return true si il n'y a que un joueur en vie
     */
    private boolean aUnGagnant(){
        int nbdeTrue = 0;
        for (Joueur joueur : joueurs)
            if (joueur.isEstVivant())
                nbdeTrue++;

        return nbdeTrue == 1;
    }

    /**
     * methode qui permet de tirer au sort la petite blinde pour le debut de la partie
     */
    private void tireAuSortBlinde(){
        int nbJoueur = joueurs.length;
        int indexRandom = (int) (Math.random() * nbJoueur);
        joueurs[indexRandom].setBlinde(1);
        if (indexRandom  == joueurs.length - 1)
            indexRandom = -1;
        joueurs[indexRandom+1].setBlinde(2);
    }

    /**
     * methode qui permet le deroulement d'une manche
     */ //Grosse methode du prog
    private void manche() throws CloneNotSupportedException {

        //init
        pot = 0;
        compteurTour = 0;
        indexHautPaquet = 51;
        boolean relance;
        int miseMinimalPourSuivre;
        int nbJoeurEncoreEnVie;
        int nbSuiviRequis; // une fois que ce nombre est a 0 on peut retourner les carte et passer au tour de table suivant
        ArrayList<Joueur> fileJoueur = new ArrayList<>(); //FIFO - on push a la fin et on recupére a l'index 0
        //si le joueur est present dans la fifo c'est qu'il n'est pas couché

        //mise en place de la fifo
        int i = 0;
        boolean krakzi = false;

        while (fileJoueur.size() != joueurs.length){
            if (krakzi && joueurs[i].isEstVivant()) {
                joueurs[i].poseBlinde(petiteBlindeActuelle);
                fileJoueur.add(joueurs[i]);
            }

            if (joueurs[i].getBlinde() == 2){
                krakzi = true;
            }

            i++;
            if (i == joueurs.length)
                i = 0;
        }
        //a partir d'ici il faut veiller a ce que le joueur a qui c'est le tour de parler est a la tête de la FIFO

        //debut
        creerPaquet();
        melangerCarte();
        distribuerCarte();
        miseMinimalPourSuivre = grosseBlindeActuelle;
        while (compteurTour < 4 && auMoin2JoueurPasCoucher()) {

            nbSuiviRequis = fileJoueur.size(); //personne n'a encore parlé
            while (nbSuiviRequis != 0) {

                //le joueur en tête de file parle
                relance = fileJoueur.get(0).parle(miseMinimalPourSuivre,riviere,grosseBlindeActuelle,fileJoueur);

                //maj de nbSuiviRequis en fonction de si le joueur a relancer ou pas
                if (relance) {
                    nbSuiviRequis = fileJoueur.size() - 1;
                    miseMinimalPourSuivre = fileJoueur.get(0).getJetonSurTable();
                }
                else
                    nbSuiviRequis--;

                //maj de la fifo en fonction de si le joueur c'est coucher ou non
                if (fileJoueur.get(0).isEstCouche())
                    fileJoueur.remove(0);
                else{
                    fileJoueur.add(fileJoueur.get(0));
                    fileJoueur.remove(0);
                }

                //rare situation ou tout le monde se couche dés le premier tour de parole
                //il faut alors pas que le dernier joueur(qui a free win du coup) puisse parler
                if (fileJoueur.size() == 1)
                    nbSuiviRequis = 0;
            }

            pot += rammasserJetonSurLaTable();

            //retourner 3 ou 1 carte
            piocheHautDuPaquet();
            if (compteurTour == 0) //3 cartes
                for (int index = 0 ; index < 3 ; index++)
                    riviere[index] = piocheHautDuPaquet();
            else if (compteurTour < 3) // 1 cartes
                riviere[compteurTour+2] = piocheHautDuPaquet();


            //remettre en ordre la fifo en fonction de la petite blinde
            nbJoeurEncoreEnVie = fileJoueur.size();
            fileJoueur.clear();
            krakzi = false;
            i = 0;
            while (fileJoueur.size() != nbJoeurEncoreEnVie){

                if (joueurs[i].getBlinde() == 1){
                    krakzi = true;
                }

                if (krakzi && !joueurs[i].isEstCouche() && joueurs[i].isEstVivant()) {
                    fileJoueur.add(joueurs[i]);
                }

                i++;
                if (i == joueurs.length)
                    i = 0;
            }

            miseMinimalPourSuivre = 0;
            compteurTour++;
        }

        if (compteurTour == 4 && auMoin2JoueurPasCoucher()){
            int nbJoueurFinal = fileJoueur.size();
            Carte[][] mainJoueur = new Carte[nbJoueurFinal][2];
            System.out.println("il faut determiner le vainqueur entre :");
            for (int indexJ = 0 ; indexJ < nbJoueurFinal ; indexJ++)
                mainJoueur[indexJ] = fileJoueur.get(indexJ).getMain();
            Croupier_v1.determineGagnat_v1(riviere,mainJoueur);
        }else{
            System.out.println("c'est le joueur " + fileJoueur.get(0).getPseudo() + " qui a gagné cette manche !");
        }

        System.out.println("\nPartie test!\nAucun gain, desolé la banque garde tout ^^ ");
        System.out.println("\nVeuillez patienter 10s le temps que l'ordi melange les cartes");

        int millis = 5000;

        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            // ...
        }

        System.out.println("Promis il ne triche pas.. ;)");

        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            // ...
        }

    }

    private int rammasserJetonSurLaTable(){
        int total = 0;
        for (Joueur j : joueurs)
            total += j.donneJetonPoser();
        return total;
    }

    public void melangerCarte(){

        Carte[] newPaquet = new Carte[52];
        int placer = 0;
        int index;

        while (placer < 52){
            index = (int) (Math.random() * 52);
            if (newPaquet[index] == null){
                newPaquet[index] = paquet[placer];
                placer++;
            }
        }

        paquet = newPaquet;

    }

    private Carte piocheHautDuPaquet(){
        Carte carte = paquet[indexHautPaquet];
        paquet[indexHautPaquet] = null;
        indexHautPaquet--;
        return carte;
    }

    public void distribuerCarte(){

        for (int t = 1 ; t <= 2 ; t++){
            for (Joueur j : joueurs) {
                j.recoitCarte(piocheHautDuPaquet());
            }
        }

    }

    /**
     * methode qui permet de mettre a jour les booleans encore en vie et est coucher des joueurs
     * elle gére tout l'entre 2 manche
     */
    private void remiseEnEtat(){
        for (Joueur j : joueurs) {
            if (j.getTotalJeton() == 0)
                j.setEstVivant(false);
            if (j.isEstVivant())
                j.setEstCouche(false);
            j.defausseSesCartes();
        }
        riviere = new Carte[5];
    }

    public void doublerBlindes(){
        petiteBlindeActuelle += petiteBlindeActuelle;
        grosseBlindeActuelle += grosseBlindeActuelle;
    }

    /*
    public void passerJoueurSuivant(ArrayList<Joueur> fileJoueur){
        Joueur joueurPasse = fileJoueur.get(0);

        fileJoueur.set(0,null);
        for (int i =1; i< fileJoueur.size(); i++){
            fileJoueur.set(i-1, fileJoueur.get(i));

        }
        fileJoueur.add(joueurPasse);
    }
    */

}