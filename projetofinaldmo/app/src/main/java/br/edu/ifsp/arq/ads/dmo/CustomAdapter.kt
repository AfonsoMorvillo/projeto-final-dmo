package br.edu.ifsp.arq.ads.dmo

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.arq.ads.dmo.model.Grupo
import com.bumptech.glide.Glide

class CustomAdapter(private val listener: OnItemClickListener, private var grupos: List<Grupo>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(groupId: String)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val grupo = grupos[i]
        viewHolder.itemTitle.text = grupo.nome
        viewHolder.progressBar.progress = grupo.calculaPercentual()

        if (grupo.foto.isNotEmpty()) {
            Glide.with(viewHolder.itemImage.context)
                .load(grupo.foto)
                .placeholder(R.drawable.carregando) // Imagem de placeholder enquanto a imagem carrega
                .error(R.drawable.menu_vazio)
                .fallback(R.drawable.menu_vazio)
                .into(viewHolder.itemImage)
        } else {
            viewHolder.itemImage.setImageResource(R.drawable.menu_vazio)
        }

        viewHolder.itemView.setOnClickListener {
            listener.onItemClick(grupo.id)
        }
    }


    override fun getItemCount(): Int {
        return grupos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(grupos[position].id)
                }
            }
        }
    }

    // Adicione um método para atualizar os dados
    fun updateGrupos(novosGrupos: List<Grupo>) {
        grupos = novosGrupos
        notifyDataSetChanged()
    }
}
