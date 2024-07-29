package br.edu.ifsp.arq.ads.dmo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.arq.ads.dmo.model.User
import br.edu.ifsp.arq.ads.dmo.viewmodel.GrupoViewModel
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel

class ListaGruposActivity : AppCompatActivity(), CustomAdapter.OnItemClickListener {

    private lateinit var adapter: CustomAdapter
    private lateinit var recycylerView: RecyclerView

    private val grupoViewModel by viewModels<GrupoViewModel>()
    private val userViewModel by viewModels<UserViewModel>()

    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_lista_grupos)

        recycylerView = findViewById<RecyclerView>(R.id.recyclerView)
        loadUserLogged()
    }

    private fun setAdapter() {
        grupoViewModel.getAllGroups(user.id).observe(this, Observer{

            println(it)
            adapter = CustomAdapter(
                this,
                it
            )

            recycylerView.layoutManager = LinearLayoutManager(this)
            recycylerView.adapter = adapter
            adapter.notifyDataSetChanged()
        })
    }

    private fun loadUserLogged() {
        userViewModel.isLogged().observe(this,  Observer {
            if (it == null) {
                startActivity(
                    Intent(
                        this,
                        LoginActivity::class.java
                    )
                )
                finish()
            } else {
                this@ListaGruposActivity.user = it
                setAdapter()
            }
        })
    }

    override fun onItemClick(position: Int) {

        Toast.makeText(this, "Clicou no item $position", Toast.LENGTH_SHORT).show()

    }
}