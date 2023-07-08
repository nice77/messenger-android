package com.example.messenger.ui.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.databinding.ChatItemBinding
import com.example.messenger.messanger.Beseda
import com.example.messenger.messanger.DataRepository

class ChatsAdapter (
    private var besedas : List<Beseda> = DataRepository.getInstance().getBesedas()!!,
    private val onItemClick: (Beseda) -> Unit
) : RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {
    class ChatsViewHolder(
        private val binding: ChatItemBinding,
        private val onItemClick: (Beseda) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(beseda : Beseda) {
            binding.run {
                tvUsername.text = beseda.besedaId.toString()

                root.setOnClickListener {
                    onItemClick(beseda)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        return ChatsViewHolder(
            ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClick
        )
    }

    override fun getItemCount(): Int {
        return besedas.size
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.onBind(besedas[position])
    }
}
