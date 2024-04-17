import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.math.sqrt

fun predecirClaseKNN(punto: DataPoint, puntosDeDatos: List<DataPoint>, k: Int, tipoDistancia: Int): String {

    val datosConDistancia = when (tipoDistancia) {
        1 -> puntosDeDatos.map { it to distanciaEuclidiana(punto, it) } //Lista de Pares, DataPoint y Distancia
        2 -> puntosDeDatos.map { it to distanciaManhattan(punto, it) } //Lista de Pares, DataPoint y Distancia
        else -> {puntosDeDatos.map { it to distanciaEuclidiana(punto, it) }}
    }

    val datosOrdenados = datosConDistancia.sortedBy { it.second } //Ordena la Lista segun la Distancia
    val kVecinos = datosOrdenados.take(k) //Toma los k vecinos
    val clasesKVecinos = kVecinos.map { it.first.clase } //Extrae las clases de k vecinos

    return clasesKVecinos.groupingBy { it }
        .eachCount()
        .maxBy { it.value }.key
}

fun distanciaEuclidiana(p1: DataPoint, p2: DataPoint): Double {
    val dx_imu = p1.x_imu - p2.x_imu
    val dy_imu = p1.y_imu - p2.y_imu
    val dy_fr = p1.y_imu - p2.y_imu
    val dz_fr = p1.y_imu - p2.y_imu
    return sqrt((dx_imu * dx_imu) + (dy_imu * dy_imu) + (dy_fr * dy_fr) + (dz_fr * dz_fr))
}



fun seleccionarArchivo(tituloVentana: String, archivoDefault: String): String {
    val fileChooser = JFileChooser()
    fileChooser.dialogTitle = tituloVentana
    val desktop = javax.swing.filechooser.FileSystemView.getFileSystemView().homeDirectory
    fileChooser.currentDirectory = desktop

    // Filtrar solo archivos .txt
    val filter = FileNameExtensionFilter("Archivos separados con coma", "csv")
    fileChooser.fileFilter = filter

    val result = fileChooser.showOpenDialog(null)
    if (result == JFileChooser.APPROVE_OPTION) {
        val selectedFilePath = fileChooser.selectedFile.absolutePath
        // Aquí puedes manejar la ruta del archivo seleccionado
        return selectedFilePath.toString()
    }
    return archivoDefault
}

fun leerDataSetID(direccionArchivo: String): String {
    return File(direccionArchivo).readLines().get(0)
}