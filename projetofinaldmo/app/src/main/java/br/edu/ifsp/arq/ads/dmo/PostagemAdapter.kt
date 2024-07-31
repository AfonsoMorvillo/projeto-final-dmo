package br.edu.ifsp.arq.ads.dmo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.arq.ads.dmo.model.Grupo
import br.edu.ifsp.arq.ads.dmo.model.Postagem

class PostagemAdapter(private val listener: OnItemClickListener, private var postagens: List<Postagem>) : RecyclerView.Adapter<PostagemAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.postagem_card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val postagem = postagens[i]
        viewHolder.itemTitle.text = postagem.nome
        viewHolder.itemImage.setImageResource(R.drawable.menu_vazio) // Ajuste conforme necessário para as imagens

        viewHolder.itemView.setOnClickListener {
            listener.onItemClick(i)
        }
    }

    override fun getItemCount(): Int {
        return postagens.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    // Adicione um método para atualizar os dados
    fun updatePostagens(novasPostagens: List<Postagem>) {
        postagens = novasPostagens
        notifyDataSetChanged()
    }
}
