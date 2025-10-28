package com.yourpackage.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible // Usamos androidx.core.view.isVisible para evitar IDs duplicados

/**
 * Aplicación de demostración del ciclo de vida de una Activity de Android en Kotlin.
 * Implementa todos los callbacks, persistencia de estado y botones de control.
 */
class MainActivity : AppCompatActivity() {

    private val TAG = "LCYCLE"
    private val KEY_COUNTER = "instance_counter"
    private val KEY_LOGTEXT = "log_text"

    // Variables de estado
    private var instanceCounter = 1 // Inicializado en 1, se incrementa en recreación
    private lateinit var txtLog: TextView
    private lateinit var txtInstance: TextView
    private lateinit var btnRecreate: Button
    private lateinit var btnClear: Button

    /**
     * Registra un evento del ciclo de vida tanto en la pantalla (TextView) como en Logcat (TAG LCYCLE).
     * @param method El nombre del callback que se ejecutó.
     */
    private fun addEvent(method: String) {
        // Formatear la línea de log en pantalla
        val line = "${System.currentTimeMillis()} • MainActivity.$method\n"

        // 1. Log en pantalla: Asegúrate de que txtLog esté inicializado.
        if (::txtLog.isInitialized) {
            txtLog.append(line)
        }
        // 2. Logcat con tag LCYCLE
        Log.d(TAG, "MainActivity.$method")
    }

    // --- FASE DE CREACIÓN ---

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Asegúrate de usar el layout que definiste en activity_main.xml
        setContentView(R.layout.activity_main)

        // Inicialización de Views (Usando el binding o findViewById)
        // Nota: En un proyecto moderno de Android Studio, se recomienda usar View Binding.
        // Aquí usamos findViewById ya que es compatible con el layout proporcionado.
        txtLog = findViewById(R.id.txtLog)
        txtInstance = findViewById(R.id.txtInstance)
        btnRecreate = findViewById(R.id.btnRecreate)
        btnClear = findViewById(R.id.btnClear)

        // Restaurar estado previo (si savedInstanceState no es null, es una recreación)
        if (savedInstanceState != null) {
            // Si es una recreación, recuperamos el estado y aumentamos el contador.
            // Guardamos instanceCounter para que sea el valor que se guarda en onSaveInstanceState
            val oldCounter = savedInstanceState.getInt(KEY_COUNTER, 0)
            instanceCounter = oldCounter + 1

            val oldLog = savedInstanceState.getString(KEY_LOGTEXT, "")
            txtLog.text = oldLog // Restaurar el log anterior
        }

        // Actualizar el TextView del contador de instancia
        txtInstance.text = "Instancia #: $instanceCounter"

        // Listeners para los botones
        // Botón Recrear: llama a recreate() para simular la rotación/cambio de configuración
        btnRecreate.setOnClickListener { recreate() }
        // Botón Limpiar: borra el log de la pantalla
        btnClear.setOnClickListener { txtLog.text = "" }

        addEvent("onCreate(recreada=${savedInstanceState != null})")
    }

    // --- PERSISTENCIA DE ESTADO (ANTES DE DESTRUCCIÓN O PAUSA EXTENDIDA) ---

    /**
     * Guarda el estado de la Activity (contador y log) antes de ser destruida por el sistema.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        // MUY IMPORTANTE: Llamar primero al super para guardar el estado de las vistas.
        super.onSaveInstanceState(outState)
        // Guardar el estado según los requisitos
        outState.putInt(KEY_COUNTER, instanceCounter)
        outState.putString(KEY_LOGTEXT, txtLog.text.toString())
        addEvent("onSaveInstanceState (Datos guardados)")
    }

    // Este callback se llama después de onStart, solo si hubo un Bundle guardado.
    // Usamos esta opción para registrar el evento, aunque la restauración principal se hizo en onCreate.
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        addEvent("onRestoreInstanceState (Estado recuperado)")
    }

    // --- OTROS CALLBACKS DEL CICLO DE VIDA ---

    override fun onStart() {
        super.onStart()
        addEvent("onStart")
    }

    override fun onResume() {
        super.onResume()
        addEvent("onResume")
    }

    override fun onPause() {
        super.onPause()
        addEvent("onPause")
    }

    override fun onStop() {
        super.onStop()
        addEvent("onStop")
    }

    override fun onRestart() {
        super.onRestart()
        addEvent("onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        addEvent("onDestroy")
    }
}
