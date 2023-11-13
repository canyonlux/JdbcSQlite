package org.example;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OperacionesCRUDPilotos {

    // Ruta de la base de datos
    private static final String RUTA_BASE_DATOS = Paths.get("src", "main", "resources", "db", "f12006sqlite.db").toString();

    // Método para crear y agregar un nuevo piloto a la base de datos
    public static boolean crearYAgregarNuevoPiloto(Piloto piloto) {
        if (piloto.getCode() == null || piloto.getCode().isEmpty()) {
            System.out.println("El código del piloto no puede ser nulo o vacío.");
            return false;
        }
        String sqlAgregarPiloto = "INSERT INTO drivers (code, forename, surname, dob, nationality, url) VALUES (?,?,?,?,?,?)";
        return ejecutarActualizacion(sqlAgregarPiloto, ps -> {
            ps.setString(1, piloto.getCode());
            ps.setString(2, piloto.getForename());
            ps.setString(3, piloto.getSurname());
            ps.setString(4, piloto.getDob());
            ps.setString(5, piloto.getNationality());
            ps.setString(6, piloto.getUrl());
        });
    }

    // Método para leer un piloto específico por su ID
    public static Piloto leerPiloto(int id) {
        String sqlConsultaPiloto = "SELECT * FROM drivers WHERE driverId = ?";
        try (Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + RUTA_BASE_DATOS);
             PreparedStatement consulta = conexion.prepareStatement(sqlConsultaPiloto)) {

            consulta.setInt(1, id);
            ResultSet resultado = consulta.executeQuery();
            if (resultado.next()) {
                return new Piloto(resultado.getInt("driverId"),
                        resultado.getString("code"),
                        resultado.getString("forename"),
                        resultado.getString("surname"),
                        resultado.getString("dob"),
                        resultado.getString("nationality"),
                        resultado.getString("url"));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para leer todos los pilotos
    public static List<Piloto> leerPilotos() {
        String sqlConsultaTodos = "SELECT * FROM drivers";
        List<Piloto> pilotos = new ArrayList<>();
        try (Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + RUTA_BASE_DATOS);
             PreparedStatement consulta = conexion.prepareStatement(sqlConsultaTodos);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                pilotos.add(new Piloto(resultado.getInt("driverId"),
                        resultado.getString("code"),
                        resultado.getString("forename"),
                        resultado.getString("surname"),
                        resultado.getString("dob"),
                        resultado.getString("nationality"),
                        resultado.getString("url")));
            }
            return pilotos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para actualizar un piloto
    public static boolean actualizarPiloto(Piloto piloto) {
        String sqlActualizarPiloto = "UPDATE drivers SET code = ?, forename = ?, surname = ?, dob = ?, nationality = ?, url = ? WHERE driverId = ?";
        return ejecutarActualizacion(sqlActualizarPiloto, ps -> {
            ps.setString(1, piloto.getCode());
            ps.setString(2, piloto.getForename());
            ps.setString(3, piloto.getSurname());
            ps.setString(4, piloto.getDob());
            ps.setString(5, piloto.getNationality());
            ps.setString(6, piloto.getUrl());
            ps.setInt(7, piloto.getDriverid());
        });
    }

    // Método para borrar un piloto
    public static boolean borrarPiloto(Piloto piloto) {
        String sqlBorrarPiloto = "DELETE FROM drivers WHERE driverId = ?";
        return ejecutarActualizacion(sqlBorrarPiloto, ps -> ps.setInt(1, piloto.getDriverid()));
    }

    // Método para mostrar la clasificación de los pilotos
    public static void mostrarClasificacionPiloto() {
        String sqlClasificacionPilotos = "SELECT d.code, sum(r.points) AS puntos FROM drivers d JOIN results r ON d.driverid = r.driverid GROUP BY d.code ORDER BY puntos DESC";
        ejecutarConsultaYMostrarResultados(sqlClasificacionPilotos, rs ->
                System.out.println(rs.getString("code") + " " + rs.getInt("puntos"))
        );
    }

    // Método privado para ejecutar una actualización en la base de datos
    private static boolean ejecutarActualizacion(String sql, SQLConsumer<PreparedStatement> preparador) {
        try (Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + RUTA_BASE_DATOS);
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            preparador.accept(consulta);
            consulta.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Método privado para ejecutar una consulta y procesar el ResultSet con un consumidor
    private static void ejecutarConsultaYMostrarResultados(String sql, SQLConsumer<ResultSet> consumidor) {
        try (Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + RUTA_BASE_DATOS);
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                consumidor.accept(resultado);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Interfaz funcional para consumir SQL
    @FunctionalInterface
    private interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }
}
