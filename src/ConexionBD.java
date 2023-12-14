import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {
    private static String SERVER="localhost";
    private static String PUERTO="3306";
    private static String DATABASE="dantebd";
    private static String USER="dante";
    private static String PASSWORD="abc123.";
    private static ConexionBD conexionBD;
    private Connection conn;

    private ConexionBD(){
        Properties propiedades = new Properties();
        propiedades.put("user",USER);
        propiedades.put("password",PASSWORD);
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://"+SERVER+":"+PUERTO+"/"+DATABASE,propiedades);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ConexionBD getConexionBD (){
        if (conexionBD == null){
            conexionBD = new ConexionBD();
        }
        return conexionBD;
    }

    public Connection getConnection(){
        return conn;
    }

    public void cerrarConexion(){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // private PreparedStatement prepararConsulta(String consulta){
    //     PreparedStatement stm=null;
    //     try {
    //         stm = conn.prepareStatement(consulta);
    //     } catch (SQLException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    //     return stm;
    // }

}