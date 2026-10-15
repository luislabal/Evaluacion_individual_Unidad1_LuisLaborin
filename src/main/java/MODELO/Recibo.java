package MODELO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Recibo {

    private static int contador = 0;

    private final String folio;
    private final Date fecha;
    private final double monto;
    private final Cliente cliente;
    private final Pago pago;

    private Recibo(String folio, Date fecha, double monto, Cliente cliente, Pago pago) {
        this.folio = folio;
        this.fecha = fecha;
        this.monto = monto;
        this.cliente = cliente;
        this.pago = pago;
    }

    public static Recibo generar(Cliente cliente, Pago pago) {
        contador++;
        String dia = new SimpleDateFormat("yyyyMMdd").format(pago.getFecha());
        String folio = String.format("LUZ-%s-%04d", dia, contador);
        return new Recibo(folio, pago.getFecha(), pago.getMonto(), cliente, pago);
    }

    public String getFolio() {
        return folio;
    }

    public Date getFecha() {
        return fecha;
    }

    public double getMonto() {
        return monto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Pago getPago() {
        return pago;
    }
}
