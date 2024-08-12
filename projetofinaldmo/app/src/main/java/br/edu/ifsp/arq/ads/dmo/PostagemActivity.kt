package br.edu.ifsp.arq.ads.dmo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import br.edu.ifsp.arq.ads.dmo.model.Postagem
import br.edu.ifsp.arq.ads.dmo.viewmodel.PostagemViewModel
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class PostagemActivity : AppCompatActivity() {

    private val postagemViewModel by viewModels<PostagemViewModel>()

    var postId: String? = null

    lateinit var postagem: Postagem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_postagem)

        postId = intent.getStringExtra("POST_ID")

        postagemViewModel.getPost(postId).observe(this, Observer {
           postagem =  it
            setComponents()
        })
    }

    fun setComponents(){

        val imagem = findViewById<ImageView>(R.id.item_image)

        if (postagem.foto.isNotEmpty()) {
            Glide.with(this)
                .load(postagem.foto)
                .placeholder(R.drawable.carregando) // Imagem de placeholder enquanto a imagem carrega
                .error(R.drawable.menu_vazio)
                .fallback(R.drawable.menu_vazio)
                .into(imagem)
        } else {
            imagem.setImageResource(R.drawable.menu_vazio)
        }
    }
}