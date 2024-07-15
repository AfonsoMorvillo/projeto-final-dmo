package br.edu.ifsp.arq.ads.dmo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val listener: OnItemClickListener): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    val titles = arrayOf("Grupo A", "Grupo B")
    val details = arrayOf("Descricao A", "Descricao B")

    val images = intArrayOf(R.drawable.menu_vazio, R.drawable.logo_verde)

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout,viewGroup,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTitle.text = titles[i]
        viewHolder.itemDetail.text = details[i]
        viewHolder.itemImage.setImageResource( images[i])

        viewHolder.itemView.setOnClickListener {
            listener.onItemClick(i) // Notifica o listener quando o card é clicado
        }
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        var itemTitle: TextView
        var itemDetail: TextView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)
            itemDetail = itemView.findViewById(R.id.item_detail)
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}