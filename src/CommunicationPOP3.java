import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CommunicationPOP3 implements Runnable {
    private Socket sComm;
    private BufferedReader br;
    private BufferedWriter bw;
    private boolean connected = false;
    private String timestamp;

    public CommunicationPOP3(Socket s){
        this.sComm =s;
        System.out.println("new client");
        System.out.println(s.getInetAddress());

    }

    public void response(BufferedReader br){
        try{
            if(br.ready()) {
                readBuffer(br);
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }



/*
    public void sendResponseOK(String filename){
        try{
            String[] ct=contentType(filename);
            bw.write("HTTP/1.1 200 OK\r\n");
            bw.write("Content-Type: "+ct[0]+ct[1]+"\r\n");
            bw.write("\r\n");

            //lecture fichier texte
            if(ct[0].equals("image") || ct[0].equals("text")) {
                InputStream flux = new FileInputStream(Server.getFolder() + filename);
                InputStreamReader lecture = new InputStreamReader(flux);
                BufferedReader buff = new BufferedReader(lecture);
                String textline;
                while ((textline = buff.readLine()) != null) {
                    bw.write(textline + "\r\n");
                    System.out.println(textline);
                }
                buff.close();
            }

            /*else if(ct[0].equals("")){
                File img = new File(Server.getFolder()+filename);
                BufferedImage bufferedImage = ImageIO.read(img);
                WritableRaster raster = bufferedImage .getRaster();
                DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
                System.out.println(data.getData().length);
                for(int i=0;i<data.getData().length;i++){
                    bw.write(data.getData()[i]);
                    System.out.println(data.getData()[i]);
                }
            }

            bw.flush();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
*/
    public boolean readBuffer(BufferedReader br){
        ArrayList<ArrayList<String>> tab=new ArrayList<>();
        ArrayList<String> contenu = new ArrayList<>();
        try{
            System.out.println("----------------");
            String commande = br.readLine();

            String[] parametres = commande.split(" ");

            switch (parametres[0]) {

                case "APOP" : {
                    //TODO : Handle connection
                    System.out.println("apop recu");
                    break;
                }

                case "STAT" : {
                    //TODO : Handle STAT
                    break;
                }

                case "RETR" : {
                    //TODO : Handle lecture + co BD
                    break;
                }

                case "DELE" :{
                    //TODO : Handle Deletion
                }

                case "QUIT" : {
                    //TODO : Handle close
                    break;
                }

                default :
                    // hmm
            }

        }catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return true;
    }


    @Override
    public void run() {
        try {
            while(true) {
                br = new BufferedReader(new InputStreamReader(sComm.getInputStream(), StandardCharsets.ISO_8859_1));
                bw = new BufferedWriter(new OutputStreamWriter(sComm.getOutputStream()));
                response(br);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
