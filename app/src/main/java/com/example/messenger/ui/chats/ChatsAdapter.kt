package com.example.messenger.ui.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.databinding.ChatItemBinding
import com.example.messenger.messanger.Beseda

class ChatsAdapter(
    private var besedas: List<Beseda> = emptyList(),
    private val onItemClick: (Beseda) -> Unit
) : RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatsViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val beseda = besedas[position]
        holder.bind(beseda)
    }

    fun getData(): List<Beseda> {
        return besedas
    }

    override fun getItemCount(): Int {
        return besedas.size
    }

    fun setData(newBesedas: List<Beseda>?) {
        if (newBesedas != null) {
            besedas = newBesedas
            notifyDataSetChanged()
        }
    }

    inner class ChatsViewHolder(
        private val binding: ChatItemBinding,
        private val onItemClick: (Beseda) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(beseda: Beseda) {
            binding.tvUsername.text = beseda.besedaId.toString()

            binding.root.setOnClickListener {
                onItemClick(beseda)
            }
        }
    }
}
