package br.edu.ifsp.arq.ads.dmo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.newSingleThreadContext

class CadastroGrupoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_grupo);

        val type = arrayOf("Lata", "Papel", "Vidro", "Lacre")
        val adapter = ArrayAdapter<String>(this, R.layout.drop_down_material_item, type)
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.material)
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, "Item selecionado: $selectedItem", Toast.LENGTH_SHORT).show()
        }

    }
}