package CONTROLADOR;

import MODELO.ModeloPagoLuz;

/**
 * Controlador (MVC): recibe los eventos de la vista y actualiza el modelo.
 */
public class ControladorPagoLuz {

    private final ModeloPagoLuz modelo;

    public ControladorPagoLuz(ModeloPagoLuz modelo) {
        this.modelo = modelo;
    }

    public void ingresarNumeroServicio(String texto) {
        modelo.buscarClientes(texto);
    }

    public void seleccionarCliente(int indice) {
        modelo.seleccionarCliente(indice);
    }

    public void pagar(String numeroTarjeta) {
        modelo.registrarPago(numeroTarjeta);
    }
}
