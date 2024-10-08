package br.edu.ifsp.arq.ads.dmo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import br.edu.ifsp.arq.ads.dmo.dialog.DialogLoading
import br.edu.ifsp.arq.ads.dmo.model.Postagem
import br.edu.ifsp.arq.ads.dmo.utils.formatTimestamp
import br.edu.ifsp.arq.ads.dmo.viewmodel.PostagemViewModel
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class PostagemActivity : AppCompatActivity() {

    private val postagemViewModel by viewModels<PostagemViewModel>()

    var postId: String? = null

    lateinit var postagem: Postagem
    lateinit var fotoUser: String

    lateinit var loading: DialogLoading
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_postagem)
        loading = DialogLoading(this)

        loading.showDialog("Carregando...")

        postId = intent.getStringExtra("POST_ID")

        postagemViewModel.getPost(postId).observe(this, Observer {
           postagem =  it.postagem
           fotoUser = it.fotoUser
            setComponents()
        })
    }

    fun setComponents(){

        val imagem = findViewById<ImageView>(R.id.item_image)
        val usuario = findViewById<TextView>(R.id.item_usuario)
        val titulo =findViewById<TextView>(R.id.item_title)
        val data = findViewById<TextView>(R.id.item_data)
        val descricao =findViewById<TextView>(R.id.item_descricao)
        val imagemUsuario = findViewById<ImageView>(R.id.post_user_foto)

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

        if (fotoUser.isNotEmpty()) {
            Glide.with(this)
                .load(fotoUser)
                .placeholder(R.drawable.carregando)
                .error(R.drawable.user_svgrepo_com)
                .fallback(R.drawable.user_svgrepo_com)
                .into(imagemUsuario)
        }else{
            imagem.setImageResource(R.drawable.user_svgrepo_com)
        }

        usuario.text = postagem.nomeUsuario
        titulo.text = postagem.nome
        data.text = formatTimestamp(postagem.data)
        descricao.text = "Descrição: ${postagem.descricao}"

        loading.hideDialog()
    }
}