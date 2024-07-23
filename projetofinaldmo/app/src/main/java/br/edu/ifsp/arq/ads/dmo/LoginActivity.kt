package br.edu.ifsp.arq.ads.dmo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import br.edu.ifsp.arq.ads.dmo.dialog.DialogLoading
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    lateinit var edtEmail: TextInputEditText
    lateinit var edtPassword: TextInputEditText
    lateinit var loading: DialogLoading
    lateinit var forgotPassword: TextView
    lateinit var dialogResetPassword: AlertDialog

    private val userViewModel by viewModels<UserViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnEntrar = findViewById<MaterialButton>(R.id.btn_entrar)
        val btnNewUser = findViewById<Button>(R.id.btn_login_new_user)
        edtEmail = findViewById(R.id.editTextEmail)
        edtPassword = findViewById(R.id.editTextSenha)
        forgotPassword = findViewById(R.id.txt_forgot_password)

        buildResetPasswordDialog()

        loading = DialogLoading(this)

        btnEntrar.setOnClickListener {
            loading.showDialog("Carregando...")

            userViewModel.login(edtEmail.text.toString(), edtPassword.text.toString())
                .observe(this, Observer { response ->
                    if (response != null) {
                        if (response?.id == "ERRO") {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.login_message_erro),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        loading.hideDialog()
                    }
                })
        }

        btnNewUser.setOnClickListener {
            val intent = Intent(this, CadastroContaActivity::class.java)
            startActivity(intent)
            finish()
        }

        forgotPassword.setOnClickListener {
            dialogResetPassword.show()
        }
    }

    private fun buildResetPasswordDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_reset_password, null)
        val edtEmail = view.findViewById<TextInputEditText>(R.id.edt_reset_email)

        dialogResetPassword = MaterialAlertDialogBuilder(this, R.style.CustomAlertDialogTheme) // Aplica o estilo personalizado aqui
            .setPositiveButton("Resetar") { dialog, which ->
                userViewModel.resetPassword(edtEmail.text.toString())
                Toast.makeText(this, "Verifique seu email para resetar a senha", Toast.LENGTH_LONG).show()
                edtEmail.text?.clear()
            }
            .setNegativeButton("Cancelar") { dialog, which ->
                edtEmail.text?.clear()
            }
            .setIcon(android.R.drawable.ic_dialog_email)
            .setView(view)
            .setTitle("Preencha seu email para resetar sua senha.")
            .create()
    }

}