import java.io.File
import java.lang.Math.abs

fun distanciaManhattan(p1: DataPoint, p2: DataPoint): Double {
    val dx_imu = p1.x_imu - p2.x_imu
    val dy_imu = p1.y_imu - p2.y_imu
    val dy_fr = p1.y_imu - p2.y_imu
    val dz_fr = p1.y_imu - p2.y_imu
    return abs((dx_imu * dx_imu) + (dy_imu * dy_imu) + (dy_fr * dy_fr) + (dz_fr * dz_fr))
}


fun crearArchivoDatosProcesados(tabla: List<String>) {

    File("resources/datosProcesados.csv").bufferedWriter().use { output ->
        tabla.forEach { linea ->
            output.write(linea)
            output.newLine()
        }
    }

    println("\nArchivo datosProcesados.csv creado en la carpeta resources del proyecto")
}

fun crearArchivoMatrizAdyacencia(matrizAdyacencia: Array<Array<Int>>) {
    val listaAdyacencia = matrizAdyacencia.map { it.joinToString(",") }
    File("resources/matrizAdyacencia.csv").bufferedWriter().use { output ->
        listaAdyacencia.forEach { linea ->
            output.write(linea)
            output.newLine()
        }
    }

    println("\nArchivo matrizAdyacencia.csv creado en la carpeta resources del proyecto")
}

fun imprimirDatos(i: Int, datosPrueba: List<DataPoint>, datosEntrenamiento: List<DataPoint>, k: Int, metodo: Int): String {
    val prediccion = predecirClaseKNN(datosPrueba.get(i), datosEntrenamiento, k, metodo)
    val realidad = datosPrueba.get(i).clase
    val id = i+1

    //Puro texto
    val texto = "\nDato $id" +
            " - Prediccion: clase $prediccion" +
            ", Realidad: clase $realidad"


    //Tabla
    val cabecera = listOf("ID","Prediccion","Realidad","0","1","2","3","4")
    val evals = generarEvaluaciones(prediccion, realidad)
    val dato = listOf("$id",prediccion, realidad,
        evals.get(0),
        evals.get(1),
        evals.get(2),
        evals.get(3),
        evals.get(4),)

    val tabla = "\n${cabecera.joinToString(" | ", prefix = "| ", postfix = " |") { it.padEnd(10) }}" +
            "\n${dato.joinToString(" | ", prefix = "| ", postfix = " |") { it.padEnd(10) }}"



    return texto + tabla
}

fun generarFilaTabla(i: Int, datosPrueba: List<DataPoint>, datosEntrenamiento: List<DataPoint>, k: Int, metodo: Int): String {
    val prediccion = predecirClaseKNN(datosPrueba.get(i), datosEntrenamiento, k, metodo)
    val realidad = datosPrueba.get(i).clase
    val id = i+1

    //Tabla

    val evals = generarEvaluaciones(prediccion, realidad)
    val dato = listOf("$id",prediccion, realidad,
        evals.get(0),
        evals.get(1),
        evals.get(2),
        evals.get(3),
        evals.get(4),)

    val fila = dato.joinToString(",")

    return fila
}

fun generarEvaluaciones(prediccion: String, realidad: String): List<String> {
    val clases = mutableListOf("0","1`","2","3","4")

    clases.set(0, evaluarClase("0", prediccion, realidad))
    clases.set(1, evaluarClase("1", prediccion, realidad))
    clases.set(2, evaluarClase("2", prediccion, realidad))
    clases.set(3, evaluarClase("3", prediccion, realidad))
    clases.set(4, evaluarClase("4", prediccion, realidad))

    return clases
}

fun evaluarClase(clase: String, prediccion: String, realidad: String): String {
    if (prediccion == clase) {
        if (realidad == clase) {
            return "TP"
        } else { //realidad != clase
            return "FP"
        }
    } else { //prediccion != clase
        if (realidad == clase) {
            return "FN"
        } else { //realidad != clase
            return "TN"
        }
    }
}

fun generarMatrizAdyacencia(tabla: List<String>): Array<Array<Int>> {
    val valores = tabla.map { it.split(",") } //Convertirlo a lista normal
        .map { it.subList(1,3) } //Tomar solo la prediccion y la realidad
        .map { it.map { numero -> numero.toInt() } } //Convertirlo de String a Int
    //Prediccion, Realidad

    val matrizAdyacencia = Array(5) { Array(5) {0} }

    for (eval in valores) {
        matrizAdyacencia[eval.get(1)][eval.get(0)] += 1
    }

    return matrizAdyacencia
}

fun contarEvaluaciones(tabla: List<String>): List<Int> {
    val valores = tabla.map { it.split(",") } //Convertirlo a lista normal
        .map { it.subList(3,8) } //Tomar solo la prediccion y la realidad
    //Prediccion, Realidad

    val tp = valores.flatten().count { it == "TP" }
    val fp = valores.flatten().count { it == "FP" }
    val fn = valores.flatten().count { it == "FN" }
    val tn = valores.flatten().count { it == "TN" }

    return listOf(tp,fp,fn,tn)
}