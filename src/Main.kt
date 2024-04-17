//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    //Preparacion de banderas, lectura y procesamiento
    var leerTodos = false
    var salir = false
    var opcion: String
    val tabla = mutableListOf(String())
    tabla.add(listOf("ID","Prediccion","Realidad","0","1","2","3","4")
        .joinToString(","))
    tabla.removeAt(0)


    //Pedir datos
    val archivoEntrenamiento = seleccionarArchivo("Seleccionar datos de entrenamiento",
        "resources/Datos_sujeto1_70.csv")
    println("Archivo '$archivoEntrenamiento' con ID: ${leerDataSetID(archivoEntrenamiento)} seleccionado como entrenamiendo")
    val archivoPrueba = seleccionarArchivo("Seleccionar datos de prueba",
        "resources/Datos_sujeto1_30.csv")
    println("Archivo '$archivoEntrenamiento' con ID: ${leerDataSetID(archivoEntrenamiento)} seleccionado como prueba")

    val datosEntrenamiento = leerDataSet(archivoEntrenamiento).shuffled()
    val datosPrueba = leerDataSet(archivoPrueba).shuffled()

    print("Valor de K:\n \t -> ")
    val k = Integer.valueOf(readlnOrNull())

    print("Método de distancias: \t 1.EUCLIDIANA \t 2.MANHATTAN\n \t -> ")
    val metodo = Integer.valueOf(readlnOrNull())



    //Imprimir en pantalla datos procesados
    for (i in datosPrueba.indices) {
        tabla.add(generarFilaTabla(i, datosPrueba, datosEntrenamiento, k, metodo))
        println("\n${imprimirDatos(i, datosPrueba, datosEntrenamiento, k, metodo)}")

        if (leerTodos == false) {

            print("[C]ontinuar con el siguiente dato \t [T]erminar de procesar datos \t [S]alir\n \t -> ")
            opcion = readLine().toString()
            when (opcion.uppercase()) {
                "C" -> println("")
                "T" -> leerTodos = true
                "S" -> salir = true
                else -> println("\nIngresa un valor valido")
            }
        }
        if (salir) break
    }

    crearArchivoDatosProcesados(tabla)
    tabla.removeAt(0)

    val matrizAdyacencia = generarMatrizAdyacencia(tabla)
    crearArchivoMatrizAdyacencia(matrizAdyacencia)

    println("\nMatriz de Adyacencia: ")
    // Imprime la matriz en forma de tabla
    for (fila in matrizAdyacencia) {
        println(fila.joinToString(" | ", prefix = "| ", postfix = " |"))
    }

    // 0=TP, 1=FP, 2=FN, 3=TN
    val TP = contarEvaluaciones(tabla).get(0).toFloat()
    val FP = contarEvaluaciones(tabla).get(1).toFloat()
    val FN = contarEvaluaciones(tabla).get(2).toFloat()
    val TN = contarEvaluaciones(tabla).get(3).toFloat()

    val presicion = (TP/(TP+FP))
    val recall = (TP/(TP+FN))
    println("\nPrecision: $presicion")
    println("Recall: $recall")
    println("Accuracy: ${(TP+TN)/(TP+TN+FP+FN)}")
    println("F-Score: ${2*(presicion*recall)/(presicion+recall)}")
    println("\nTP=${TP.toInt()} FP=${FP.toInt()} FN=${FN.toInt()} TN=${TN.toInt()} ")


}