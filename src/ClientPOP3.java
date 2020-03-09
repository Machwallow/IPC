import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.security.MessageDigest;

public class ClientPOP3 {

    private BufferedWriter bw;
    private BufferedReader br;
    private Socket sClient;
    private String timestamp;



    public ClientPOP3(String nomServ, int port){
        try{

            sClient = new Socket(InetAddress.getByName(nomServ), port);
            System.out.println("Connexion réussie sur le serveur : " + nomServ);
            bw = new BufferedWriter(new OutputStreamWriter(sClient.getOutputStream(), StandardCharsets.UTF_8));
            br = new BufferedReader(new InputStreamReader(sClient.getInputStream()));
        }catch (Exception e){
            System.out.println("ERROR : connection failed");
        }
    }

    public String getTimeStamp(){
        return this.timestamp;
    }

    public void setTimestamp(String str){
        String[] bloc=str.split(" ");
        this.timestamp=bloc[4];
    }

    public boolean sendApop(String username, String password){
        try{
            byte[] bytesOfMessage = (this.timestamp+password).getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(bytesOfMessage);
            bw.write("APOP "+username+" "+digest);
            bw.flush();
        }catch (Exception e) {
            System.out.println("ERROR : send apop");
            return false;
        }
        return true;
    }

    public boolean sendStat(){
        try{

            bw.write("STAT");
            bw.flush();
        }catch (Exception e) {
            System.out.println("ERROR : send stat");
            return false;
        }
        return true;
    }

    public boolean readResponse(){
        try {
            String type = "";
            ArrayList<String> reponse = new ArrayList<>();
            String line="";

            while((line=br.readLine())!= null){
                System.out.println(line);
                reponse.add(line);
                //cas erreur
                if(line.contains("-ERR")) {
                    return false;
                }
                //premier retour connexion
                if(line.contains("+OK POP3 server ready")) {
                    setTimestamp(line);
                    return true;
                }
                //apop sucessful
                if(line.contains("+OK maildrop")) {
                    return true;
                }
                //apop sucessful
                if(line.contains("+OK dewey POP3 server signing off")) {
                    return true;
                }
                if(!br.ready()){
                    break;
                }
            }
            return true;
        } catch (Exception e){
            System.out.println("ERROR : read response");
            return false;
        }
    }

    public int readResponseStat(){
        try {
            String type = "";
            ArrayList<String> reponse = new ArrayList<>();
            String line="";

            while((line=br.readLine())!= null){
                System.out.println(line);
                reponse.add(line);
                //ok
                if(line.contains("+OK")) {
                    String[] bloc=line.split(" ");
                    return Integer.parseInt(bloc[1]);
                }

                if(!br.ready()){
                    break;
                }
            }
            return 0;
        } catch (Exception e){
            System.out.println("ERROR : read response stat");
            return 0;
        }
    }

    public void doRetr(int n){
        //send
        try{
            bw.write("RETR "+n);
            bw.flush();
        }catch (Exception e) {
            System.out.println("ERROR : send retr "+n);
        }
        //read
        try {
            String type = "";
            ArrayList<String> reponse = new ArrayList<>();
            String line="";

            while((line=br.readLine())!= null){
                System.out.println(line);
                reponse.add(line);
                //ok
                if(line.contains("+OK")) {

                }
                //erreur
                if(line.contains("-ERR")) {

                }

                if(!br.ready()){
                    break;
                }
            }
        } catch (Exception e){
            System.out.println("ERROR : read retr "+n);
        }
    }

    public void sendQuit(){
        try{
            bw.write("QUIT");
            bw.flush();
        }catch (Exception e) {
            System.out.println("ERROR : send quit");
        }
    }

    private String askForUsername(){
        Scanner sc=new Scanner(System.in);
        System.out.println("Nom d'utilisateur :");
        return sc.nextLine();
    }
    private String askForPassword(){
        Scanner sc=new Scanner(System.in);
        System.out.println("Mot de passe :");
        return sc.nextLine();
    }
    private boolean askForRetryApop(){
        Scanner sc=new Scanner(System.in);
        System.out.println("Utilisateur ou mot de passe incorrect, voulez-vous réessayer ? (yes/no)");
        String str= sc.nextLine();
        if(str=="yes" || str=="oui" || str=="y"){
            return true;
        }
        else{
            return false;
        }
    }



    public void getMails(){
        boolean apopFailed=false;
        //get timestamp
        readResponse();
        System.out.println("--CONSULTATION MAILS POP3--");
        do{
            //connexion APOP
            String username = askForUsername();
            String password = askForPassword();
            sendApop(username, password);
            //si APOP ok démarrer l'échange
            if (readResponse()) {
                sendStat();
                int nbMail = readResponseStat();
                for (int i = 1; nbMail > 0 && i <= nbMail; i++) {
                    doRetr(i);
                }
            }
            else{
                apopFailed=askForRetryApop();
            }
        }while(apopFailed);
        sendQuit();
        //read inutile mais bloquant
        readResponse();
        try {
            br.close();
            bw.close();
            sClient.close();
        } catch (IOException e) {
            System.out.println("ERROR : close socket client");
        }
    }

    public static void main(String[] args){
        ClientPOP3 c = new ClientPOP3("192.168.43.60", 8000);
        c.getMails();
    }
}
