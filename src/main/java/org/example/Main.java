package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {

        Piloto hakkinen = new Piloto("1", "Mika", "Hakkinen", "1960-05-09", "Finland", "https://es.wikipedia.org/wiki/Mika_H%C3%A4kkinen");

        ejecutarYMostrarResultado(() -> ConectarConBaseDatos.crearYAgregarPiloto(hakkinen), "Piloto agregado", "No se pudo agregar el piloto");

        Piloto pilotoLeido = ConectarConBaseDatos.leerPiloto(3);
        //System.out.println(pilotoLeido);

        List<Piloto> pilotos = ConectarConBaseDatos.leerPilotos();
        pilotos.forEach(System.out::println);

        Piloto pilotoActualizado = new Piloto(9, "AAA", "AAA", "AAA", "1985-01-07", "British", "http://en.wikipedia.org/wiki/Lewis_Hamilton");
        ejecutarYMostrarResultado(() -> ConectarConBaseDatos.actualizarPiloto(pilotoActualizado), "Piloto actualizado", "No se pudo actualizar el piloto");

        Piloto pilotoAEliminar = new Piloto(9, "AAA", "AAA", "AAA", "1985-01-07", "British", "http://en.wikipedia.org/wiki/Lewis_Hamilton");
        ejecutarYMostrarResultado(() -> ConectarConBaseDatos.borrarPiloto(pilotoAEliminar), "Piloto eliminado");

        ConectarConBaseDatos.mostrarClasificacionPiloto();
    }

    private static void ejecutarYMostrarResultado(Supplier<Boolean> accion, String mensajeExito, String mensajeFallo) {
        if (accion.get()) {
            System.out.println(mensajeExito);
        } else {
            System.out.println(mensajeFallo);
        }
    }

    private static void ejecutarYMostrarResultado(Supplier<Boolean> accion, String mensajeExito) {
        ejecutarYMostrarResultado(accion, mensajeExito, "");
    }
}
