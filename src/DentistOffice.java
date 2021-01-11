import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


public class DentistOffice {
    public static void main(String[] args) throws Exception{
        Connection conn = null;
        try {
            String url = "jdbc:postgresql://localhost:5432/DentistOfficeDB";
            Properties props = new Properties();
            props.setProperty("user","postgres"); // it is good to change this!
            props.setProperty("password","dibimibi"); //change to your password
            conn = DriverManager.getConnection(url, props);

            Statement stmt = null;
            String query = "select * from patientT";
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String name = rs.getString("first_name");
                    System.out.println(name);
                }
            } catch (SQLException e ) {
                throw new Error("Problem", e);
            } finally {
                if (stmt != null) { stmt.close(); }
            }

        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
