package br.edu.ifsp.arq.ads.dmo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListaGruposActivity : AppCompatActivity(), CustomAdapter.OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_lista_grupos)

        val recycylerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = CustomAdapter(this)

        recycylerView.layoutManager = LinearLayoutManager(this)
        recycylerView.adapter = adapter

    }

    override fun onItemClick(position: Int) {

        Toast.makeText(this, "Clicou no item $position", Toast.LENGTH_SHORT).show()

    }
}