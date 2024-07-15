package br.edu.ifsp.arq.ads.dmo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    lateinit var edtEmail: TextInputEditText
    lateinit var edtPassword: TextInputEditText

    private val userViewModel by viewModels<UserViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnEntrar = findViewById<MaterialButton>(R.id.btn_entrar)
        val btnNewUser = findViewById<Button>(R.id.btn_login_new_user)
        edtEmail = findViewById(R.id.editTextEmail)
        edtPassword = findViewById(R.id.editTextSenha)

        btnEntrar.setOnClickListener {
            userViewModel.login(edtEmail.text.toString(), edtPassword.text.toString()).observe(this, Observer {

                if(it == null){
                    Toast.makeText(applicationContext, getString(R.string.login_message_erro), Toast.LENGTH_SHORT).show()
                }
                else{
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

            })
        }

        btnNewUser.setOnClickListener {
            val intent = Intent(this, CadastroContaActivity::class.java)
            startActivity(intent)
        }
    }
}