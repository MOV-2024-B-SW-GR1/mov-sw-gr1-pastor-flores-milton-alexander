package com.example.sw2024bgr1_mapf

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class ACicloVida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_aciclo_vida)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_ciclo_vida)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.d("MiApp", "Aqui si vale")

        mostrarSnackbar("onCreate")

    }

    override fun onStart(){
        super.onStart()
        Log.d("MiApp", "Aqui no se ")
        mostrarSnackbar("OnStart")
    }

    override fun onResume(){
        super.onResume()
        mostrarSnackbar("OnResume")
    }

    override fun onRestart(){
        super.onRestart()
        mostrarSnackbar("onRestart")
    }

    override fun onPause(){
        super.onPause()
        mostrarSnackbar("onPause")
    }

    override fun onStop(){
        super.onStop()
        mostrarSnackbar("onStop")
    }

    //---------------Cuando se destruyan las variabales-------------------
    //Para guardar variables
    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            // Guardar las vaariables
            putString("variableTextoGuardado", textoGlobal)
        }
        super.onSaveInstanceState(outState)
    }

    //Para recuperar variables
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Recuperar las variables
        val textoRecuperado: String? = savedInstanceState
            .getString("variableTextoGuardado")
        if(textoRecuperado != null){
            // textoGlobal = textoRecuperado
            mostrarSnackbar(textoRecuperado) // ya guarda el texto global
        }
    }
    //-----------------------------


    var textoGlobal = ""
    fun mostrarSnackbar(text:String){
        textoGlobal += text
        val snack = Snackbar.make(
            findViewById(R.id.main_ciclo_vida),
            textoGlobal,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }
}