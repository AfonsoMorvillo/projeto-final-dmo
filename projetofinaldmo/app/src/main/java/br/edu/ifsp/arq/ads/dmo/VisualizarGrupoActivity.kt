package br.edu.ifsp.arq.ads.dmo

import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.arq.ads.dmo.model.Grupo
import br.edu.ifsp.arq.ads.dmo.model.Postagem
import br.edu.ifsp.arq.ads.dmo.model.User
import br.edu.ifsp.arq.ads.dmo.viewmodel.GrupoViewModel
import br.edu.ifsp.arq.ads.dmo.viewmodel.PostagemViewModel
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton

class VisualizarGrupoActivity : AppCompatActivity(), PostagemAdapter.OnItemClickListener {

    private lateinit var adapter: PostagemAdapter
    private lateinit var recyclerView: RecyclerView

    private val postagemViewModel by viewModels<PostagemViewModel>()
    private val grupoViewModel by viewModels<GrupoViewModel>()

    lateinit var user: User

    var grupoId: String? = null
    lateinit var grupo: Grupo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_visualizar_grupo)

        grupoId = intent.getStringExtra("GROUP_ID")

        recyclerView = findViewById(R.id.recyclerViewPostagens)

        grupoViewModel.getGrupo(grupoId!!).observe(this, Observer {
            grupo = it
            setComponents()
        })

        setAdapter()
        setFloatButton()

        // Configurar o clique no ícone
        val iconView: ImageView = findViewById(R.id.iconView)
        iconView.setOnClickListener { view -> onIconClick(view) }
    }

    private fun setComponents() {
        val nomeGrupo = findViewById<TextView>(R.id.textView)
        val percentual = findViewById<TextView>(R.id.textViewPercentual)

        nomeGrupo.text = grupo.nome
        percentual.text = grupo.calculaPercentual().toString() + "%"
    }

    private fun setAdapter() {
        adapter = PostagemAdapter(this, emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        postagemViewModel.getAllPosts(grupoId).observe(this, Observer {
            adapter.updatePostagens(it)
        })
    }

    private fun setFloatButton() {
        val fab: FloatingActionButton = findViewById(R.id.fab_add)

        fab.setOnClickListener {
            val intent = Intent(this, CadastroPostagemActivity::class.java)
            intent.putExtra("GROUP_ID", grupoId)
            startActivityForResult(intent, REQUEST_CODE_ADD_POST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_POST && resultCode == RESULT_OK) {
            postagemViewModel.getAllPosts(grupoId).observe(this, Observer {
                adapter.updatePostagens(it)
            })
        }
    }

    companion object {
        private const val REQUEST_CODE_ADD_POST = 1
    }

    override fun onItemClick(postagemId: String) {
        val intent = Intent(this, PostagemActivity::class.java)
        intent.putExtra("POST_ID", postagemId)
        startActivity(intent)
    }

    fun onIconClick(view: View) {
        // Obtém o ClipboardManager
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        // Cria um ClipData com o ID do grupo
        val clip = ClipData.newPlainText("Group ID", grupoId)

        // Adiciona o ClipData ao ClipboardManager
        clipboard.setPrimaryClip(clip)

        // Mostra uma mensagem de confirmação
        Toast.makeText(this, "Link do Grupo copiado!", Toast.LENGTH_SHORT).show()
    }
}
