package com.example.sw2024bgr1_mapf

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class HFirebaseUIAuth : AppCompatActivity() {
    private val respuestaLoginUiAuth = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ){
            res: FirebaseAuthUIAuthenticationResult ->
        if(res.resultCode == RESULT_OK){
            if(res.idpResponse != null){
                seLogeo(res.idpResponse!!)
            }
        }
    }

    // Setear el nombre del usuario
    fun seLogeo(res: IdpResponse){
        val nombre = FirebaseAuth.getInstance().currentUser?.displayName
        cambiarInterfaz(View.INVISIBLE, View.VISIBLE, nombre!!)
        if(res.isNewUser == true){ // si es primera vez
            registrarUsuarioPorPrimeraVez(res)
        }
    }

    // Funcion para cambiar la interfaz
    fun cambiarInterfaz(
        visibilidadLogin:Int = View.VISIBLE,
        visibilidadLogout: Int = View.INVISIBLE,
        textoTextView:String = "Bienvenido"){
        val btnLogin = findViewById<Button>(R.id.btn_login_firebase)
        val btnLogout = findViewById<Button>(R.id.btn_logout_firebase)
        val tvBienvenida = findViewById<TextView>(R.id.tv_bienvenido)
        btnLogin.visibility = visibilidadLogin
        btnLogout.visibility = visibilidadLogout
        tvBienvenida.text = textoTextView
    }

    // Registramos en nuestro sistema y, por ejemplo, enviamos correo
    fun registrarUsuarioPorPrimeraVez(usuario: IdpResponse){}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hfirebase_uiauth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Logica para que cuando este logeado se quite el boton login y aparezca el logout

        val btnLogin = findViewById<Button>(R.id.btn_login_firebase)
        btnLogin.setOnClickListener {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build() //Email
            )
            // Lanzamos el intent y recibimos respuesta
            val logearseIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers).build()
            respuestaLoginUiAuth.launch(logearseIntent)
        }
        val btnLogout = findViewById<Button>(R.id.btn_logout_firebase)
        btnLogout.setOnClickListener {
            cambiarInterfaz()
            FirebaseAuth.getInstance().signOut()
        }

        // Si retrocede/sale y vuelve a la interfaz que se mantenga su estado de login
        val usuario = FirebaseAuth.getInstance().currentUser
        if(usuario !=null) {
            cambiarInterfaz(View.INVISIBLE, View.VISIBLE, usuario.displayName!!)
        }
    }
}