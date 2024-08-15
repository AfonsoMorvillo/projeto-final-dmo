package br.edu.ifsp.arq.ads.dmo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class JuntarGrupoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juntar_grupo)

        val btnJuntarse = findViewById<MaterialButton>(R.id.btn_juntar)

        btnJuntarse.setOnClickListener {
            val intent = Intent(this, ListaGruposActivity::class.java)
            startActivity(intent)
        }

    }
}