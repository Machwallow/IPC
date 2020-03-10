import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MailDAO {

    public static ArrayList<Mail> getMails(int refUserDst){

        ArrayList<Mail> listMails = new ArrayList<>();

        Connection co = ConnexionBD.getInstance();

        try {
            PreparedStatement rechercheMails = co.prepareStatement("SELECT * FROM mail WHERE refUserDst= ?");
            rechercheMails.setInt(1,refUserDst);

            ResultSet rs = rechercheMails.executeQuery();

            boolean notEmpty = rs.next();

            while(notEmpty){
                Mail mail = new Mail();

                mail.setIdMail(rs.getInt(1));
                mail.setRefUserSrc(rs.getInt(2));
                mail.setRefUserDst(rs.getInt(3));
                mail.setObjet(rs.getString(4));
                mail.setCorps(rs.getString(5));
                mail.setDate(rs.getDate(6));

                listMails.add(mail);
                notEmpty = rs.next();
            }

            rs.close();
        } catch (Exception e){
            System.out.println("erreur getmails");
        }

        return listMails;
    }

    public static int countMails(int refUserDst){

        int count = -1;
        Connection co = ConnexionBD.getInstance();

        try {
            PreparedStatement rechercheMails = co.prepareStatement("SELECT COUNT(*) FROM mail WHERE refUserDst= ?");
            rechercheMails.setInt(1,refUserDst);

            ResultSet rs = rechercheMails.executeQuery();

            boolean notEmpty = rs.next();

            while(notEmpty){

                count = rs.getInt(1);
                notEmpty = rs.next();
            }

            rs.close();
        } catch (Exception e){
            System.out.println("erreur count mails");
        }

        return count;
    }

    public static Mail getMail(int refUserDst, int numMail){

        Mail mail = new Mail();
        Connection co = ConnexionBD.getInstance();

        try {
            PreparedStatement rechercheMails = co.prepareStatement("SELECT * FROM mail WHERE refUserDst= ? ORDER BY date LIMIT 1 OFFSET ?");
            rechercheMails.setInt(1,refUserDst);
            rechercheMails.setInt(2, numMail-1);

            ResultSet rs = rechercheMails.executeQuery();

            boolean notEmpty = rs.next();

            while(notEmpty){

                mail.setIdMail(rs.getInt(1));
                mail.setRefUserSrc(rs.getInt(2));
                mail.setRefUserDst(rs.getInt(3));
                mail.setObjet(rs.getString(4));
                mail.setCorps(rs.getString(5));
                //TODO : modifier date dans bdd
                mail.setDate(rs.getDate(6));

                notEmpty = rs.next();
            }

            rs.close();
        } catch (Exception e){
            System.out.println("erreur getMail");
        }

        return mail;

    }

}
