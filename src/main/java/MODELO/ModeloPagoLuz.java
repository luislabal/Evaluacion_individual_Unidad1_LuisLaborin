package MODELO;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo (MVC): guarda el estado y la lógica del pago de luz.
 * Notifica a sus observadores cada vez que cambia.
 */
public class ModeloPagoLuz extends Sujeto {

    private final List<Cliente> clientes = new ArrayList<>();
    private List<Cliente> clientesEncontrados = new ArrayList<>();
    private Cliente clienteSeleccionado;
    private Consumo consumo;
    private Pago pago;
    private Recibo recibo;

    public ModeloPagoLuz() {
        // Clientes de ejemplo (deben coincidir con los de Consumo)
        clientes.add(new Cliente("123456789012", "Juan Pérez López"));
        clientes.add(new Cliente("123456781234", "María Fernanda Ruiz"));
        clientes.add(new Cliente("123450987654", "Carlos Alberto Soto"));
        clientes.add(new Cliente("987654321098", "Ana Lucía Valenzuela"));
        clientes.add(new Cliente("987651234567", "Roberto Gámez Ochoa"));
        clientes.add(new Cliente("456789123456", "Laura Beltrán Cota"));
        clientes.add(new Cliente("456781230001", "José Luis Moreno"));
    }

    /** CU1: filtra los clientes cuyo número de servicio coincide con el texto. */
    public void buscarClientes(String texto) {
        clientesEncontrados = new ArrayList<>();
        for (Cliente c : clientes) {
            if (c.coincideCon(texto)) {
                clientesEncontrados.add(c);
            }
        }
        // Una nueva búsqueda invalida lo que se había seleccionado/pagado
        clienteSeleccionado = null;
        consumo = null;
        pago = null;
        recibo = null;
        notificarObservadores();
    }

    /** CU2 + CU3: selecciona el cliente de la lista y consulta su consumo. */
    public void seleccionarCliente(int indice) {
        if (indice < 0 || indice >= clientesEncontrados.size()) {
            return;
        }
        clienteSeleccionado = clientesEncontrados.get(indice);
        consumo = Consumo.obtenerPorCliente(clienteSeleccionado);
        pago = null;
        recibo = null;
        notificarObservadores();
    }

    /** CU4 + CU5: registra el pago con la tarjeta y genera el recibo. */
    public void registrarPago(String numeroTarjeta) {
        if (clienteSeleccionado == null || consumo == null || recibo != null) {
            return; // nada que pagar o ya está pagado
        }
        pago = Pago.registrar(numeroTarjeta, consumo);
        recibo = Recibo.generar(clienteSeleccionado, pago);
        notificarObservadores();
    }

    public List<Cliente> getClientesEncontrados() {
        return clientesEncontrados;
    }

    public Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public Consumo getConsumo() {
        return consumo;
    }

    public Recibo getRecibo() {
        return recibo;
    }
}
