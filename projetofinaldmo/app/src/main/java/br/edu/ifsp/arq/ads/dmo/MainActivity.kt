package br.edu.ifsp.arq.ads.dmo

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel
import com.google.android.material.button.MaterialButton
import androidx.activity.viewModels
import androidx.lifecycle.Observer


class MainActivity : AppCompatActivity() {
    private val userViewModel by viewModels<UserViewModel>()

    lateinit var txtBemVindo: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtBemVindo = findViewById(R.id.txt_bemVindo)

        userViewModel.isLogged().observe(this, Observer {
            it?.let {
                txtBemVindo.text = "Bem-vindo ${it.name}"
            }
        })

        val btnCriarGrupo = findViewById<MaterialButton>(R.id.btn_criar_grupo)
        val btnJuntarGrupo = findViewById<MaterialButton>(R.id.btn_entrar_grupo)

        btnCriarGrupo.setOnClickListener {

            val intent = Intent(this, CadastroGrupoActivity::class.java)
            startActivity(intent)
        }

        btnJuntarGrupo.setOnClickListener {
            val intent = Intent(this, JuntarGrupoActivity::class.java)
            joinGroupActivityResultLauncher.launch(intent)
        }
    }

    private val joinGroupActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent = Intent(this, ListaGruposActivity::class.java)
                startActivity(intent)
            }
        }
}