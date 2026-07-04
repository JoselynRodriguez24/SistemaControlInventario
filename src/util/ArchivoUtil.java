/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author HP
 */
import excepciones.ArchivoException;
import modelo.Producto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Capa Util.
 * Utilidades de manejo de archivos, usadas por la capa de presentación
 * a través de JFileChooser para exportar el inventario.
 */
public class ArchivoUtil {

    private static final String ENCABEZADO = "ID,Código,Nombre,Categoría,Cantidad,Precio,Disponible";

    /**
     * Exporta la lista de productos a un archivo .csv o .txt en la ruta indicada.
     * Si ocurre un error de E/S, se envuelve en un ArchivoException (encadenamiento).
     */
    public static void exportarInventario(File archivo, List<Producto> productos) throws ArchivoException {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(archivo))) {
            escritor.println(ENCABEZADO);
            for (Producto p : productos) {
                escritor.println(
                        p.getId() + "," +
                        p.getCodigo() + "," +
                        p.getNombre() + "," +
                        p.getCategoria() + "," +
                        p.getCantidad() + "," +
                        p.getPrecio() + "," +
                        p.isDisponible()
                );
            }
        } catch (IOException e) {
            throw new ArchivoException(
                    "No se pudo exportar el inventario al archivo: " + archivo.getName(), e);
        }
    }
}