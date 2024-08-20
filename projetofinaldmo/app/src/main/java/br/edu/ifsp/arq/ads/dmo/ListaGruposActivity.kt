package br.edu.ifsp.arq.ads.dmo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.arq.ads.dmo.dialog.ActionBottomSheetFragment
import br.edu.ifsp.arq.ads.dmo.model.User
import br.edu.ifsp.arq.ads.dmo.viewmodel.GrupoViewModel
import br.edu.ifsp.arq.ads.dmo.viewmodel.UserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaGruposActivity : AppCompatActivity(), CustomAdapter.OnItemClickListener, ActionBottomSheetFragment.OnActionSelectedListener {

    private lateinit var adapter: CustomAdapter
    private lateinit var recyclerView: RecyclerView

    private val grupoViewModel by viewModels<GrupoViewModel>()
    private val userViewModel by viewModels<UserViewModel>()

    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_lista_grupos)

        recyclerView = findViewById(R.id.recyclerView)
        loadUserLogged()
        setFloatButton()
    }

    private fun setAdapter() {
        grupoViewModel.getAllGroups(user.id).observe(this, Observer {
            if (it.isNullOrEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                adapter = CustomAdapter(this, it)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun setFloatButton() {
        val fab: FloatingActionButton = findViewById(R.id.fab_add)
        fab.setOnClickListener {
            val bottomSheetFragment = ActionBottomSheetFragment()
            bottomSheetFragment.setOnActionSelectedListener(this)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun loadUserLogged() {
        userViewModel.isLogged().observe(this, Observer {
            if (it == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                user = it
                setAdapter()
            }
        })
    }

    override fun onItemClick(groupId: String) {
        val intent = Intent(this, VisualizarGrupoActivity::class.java)
        intent.putExtra("GROUP_ID", groupId)
        startActivity(intent)
    }

    override fun onActionSelected(action: String) {
        when (action) {
            "ADD_GROUP" -> {
                val intent = Intent(this, CadastroGrupoActivity::class.java)
                startActivity(intent)
            }
            "JOIN_GROUP" -> {
                val intent = Intent(this, JuntarGrupoActivity::class.java)
                startActivityForResult(intent, JOIN_GROUP_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == JOIN_GROUP_REQUEST_CODE && resultCode == RESULT_OK) {
            setAdapter()
        }
    }

    companion object {
        private const val JOIN_GROUP_REQUEST_CODE = 1
    }
}
