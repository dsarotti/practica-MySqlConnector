import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public static void generarDatos() {
        Conexion conexionBD = Conexion.getConexionBDInstance();
        insertarRegiones(conexionBD);
        insertarServidores(conexionBD);
        insertarUsuarios(conexionBD);
        insertarPersonajes(conexionBD);
        insertarMapasConZonas(conexionBD);
    }

    private static void insertarRegiones(Conexion conexionBD) {
        Connection conexion = null;
        PreparedStatement statement = null;

        try {
            conexion = conexionBD.getConnection();
            statement = conexion.prepareStatement("INSERT INTO Regiones (nombre) VALUES (?)");

            for (String region : REGIONES) {
                statement.setString(1, region);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cierre de recursos
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void insertarServidores(Conexion conexionBD) {
        Random random = new Random();
        Connection conexion = null;
        PreparedStatement statement = null;

        try {
            conexion = conexionBD.getConnection();
            statement = conexion.prepareStatement("INSERT INTO Servidores (nombre, region_id) VALUES (?, ?)");

            for (int i = 0; i < NUM_SERVIDORES; i++) {
                statement.setString(1, SERVIDORES[i]);
                statement.setInt(2, ((i + 1) % NUM_REGIONES) + 1); // Selecciona una región al azar
                statement.addBatch();
            }
            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cierre de recursos
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void insertarUsuarios(Conexion conexionBD) {
        List<String> nombresReales = Arrays.asList(
                "ANTONIO", "MANUEL", "JOSE", "FRANCISCO", "DAVID", "JUAN", "JAVIER", "DANIEL", "JOSE ANTONIO",
                "FRANCISCO JAVIER", "JOSE LUIS", "CARLOS", "ALEJANDRO", "JESUS", "MIGUEL", "JOSE MANUEL",
                "MIGUEL ANGEL", "RAFAEL", "PABLO", "PEDRO", "ANGEL", "SERGIO", "FERNANDO", "JOSE MARIA",
                "JORGE", "LUIS", "ALBERTO", "ALVARO", "JUAN CARLOS", "ADRIAN", "DIEGO", "JUAN JOSE", "RAUL",
                "IVAN", "RUBEN", "JUAN ANTONIO", "OSCAR", "ENRIQUE", "RAMON", "ANDRES", "JUAN MANUEL",
                "SANTIAGO", "VICENTE", "MARIO", "VICTOR", "JOAQUIN", "EDUARDO", "ROBERTO", "MARCOS",
                "JAIME");

        Connection conexion = null;
        PreparedStatement statement = null;

        try {
            conexion = conexionBD.getConnection();
            statement = conexion.prepareStatement("INSERT INTO Usuarios (nombre, codigo_uniq) VALUES (?, ?)");

            for (String nombre : nombresReales) {
                statement.setString(1, nombre);
                String codigoUnico = generarCodigoUnico(conexionBD);
                statement.setString(2, codigoUnico);
                statement.addBatch();
            }

            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cierre de recursos
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void insertarPersonajes(Conexion conexionBD) {
        Random random = new Random();
        Connection conexion = null;
        PreparedStatement statement = null;

        try {
            conexion = conexionBD.getConnection();
            statement = conexion
                    .prepareStatement("INSERT INTO Personajes (nombre, usuario_id, servidor_id) VALUES (?, ?, ?)");

            for (int i = 1; i <= NUM_PERSONAJES; i++) {
                statement.setString(1, "Personaje" + i);
                statement.setInt(2, random.nextInt(NUM_USUARIOS) + 1); // Selecciona un usuario al azar
                statement.setInt(3, random.nextInt(NUM_SERVIDORES) + 1); // Selecciona un servidor al azar
                statement.addBatch();
            }
            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cierre de recursos
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void insertarMapasConZonas(Conexion conexionBD) {
        Random random = new Random();
        Connection conexion = null;
        PreparedStatement statementMapas = null;
        PreparedStatement statementZonas = null;

        try {
            conexion = conexionBD.getConnection();
            statementMapas = conexion
                    .prepareStatement("INSERT INTO Mapas (nombre, dificultad, servidor_id) VALUES (?, ?, ?)");
            statementZonas = conexion
                    .prepareStatement("INSERT INTO Zonas (nombre, ancho, alto, mapa_id) VALUES (?, ?, ?, ?)");

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
        } finally {
            // Cierre de recursos
            if (statementMapas != null) {
                try {
                    statementMapas.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statementZonas != null) {
                try {
                    statementZonas.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String generarCodigoUnico(Conexion conexionBD) {
        String codigo = "";
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Boolean salir = false;
        try {
            // Genera códigos aleatorios hasta conseguir uno que no exista en la base de
            // datos.

            conexion = conexionBD.getConnection();
            statement = conexion.prepareStatement("SELECT codigo_uniq FROM Usuarios");
            resultSet = statement.executeQuery();
            ArrayList<String> codigosBD = new ArrayList<>();
            while (resultSet.next()) {
                codigosBD.add(resultSet.getString(1));
            }

            while (!salir) {
                codigo = generarCodigo();
                salir=true;
                for (String codigobd : codigosBD) {
                    if (codigo.equals(codigobd)) {
                        salir=false;
                        System.out.println("Encontrada coincidencia!");
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cierre de recursos
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return codigo;
    }

    private static String generarCodigo() {
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            codigo.append(random.nextInt(10));
        }
        return codigo.toString();
    }
}
