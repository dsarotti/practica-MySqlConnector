public class App {
    public static void main(String[] args) throws Exception {
        
        CreadorBD.crearTablas();
        GeneradorBD.generarDatos();
        ConexionBD.getConexionBDInstance().cerrarConexion();
        
    }
}