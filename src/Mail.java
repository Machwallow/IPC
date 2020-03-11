import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Mail {

    private int idMail;
    private int refUserSrc;
    private int refUserDst;
    private String objet;
    private String corps;
    private String date;
    private int nbOctets;


    public Mail(){

    }

    public int getIdMail() {
        return idMail;
    }

    public void setIdMail(int idMail) {
        this.idMail = idMail;
    }

    public int getRefUserSrc() {
        return refUserSrc;
    }

    public void setRefUserSrc(int refUserSrc) {
        this.refUserSrc = refUserSrc;
    }

    public int getRefUserDst() {
        return refUserDst;
    }

    public void setRefUserDst(int refUserDst) {
        this.refUserDst = refUserDst;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getCorps() {
        return corps;
    }

    public void setCorps(String corps) {
        this.corps = corps;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Date date) {

        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.date = sdf.format(date);
    }

    public int getNbOctets() {
        return nbOctets;
    }

    public void setNbOctets() {
        try {
            nbOctets = this.getCorps().getBytes("UTF-8").length + this.getDate().toString().getBytes("UTF-8").length
                    + this.getObjet().getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
