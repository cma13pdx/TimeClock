/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timeclock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author cmanson
 */
public class DBConnect {
        private static Connection con = null;

    public static Connection connect() throws SQLException, ClassNotFoundException {
        String database;
        String filename = "C:/Users/cmanson/Documents/timetracker.mdb;";
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + filename;
            con = DriverManager.getConnection(database, "", "");
            
            //con=DriverManager.getConnection(url,user,pass);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e);
        }
        
        return con;
    }
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (con != null && !con.isClosed())
            return con;
        connect();
        return con;
    }
}
