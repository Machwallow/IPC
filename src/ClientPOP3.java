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
    private BufferedInputStream br;
    private Socket sClient;
    private String timestamp;



    public ClientPOP3(String nomServ, int port){
        try{

            sClient = new Socket(InetAddress.getByName(nomServ), port);
            System.out.println("Connexion réussie sur le serveur : " + nomServ);
            bw = new BufferedWriter(new OutputStreamWriter(sClient.getOutputStream(), StandardCharsets.UTF_8));
            br = new BufferedInputStream(sClient.getInputStream());
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
            String res =new String(digest,"UTF-8");
            System.out.println(res);
            System.out.println("C : APOP "+username+" "+res);
            bw.write("APOP "+username+" "+res);
            bw.flush();
        }catch (Exception e) {
            System.out.println("ERROR : send apop");
            return false;
        }
        return true;
    }

    public boolean sendStat(){
        try{
            System.out.println("C : STAT");
            bw.write("STAT");
            bw.flush();
        }catch (Exception e) {
            System.out.println("ERROR : send stat");
            return false;
        }
        return true;
    }

    public int readResponse(){
        //return 1 si ok
        //return -1 si pb connexion
        //return -2 si serveur envoi -ERR
        try {
            String response = "";
            int stream;
            byte[] b = new byte[4096];
            stream = br.read(b);
            response = new String(b, 0, stream);
            System.out.println("S : "+response);

                //cas erreur
                if(response.contains("-ERR")) {
                    return -2;
                }
                //premier retour connexion
                if(response.contains("+OK POP3 server ready")) {
                    setTimestamp(response);
                    return 1;
                }
                //apop sucessful
                if(response.contains("+OK maildrop")) {
                    return 1;
                }
                //connection sucessful
                if(response.contains("+OK dewey POP3 server signing off")) {
                    return 1;
                }
                return 1;
        } catch (Exception e){
            System.out.println("ERROR : read response");
            return -1;
        }
    }

    public int readResponseStat(){
        try {
            String response = "";
            int stream;
            byte[] b = new byte[4096];
            stream = br.read(b);
            response = new String(b, 0, stream);
            System.out.println("S : "+response);

                //ok
                if(response.contains("+OK")) {
                    String[] bloc=response.split(" ");
                    return Integer.parseInt(bloc[1]);
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
            System.out.println("C : RETR "+n);
            bw.write("RETR "+n);
            bw.flush();
        }catch (Exception e) {
            System.out.println("ERROR : send retr "+n);
        }
        //read
        try {
            String response = "";
            int stream;
            byte[] b = new byte[4096];
            stream = br.read(b);
            response = new String(b, 0, stream);
            System.out.println("S : "+response);

                //ok
                if(response.contains("+OK")) {

                }
                //erreur
                if(response.contains("-ERR")) {

                }


        } catch (Exception e){
            System.out.println("ERROR : read retr "+n);
        }
    }

    public void sendQuit(){
        try{
            System.out.println("C : QUIT");
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
        System.out.println("--CONNEXION MAILS POP3--");
        do{
            //connexion APOP
            String username = askForUsername();
            String password = askForPassword();
            sendApop(username, password);
            //si APOP ok démarrer l'échange
            int response=readResponse();
            if (response==1) {
                sendStat();
                int nbMail = readResponseStat();
                for (int i = 1; nbMail > 0 && i <= nbMail; i++) {
                    doRetr(i);
                }
            }
            else if(response==-2){
                apopFailed=askForRetryApop();
                System.out.println("apopFailed="+apopFailed);
            }
        }while(apopFailed);
        sendQuit();
        //read final mais on ferme quoi qu'il arrive
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
        //adresse IP avant : 192.168.43.60
        ClientPOP3 c = new ClientPOP3("192.168.1.82", 8000);
        c.getMails();
    }
}
