package br.edu.ifsp.arq.ads.dmo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.arq.ads.dmo.model.Postagem
import br.edu.ifsp.arq.ads.dmo.model.User
import br.edu.ifsp.arq.ads.dmo.viewmodel.GrupoViewModel
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel

class VisualizarGrupoActivity : AppCompatActivity(), PostagemAdapter.OnItemClickListener {

    private lateinit var adapter: PostagemAdapter
    private lateinit var recycylerView: RecyclerView

    private val grupoViewModel by viewModels<GrupoViewModel>()
    private val userViewModel by viewModels<UserViewModel>()

    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_visualizar_grupo)

        recycylerView = findViewById<RecyclerView>(R.id.recyclerViewPostagens)
        adapter = PostagemAdapter(this,  listOf(Postagem("teste", "teste","nova postagen", "descricao", 10)))


        recycylerView.layoutManager = LinearLayoutManager(this)
        recycylerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {

        Toast.makeText(this, "Clicou no item $position", Toast.LENGTH_SHORT).show()

    }
}