import java.io.File

fun leerDataSet(direccionArchivo: String): (List<DataPoint>) {
    val archivo = File(direccionArchivo)
    val datos = mutableListOf<DataPoint>()

    archivo.readLines().forEachIndexed { i, linea ->
        if (i > 1) { // Skip the first row (header)
            val datosI = linea.split(",")
            val x_imu = datosI.get(0).toDouble()
            val y_imu = datosI.get(1).toDouble()
            val y_fr = datosI.get(2).toDouble()
            val z_fr = datosI.get(3).toDouble()
            val clase = datosI.get(4)
            datos.add(DataPoint(x_imu,y_imu,y_fr,z_fr,clase))
        }
    }

    return datos.toList()
}