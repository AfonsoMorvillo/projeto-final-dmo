package br.edu.ifsp.arq.ads.dmo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.arq.ads.dmo.model.Grupo

class CustomAdapter(private val listener: OnItemClickListener, private var grupos: List<Grupo>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val grupo = grupos[i]
        viewHolder.itemTitle.text = grupo.nome
        viewHolder.teste.progress = 80
        viewHolder.itemImage.setImageResource(R.drawable.menu_vazio) // Ajuste conforme necessário para as imagens

        viewHolder.itemView.setOnClickListener {
            listener.onItemClick(i)
        }
    }

    override fun getItemCount(): Int {
        return grupos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)

        var teste: ProgressBar= itemView.findViewById(R.id.progressBar)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    // Adicione um método para atualizar os dados
    fun updateGrupos(novosGrupos: List<Grupo>) {
        grupos = novosGrupos
        notifyDataSetChanged()
    }
}
