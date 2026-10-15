package MODELO;

public class Cliente {

    private final String numeroServicio;
    private final String nombre;

    public Cliente(String numeroServicio, String nombre) {
        this.numeroServicio = numeroServicio;
        this.nombre = nombre;
    }

    /**
     * Indica si el número de servicio del cliente cumple con el criterio
     * tecleado (coincidencia parcial).
     */
    public boolean coincideCon(String texto) {
        if (texto == null) {
            return false;
        }
        String criterio = texto.trim();
        return !criterio.isEmpty() && numeroServicio.contains(criterio);
    }

    public String getNumeroServicio() {
        return numeroServicio;
    }

    public String getNombre() {
        return nombre;
    }

    /** Texto que se muestra en la lista de clientes. */
    @Override
    public String toString() {
        return numeroServicio + " - " + nombre;
    }
}
