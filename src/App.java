public class App {
    public static void main(String[] args) throws Exception {
        ConexionBD conexionBD= ConexionBD.getConexionBD();
        if(conexionBD!=null){
            System.out.println("Existe conexión!");
        }else{
            System.out.println("La conexion no está creada :(");
        }
        
        System.out.println(conexionBD.getConnection().isValid(200)?"Existe conexión!":"La conexion no está creada :(");
        

        conexionBD.cerrarConexion();
    }
}