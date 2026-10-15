package MODELO;

/**
 * Interfaz Observer (diagrama MVC).
 * Toda clase que quiera enterarse de los cambios del modelo la implementa.
 */
public interface Observador {

    void actualizar();
}
