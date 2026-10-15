package MODELO;

import java.util.ArrayList;
import java.util.List;


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
        for (Observador o : new ArrayList<>(observadores)) {
            o.actualizar();
        }
    }
}
