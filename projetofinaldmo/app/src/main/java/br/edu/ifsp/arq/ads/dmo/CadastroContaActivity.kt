package br.edu.ifsp.arq.ads.dmo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import br.edu.ifsp.arq.ads.dmo.model.User

class CadastroContaActivity : AppCompatActivity() {

    lateinit var edtName: TextInputEditText
    lateinit var edtEmail: TextInputEditText
    lateinit var edtPassword: TextInputEditText


    private val userViewModel by viewModels<UserViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_conta)

        edtName = findViewById(R.id.editTextNome)
        edtEmail = findViewById(R.id.editTextEmail)
        edtPassword = findViewById(R.id.editTextSenha)

        val btnCriarConta = findViewById<MaterialButton>(R.id.btn_criar_conta)

        btnCriarConta.setOnClickListener {
            if (validate()) {

                val user = User(
                    email = edtEmail.text.toString(),
                    name = edtName.text.toString(),
                    password = edtPassword.text.toString(),
                    image = "",
                    dateOfBirth = "",
                )
                userViewModel.createUser(user)
                userViewModel.login(user.email, user.password).observe(this, Observer {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                })
            }

        }
    }

    private fun validate(): Boolean {
        var isValid = true

        edtName.apply {
            if (text.isNullOrEmpty()) {
                error = "Preencha o campo nome!"
                isValid = false
            } else {
                error = null
            }
        }
        edtEmail.apply {
            if (text.isNullOrEmpty()) {
                error = "Preencha o campo e-mail!"
                isValid = false
            } else {
                error = null
            }
        }
        edtPassword.apply {
            if (text.isNullOrEmpty()) {
                error = "Preencha o campo a senha!"
                isValid = false
            } else if (text!!.length < 6) {
                error = "Senha deve conter 6 ou mais caracteres"
                isValid = false
            } else {
                error = null
            }
        }

        return isValid
    }


}