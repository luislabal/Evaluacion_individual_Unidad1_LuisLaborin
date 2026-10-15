package MODELO;

import java.util.ArrayList;
import java.util.List;

/**
 * Parte observable del patrón Observer (diagrama MVC).
 */
public class Sujeto {

    private final List<Observador> observadores = new ArrayList<>();

    public void agregarObservador(Observador o) {
        if (o != null && !observadores.contains(o)) {
            observadores.add(o);
        }
    }

    public void quitarObservador(Observador o) {
        observadores.remove(o);
    }

    public void notificarObservadores() {
        // Copia para evitar errores si un observador se quita durante la notificación
        for (Observador o : new ArrayList<>(observadores)) {
            o.actualizar();
        }
    }
}
