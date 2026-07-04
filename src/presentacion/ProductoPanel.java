/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 *
 * @author HP
 */
import excepciones.DatoInvalidoException;
import excepciones.ProductoDuplicadoException;
import modelo.Producto;
import negocio.ProductoNegocio;

import javax.swing.*;
import java.awt.*;

/**
 * Capa Presentación - Pestaña "Registro de productos".
 * Solo captura datos, muestra datos y llama a la capa de negocio.
 * No contiene validaciones ni accede directamente a las Collections.
 */
public class ProductoPanel extends JPanel {

    private final ProductoNegocio negocio;
    private final MainFrame mainFrame;

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JComboBox<String> cmbCategoria;
    private JTextField txtCantidad;
    private JTextField txtPrecio;
    private JCheckBox chkDisponible;
    private JRadioButton rbFisico;
    private JRadioButton rbDigital;
    private JTextArea txtDescripcion;

    private JButton btnGuardar;
    private JButton btnEditar;
    private JButton btnLimpiar;

    private Integer idEnEdicion = null;

    public ProductoPanel(ProductoNegocio negocio, MainFrame mainFrame) {
        this.negocio = negocio;
        this.mainFrame = mainFrame;
        construirInterfaz();
    }
    
    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        agregarEtiqueta(formulario, gbc, "Código:", fila);
        txtCodigo = new JTextField(20);
        agregarCampo(formulario, gbc, txtCodigo, fila++);

        agregarEtiqueta(formulario, gbc, "Nombre:", fila);
        txtNombre = new JTextField(20);
        agregarCampo(formulario, gbc, txtNombre, fila++);

        agregarEtiqueta(formulario, gbc, "Categoría:", fila);
        cmbCategoria = new JComboBox<>(new String[]{
                "Electrónica", "Oficina", "Hogar", "Alimentos", "Ropa", "Herramientas", "Otros"
        });
        cmbCategoria.setEditable(true);
        agregarCampo(formulario, gbc, cmbCategoria, fila++);

        agregarEtiqueta(formulario, gbc, "Cantidad:", fila);
        txtCantidad = new JTextField(20);
        agregarCampo(formulario, gbc, txtCantidad, fila++);

        agregarEtiqueta(formulario, gbc, "Precio:", fila);
        txtPrecio = new JTextField(20);
        agregarCampo(formulario, gbc, txtPrecio, fila++);

        agregarEtiqueta(formulario, gbc, "Disponible:", fila);
        chkDisponible = new JCheckBox("Producto disponible");
        chkDisponible.setSelected(true);
        agregarCampo(formulario, gbc, chkDisponible, fila++);

        agregarEtiqueta(formulario, gbc, "Tipo de producto:", fila);
        JPanel panelRadios = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        rbFisico = new JRadioButton("Físico", true);
        rbDigital = new JRadioButton("Digital");
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(rbFisico);
        grupoTipo.add(rbDigital);
        panelRadios.add(rbFisico);
        panelRadios.add(rbDigital);
        agregarCampo(formulario, gbc, panelRadios, fila++);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formulario.add(new JLabel("Descripción:"), gbc);

        txtDescripcion = new JTextArea(5, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formulario.add(scrollDescripcion, gbc);

        add(formulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnGuardar = new JButton("Guardar");
        btnEditar = new JButton("Editar");
        btnLimpiar = new JButton("Limpiar campos");

        btnGuardar.setBackground(new Color(46, 125, 50));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnEditar.setEnabled(false);

        btnGuardar.addActionListener(e -> guardarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnLimpiar);

        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void agregarEtiqueta(JPanel panel, GridBagConstraints gbc, String texto, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(texto), gbc);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, Component campo, int fila) {
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campo, gbc);
    }
    
    public void guardarProducto() {
        try {
            Producto producto = leerFormulario();
            negocio.agregar(producto);
            JOptionPane.showMessageDialog(this,
                    "Producto guardado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            mainFrame.refrescarTodo();
        } catch (DatoInvalidoException | ProductoDuplicadoException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarProducto() {
        if (idEnEdicion == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un producto de la lista para editar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Producto producto = leerFormulario();
            producto.setId(idEnEdicion);
            negocio.editar(producto);
            JOptionPane.showMessageDialog(this,
                    "Producto editado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            mainFrame.refrescarTodo();
        } catch (DatoInvalidoException | ProductoDuplicadoException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lee los datos del formulario y construye un Producto.
     * Las conversiones numéricas se delegan a la capa de negocio,
     * que es quien decide si son válidas (lanza DatoInvalidoException si no).
     */
    private Producto leerFormulario() throws DatoInvalidoException {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String categoria = String.valueOf(cmbCategoria.getEditor().getItem()).trim();
        int cantidad = negocio.parsearCantidad(
                txtCantidad.getText().isBlank() ? "-1" : txtCantidad.getText());
        double precio = negocio.parsearPrecio(
                txtPrecio.getText().isBlank() ? "0" : txtPrecio.getText());
        boolean disponible = chkDisponible.isSelected();
        String tipo = rbFisico.isSelected() ? "Físico" : "Digital";
        String descripcion = "[" + tipo + "] " + txtDescripcion.getText().trim();

        Producto producto = new Producto();
        producto.setCodigo(codigo);
        producto.setNombre(nombre);
        producto.setCategoria(categoria);
        producto.setCantidad(cantidad);
        producto.setPrecio(precio);
        producto.setDisponible(disponible);
        producto.setDescripcion(descripcion);
        return producto;
    }

    public void limpiarCampos() {
        idEnEdicion = null;
        txtCodigo.setText("");
        txtNombre.setText("");
        cmbCategoria.setSelectedIndex(0);
        txtCantidad.setText("");
        txtPrecio.setText("");
        chkDisponible.setSelected(true);
        rbFisico.setSelected(true);
        txtDescripcion.setText("");
        btnEditar.setEnabled(false);
    }
    
   /**
     * Carga un producto existente en el formulario para su edición.
     * Llamado desde ListaPanel cuando el usuario selecciona una fila.
     */
    public void cargarProductoParaEdicion(Producto producto) {
        idEnEdicion = producto.getId();
        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());
        cmbCategoria.setSelectedItem(producto.getCategoria());
        txtCantidad.setText(String.valueOf(producto.getCantidad()));
        txtPrecio.setText(String.valueOf(producto.getPrecio()));
        chkDisponible.setSelected(producto.isDisponible());
        if (producto.getDescripcion() != null && producto.getDescripcion().startsWith("[Digital]")) {
            rbDigital.setSelected(true);
        } else {
            rbFisico.setSelected(true);
        }
        txtDescripcion.setText(producto.getDescripcion());
        btnEditar.setEnabled(true);
    }
}