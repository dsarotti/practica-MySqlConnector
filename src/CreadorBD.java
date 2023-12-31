import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreadorBD {

    public static void crearTablas() {
        ConexionBD conexionBD = ConexionBD.getConexionBD();
        try (Connection conexion = conexionBD.getConnection();
             Statement statement = conexion.createStatement()) {

            // Tabla de servidores
            statement.execute("CREATE TABLE IF NOT EXISTS Servidores ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "nombre VARCHAR(255) NOT NULL, "
                    + "region VARCHAR(255) NOT NULL)");

            // Tabla de usuarios
            statement.execute("CREATE TABLE IF NOT EXISTS Usuarios ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "nombre VARCHAR(255) NOT NULL, "
                    + "codigo_uniq VARCHAR(4) NOT NULL)");

            // Tabla de personajes
            statement.execute("CREATE TABLE IF NOT EXISTS Personajes ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "nombre VARCHAR(255) NOT NULL, "
                    + "usuario_id INT, "
                    + "FOREIGN KEY (usuario_id) REFERENCES Usuarios(id))");

            // Tabla de servidores_personajes
            statement.execute("CREATE TABLE IF NOT EXISTS Servidores_Personajes ("
                    + "servidor_id INT, "
                    + "personaje_id INT, "
                    + "FOREIGN KEY (servidor_id) REFERENCES Servidores(id), "
                    + "FOREIGN KEY (personaje_id) REFERENCES Personajes(id), "
                    + "PRIMARY KEY (servidor_id, personaje_id))");

            // Tabla de mapas
            statement.execute("CREATE TABLE IF NOT EXISTS Mapas ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "nombre VARCHAR(255) NOT NULL, "
                    + "dificultad INT CHECK (dificultad >= 0 AND dificultad <= 9), "
                    + "servidor_id INT, "
                    + "FOREIGN KEY (servidor_id) REFERENCES Servidores(id))");

            // Tabla de zonas
            statement.execute("CREATE TABLE IF NOT EXISTS Zonas ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "nombre VARCHAR(255) NOT NULL, "
                    + "ancho INT NOT NULL, "
                    + "alto INT NOT NULL, "
                    + "mapa_id INT, "
                    + "FOREIGN KEY (mapa_id) REFERENCES Mapas(id))");

            System.out.println("Tablas creadas con éxito.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}