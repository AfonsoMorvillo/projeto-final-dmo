package br.edu.ifsp.arq.ads.dmo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.arq.ads.dmo.model.Postagem
import br.edu.ifsp.arq.ads.dmo.model.User
import br.edu.ifsp.arq.ads.dmo.viewmodel.PostagemViewModel
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class VisualizarGrupoActivity : AppCompatActivity(), PostagemAdapter.OnItemClickListener {

    private lateinit var adapter: PostagemAdapter
    private lateinit var recyclerView: RecyclerView

    private val postagemViewModel by viewModels<PostagemViewModel>()
    private val userViewModel by viewModels<UserViewModel>()

    lateinit var user: User
    var grupoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_visualizar_grupo)

        grupoId = intent.getStringExtra("GROUP_ID")

        recyclerView = findViewById(R.id.recyclerViewPostagens)

        setAdapter()
        setFloatButton()
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

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "Clicou no item $position", Toast.LENGTH_SHORT).show()
    }
}
