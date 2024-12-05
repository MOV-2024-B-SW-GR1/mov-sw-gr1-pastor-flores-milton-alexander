package org.example

import java.util.*

fun main() {
    println("Hello World!")

    //---- Duck Typing + Variables------------
    val ejemploVariable = "Milton Pástor"
    ejemploVariable.trim()

    val edadEjemplo: Int = 21

    val nombreProfesor: String = "Adrian Eguez"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'C'
    val mayorEdad: Boolean = true

    val fechaNacimiento: Date = Date()

    //---- WHEN ----------
    val estadoCivilWhen = "C"

    when (estadoCivilWhen) {
        ("C") -> {
            println("Casado")
        }

        "S" -> {
            println("Soltero")
        }

        else -> {
            println("No sabemos")
        }
    }

    // -------- if - else ------------

    val esSoltero = (estadoCivilWhen == "S")
    val coqueto = if (esSoltero) "Si" else "No" // if else


    // -------------- LLAMADA DE FUNCIONES ---------------
    // ----------------------------------------------------//

    imprimirNombre(ejemploVariable)

    calcularSueldo(10.00) //solo parametro requerido
    calcularSueldo(10.00, 15.00, 20.00) //parametro requerido y sobreescribir parametros opcionales

    //Named parameters
    calcularSueldo(10.00, bonoEspecial = 20.00) // usando el parametro bonoEspecial en la segunda posicion
    calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 14.00)

    // usando el parametro bonoEspecial en primera posicion
    // usando el parametro sueldo en la segunda posicion
    // usando el parametro tasa es tercera posicion
    // gracias a los parametros nombrados


    // -------------------CLASES USO-----------------------
    // ---------------------------------------------------//

    //4 instancias usando todos los constructores
    val sumaA = Suma(1,1)
    val sumaB = Suma(null,1)
    val sumaC = Suma(1,null)
    val sumaD = Suma(null,null)

    //Usamos la función sumar dentro de cada instancia
    sumaA.sumar()
    sumaB.sumar()
    sumaC.sumar()
    sumaD.sumar()

    //Uso de component object como static
    println(Suma.pi)
    println(Suma.elevarAlCuadrado(2))
    println(Suma.historialSumas)


   //-------------------ARREGLOS------------------------------//
    // ---------------------------------------------------------//

   //Estaticos
    val arregloEstatio: Array<Int> = arrayOf<Int>(1,2,3)
    println(arregloEstatio)

    //Dianmicos
    val arregloDinamico: ArrayList<Int> = arrayListOf<Int>(
        1,2,3,4,5,6,7,8,9,10
    )

    println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico)


    // FOR EACH = > Unit
    // Iterar un arreglo
    val respuestaForEach: Unit = arregloDinamico
        .forEach { valorActual: Int -> //  - > = >
            println("Valor actual: ${valorActual}");
        }


    // "it" (en ingles "eso") significa el elemento iterado
    arregloDinamico.forEach{ println("Valor Actual (it): ${it}")}

    // MAP -> MUTA(Modifica cambia) el arreglo
    // 1) Enviamos el nuevo valor de la iteracion
    // 2) Nos devuelve un NUEVO ARREGLO con valores de las iteraciones
    val respuestaMap: List<Double> = arregloDinamico
        .map { valorActual: Int ->
            return@map valorActual.toDouble() + 100.00
        }
    println(respuestaMap)


    val respuestaMapDos = arregloDinamico.map{ it + 15 }
    println(respuestaMapDos)

    // Filter - > Filtrar el ARREGLO
    // 1) Devolver una expresion (TRUE o FALSE)
    // 2) Nuevo arreglo FILTRADO
    val respuestaFilter: List<Int> = arregloDinamico
        .filter{ valorActual:Int ->
            // Expresion o CONDICION
            val mayoresACinco: Boolean = valorActual > 5
            return@filter mayoresACinco
        }

    val respuestaFilterDos =arregloDinamico.filter{ it <=5 }
    println(respuestaFilter)
    println(respuestaFilterDos)


    // OR AND
    // OR -> ANY (Alguno Cumple?)
    // And -> ALL (Todos cumplen?)
    val respuestaAny: Boolean = arregloDinamico
        .any{ valorActual:Int ->
            return@any (valorActual > 5)
        }
    println(respuestaAny) // True
    val respuestaAll: Boolean = arregloDinamico
        .all{ valorActual:Int ->
            return@all (valorActual > 5)
        }
    println(respuestaAll) // False

    //---------------------------------------------------------------

}


