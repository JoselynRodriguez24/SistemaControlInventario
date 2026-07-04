/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 *
 * @author HP
 */
import negocio.ProductoNegocio;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Capa Presentación - Pestaña "Estadísticas".
 * Solo se encarga de mostrar los resultados calculados por la capa de negocio.
 */
public class EstadisticaPanel extends JPanel {

    private final ProductoNegocio negocio;
    private JTextArea areaResumen;
    private JTable tablaCategorias;
    private javax.swing.table.DefaultTableModel modeloCategorias;

    public EstadisticaPanel(ProductoNegocio negocio) {
        this.negocio = negocio;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        areaResumen = new JTextArea(10, 40);
        areaResumen.setEditable(false);
        areaResumen.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        add(new JScrollPane(areaResumen), BorderLayout.CENTER);

        modeloCategorias = new javax.swing.table.DefaultTableModel(
                new String[]{"Categoría", "Cantidad de productos"}, 0);
        tablaCategorias = new JTable(modeloCategorias);
        JScrollPane scrollCategorias = new JScrollPane(tablaCategorias);
        scrollCategorias.setPreferredSize(new Dimension(300, 150));
        add(scrollCategorias, BorderLayout.EAST);

        JButton btnActualizar = new JButton("Actualizar estadísticas");
        btnActualizar.addActionListener(e -> refrescar());
        add(btnActualizar, BorderLayout.SOUTH);
    }

    public void refrescar() {
        ProductoNegocio.Estadisticas est = negocio.calcularEstadisticas();

        StringBuilder sb = new StringBuilder();
        sb.append("Cantidad total de productos:      ").append(est.totalProductos).append("\n");
        sb.append("Productos disponibles:            ").append(est.disponibles).append("\n");
        sb.append("Productos no disponibles:         ").append(est.noDisponibles).append("\n");
        sb.append("Unidades totales almacenadas:     ").append(est.unidadesTotales).append("\n");
        sb.append(String.format("Valor total del inventario:       ₡%.2f%n", est.valorTotalInventario));
        sb.append("\n");

        if (est.masCaro != null) {
            sb.append(String.format("Producto con mayor precio:        %s (₡%.2f)%n",
                    est.masCaro.getNombre(), est.masCaro.getPrecio()));
        } else {
            sb.append("Producto con mayor precio:        N/A\n");
        }
        if (est.masBarato != null) {
            sb.append(String.format("Producto con menor precio:        %s (₡%.2f)%n",
                    est.masBarato.getNombre(), est.masBarato.getPrecio()));
        } else {
            sb.append("Producto con menor precio:        N/A\n");
        }

        areaResumen.setText(sb.toString());

        modeloCategorias.setRowCount(0);
        for (Map.Entry<String, Integer> entrada : est.porCategoria.entrySet()) {
            modeloCategorias.addRow(new Object[]{entrada.getKey(), entrada.getValue()});
        }
    }
}