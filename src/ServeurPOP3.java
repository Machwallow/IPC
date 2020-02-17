import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurPOP3 {

    public final int PORT = 8000;
    public ServerSocket ss;
    public BufferedReader in;
    public PrintStream out;
    public Socket comm;

    public Socket initServeurSocket(){

        try{
            ss = new ServerSocket(PORT);
            comm = ss.accept();

            in = new BufferedReader(new InputStreamReader(comm.getInputStream()));
            out = new PrintStream(comm.getOutputStream());

            System.out.println("Connexion établie avec " + comm.getInetAddress());

        } catch (Exception e){
            System.out.println(e.getStackTrace());
        }

        return comm;
    }



}
