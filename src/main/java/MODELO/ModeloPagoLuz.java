package MODELO;

import java.util.ArrayList;
import java.util.List;

public class ModeloPagoLuz extends Sujeto {

    private final List<Cliente> clientes = new ArrayList<>();
    private List<Cliente> clientesEncontrados = new ArrayList<>();
    private Cliente clienteSeleccionado;
    private Consumo consumo;
    private Pago pago;
    private Recibo recibo;

    public ModeloPagoLuz() {
        clientes.add(new Cliente("00000268492", "LUIS CARLOS LABORIN"));
        clientes.add(new Cliente("123456781234", "María Fernanda Ruiz"));
        clientes.add(new Cliente("123450987654", "Carlos Alberto Soto"));
        clientes.add(new Cliente("987654321098", "Ana Lucía Valenzuela"));
        clientes.add(new Cliente("987651234567", "Roberto Gámez Ochoa"));
        clientes.add(new Cliente("456789123456", "Laura Beltrán Cota"));
        clientes.add(new Cliente("456781230001", "José Luis Moreno"));
    }

  
    public void buscarClientes(String texto) {
        clientesEncontrados = new ArrayList<>();
        for (Cliente c : clientes) {
            if (c.coincideCon(texto)) {
                clientesEncontrados.add(c);
            }
        }
      
        clienteSeleccionado = null;
        consumo = null;
        pago = null;
        recibo = null;
        notificarObservadores();
    }

  
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

    public void registrarPago(String numeroTarjeta) {
        if (clienteSeleccionado == null || consumo == null || recibo != null) {
            return; 
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
