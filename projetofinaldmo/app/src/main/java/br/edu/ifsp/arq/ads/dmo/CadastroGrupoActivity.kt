package br.edu.ifsp.arq.ads.dmo

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.edu.ifsp.arq.ads.dmo.dialog.DialogLoading
import br.edu.ifsp.arq.ads.dmo.model.Grupo
import br.edu.ifsp.arq.ads.dmo.model.User
import br.edu.ifsp.arq.ads.dmo.viewmodel.GrupoViewModel
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class CadastroGrupoActivity : AppCompatActivity() {

    lateinit var txtNome: TextInputEditText
    lateinit var txtDescricao: TextInputEditText
    lateinit var txtDataFinal: TextInputEditText
    lateinit var txtMeta: TextInputEditText
    lateinit var autoCompleteTipo: AutoCompleteTextView
    lateinit var btnSave: Button
    lateinit var loading: DialogLoading

    lateinit var imageView: ImageView

    private val userViewModel by viewModels<UserViewModel>()
    private val grupoViewModel by viewModels<GrupoViewModel>()

    lateinit var user: User
    lateinit var grupo: Grupo

    lateinit var adicionarImagem: Button

    private var imageUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 1

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_grupo);

        grupo = Grupo()
        grupo.id = "0"
        loading = DialogLoading(this)

        setComponents()
        setBtnSave()
    }

    private fun setComponents() {
        setSelectTipo()
        txtNome = findViewById<TextInputEditText>(R.id.editTextNome)
        txtDescricao = findViewById<TextInputEditText>(R.id.editTextDescricao)
        txtDataFinal = findViewById<TextInputEditText>(R.id.editTextDataFinal)
        txtMeta = findViewById<TextInputEditText>(R.id.editTextMeta)
        autoCompleteTipo = findViewById<AutoCompleteTextView>(R.id.complete_tipo)
        btnSave = findViewById<Button>(R.id.btn_criar_grupo)
        imageView = findViewById(R.id.imageViewGrupo)

        val textViewAdicionarImagem: TextView = findViewById(R.id.textViewAdicionarImagemGrupo)
        textViewAdicionarImagem.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Configura o DatePicker para o campo de data
        txtDataFinal.setOnClickListener {
            showDatePickerDialog()
        }
    }

    fun selecionarImagem(view: View) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
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
                txtDataFinal.setText(dateFormat.format(selectedDate))
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            imageView.setImageURI(imageUri)
        }
    }
    private fun setSelectTipo() {
        val type = Grupo.TipoMaterial.values().map { it.value }.toTypedArray()
        val adapter = ArrayAdapter(this, R.layout.drop_down_material_item, type)
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.complete_tipo)
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, "Item selecionado: $selectedItem", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setBtnSave() {
        btnSave.setOnClickListener {
            loading.showDialog("Carregando...")
            if (grupo.id == "0") {
                addGrupo()
            } else {
                updateGrupo()
            }
        }
    }

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

    private fun addGrupo() {
        if (validate()) {

            val id = UUID.randomUUID().toString();
            val tipoMaterialText = autoCompleteTipo.text.toString()
            val tipoMaterial = Grupo.TipoMaterial.values().find { it.value == tipoMaterialText }
            val metaText = txtMeta.text.toString()
            val metaNumber: Int? = metaText.toIntOrNull()

            val grupo = Grupo(
                id,
                user.id,
                txtNome.text.toString(),
                txtDescricao.text.toString(),
                txtDataFinal.text.toString(),
                "",
                tipoMaterial,
                memberIds = listOf(user.id),
                metaNumber,
                0
            )

            if (imageUri != null) {

                grupoViewModel.uploadGrupoImage(id, imageUri!!).observe(this) {
                    grupo.foto = it
                    grupoViewModel.createGrupo(grupo)
                    Toast.makeText(
                        this@CadastroGrupoActivity,
                        "Grupo adicionado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    loading.hideDialog()
                    val intent = Intent(this, ListaGruposActivity::class.java)
                    startActivity(intent)
                }
            }else{
                // sem imagem
                grupoViewModel.createGrupo(grupo)
                Toast.makeText(
                    this@CadastroGrupoActivity,
                    "Grupo adicionado com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()
                loading.hideDialog()
                val intent = Intent(this, ListaGruposActivity::class.java)
                startActivity(intent)
            }

        }else{
            loading.hideDialog()
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        if (txtNome.text.toString().trim { it <= ' ' }.isEmpty()) {
            txtNome.error = "Preencha o nome do grupo."
            isValid = false
        } else {
            txtNome.error = null
        }
        if (txtDataFinal.text.toString().trim { it <= ' ' }.isEmpty()) {
            txtDataFinal.error = "Preencha a data final."
            isValid = false
        } else {
            txtDataFinal.error = null
        }

        val tipoMaterialText = autoCompleteTipo.text.toString()
        val tipoMaterialSelecionado = Grupo.TipoMaterial.values().find { it.value == tipoMaterialText }
        if (tipoMaterialSelecionado == null) {
            autoCompleteTipo.error = "Selecione um tipo de material."
            isValid = false
        } else {
            autoCompleteTipo.error = null
        }

        if (txtMeta.text.toString().trim { it <= ' ' }.isEmpty()) {
            txtMeta.error = "Preencha a quantidade."
            isValid = false
        } else {
            txtMeta.error = null
        }

        return isValid
    }

    private fun loadUserLogged() {
        userViewModel.isLogged().observe(this,  Observer {
            println(it)
            if (it == null) {
                startActivity(
                    Intent(
                        this@CadastroGrupoActivity,
                        LoginActivity::class.java
                    )
                )
                finish()
            } else {
                this@CadastroGrupoActivity.user = it
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadUserLogged()
    }

}