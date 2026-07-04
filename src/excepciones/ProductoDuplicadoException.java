/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 *
 * @author HP
 */
/**
 * Se lanza cuando se intenta registrar un producto con un código
 * que ya existe en el inventario.
 */
public class ProductoDuplicadoException extends Exception {

    public ProductoDuplicadoException(String mensaje) {
        super(mensaje);
    }

    public ProductoDuplicadoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}