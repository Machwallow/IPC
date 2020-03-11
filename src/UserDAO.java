import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public static User getUser(String login, String password){

        User user = new User();

        Connection co = ConnexionBD.getInstance();

        try {

            PreparedStatement rechercheUser = co.prepareStatement("SELECT * FROM user WHERE login = ? AND password = ?");
            rechercheUser.setString(1, login);
            rechercheUser.setString(2, password);

            ResultSet rs = rechercheUser.executeQuery();

            boolean notEmpty = rs.next();

            while(notEmpty){
                user.setIdUser(rs.getInt(1));
                user.setLogin(rs.getString(2));
                user.setPassword(rs.getString(3));
                notEmpty = rs.next();
            }

            rs.close();
        } catch (Exception e){
            user.setIdUser(-1);
            System.out.println(e.getMessage());
        }

        return user;
    }

}
