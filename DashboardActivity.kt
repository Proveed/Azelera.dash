package com.azelera.dashboard

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val etContenido = findViewById<EditText>(R.id.etContenido)
        val sbPresupuesto = findViewById<SeekBar>(R.id.sbPresupuesto)
        val tvBudgetLabel = findViewById<TextView>(R.id.tvBudgetLabel)
        val etDias = findViewById<EditText>(R.id.etDias)
        val btnPublicar = findViewById<Button>(R.id.btnPublicar)
        val tvAlcance = findViewById<TextView>(R.id.tvAlcance)

        // 1. Lógica de Calculo Estilo Meta
        sbPresupuesto.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val presupuesto = progress + 1
                tvBudgetLabel.text = "Presupuesto: $$presupuesto USD"
                
                val dias = etDias.text.toString().toIntOrNull() ?: 1
                val alcance = presupuesto * 150 * dias
                tvAlcance.text = "Alcance estimado: $alcance personas"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 2. Publicar en Tiempo Real
        btnPublicar.setOnClickListener {
            val contenido = etContenido.text.toString()
            val presupuesto = sbPresupuesto.progress + 1
            val dias = etDias.text.toString().toLong()
            val vencimiento = System.currentTimeMillis() + (dias * 24 * 60 * 60 * 1000)

            if (contenido.isNotEmpty()) {
                val db = FirebaseDatabase.getInstance().getReference("anuncio_actual")
                
                val data = mapOf(
                    "tipo" to "TEXT",
                    "contenido" to contenido,
                    "presupuesto" to presupuesto,
                    "vencimiento" to vencimiento,
                    "ciudad" to "Guayaquil", // Ejemplo, puedes usar el Spinner
                    "stats" to mapOf("vistas" to 0, "clics" to 0)
                )

                db.setValue(data).addOnSuccessListener {
                    Toast.makeText(this, "¡Campaña Publicada!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
