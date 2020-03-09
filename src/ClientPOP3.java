import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

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

    public void setTimestamp(String str){
        String[] bloc=str.split(" ");
        this.timestamp=bloc[4];
    }

    public boolean sendApop(String username, String password){

        try{

            bw.write("APOP "+username+" "+password);
            bw.flush();
        }catch (Exception e) {
            System.out.println("ERROR : send apop");
            return false;
        }
        return true;
    }

    public int readResponse(){
        try {
            String type = "";
            ArrayList<String> reponse = new ArrayList<>();
            String line="";

            while((line=br.readLine())!= null){
                System.out.println(line);
                reponse.add(line);
                if(line.contains("+OK POP3 server ready")) {
                    setTimestamp(line);
                }

                if(!br.ready()){
                    break;
                }
            }




            return 2;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return -2;
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


    public void getMails(){
        //get timestamp
        readResponse();
        System.out.println("--CONSULTATION MAILS POP3--");
        //connexion APOP
        String username=askForUsername();
        String password=askForPassword();
        sendApop(username,password);


        try {
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
