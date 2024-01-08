import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class GeneradorBD {

    public static void insertarDatosEnLote() {
        ConexionBD conexionBD = ConexionBD.getConexionBD();
        Connection conexion = conexionBD.getConnection();

        try {
            // Inserción de servidores
            insertarServidoresEnLote(conexion, 10, 3);

            // Inserción de usuarios
            insertarUsuariosEnLote(conexion, 50);

            // Inserción de personajes
            insertarPersonajesEnLote(conexion, 100);

            // Inserción de mapas y zonas
            insertarMapasYZonasEnLote(conexion, 20, 5);

            System.out.println("Datos insertados en lote con éxito.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexionBD.cerrarConexion();
        }
    }

    private static void insertarServidoresEnLote(Connection conexion, int cantidadServidores, int cantidadRegiones) throws SQLException {
        String sql = "INSERT INTO Servidores (nombre, region) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
            String[] regiones = {"America", "Europa", "Asia"};

            Random random = new Random();

            for (int i = 0; i < cantidadServidores; i++) {
                preparedStatement.setString(1, "Servidor" + (i + 1));
                preparedStatement.setString(2, regiones[random.nextInt(cantidadRegiones)]);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        }
    }

    private static void insertarUsuariosEnLote(Connection conexion, int cantidadUsuarios) throws SQLException {
        String sql = "INSERT INTO Usuarios (nombre, codigo_uniq) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
            for (int i = 0; i < cantidadUsuarios; i++) {
                preparedStatement.setString(1, "Usuario" + (i + 1));
                preparedStatement.setString(2, generarCodigoUnico());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        }
    }

    private static void insertarPersonajesEnLote(Connection conexion, int cantidadPersonajes) throws SQLException {
        String sql = "INSERT INTO Personajes (nombre, usuario_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(sql)) {
            for (int i = 0; i < cantidadPersonajes; i++) {
                preparedStatement.setString(1, "Personaje" + (i + 1));
                preparedStatement.setInt(2, i % 50 + 1); // Asigna usuarios de forma circular
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    private static void insertarMapasYZonasEnLote(Connection conexion, int cantidadMapas, int cantidadZonas) throws SQLException {
        String mapaSQL = "INSERT INTO Mapas (nombre, dificultad, servidor_id) VALUES (?, ?, ?)";
        String zonaSQL = "INSERT INTO Zonas (nombre, ancho, alto, mapa_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatementMapa = conexion.prepareStatement(mapaSQL);
             PreparedStatement preparedStatementZona = conexion.prepareStatement(zonaSQL)) {

            Random random = new Random();

            for (int i = 0; i < cantidadMapas; i++) {
                preparedStatementMapa.setString(1, "Mapa" + (i + 1));
                preparedStatementMapa.setInt(2, random.nextInt(10)); // Dificultad aleatoria
                preparedStatementMapa.setInt(3, random.nextInt(10) + 1); // Servidor aleatorio

                preparedStatementMapa.addBatch();

                for (int j = 0; j < cantidadZonas; j++) {
                    preparedStatementZona.setString(1, "Zona" + (i * cantidadZonas + j + 1));
                    preparedStatementZona.setInt(2, random.nextInt(100) + 1); // Ancho aleatorio
                    preparedStatementZona.setInt(3, random.nextInt(100) + 1); // Alto aleatorio
                    preparedStatementZona.setInt(4, i + 1); // Asigna mapa actual

                    preparedStatementZona.addBatch();
                }
            }

            preparedStatementMapa.executeBatch();
            preparedStatementZona.executeBatch();
        }
    }

    private static String generarCodigoUnico() {
        Random random = new Random();
        StringBuilder codigoUnico = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            codigoUnico.append(random.nextInt(10));
        }
        return codigoUnico.toString();
    }
}