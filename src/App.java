public class App {
    public static void main(String[] args) throws Exception {
        ConexionBD conexionBD= ConexionBD.getConexionBD();
        
        System.out.println(
            conexionBD.getConnection().isValid(200)?"Existe conexión!"
            :
            "La conexion no está creada :("
        );
        

        conexionBD.cerrarConexion();
    }
}