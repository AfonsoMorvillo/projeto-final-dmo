package br.edu.ifsp.arq.ads.dmo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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

        loadUserLogged()
        setFloatButton()
    }

    private fun setAdapter() {
        grupoViewModel.getAllGroups(user.id).observe(this, Observer { groups ->
            val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
            val imageEmpty: ImageView = findViewById(R.id.image_empty)
            val textEmpty: TextView = findViewById(R.id.text_empty)

            if (groups.isNullOrEmpty()) {
                recyclerView.visibility = View.GONE
                imageEmpty.visibility = View.VISIBLE
                textEmpty.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                imageEmpty.visibility = View.GONE
                textEmpty.visibility = View.GONE

                adapter = CustomAdapter(this, groups)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })
    }

    fun navigatePerfilActivity(view: View){
        val intent = Intent(this, PerfilActivity::class.java)
        startActivity(intent)
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
