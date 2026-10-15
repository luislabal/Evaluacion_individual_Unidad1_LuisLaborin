package MODELO;

import java.util.Date;

public class Pago {

    private final String numeroTarjeta;
    private final double monto;
    private final Date fecha;

    public Pago(String numeroTarjeta, double monto, Date fecha) {
        this.numeroTarjeta = numeroTarjeta;
        this.monto = monto;
        this.fecha = fecha;
    }

    public static Pago registrar(String numeroTarjeta, Consumo consumo) {
        return new Pago(numeroTarjeta, consumo.getImporte(), new Date());
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public String getTarjetaEnmascarada() {
        String ultimos = numeroTarjeta.length() >= 4
                ? numeroTarjeta.substring(numeroTarjeta.length() - 4)
                : numeroTarjeta;
        return "**** **** **** " + ultimos;
    }

    public double getMonto() {
        return monto;
    }

    public Date getFecha() {
        return fecha;
    }
}
