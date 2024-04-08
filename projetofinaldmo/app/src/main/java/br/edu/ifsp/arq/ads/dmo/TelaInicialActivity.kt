package br.edu.ifsp.arq.ads.dmo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class TelaInicialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_inicial)

        val btnCriarConta = findViewById<MaterialButton>(R.id.btn_criar_conta)

        // Configure o evento de clique para o botão "Criar Conta"
        btnCriarConta.setOnClickListener {
            // Ao clicar no botão "Criar Conta", inicie a nova atividade (Tela de Cadastro)
            val intent = Intent(this, CadastroContaActivity::class.java)
            startActivity(intent)
        }

    }
}