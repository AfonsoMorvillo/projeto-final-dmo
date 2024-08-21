package br.edu.ifsp.arq.ads.dmo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.edu.ifsp.arq.ads.dmo.dialog.DialogLoading
import br.edu.ifsp.arq.ads.dmo.model.Postagem
import br.edu.ifsp.arq.ads.dmo.model.User
import br.edu.ifsp.arq.ads.dmo.viewmodel.PostagemViewModel
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class CadastroPostagemActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1

    lateinit var txtTitulo: TextInputEditText
    lateinit var txtDescricao: TextInputEditText
    lateinit var txtQuantidade: TextInputEditText
    lateinit var btnSave: Button
    private lateinit var imageView: ImageView

    lateinit var user: User
    lateinit var grupoId: String
    lateinit var postagem: Postagem

    private val userViewModel by viewModels<UserViewModel>()
    private val postViewModel by viewModels<PostagemViewModel>()

    lateinit var loading: DialogLoading

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_postagem)

        grupoId = intent.getStringExtra("GROUP_ID").toString()

        postagem = Postagem()
        postagem.id = "0"

        loading = DialogLoading(this)

        setComponents()
        setBtnSave()
    }

    private fun setComponents() {
        txtTitulo = findViewById(R.id.editTextTitulo)
        txtDescricao = findViewById(R.id.edtDescricao)
        txtQuantidade = findViewById(R.id.editTextQuantidade)
        btnSave = findViewById(R.id.btn_criar_grupo)
        imageView = findViewById(R.id.imageView)

        val textViewAdicionarImagem: TextView = findViewById(R.id.textViewAdicionarImagem)
        textViewAdicionarImagem.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    fun selecionarImagem(view: View) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun setBtnSave() {
        btnSave.setOnClickListener {
            if (postagem.id == "0") {
                addPostagem()
            } else {
                //updateGrupo()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun addPostagem() {
        if (validate()) {
            loading.showDialog("Publicando...")
            val storageReference = FirebaseStorage.getInstance().reference
            val imageRef = storageReference.child("images/${UUID.randomUUID()}.jpg")
            val quantidade = txtQuantidade.text.toString()
            val quantidadeNumber: Int? = quantidade.toIntOrNull()

            if (imageUri != null) {
                imageRef.putFile(imageUri!!)
                    .addOnSuccessListener { taskSnapshot ->
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val postagem = Postagem(
                                UUID.randomUUID().toString(),
                                user.id,
                                txtTitulo.text.toString(),
                                txtDescricao.text.toString(),
                                quantidadeNumber,
                                uri.toString(), // URL da imagem salva no Firebase Storage
                                data = Timestamp.now(),
                                grupoId
                            )
                            postViewModel.createPostagem(postagem)
                            Toast.makeText(
                                this@CadastroPostagemActivity,
                                "Postagem publicada com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                            loading.hideDialog()
                            setResult(RESULT_OK)
                            finish()
                        }
                    }
                    .addOnFailureListener {
                        loading.hideDialog()
                        Toast.makeText(this, "Falha ao carregar a imagem", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // Caso sem imagem
                val postagem = Postagem(
                    UUID.randomUUID().toString(),
                    user.id,
                    txtTitulo.text.toString(),
                    txtDescricao.text.toString(),
                    quantidadeNumber,
                    "", // Sem URL da imagem
                    data = Timestamp.now(),
                    grupoId
                )
                postViewModel.createPostagem(postagem)
                Toast.makeText(
                    this@CadastroPostagemActivity,
                    "Postagem publicada com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()
                loading.hideDialog()
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        if (txtTitulo.text.toString().trim { it <= ' ' }.isEmpty()) {
            txtTitulo.error = "Preencha o nome do título."
            isValid = false
        } else {
            txtTitulo.error = null
        }
        if (txtQuantidade.text.toString().trim { it <= ' ' }.isEmpty()) {
            txtQuantidade.error = "Preencha a quantidade."
            isValid = false
        } else {
            txtQuantidade.error = null
        }

        return isValid
    }

    private fun loadUserLogged() {
        userViewModel.isLogged().observe(this, Observer {
            if (it == null) {
                startActivity(
                    Intent(
                        this@CadastroPostagemActivity,
                        LoginActivity::class.java
                    )
                )
                finish()
            } else {
                this@CadastroPostagemActivity.user = it
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadUserLogged()
    }
}
