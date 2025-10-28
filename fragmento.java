import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LCYCLE";
    private static final String KEY_COUNTER = "instance_counter";
    private static final String KEY_LOGTEXT = "log_text";

    // El contador se inicializa en 1 si no hay estado previo.
    private int instanceCounter = 1;
    private TextView txtLog, txtInstance;

    /**
     * Registra un evento del ciclo de vida tanto en la pantalla como en Logcat.
     * @param method El nombre del callback que se ejecutó.
     */
    private void addEvent(String method) {
        String line = System.currentTimeMillis() + " • MainActivity." + method + "\n";
        // 1. Log en pantalla
        txtLog.append(line);
        // 2. Logcat con tag LCYCLE
        Log.d(TAG, "MainActivity." + method);
    }

    // --- FASE DE CREACIÓN ---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLog = findViewById(R.id.txtLog);
        txtInstance = findViewById(R.id.txtInstance);
        Button btnRecreate = findViewById(R.id.btnRecreate);
        Button btnClear = findViewById(R.id.btnClear);

        // Restaurar estado previo (si lo hay) y actualizar Instancia #
        if (savedInstanceState != null) {
            // El contador se incrementa solo si venimos de una recreación
            instanceCounter = savedInstanceState.getInt(KEY_COUNTER, 0) + 1;
            String oldLog = savedInstanceState.getString(KEY_LOGTEXT, "");
            txtLog.setText(oldLog);
        }
        txtInstance.setText("Instancia #: " + instanceCounter);

        // Listeners para los botones
        btnRecreate.setOnClickListener(v -> recreate());
        btnClear.setOnClickListener(v -> txtLog.setText(""));

        addEvent("onCreate(recreada=" + (savedInstanceState != null) + ")");
    }

    // --- FASE VISIBLE ---

    @Override
    protected void onStart() {
        super.onStart();
        addEvent("onStart");
    }

    // --- FASE INTERACTIVA ---

    @Override
    protected void onResume() {
        super.onResume();
        addEvent("onResume");
    }

    // --- PERSISTENCIA DE ESTADO (ANTES DE DESTRUCCIÓN O PAUSA EXTENDIDA) ---

    /**
     * Guarda el estado de la Activity antes de que sea destruida (p. ej. por rotación)
     * para que pueda ser recuperado en onCreate/onRestoreInstanceState.
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar el contador de instancia (el valor actual)
        outState.putInt(KEY_COUNTER, instanceCounter);
        // Guardar todo el texto del log
        outState.putString(KEY_LOGTEXT, txtLog.getText().toString());
        addEvent("onSaveInstanceState (Datos guardados)");
    }

    // Este callback se llama después de onStart, solo si hubo un Bundle guardado.
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        addEvent("onRestoreInstanceState (Estado recuperado)");
    }

    // --- FASE DE PAUSA/NO INTERACTIVA ---

    @Override
    protected void onPause() {
        super.onPause();
        addEvent("onPause");
    }

    // --- FASE NO VISIBLE ---

    @Override
    protected void onStop() {
        super.onStop();
        addEvent("onStop");
    }

    // --- FASE DE REINICIO ---

    /**
     * Se llama cuando la Activity regresa desde el estado de Stop (background).
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        addEvent("onRestart");
    }

    // --- FASE DE DESTRUCCIÓN ---

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addEvent("onDestroy");
    }
}
