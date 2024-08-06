package br.edu.ifsp.arq.ads.dmo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import br.edu.ifsp.arq.ads.dmo.model.Postagem
import br.edu.ifsp.arq.ads.dmo.model.User
import br.edu.ifsp.arq.ads.dmo.viewmodel.PostagemViewModel
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class CadastroPostagemActivity : AppCompatActivity() {
    lateinit var txtTitulo: TextInputEditText
    lateinit var txtDescricao: TextInputEditText
    lateinit var txtData: TextInputEditText
    lateinit var txtQuantidade: TextInputEditText
    lateinit var btnSave: Button
    private lateinit var imageView: ImageView

    lateinit var user: User
    lateinit var postagem: Postagem

    lateinit var adicionarImagem: Button
    lateinit var photoURI: Uri

    val REQUEST_TAKE_PHOTO = 1

    lateinit var grupoId: String

    private val userViewModel by viewModels<UserViewModel>()
    private val postViewModel by viewModels<PostagemViewModel>()
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_postagem)

        grupoId = intent.getStringExtra("GROUP_ID").toString()
        println(grupoId)

        postagem = Postagem()
        postagem.id = "0"

        setComponents()
        setBtnSave()
        //setImageProfile()
    }

    private fun setComponents() {

        txtTitulo = findViewById<TextInputEditText>(R.id.editTextTitulo)
        txtDescricao = findViewById<TextInputEditText>(R.id.edtDescricao)
        txtData = findViewById<TextInputEditText>(R.id.edtData)
        txtQuantidade = findViewById<TextInputEditText>(R.id.editTextQuantidade)

        btnSave = findViewById<Button>(R.id.btn_criar_grupo)

        imageView = findViewById(R.id.imageView)

        // Configura o clique na área para adicionar imagem
        val textViewAdicionarImagem: TextView = findViewById(R.id.textViewAdicionarImagem)
        textViewAdicionarImagem.setOnClickListener {
            selecionarImagem()
        }
    }

//    private fun selecionarImagem() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        startActivityForResult(intent, PICK_IMAGE_REQUEST)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
//            // Obter URI da imagem selecionada
//            val imageUri = data?.data
//            // Definir imagem na ImageView
//            imageView.setImageURI(imageUri)
//        }
//    }

    private fun setBtnSave() {
        btnSave.setOnClickListener {
            if (postagem.id == "0") {
                addPostagem()
            } else {
                //updateGrupo()
            }
        }
    }
    private fun setImageProfile() {
        adicionarImagem = findViewById(R.id.btn_adicionar_imagem)
        adicionarImagem.setOnClickListener{ takePicture() }

        val profileImage = PreferenceManager.getDefaultSharedPreferences(this).getString(MediaStore.EXTRA_OUTPUT, null)

        if (profileImage != null) {
            photoURI = Uri.parse(profileImage)
            // imageProfile.setImageURI(photoURI)
        } else {
            photoURI = Uri.parse("/")
            // imageProfile.setImageResource(R.drawable.ic_menu_account)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timesTamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "PROFILE_${timesTamp}",
            ".jpg",
            storageDir
        )
    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(this,
                        "br.edu.ifsp.arq.ads.dmo.projetofinaldmo.fileprovider",
                        it)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(intent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        PreferenceManager.getDefaultSharedPreferences(this).apply {
//            edit().putString(MediaStore.EXTRA_OUTPUT, photoURI.toString()).apply()
//        }
//
//        //imageProfile.setImageURI(photoURI)
//    }


    private fun updateGrupo() {
        /* if (validate()) {
            activity.type = PhysicalActivity.PhysicalActivityType.values().get(spnActivityType.selectedItemPosition)
            activity.date = txtDate.text.toString()
            activity.distance = txtDistance.text.toString().toDouble()
            activity.duration = txtDuration.text.toString().toDouble()
            activityViewModel.updateActivity(activity)
            Toast.makeText(
                this@ActivityRegisterActivity,
                "Atividade atualizada com sucesso!",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }*/
    }

    private fun addPostagem() {
        if (validate()) {

            val postagem = Postagem(
                UUID.randomUUID().toString(),
                user.id,
                txtTitulo.text.toString(),
                txtDescricao.text.toString(),
                txtQuantidade.text.toString(),
                "", // salvar a foto
                txtData.text.toString(),
                grupoId
            )
            postViewModel.createPostagem(postagem)
            Toast.makeText(
                this@CadastroPostagemActivity,
                "Postagem publicada com sucesso!",
                Toast.LENGTH_SHORT
            ).show()
            finish()
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
        if (txtData.text.toString().trim { it <= ' ' }.isEmpty()) {
            txtData.error = "Preencha a data."
            isValid = false
        } else {
            txtData.error = null
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
        userViewModel.isLogged().observe(this,  Observer {
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

    private fun selecionarImagem() {
        // Verifica se tem permissão para acessar a câmera

            // Escolhe entre a câmera ou a galeria
            val options = arrayOf<CharSequence>("Tirar foto", "Escolher da galeria", "Cancelar")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Adicionar foto")
            builder.setItems(options) { dialog, item ->
                when {
                    options[item] == "Tirar foto" -> {
                        abrirCamera()
                    }
                    options[item] == "Escolher da galeria" -> {
                        escolherDaGaleria()
                    }
                    options[item] == "Cancelar" -> {
                        dialog.dismiss()
                    }
                }
            }
            builder.show()

    }

    private fun abrirCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun escolherDaGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == RESULT_OK && data != null) {
                    val imageBitmap = data.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(imageBitmap)
                }
            }
            PICK_IMAGE_REQUEST -> {
                if (resultCode == RESULT_OK && data != null) {
                    val imageUri: Uri? = data.data
                    imageView.setImageURI(imageUri)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selecionarImagem()
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val PICK_IMAGE_REQUEST = 2
        private const val REQUEST_CAMERA_PERMISSION = 101
    }


}