package MODELO;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Consumo {

    private final String periodo;
    private final double kWh;
    private final double importe;
    private final Date fechaLimite;

    // Datos de ejemplo: número de servicio -> consumo del periodo
    private static final Map<String, Consumo> CONSUMOS = new HashMap<>();

    static {
        CONSUMOS.put("123456789012", new Consumo("Ago-Sep 2026", 350, 612.50, diasDesdeHoy(10)));
        CONSUMOS.put("123456781234", new Consumo("Ago-Sep 2026", 220, 385.00, diasDesdeHoy(10)));
        CONSUMOS.put("123450987654", new Consumo("Ago-Sep 2026", 480, 840.00, diasDesdeHoy(8)));
        CONSUMOS.put("987654321098", new Consumo("Ago-Sep 2026", 150, 262.50, diasDesdeHoy(12)));
        CONSUMOS.put("987651234567", new Consumo("Ago-Sep 2026", 610, 1067.50, diasDesdeHoy(5)));
        CONSUMOS.put("456789123456", new Consumo("Ago-Sep 2026", 275, 481.25, diasDesdeHoy(10)));
        CONSUMOS.put("456781230001", new Consumo("Ago-Sep 2026", 330, 577.50, diasDesdeHoy(9)));
    }

    public Consumo(String periodo, double kWh, double importe, Date fechaLimite) {
        this.periodo = periodo;
        this.kWh = kWh;
        this.importe = importe;
        this.fechaLimite = fechaLimite;
    }

    private static Date diasDesdeHoy(int dias) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, dias);
        return c.getTime();
    }

    /** Devuelve el consumo del cliente o null si no tiene registrado. */
    public static Consumo obtenerPorCliente(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        return CONSUMOS.get(cliente.getNumeroServicio());
    }

    public String getPeriodo() {
        return periodo;
    }

    public double getKWh() {
        return kWh;
    }

    public double getImporte() {
        return importe;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }
}
