package com.example.lifecyclemini

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity que implementa y registra todos los callbacks del ciclo de vida.
 */
class MainActivity : AppCompatActivity() {

    // Constantes para las claves del Bundle de estado
    private val KEY_LOG_TEXT = "log_text"
    private val KEY_INSTANCE_COUNT = "instance_count"
    private val TAG = "LCYCLE" // Tag para filtrar en Logcat

    // Componentes de la UI
    private lateinit var logTextView: TextView
    private lateinit var instanceCounterTextView: TextView
    private lateinit var recreateButton: Button
    private lateinit var clearButton: Button

    // Estado persistido: Un contador para ver cuántas veces se ha recreado la Activity
    private var instanceCounter: Int = 0

    /**
     * Función de utilidad para registrar un evento del ciclo de vida.
     * 1. Lo registra en Logcat con el TAG LCYCLE.
     * 2. Lo añade al TextView del log en pantalla.
     */
    private fun logEvent(methodName: String) {
        val message = "$methodName()"
        // 1. Logcat: Requiere la importación de android.util.Log
        Log.d(TAG, message)
        // 2. Pantalla: Añadir nueva línea al TextView del log
        logTextView.append("\n$message")
    }

    // =================================================================
    // Callbacks del Ciclo de Vida
    // =================================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logEvent("onCreate")

        // 1. Inicialización del Layout y componentes
        setContentView(R.layout.activity_main)

        logTextView = findViewById(R.id.textViewLog)
        instanceCounterTextView = findViewById(R.id.textViewInstanceCounter)
        recreateButton = findViewById(R.id.buttonRecreate)
        clearButton = findViewById(R.id.buttonClearLog)

        // 2. Restauración del estado (si savedInstanceState no es null)
        if (savedInstanceState != null) {
            logEvent("Restaurando estado en onCreate (Bundle)")
            // Restaurar el texto del log
            logTextView.text = savedInstanceState.getString(KEY_LOG_TEXT, "Log inicial:")
            // Restaurar el contador
            instanceCounter = savedInstanceState.getInt(KEY_INSTANCE_COUNT, 0)
        } else {
            // Estado inicial si no hay Bundle (primera ejecución)
            logTextView.text = "Log inicial:"
        }

        // 3. Incrementar el contador de instancia y actualizar la UI
        instanceCounter++
        instanceCounterTextView.text = "Instancia #$instanceCounter"

        // 4. Configuración de listeners de botones
        recreateButton.setOnClickListener {
            // Dispara la secuencia de recreación (onPause -> onSaveInstanceState -> onStop -> onDestroy -> onCreate -> ...)
            logEvent("--> Botón Recrear presionado (llamando a recreate()) <--");
            recreate()
        }

        clearButton.setOnClickListener {
            // Borra el log mostrado en pantalla
            logEvent("--> Botón Limpiar presionado <--");
            logTextView.text = "Log inicial:"
        }
    }

    override fun onStart() {
        super.onStart()
        logEvent("onStart")
    }

    override fun onResume() {
        super.onResume()
        logEvent("onResume")
    }

    override fun onPause() {
        super.onPause()
        logEvent("onPause")
    }

    /**
     * Persistencia del estado.
     * Se llama antes de que la Activity sea destruida (por recreación, p. ej.) o detenida.
     * Guarda datos necesarios para que la Activity pueda restaurar su estado más tarde.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        logEvent("onSaveInstanceState")
        // Guardar el contador de instancia
        outState.putInt(KEY_INSTANCE_COUNT, instanceCounter)
        // Guardar el texto completo del log
        outState.putString(KEY_LOG_TEXT, logTextView.text.toString())
        super.onSaveInstanceState(outState)
    }

    /**
     * Restauración del estado.
     * Se llama entre onStart() y onResume() SOLO si la Activity se recrea.
     * Es una alternativa a restaurar en onCreate().
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        logEvent("onRestoreInstanceState")
        // La lógica de restauración principal ya está en onCreate(), pero registramos el callback.
    }

    override fun onStop() {
        super.onStop()
        logEvent("onStop")
    }

    override fun onRestart() {
        super.onRestart()
        logEvent("onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        logEvent("onDestroy")
    }
}