//-------------FUNCIONES------------------------
//-----------------------------------------//

fun imprimirNombre(nombre:String):Unit{
    fun otraFuncionAdentro(){
        print("Otra funcion adentro")
    }

    println("Nombre: $nombre") //Template Strings
    println("Nombre: ${nombre}") //Template Strings
    println("Nombre: ${nombre + nombre}") //Uso con llaves (concatenado)
    println("Nombre: ${nombre.uppercase()}") //Uso con llaves (funcion)
    println("Nombre: $nombre.uppercase()") //INCORRECTO!
                                            //No puedo usar sin llaves
}

fun calcularSueldo(
    sueldo: Double, //Requerido
    tasa: Double = 12.00, //Opcional (defecto)
    bonoEspecial: Double? = null //Opcional (nullable)
    // Variable? - "?" Es Nullable (quiere decir que algun momento puede se nulo)
): Double{
    // Into -> Int? (nullable)
    // String -> String? (nullable)
    // Date -> Date? (nullable)
    return if(bonoEspecial == null){
        sueldo * (100/tasa)
    }else{
        sueldo * (100/tasa) * bonoEspecial
    }
}



//------------ CLASES --------------

//Clase normal de java
abstract class NumerosJava{
    protected val numeroUno: Int
    private val numeroDos: Int

    constructor(
        uno:Int,
        dos:Int
    ){
        this.numeroUno = uno
        this.numeroDos = dos
    }
}


//-------Kotlin Classes


//Clase Padre
abstract class Numeros( //Constructor Primario
    //Caso 1) Parametro normal
    //uno:Int, (parametro (sin modificador acceso))

    //Caso 2) Parámetro y propiedad (atributo) (protected)
    // private var uno: Int (propiedad "instancia.uno")
    protected val numeroUno: Int,
    protected val numeroDos: Int,
    parámetroNoUsadoNoPropiedadDeLaClase: Int? = null
){
    init {
        this.numeroUno
        this.numeroDos
        println("Inicializando")
    }
}

//Clase Hijo
class Suma( //Constructor Primario
    unoParametro: Int,
    dosParametro: Int,
): Numeros( //Clase padre, Numeros (extendiendo)     ---> Pasamos los atributos de Suma al padre Números
    unoParametro,
    dosParametro
){
    //Modificadores de Acceso
    public val soyPublicoExplicito: String = "Publicas"
    val soyPublicoImplicito: String = "Publico implicito"

    init{ //Bloque constructor primario
        this.numeroUno //Heredamos del Padre
        this.numeroDos
        numeroUno //this. OPCIONAL (propiedades, metodos)
        numeroDos //this. OPCIONAL (propiedades, metodos)
        this.soyPublicoExplicito
        soyPublicoImplicito
    }

    //-----------Constructores Secundarios
    constructor(
        uno: Int?, //Entero nullable
        dos: Int,
    ):this(
        if(uno == null) 0 else uno,
        dos
    ){
        //OPCIONAL
        //Bloque de código de constructor secundario
    }

    constructor(
        uno: Int,
        dos: Int?, //Entero nullable
    ):this(
        uno,
        if(dos==null) 0 else dos,
    )

    constructor(
        uno: Int?,//Entero nullable
        dos: Int?,//Entero nullable
    ):this(
        if(uno==null) 0 else uno,
        if(dos==null) 0 else dos
    )


    fun sumar():Int{
        val total = numeroUno + numeroDos
        agregarHistorial(total)
        return total
    }


    companion object{ //Comparte entre todas las instancias, similar al STATIC
        //funciones, variables
        //NombreClase.metodo, NombreClase.funcion =>
        //Suma.pi
        val pi = 3.14
        //Suma.elevarAlCuadrado
        fun elevarAlCuadrado(num:Int):Int{ return num*num}
        val historialSumas = arrayListOf<Int>()

        fun agregarHistorial(valorTotalSuma:Int){
            historialSumas.add(valorTotalSuma)
        }
    }
}















