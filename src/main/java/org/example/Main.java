package org.example;

import java.util.List;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {

        // Crear un nuevo piloto (Mika Hakkinen) y agregarlo a la base de datos
        Piloto hakkinen = new Piloto(1, "HAKK", "Mika", "Hakkinen", "1960-05-09", "Finland", "https://es.wikipedia.org/wiki/Mika_H%C3%A4kkinen");
        ejecutarYMostrarResultado(() -> OperacionesCRUDPilotos.crearYAgregarNuevoPiloto(hakkinen), "Piloto agregado", "No se pudo agregar el piloto");

        // Leer un piloto por su ID y mostrar la información
        Piloto pilotoLeido = OperacionesCRUDPilotos.leerPiloto(3);
        if (pilotoLeido != null) {
            System.out.println(pilotoLeido);
        } else {
            System.out.println("Piloto no encontrado.");
        }

        // Leer y mostrar todos los pilotos
        List<Piloto> pilotos = OperacionesCRUDPilotos.leerPilotos();
        pilotos.forEach(System.out::println);

        // Actualizar un piloto y mostrar el resultado
        Piloto pilotoActualizado = new Piloto(1, "HAKK", "Mika", "Hakkinen", "1960-05-09", "Finland", "https://es.wikipedia.org/wiki/Mika_H%C3%A4kkinen");
        ejecutarYMostrarResultado(() -> OperacionesCRUDPilotos.actualizarPiloto(pilotoActualizado), "Piloto actualizado", "No se pudo actualizar el piloto");

        // Eliminar un piloto y mostrar el resultado
        Piloto pilotoAEliminar = new Piloto(1, "HAKK", "Mika", "Hakkinen", "1960-05-09", "Finland", "https://es.wikipedia.org/wiki/Mika_H%C3%A4kkinen");
        ejecutarYMostrarResultado(() -> OperacionesCRUDPilotos.borrarPiloto(pilotoAEliminar), "Piloto eliminado");

        // Mostrar la clasificación de los pilotos
        OperacionesCRUDPilotos.mostrarClasificacionPiloto();
    }

    // Método para ejecutar una acción y mostrar el resultado
    private static void ejecutarYMostrarResultado(Supplier<Boolean> accion, String mensajeExito, String mensajeFallo) {
        if (accion.get()) {
            System.out.println(mensajeExito);
        } else {
            System.out.println(mensajeFallo);
        }
    }

    // Sobrecarga del método para manejar acciones sin mensaje de fallo
    private static void ejecutarYMostrarResultado(Supplier<Boolean> accion, String mensajeExito) {
        ejecutarYMostrarResultado(accion, mensajeExito, "");
    }
}
