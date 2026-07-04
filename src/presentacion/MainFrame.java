/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 *
 * @author HP
 */
import excepciones.ArchivoException;
import modelo.Producto;
import negocio.ProductoNegocio;
import repositorio.ProductoRepositorio;
import util.ArchivoUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Stack;
import java.util.List;

/**
 * Capa Presentación - Ventana principal del sistema.
 * Contiene el JMenuBar, el JToolBar y el JTabbedPane con las 3 pestañas.
 * Se limita a capturar/mostrar datos y delegar en la capa de negocio.
 */
public class MainFrame extends JFrame {

    private final ProductoNegocio negocio;

    private ProductoPanel productoPanel;
    private ListaPanel listaPanel;
    private EstadisticaPanel estadisticaPanel;
    private JTabbedPane tabbedPane;

    public MainFrame() {
        super("Sistema de Control de Inventario");
        this.negocio = new ProductoNegocio(new ProductoRepositorio());
        construirInterfaz();
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setJMenuBar(crearMenuBar());
        add(crearToolBar(), BorderLayout.NORTH);
        add(crearTabbedPane(), BorderLayout.CENTER);
    }

    private JMenuBar crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemNuevo = new JMenuItem("Nuevo producto");
        JMenuItem itemExportar = new JMenuItem("Exportar inventario");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemNuevo.addActionListener(e -> nuevoProducto());
        itemExportar.addActionListener(e -> exportarInventario());
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemNuevo);
        menuArchivo.add(itemExportar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        JMenu menuHerramientas = new JMenu("Herramientas");
        JMenuItem itemOrdenar = new JMenuItem("Ordenar productos");
        JMenuItem itemEstadisticas = new JMenuItem("Ver estadísticas");
        JMenuItem itemHistorial = new JMenuItem("Ver historial");
        itemOrdenar.addActionListener(e -> {
            listaPanel.refrescar();
            tabbedPane.setSelectedIndex(1);
        });
        itemEstadisticas.addActionListener(e -> {
            estadisticaPanel.refrescar();
            tabbedPane.setSelectedIndex(2);
        });
        itemHistorial.addActionListener(e -> mostrarHistorial());
        menuHerramientas.add(itemOrdenar);
        menuHerramientas.add(itemEstadisticas);
        menuHerramientas.add(itemHistorial);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcerca = new JMenuItem("Acerca del sistema");
        itemAcerca.addActionListener(e -> mostrarAcercaDe());
        menuAyuda.add(itemAcerca);

        menuBar.add(menuArchivo);
        menuBar.add(menuHerramientas);
        menuBar.add(menuAyuda);
        return menuBar;
    }
    
    private JToolBar crearToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(232, 240, 254));
        toolBar.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnOrdenar = new JButton("Ordenar");
        JButton btnExportar = new JButton("Exportar");

        btnNuevo.addActionListener(e -> nuevoProducto());
        btnGuardar.addActionListener(e -> {
            tabbedPane.setSelectedIndex(0);
            productoPanel.guardarProducto();
        });
        btnEditar.addActionListener(e -> {
            tabbedPane.setSelectedIndex(1);
            listaPanel.editarSeleccionado();
        });
        btnEliminar.addActionListener(e -> {
            tabbedPane.setSelectedIndex(1);
            listaPanel.eliminarSeleccionado();
        });
        btnOrdenar.addActionListener(e -> {
            List<Producto> lista = negocio.ordenarPorNombre(negocio.listar());
            listaPanel.refrescar();
            tabbedPane.setSelectedIndex(1);
        });
        btnExportar.addActionListener(e -> exportarInventario());

        toolBar.add(btnNuevo);
        toolBar.add(btnGuardar);
        toolBar.add(btnEditar);
        toolBar.add(btnEliminar);
        toolBar.add(btnOrdenar);
        toolBar.add(btnExportar);
        return toolBar;
    }

    private JTabbedPane crearTabbedPane() {
        tabbedPane = new JTabbedPane();

        productoPanel = new ProductoPanel(negocio, this);
        listaPanel = new ListaPanel(negocio, this);
        estadisticaPanel = new EstadisticaPanel(negocio);

        tabbedPane.addTab("Registro de productos", productoPanel);
        tabbedPane.addTab("Lista de productos", listaPanel);
        tabbedPane.addTab("Estadísticas", estadisticaPanel);

        return tabbedPane;
    }
    
    private void nuevoProducto() {
        productoPanel.limpiarCampos();
        tabbedPane.setSelectedIndex(0);
    }

    /**
     * Llamado desde ListaPanel cuando el usuario elige "Editar seleccionado".
     */
    public void cargarProductoEnFormulario(Producto producto) {
        productoPanel.cargarProductoParaEdicion(producto);
        tabbedPane.setSelectedIndex(0);
    }

    /**
     * Refresca la lista y las estadísticas después de agregar/editar/eliminar.
     */
    public void refrescarTodo() {
        listaPanel.refrescar();
        estadisticaPanel.refrescar();
    }

    private void exportarInventario() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar inventario");
        fileChooser.setSelectedFile(new File("inventario.csv"));

        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File archivo = fileChooser.getSelectedFile();
        try {
            ArchivoUtil.exportarInventario(archivo, negocio.listar());
            negocio.registrarAccion("Inventario exportado correctamente");
            JOptionPane.showMessageDialog(this,
                    "Inventario exportado correctamente en:\n" + archivo.getAbsolutePath(),
                    "Exportación exitosa", JOptionPane.INFORMATION_MESSAGE);
        } catch (ArchivoException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al exportar el inventario:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarHistorial() {
        Stack<String> historial = negocio.obtenerHistorial();
        StringBuilder sb = new StringBuilder();
        if (historial.isEmpty()) {
            sb.append("Aún no hay acciones registradas.");
        } else {
            // Se recorre desde el tope (última acción) hacia el fondo.
            for (int i = historial.size() - 1; i >= 0; i--) {
                sb.append("• ").append(historial.get(i)).append("\n");
            }
        }
        JTextArea area = new JTextArea(sb.toString(), 15, 40);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area),
                "Historial de acciones", JOptionPane.PLAIN_MESSAGE);
    }

    private void mostrarAcercaDe() {
        JOptionPane.showMessageDialog(this,
                "Sistema de Control de Inventario\n" +
                        "Proyecto de Reposición - Programación IV\n" +
                        "Universidad Internacional San Isidro Labrador\n" +
                        "Desarrollado en Java Swing",
                "Acerca del sistema", JOptionPane.INFORMATION_MESSAGE);
    }
}