package br.edu.ifsp.arq.ads.dmo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import br.edu.ifsp.arq.ads.dmo.model.Postagem
import br.edu.ifsp.arq.ads.dmo.model.User
import br.edu.ifsp.arq.ads.dmo.viewmodel.GrupoViewModel
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel
import kotlin.math.log

class VisualizarGrupoActivity : AppCompatActivity(), PostagemAdapter.OnItemClickListener {

    private lateinit var adapter: PostagemAdapter
    private lateinit var recycylerView: RecyclerView

    private val grupoViewModel by viewModels<GrupoViewModel>()
    private val userViewModel by viewModels<UserViewModel>()

    lateinit var user: User
    var grupoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_visualizar_grupo)

        grupoId = intent.getStringExtra("GROUP_ID")
        println(grupoId)

        recycylerView = findViewById<RecyclerView>(R.id.recyclerViewPostagens)
        adapter = PostagemAdapter(this, listOf(Postagem("teste", "teste", "nova postagen", "descricao", "","","")))

        recycylerView.layoutManager = LinearLayoutManager(this)
        recycylerView.adapter = adapter
        adapter.notifyDataSetChanged()

        val fab: FloatingActionButton = findViewById(R.id.fab_add)

        fab.setOnClickListener {
            val intent = Intent(this, CadastroPostagemActivity::class.java)
            intent.putExtra("GROUP_ID", grupoId)
            startActivity(intent)
        }
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Clicou no item $position", Toast.LENGTH_SHORT).show()
    }
}
