package br.edu.ifsp.arq.ads.dmo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnEntrar = findViewById<MaterialButton>(R.id.btn_entrar)
        val btnNewUser = findViewById<Button>(R.id.btn_login_new_user)

        btnEntrar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnNewUser.setOnClickListener {
            val intent = Intent(this, CadastroContaActivity::class.java)
            startActivity(intent)
        }
    }
}