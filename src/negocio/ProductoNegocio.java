/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

/**
 *
 * @author HP
 */
import excepciones.DatoInvalidoException;
import excepciones.ProductoDuplicadoException;
import modelo.Producto;
import repositorio.ProductoRepositorio;
import java.util.List;
import java.util.Stack;
import java.util.Map;

/**
 * Capa de Negocio.
 * Contiene las validaciones, reglas del sistema, ordenamientos,
 * cálculo de estadísticas y manejo/lanzamiento de excepciones.
 * La capa de presentación solo debe llamar a estos métodos.
 */
public class ProductoNegocio {

    private final ProductoRepositorio repositorio;

    public ProductoNegocio(ProductoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Aplica todas las reglas de validación obligatorias del enunciado.
     * Lanza DatoInvalidoException con un mensaje claro si algo falla.
     */
    private void validarDatos(Producto producto) throws DatoInvalidoException {
        if (producto.getCodigo() == null || producto.getCodigo().isBlank()) {
            throw new DatoInvalidoException("El código es obligatorio.");
        }
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new DatoInvalidoException("El nombre es obligatorio.");
        }
        if (producto.getNombre().trim().length() < 3) {
            throw new DatoInvalidoException("El nombre debe tener mínimo tres caracteres.");
        }
        if (producto.getCategoria() == null || producto.getCategoria().isBlank()) {
            throw new DatoInvalidoException("La categoría es obligatoria.");
        }
        if (producto.getCantidad() < 0) {
            throw new DatoInvalidoException("La cantidad debe ser mayor o igual que cero.");
        }
        if (producto.getPrecio() <= 0) {
            throw new DatoInvalidoException("El precio debe ser mayor que cero.");
        }
    }
    
    /**
     * Valida y registra un nuevo producto.
     */
    public Producto agregar(Producto producto) throws DatoInvalidoException, ProductoDuplicadoException {
        validarDatos(producto);
        if (repositorio.existeCodigo(producto.getCodigo())) {
            throw new ProductoDuplicadoException(
                    "Ya existe un producto con el código: " + producto.getCodigo());
        }
        return repositorio.agregar(producto);
    }

    /**
     * Valida y actualiza un producto existente.
     */
    public void editar(Producto producto) throws DatoInvalidoException, ProductoDuplicadoException {
        validarDatos(producto);

        Producto conMismoCodigo = repositorio.buscarPorCodigo(producto.getCodigo());
        if (conMismoCodigo != null && conMismoCodigo.getId() != producto.getId()) {
            throw new ProductoDuplicadoException(
                    "Ya existe otro producto con el código: " + producto.getCodigo());
        }

        boolean actualizado = repositorio.editar(producto);
        if (!actualizado) {
            throw new DatoInvalidoException("No se encontró el producto a editar (ID: " + producto.getId() + ")");
        }
    }
    
    public boolean eliminar(int id) {
        return repositorio.eliminar(id);
    }

    public List<Producto> listar() {
        return repositorio.listar();
    }

    public Producto buscarPorCodigo(String codigo) {
        return repositorio.buscarPorCodigo(codigo);
    }

    public List<Producto> buscarPorNombreOCodigo(String texto) {
        String filtro = texto == null ? "" : texto.trim().toLowerCase();
        return repositorio.listar().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(filtro)
                        || p.getCodigo().toLowerCase().contains(filtro))
                .toList();
    }

    public List<Producto> filtrarPorCategoria(String categoria) {
        if (categoria == null || categoria.isBlank() || categoria.equalsIgnoreCase("Todas")) {
            return repositorio.listar();
        }
        return repositorio.listar().stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .toList();
    }
    
    public List<Producto> ordenarPorNombre(List<Producto> lista) {
        repositorio.ordenarPorNombre(lista);
        return lista;
    }

    public List<Producto> ordenarPorPrecio(List<Producto> lista) {
        repositorio.ordenarPorPrecio(lista);
        return lista;
    }

    public List<Producto> ordenarPorCantidad(List<Producto> lista) {
        repositorio.ordenarPorCantidad(lista);
        return lista;
    }

    public Stack<String> obtenerHistorial() {
        return repositorio.obtenerHistorial();
    }

    public void registrarAccion(String descripcion) {
        repositorio.registrarAccion(descripcion);
    }
    
    /**
     * Calcula todas las estadísticas requeridas por el enunciado.
     */
    public Estadisticas calcularEstadisticas() {
        List<Producto> todos = repositorio.listar();

        int totalProductos = todos.size();
        int disponibles = 0;
        int noDisponibles = 0;
        int unidadesTotales = 0;
        double valorTotalInventario = 0.0;
        Producto masCaro = null;
        Producto masBarato = null;

        for (Producto p : todos) {
            if (p.isDisponible()) {
                disponibles++;
            } else {
                noDisponibles++;
            }
            unidadesTotales += p.getCantidad();
            valorTotalInventario += p.getCantidad() * p.getPrecio();

            if (masCaro == null || p.getPrecio() > masCaro.getPrecio()) {
                masCaro = p;
            }
            if (masBarato == null || p.getPrecio() < masBarato.getPrecio()) {
                masBarato = p;
            }
        }

        Map<String, Integer> porCategoria = repositorio.obtenerConteoPorCategoria();

        return new Estadisticas(totalProductos, disponibles, noDisponibles,
                unidadesTotales, valorTotalInventario, masCaro, masBarato, porCategoria);
    }
    
    /**
     * Valida y convierte el texto de cantidad ingresado en el formulario.
     * Lanza DatoInvalidoException (con la excepción original encadenada) si no es numérico.
     */
    public int parsearCantidad(String texto) throws DatoInvalidoException {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new DatoInvalidoException("La cantidad debe ser un número entero válido.", e);
        }
    }

    /**
     * Valida y convierte el texto de precio ingresado en el formulario.
     * Lanza DatoInvalidoException (con la excepción original encadenada) si no es numérico.
     */
    public double parsearPrecio(String texto) throws DatoInvalidoException {
        try {
            return Double.parseDouble(texto.trim());
        } catch (NumberFormatException e) {
            throw new DatoInvalidoException("El precio debe ser un número válido.", e);
        }
    }
    
    /**
     * Objeto simple para transportar las estadísticas calculadas hacia la vista.
     */
    public static class Estadisticas {
        public final int totalProductos;
        public final int disponibles;
        public final int noDisponibles;
        public final int unidadesTotales;
        public final double valorTotalInventario;
        public final Producto masCaro;
        public final Producto masBarato;
        public final Map<String, Integer> porCategoria;

        public Estadisticas(int totalProductos, int disponibles, int noDisponibles,
                             int unidadesTotales, double valorTotalInventario,
                             Producto masCaro, Producto masBarato, Map<String, Integer> porCategoria) {
            this.totalProductos = totalProductos;
            this.disponibles = disponibles;
            this.noDisponibles = noDisponibles;
            this.unidadesTotales = unidadesTotales;
            this.valorTotalInventario = valorTotalInventario;
            this.masCaro = masCaro;
            this.masBarato = masBarato;
            this.porCategoria = porCategoria;
        }
    }

}