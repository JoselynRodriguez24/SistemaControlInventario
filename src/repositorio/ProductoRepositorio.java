/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositorio;

/**
 *
 * @author HP
 */
import modelo.Producto;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Capa Repositorio.
 * Administra las Collections y las operaciones de almacenamiento en memoria.
 * No contiene validaciones de negocio: solo acceso a datos.
 */
public class ProductoRepositorio {

    // Almacena todos los productos registrados.
    private final List<Producto> productos = new ArrayList<>();

    // Controla los códigos de producto existentes para evitar duplicados.
    private final Set<String> codigosRegistrados = new HashSet<>();

    // Contabiliza la cantidad de productos por categoría.
    private final Map<String, Integer> conteoPorCategoria = new HashMap<>();

    // Guarda el historial de acciones realizadas (última acción arriba).
    private final Stack<String> historialAcciones = new Stack<>();

    // Contador para asignar IDs automáticos a los productos nuevos.
    private int siguienteId = 1;
    
    public Producto agregar(Producto producto) {
        producto.setId(siguienteId++);
        productos.add(producto);
        codigosRegistrados.add(producto.getCodigo());
        incrementarCategoria(producto.getCategoria());
        registrarAccion("Producto registrado: " + producto.getNombre());
        return producto;
    }

    private void incrementarCategoria(String categoria) {
        conteoPorCategoria.merge(categoria, 1, Integer::sum);
    }

    public void registrarAccion(String descripcion) {
        historialAcciones.push(descripcion);
    }
    
    public List<Producto> listar() {
        return new ArrayList<>(productos);
    }

    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    public Producto buscarPorId(int id) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    
    public boolean editar(Producto productoEditado) {
        Producto existente = buscarPorId(productoEditado.getId());
        if (existente == null) {
            return false;
        }

        // Si el código cambió, actualizamos el Set de códigos.
        if (!existente.getCodigo().equalsIgnoreCase(productoEditado.getCodigo())) {
            codigosRegistrados.remove(existente.getCodigo());
            codigosRegistrados.add(productoEditado.getCodigo());
        }

        // Si la categoría cambió, actualizamos el Map de conteo por categoría.
        if (!existente.getCategoria().equalsIgnoreCase(productoEditado.getCategoria())) {
            decrementarCategoria(existente.getCategoria());
            incrementarCategoria(productoEditado.getCategoria());
        }

        existente.setCodigo(productoEditado.getCodigo());
        existente.setNombre(productoEditado.getNombre());
        existente.setCategoria(productoEditado.getCategoria());
        existente.setCantidad(productoEditado.getCantidad());
        existente.setPrecio(productoEditado.getPrecio());
        existente.setDisponible(productoEditado.isDisponible());
        existente.setDescripcion(productoEditado.getDescripcion());

        registrarAccion("Producto editado: " + existente.getNombre());
        return true;
    }

    public boolean eliminar(int id) {
        Producto existente = buscarPorId(id);
        if (existente == null) {
            return false;
        }
        productos.remove(existente);
        codigosRegistrados.remove(existente.getCodigo());
        decrementarCategoria(existente.getCategoria());
        registrarAccion("Producto eliminado: " + existente.getNombre());
        return true;
    }

    private void decrementarCategoria(String categoria) {
        conteoPorCategoria.computeIfPresent(categoria, (k, v) -> (v > 1) ? v - 1 : null);
    }
    
    public boolean existeCodigo(String codigo) {
        return codigosRegistrados.contains(codigo);
    }

    public Map<String, Integer> obtenerConteoPorCategoria() {
        return new HashMap<>(conteoPorCategoria);
    }

    public Stack<String> obtenerHistorial() {
        return historialAcciones;
    }

    public void ordenarPorNombre(List<Producto> lista) {
        lista.sort(Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER));
    }

    public void ordenarPorPrecio(List<Producto> lista) {
        lista.sort(Comparator.comparingDouble(Producto::getPrecio));
    }

    public void ordenarPorCantidad(List<Producto> lista) {
        lista.sort(Comparator.comparingInt(Producto::getCantidad));
    }
}