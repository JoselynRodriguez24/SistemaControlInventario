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
 * Se lanza cuando ocurre un error al leer o escribir un archivo
 * (por ejemplo, al exportar el inventario).
 * Normalmente envuelve una excepción de más bajo nivel (IOException),
 * por lo que se usa como ejemplo de encadenamiento de excepciones.
 */
public class ArchivoException extends Exception {

    public ArchivoException(String mensaje) {
        super(mensaje);
    }

    public ArchivoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
