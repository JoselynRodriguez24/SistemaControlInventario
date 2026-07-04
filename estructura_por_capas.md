# Explicación de la estructura por capas

El proyecto **Sistema de Control de Inventario** está organizado en capas,
donde cada paquete tiene una única responsabilidad.

## 1. Modelo (`modelo`)
Contiene la clase `Producto`, que representa los datos de un producto:
atributos privados, constructor, getters/setters y `toString()`. No tiene
ninguna lógica de negocio.

## 2. Repositorio (`repositorio`)
`ProductoRepositorio` es la única capa que maneja directamente las
Collections: `List<Producto>` para almacenar los productos, `Set<String>`
para evitar códigos duplicados, `Map<String, Integer>` para contar productos
por categoría, y `Stack<String>` para el historial de acciones. También
implementa el ordenamiento con `Comparator<Producto>`.

## 3. Negocio (`negocio`)
`ProductoNegocio` contiene todas las validaciones obligatorias (código y
nombre requeridos, nombre mínimo 3 caracteres, cantidad ≥ 0, precio > 0,
código no duplicado), el cálculo de estadísticas, y el lanzamiento de las
excepciones personalizadas. La capa de presentación nunca valida datos por
sí misma, siempre llama a estos métodos.

## 4. Excepciones (`excepciones`)
Tres excepciones personalizadas (`DatoInvalidoException`,
`ProductoDuplicadoException`, `ArchivoException`), todas heredando de
`Exception` para forzar su manejo con try/catch. `ArchivoException` además
encadena la excepción original (por ejemplo, un `IOException`).

## 5. Presentación (`presentacion`)
Las 4 clases de la interfaz Swing (`MainFrame`, `ProductoPanel`,
`ListaPanel`, `EstadisticaPanel`). Solo capturan datos del usuario, los
muestran, y llaman a los métodos de `ProductoNegocio` — nunca validan ni
acceden directamente a las Collections.

## 6. Util (`util`)
`ArchivoUtil` exporta el inventario a un archivo `.csv`, usado desde
`MainFrame` junto con `JFileChooser`.

## Flujo de ejemplo: registrar un producto

```
ProductoPanel (captura los datos del formulario)
      ↓
ProductoNegocio.agregar() (valida; puede lanzar excepciones)
      ↓
ProductoRepositorio.agregar() (actualiza List, Set, Map y Stack)
```
