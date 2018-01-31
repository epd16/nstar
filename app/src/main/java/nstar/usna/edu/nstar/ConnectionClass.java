package nstar.usna.edu.nstar;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class ConnectionClass {
    String driver = "net.sourceforge.jtds.jdbc.Driver";
    String server = "midn.cs.usna.edu";
    String db = "capstone-satellite";
    String usrname = "satellite";
    String password = "ji2h90c4890ty";


    @SuppressLint("NewAPI")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String url = "jdbc:jtds:sqlserver://" + server + ";"
                + "databaseName=" + db + ";user=" + usrname
                + ";password=" + password + ";";

        try {
            Class.forName(driver);
            connection = (Connection) DriverManager.getConnection(url);
        }
        catch (SQLException e) {
            Log.e("ERROR", e.getMessage());
        }
        catch (ClassNotFoundException e){
            Log.e("ERROR", e.getMessage());
        }
        catch (Exception e){
            Log.e("ERROR", e.getMessage());
        }
        return connection;
    }

}