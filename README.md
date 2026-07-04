# Sistema de Control de Inventario

**Proyecto de Reposición — Programación IV**
Universidad Internacional San Isidro Labrador
Profesor: José Andrés Jiménez Zamora

**Estudiante:** Joselyn Tatiana Rodríguez Bonilla

## Descripción del sistema

Aplicación de escritorio en Java Swing que permite administrar un inventario
básico de productos: registrar, editar, eliminar, buscar, filtrar, ordenar y
visualizar productos, además de exportar el inventario a un archivo `.csv`.

El sistema está organizado en capas (modelo, repositorio, negocio,
excepciones, presentación y util), usa Collections y genéricos, maneja
excepciones personalizadas, y fue desarrollado con Git.

## Requisitos para ejecutarlo

- JDK 17 o superior.
- NetBeans (o cualquier IDE compatible con proyectos Java Swing).
- Librería FlatLaf (`flatlaf-3.7.1.jar`) agregada a las Libraries del proyecto.

### Ejecución

1. Abrir el proyecto en NetBeans (`File > Open Project`).
2. Ejecutar `Main.java` (`Shift+F6`).

## Componentes Swing utilizados

- JFrame, JMenuBar, JMenu, JMenuItem
- JToolBar
- JTabbedPane
- JTextField, JComboBox, JCheckBox, JRadioButton + ButtonGroup, JTextArea
- JTable + DefaultTableModel, con renderer personalizado para colorear filas
- JFileChooser (exportar inventario)
- JOptionPane (errores, confirmaciones, información)
- JScrollPane

## Collections utilizadas

- `List<Producto>` (ArrayList) — almacena los productos registrados.
- `Set<String>` (HashSet) — controla los códigos para evitar duplicados.
- `Map<String, Integer>` (HashMap) — cuenta productos por categoría.
- `Stack<String>` — historial de acciones del sistema.

Ordenamiento con `Comparator<Producto>` (por nombre, precio y cantidad).

## Excepciones personalizadas

- `DatoInvalidoException`
- `ProductoDuplicadoException`
- `ArchivoException` (con encadenamiento de la excepción original, ej. IOException)

## Instrucciones de uso

1. Registrar un producto desde la pestaña "Registro de productos".
2. Ver, buscar, filtrar, ordenar, editar o eliminar productos desde "Lista de productos".
3. Ver estadísticas en la pestaña "Estadísticas".
4. Exportar el inventario desde el menú Archivo o el botón "Exportar" del toolbar.
5. Ver historial de acciones desde el menú Herramientas.

## Capturas de la aplicación

Ver carpeta `documentacion/capturas/`.
