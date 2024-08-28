package br.edu.ifsp.arq.ads.dmo

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import br.edu.ifsp.arq.ads.dmo.dialog.DialogLoading
import br.edu.ifsp.arq.ads.dmo.model.User
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PerfilActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1

    lateinit var edtName: TextInputEditText

    lateinit var edtNascimento: TextInputEditText
    lateinit var imageView: ImageView

    lateinit var user: User

    private var imageUri: Uri? = null

    private val userViewModel by viewModels<UserViewModel>()

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    lateinit var loading: DialogLoading
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        loading = DialogLoading(this)

        loadUserLogged()
    }

    private fun loadUserLogged() {
        loading.showDialog("Carregando...")
        userViewModel.isLogged().observe(this,  Observer {
            if (it == null) {
                startActivity(
                    Intent(
                        this@PerfilActivity,
                        LoginActivity::class.java
                    )
                )
                loading.hideDialog()
                finish()
            } else {
                this@PerfilActivity.user = it
                fillFields()
            }
        })
    }

    fun fillFields(){
        setComponents()

        edtName.setText(user.name)
        edtNascimento.setText(user.dateOfBirth)

        if (user.image.isNotEmpty()) {
            Glide.with(imageView.context)
                .load(user.image)
                .placeholder(R.drawable.carregando) // Imagem de placeholder enquanto a imagem carrega
                .error(R.drawable.menu_vazio)
                .fallback(R.drawable.menu_vazio)
                .into(imageView)
        }

        loading.hideDialog()
    }

    private fun setComponents() {
        imageView = findViewById(R.id.profile_image)
        edtName = findViewById(R.id.editTextNome)
        edtNascimento = findViewById(R.id.editTextDataNascimento)

        val btnAtualizar = findViewById<MaterialButton>(R.id.btn_atualizar)

        btnAtualizar.setOnClickListener {
            onClickAtualizarConta()
        }

        // Configura o DatePicker para o campo de data
        edtNascimento.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            R.style.CustomDatePickerDialogTheme, // Aplicar o tema personalizado
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }.time
                edtNascimento.setText(dateFormat.format(selectedDate))
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    fun selecionarImagemPerfil(view: View) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    fun sair(view: View) {

        userViewModel.logout()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        startActivity(intent)

        finish()
    }

    fun onClickAtualizarConta() {

        if (validate()) {
            loading.showDialog("Atualizando...")
            val userSalvar = User(
                email = user.email,
                name = edtName.text.toString(),
                password = user.password,
                image = user.image,
                dateOfBirth = edtNascimento.text.toString(),
                id = user.id
            )

            if (imageUri != null) {
                userViewModel.uploadUserImage(userSalvar.id, imageUri!!).observe(this) {
                    userSalvar.image = it
                    userViewModel.updateUser(userSalvar)
                    Toast.makeText(
                        this@PerfilActivity,
                        "Perfil atualizado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    loading.hideDialog()
                }
            }else{
                if (userViewModel.updateUser(userSalvar)){
                    Toast.makeText(
                        this@PerfilActivity,
                        "Perfil atualizado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                }
                loading.hideDialog()
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

        return isValid
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            imageView.setImageURI(imageUri)
        }
    }
}