import java.util.Scanner;

public interface Constante {

    final String PIQUE = "pique";
    final String CARREAU = "carreau";
    final String TREFLE = "trefle";
    final String COEUR = "coeur";

    final int jetonPourPartieNormal = 2000;
    final int nbEspace = 43;
    final Scanner in = new Scanner(System.in).useDelimiter("\n");

    public default void ClearConsole(){
        try{
            String operatingSystem = System.getProperty("os.name"); //Check the current operating system

            if(operatingSystem.contains("Windows")){
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();

                startProcess.waitFor();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

}
