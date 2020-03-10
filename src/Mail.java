import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Mail {

    private int idMail;
    private int refUserSrc;
    private int refUserDst;
    private String objet;
    private String corps;
    private String date;

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

        String currentTime = sdf.format(date);
        this.date = currentTime;
    }
}
