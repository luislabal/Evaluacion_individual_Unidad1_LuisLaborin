
package VISTA;

import CONTROLADOR.ControladorPagoLuz;
import MODELO.Cliente;
import MODELO.Consumo;
import MODELO.ModeloPagoLuz;
import MODELO.Observador;
import MODELO.Recibo;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 
 *
 * @author LUIS CARLOS LABORIN ALVAREZ
 */
public class ServicioLuz extends javax.swing.JFrame implements Observador {

    private final ModeloPagoLuz modelo;
    private final ControladorPagoLuz controlador;
    private final DefaultListModel<String> listaClientes = new DefaultListModel<>();

    private static final SimpleDateFormat FECHA = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat FECHA_HORA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

   
    public ServicioLuz() {
        initComponents();
        modelo = new ModeloPagoLuz();
        controlador = new ControladorPagoLuz(modelo);
        modelo.agregarObservador(this);
        configurarVista();
        actualizar();
    }

  
    private void configurarVista() {
        setTitle("Pago de luz");
        setLocationRelativeTo(null);

        jList1.setModel(listaClientes);
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        txtDetallesConsumo.setEditable(false);
        txtDetallesConsumo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        txtDetallesConsumo.setLineWrap(true);
        txtDetallesConsumo.setWrapStyleWord(true);

    
        txtNumeroServicio.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                alIngresarNumeroServicio(txtNumeroServicio.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                alIngresarNumeroServicio(txtNumeroServicio.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                alIngresarNumeroServicio(txtNumeroServicio.getText());
            }
        });

      
        btnSeleccion.addActionListener(e -> alSeleccionarCliente(jList1.getSelectedIndex()));
        jList1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    alSeleccionarCliente(jList1.getSelectedIndex());
                }
            }
        });

        btnPagar.addActionListener(e -> alPagar(txtNumeroTarjeta.getText()));
        txtNumeroTarjeta.addActionListener(e -> btnPagar.doClick());
    }

 
    private void alIngresarNumeroServicio(String texto) {
        controlador.ingresarNumeroServicio(texto);
    }

    private void alSeleccionarCliente(int indice) {
        if (indice < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la lista",
                    "Pago de luz", JOptionPane.WARNING_MESSAGE);
            return;
        }
        controlador.seleccionarCliente(indice);
    }

    private void alPagar(String numeroTarjeta) {
        if (modelo.getConsumo() == null) {
            JOptionPane.showMessageDialog(this,
                    "Primero seleccione un cliente para ver su consumo.",
                    "Pago de luz", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String tarjeta = numeroTarjeta == null ? "" : numeroTarjeta.replaceAll("[\\s-]", "");
        if (!tarjeta.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(this,
                    "El numero de tarjeta debe tener 16 digitos",
                    "Tarjeta no valida", JOptionPane.ERROR_MESSAGE);
            txtNumeroTarjeta.requestFocus();
            return;
        }
        controlador.pagar(tarjeta);
    }

    @Override
    public void actualizar() {
        mostrarClientes(modelo.getClientesEncontrados());

        if (modelo.getRecibo() != null) {
            jLabel4.setText("RECIBO DE PAGO:"); 
            txtNumeroTarjeta.setText("");
        } else if (modelo.getClienteSeleccionado() != null) {
            jLabel4.setText("DETALLES DEL CONSUMO:");
            mostrarConsumo(modelo.getConsumo());        
        } else {
            jLabel4.setText("DETALLES DEL CONSUMO:");
            txtDetallesConsumo.setText("");
        }

        boolean puedePagar = modelo.getConsumo() != null && modelo.getRecibo() == null;
        txtNumeroTarjeta.setEnabled(puedePagar);
        btnPagar.setEnabled(puedePagar);
    }

    public void mostrarClientes(List<Cliente> lista) {
        listaClientes.clear();
        for (Cliente c : lista) {
            listaClientes.addElement(c.toString());
        }
        int seleccionado = lista.indexOf(modelo.getClienteSeleccionado());
        if (seleccionado >= 0) {
            jList1.setSelectedIndex(seleccionado);
        }
    }

    public void mostrarConsumo(Consumo consumo) {
        Cliente cliente = modelo.getClienteSeleccionado();
        if (consumo == null) {
            txtDetallesConsumo.setText("El cliente " + cliente.getNumeroServicio()
                    + "\nno tiene consumo registrado");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("DATOS DE CONSUMO\n");
        sb.append("--------------------------\n");
        sb.append(String.format("Servicio: %s%n", cliente.getNumeroServicio()));
        sb.append(String.format("Cliente:  %s%n", cliente.getNombre()));
        sb.append(String.format("Periodo:  %s%n", consumo.getPeriodo()));
        sb.append(String.format(Locale.US, "Consumo:  %.0f kWh%n", consumo.getKWh()));
        sb.append(String.format(Locale.US, "Importe:  $%,.2f%n", consumo.getImporte()));
        sb.append(String.format("Vence:    %s%n", FECHA.format(consumo.getFechaLimite())));
        txtDetallesConsumo.setText(sb.toString());
        txtDetallesConsumo.setCaretPosition(0);
    }

    public void mostrarRecibo(Recibo recibo) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== RECIBO DE PAGO =====\n");
        sb.append(String.format("Folio:    %s%n", recibo.getFolio()));
        sb.append(String.format("Fecha:    %s%n", FECHA_HORA.format(recibo.getFecha())));
        sb.append(String.format("Servicio: %s%n", recibo.getCliente().getNumeroServicio()));
        sb.append(String.format("Cliente:  %s%n", recibo.getCliente().getNombre()));
        if (modelo.getConsumo() != null) {
            sb.append(String.format("Periodo:  %s%n", modelo.getConsumo().getPeriodo()));
            sb.append(String.format(Locale.US, "Consumo:  %.0f kWh%n", modelo.getConsumo().getKWh()));
        }
        sb.append(String.format("Tarjeta:  %s%n", recibo.getPago().getTarjetaEnmascarada()));
        sb.append("--------------------------\n");
        sb.append(String.format(Locale.US, "TOTAL PAGADO: $%,.2f%n", recibo.getMonto()));
        sb.append("==========================\n");
        sb.append("   ¡Gracias por su pago!\n");
        txtDetallesConsumo.setText(sb.toString());
        txtDetallesConsumo.setCaretPosition(0);
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNumeroServicio = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDetallesConsumo = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNumeroTarjeta = new javax.swing.JTextField();
        btnSeleccion = new javax.swing.JButton();
        btnPagar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel1.setText("SERVICIO DE PAGO DE LUZ");

        jLabel2.setText("NUMERO DE SERVICIO:");

        jLabel3.setText("CLIENTES:");

        jScrollPane1.setViewportView(jList1);

        txtDetallesConsumo.setColumns(20);
        txtDetallesConsumo.setRows(5);
        jScrollPane2.setViewportView(txtDetallesConsumo);

        jLabel4.setText("DETALLES DEL CONSUMO:");

        jLabel5.setText("NUMERO DE TARJETA:");

        btnSeleccion.setText("SELECCIONAR");

        btnPagar.setText("PAGAR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(116, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(95, 95, 95))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(154, 154, 154))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtNumeroTarjeta, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNumeroServicio))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(22, 22, 22))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(btnSeleccion)
                                        .addGap(116, 116, 116))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnPagar)
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtNumeroServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(55, 55, 55)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSeleccion)))
                .addGap(27, 27, 27)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumeroTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPagar))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServicioLuz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServicioLuz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServicioLuz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServicioLuz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServicioLuz().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPagar;
    private javax.swing.JButton btnSeleccion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea txtDetallesConsumo;
    private javax.swing.JTextField txtNumeroServicio;
    private javax.swing.JTextField txtNumeroTarjeta;
    // End of variables declaration//GEN-END:variables
}
