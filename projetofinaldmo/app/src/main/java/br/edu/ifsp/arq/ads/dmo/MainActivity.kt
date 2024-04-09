package br.edu.ifsp.arq.ads.dmo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCriarGrupo = findViewById<MaterialButton>(R.id.btn_criar_grupo)

        btnCriarGrupo.setOnClickListener {
            val intent = Intent(this, CadastroGrupoActivity::class.java)
            startActivity(intent)
        }
    }
}