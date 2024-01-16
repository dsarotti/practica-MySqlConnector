import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class GeneradorBD {

    private static final String[] REGIONES = { "EUROPA", "AMERICA", "ASIA" };
    private static final String[] SERVIDORES = { "EUW", "EUE", "EUS", "AMW", "AME", "AMC", "ASW", "ASE", "ASS", "ASC" };

    private static final int NUM_SERVIDORES = SERVIDORES.length;
    private static final int NUM_REGIONES = REGIONES.length;
    private static final int NUM_USUARIOS = 50;
    private static final int NUM_PERSONAJES = 100;
    private static final int NUM_MAPAS = 20;
    private static final int MAX_ZONAS_POR_MAPA = 5;

    private final ConexionBD conexionBD;

    public GeneradorBD() {
        this.conexionBD = ConexionBD.getConexionBDInstance();
    }

    public void generarDatos() {
        insertarRegiones();
        insertarServidores();
        insertarUsuarios();
        insertarPersonajes();
        insertarMapasConZonas();
    }

    private void insertarRegiones() {
        try (Connection conexion = conexionBD.getConnection();
                PreparedStatement statement = conexion.prepareStatement("INSERT INTO Regiones (nombre) VALUES (?)")) {

            for (String region : REGIONES) {
                statement.setString(1, region);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertarServidores() {
        Random random = new Random();
        try (Connection conexion = conexionBD.getConnection();
                PreparedStatement statement = conexion
                        .prepareStatement("INSERT INTO Servidores (nombre, region_id) VALUES (?, ?)")) {

            for (int i = 0; i < NUM_SERVIDORES; i++) {
                statement.setString(1, SERVIDORES[i]);
                statement.setInt(2, ((i + 1) % NUM_REGIONES ) + 1); // Selecciona una región al azar
                statement.addBatch();
            }
            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertarUsuarios() {
        Random random = new Random();
        try (Connection conexion = conexionBD.getConnection();
                PreparedStatement statement = conexion
                        .prepareStatement("INSERT INTO Usuarios (nombre, codigo_uniq) VALUES (?, ?)")) {

            for (int i = 1; i <= NUM_USUARIOS; i++) {
                statement.setString(1, "Usuario" + i);
                statement.setString(2, generarCodigoUnico());
                statement.addBatch();
            }
            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertarPersonajes() {
        Random random = new Random();
        try (Connection conexion = conexionBD.getConnection();
                PreparedStatement statement = conexion.prepareStatement(
                        "INSERT INTO Personajes (nombre, usuario_id, servidor_id) VALUES (?, ?, ?)")) {

            for (int i = 1; i <= NUM_PERSONAJES; i++) {
                statement.setString(1, "Personaje" + i);
                statement.setInt(2, random.nextInt(NUM_USUARIOS) + 1); // Selecciona un usuario al azar
                statement.setInt(3, random.nextInt(NUM_SERVIDORES) + 1); // Selecciona un servidor al azar
                statement.addBatch();
            }
            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertarMapasConZonas() {
        Random random = new Random();
        try (Connection conexion = conexionBD.getConnection();
                PreparedStatement statementMapas = conexion
                        .prepareStatement("INSERT INTO Mapas (nombre, dificultad, servidor_id) VALUES (?, ?, ?)");
                PreparedStatement statementZonas = conexion
                        .prepareStatement("INSERT INTO Zonas (nombre, ancho, alto, mapa_id) VALUES (?, ?, ?, ?)")) {

            for (int i = 1; i <= NUM_MAPAS; i++) {
                statementMapas.setString(1, "Mapa" + i);
                statementMapas.setInt(2, random.nextInt(10)); // Dificultad entre 0 y 9
                statementMapas.setInt(3, random.nextInt(NUM_SERVIDORES) + 1); // Selecciona un servidor al azar
                statementMapas.addBatch();

                int numZonas = random.nextInt(MAX_ZONAS_POR_MAPA) + 1; // Entre 1 y MAX_ZONAS_POR_MAPA
                for (int j = 1; j <= numZonas; j++) {
                    statementZonas.setString(1, "Zona" + j + "Mapa" + i);
                    statementZonas.setInt(2, random.nextInt(100)); // Ancho
                    statementZonas.setInt(3, random.nextInt(100)); // Alto
                    statementZonas.setInt(4, i); // Mapa_id
                    statementZonas.addBatch();
                }
            }
            statementMapas.executeBatch();
            statementZonas.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String generarCodigoUnico() {
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            codigo.append(random.nextInt(10));
        }
        return codigo.toString();
    }

    public static void main(String[] args) {
        GeneradorBD generadorBD = new GeneradorBD();
        generadorBD.generarDatos();
    }
}
