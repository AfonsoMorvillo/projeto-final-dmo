package br.edu.ifsp.arq.ads.dmo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListaGruposActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_lista_grupos)

        val recycylerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = CustomAdapter()

        recycylerView.layoutManager = LinearLayoutManager(this)
        recycylerView.adapter = adapter

    }
}