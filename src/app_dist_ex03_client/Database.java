package app_dist_ex03_client;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Database {

    Connection con;
    Statement st;
    ResultSet result;
    String ip;

    public Database(String ip) throws ClassNotFoundException, SQLException {
        this.ip = ip;
       // Class.forName("com.mysql.cj.jdbc.Driver");
       	Class.forName("org.mariadb.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/appdis?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", "root", "");
    }

    public ResultSet load_table(PreparedStatement sql) {
        try {
            
            return  sql.executeQuery();
        } catch (SQLException ex) {
            return null;
        }
    }

   
    public void sql(String sql) {
        Statement st;
        try {
            st = con.createStatement();
            st.execute(sql);

        } catch (SQLException ex) {
            System.out.println("erreur sql");
        }
    }

    public int Insert(String sql) {
        int r = -1;
        Statement st;
        try {
            st = con.createStatement();
            st.execute(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                r = rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("erreur insert sql");
        }
        return r;
    }

    public Connection getCon() {
        return con;
    }
    
}
