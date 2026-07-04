/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 *
 * @author HP
 */
import modelo.Producto;
import negocio.ProductoNegocio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Capa Presentación - Pestaña "Lista de productos".
 * Muestra los productos en un JTable y permite buscar, filtrar,
 * ordenar, seleccionar, editar y eliminar. Toda la lógica real
 * (orden, filtros, borrado) se delega a la capa de negocio.
 */
public class ListaPanel extends JPanel {

    private final ProductoNegocio negocio;
    private final MainFrame mainFrame;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JComboBox<String> cmbFiltroCategoria;

    private static final String[] COLUMNAS = {
            "ID", "Código", "Nombre", "Categoría", "Cantidad", "Precio", "Disponible"
    };

   public ListaPanel(ProductoNegocio negocio, MainFrame mainFrame) {
        this.negocio = negocio;
        this.mainFrame = mainFrame;
        construirInterfaz();
    }
    
    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior: búsqueda, filtro y ordenamiento.
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));

        panelSuperior.add(new JLabel("Buscar:"));
        txtBuscar = new JTextField(15);
        panelSuperior.add(txtBuscar);
        JButton btnBuscar = new JButton("Buscar");
        panelSuperior.add(btnBuscar);

        panelSuperior.add(new JLabel("Categoría:"));
        cmbFiltroCategoria = new JComboBox<>(new String[]{
                "Todas", "Electrónica", "Oficina", "Hogar", "Alimentos", "Ropa", "Herramientas", "Otros"
        });
        panelSuperior.add(cmbFiltroCategoria);
        JButton btnFiltrar = new JButton("Filtrar");
        panelSuperior.add(btnFiltrar);

        JButton btnOrdenNombre = new JButton("Ordenar por nombre");
        JButton btnOrdenPrecio = new JButton("Ordenar por precio");
        JButton btnOrdenCantidad = new JButton("Ordenar por cantidad");
        panelSuperior.add(btnOrdenNombre);
        panelSuperior.add(btnOrdenPrecio);
        panelSuperior.add(btnOrdenCantidad);

        add(panelSuperior, BorderLayout.NORTH);

        // Tabla central.
        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);
        tabla.setDefaultRenderer(Object.class, crearRendererColores());
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        
        // Panel inferior: acciones sobre el producto seleccionado.
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JButton btnEditarSeleccionado = new JButton("Editar seleccionado");
        JButton btnEliminarSeleccionado = new JButton("Eliminar seleccionado");
        panelInferior.add(btnEditarSeleccionado);
        panelInferior.add(btnEliminarSeleccionado);
        add(panelInferior, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscar());
        btnFiltrar.addActionListener(e -> filtrar());
        btnOrdenNombre.addActionListener(e -> {
            List<Producto> lista = negocio.ordenarPorNombre(negocio.listar());
            cargarTabla(lista);
        });
        btnOrdenPrecio.addActionListener(e -> {
            List<Producto> lista = negocio.ordenarPorPrecio(negocio.listar());
            cargarTabla(lista);
        });
        btnOrdenCantidad.addActionListener(e -> {
            List<Producto> lista = negocio.ordenarPorCantidad(negocio.listar());
            cargarTabla(lista);
        });
        btnEditarSeleccionado.addActionListener(e -> editarSeleccionado());
        btnEliminarSeleccionado.addActionListener(e -> eliminarSeleccionado());
    }

    private javax.swing.table.DefaultTableCellRenderer crearRendererColores() {
        return new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component celda = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    String disponible = (String) table.getValueAt(row, 6);
                    if ("Sí".equals(disponible)) {
                        celda.setBackground(new Color(232, 245, 233));
                    } else {
                        celda.setBackground(new Color(253, 236, 234));
                    }
                }
                return celda;
            }
        };
    }

    private void buscar() {
        cargarTabla(negocio.buscarPorNombreOCodigo(txtBuscar.getText()));
    }

    private void filtrar() {
        String categoria = (String) cmbFiltroCategoria.getSelectedItem();
        cargarTabla(negocio.filtrarPorCategoria(categoria));
    }

    public void editarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un producto de la tabla.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Producto producto = buscarPorId(id);
        if (producto != null) {
            mainFrame.cargarProductoEnFormulario(producto);
        }
    }

    public void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un producto de la tabla.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 2);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el producto \"" + nombre + "\"?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            negocio.eliminar(id);
            mainFrame.refrescarTodo();
        }
    }

    private Producto buscarPorId(int id) {
        return negocio.listar().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Vuelve a cargar la tabla con todos los productos actuales.
     */
    public void refrescar() {
        cargarTabla(negocio.listar());
    }

    private void cargarTabla(List<Producto> productos) {
        modeloTabla.setRowCount(0);
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                    p.getId(), p.getCodigo(), p.getNombre(), p.getCategoria(),
                    p.getCantidad(), p.getPrecio(), p.isDisponible() ? "Sí" : "No"
            });
        }
    }
}