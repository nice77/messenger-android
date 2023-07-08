package com.example.messenger.ui.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.databinding.ChatItemBinding

class ChatsAdapter (
    private val chats : List<Chat>,
    private val onItemClick: (Chat) -> Unit
) : RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {
    class ChatsViewHolder(
        private val binding: ChatItemBinding,
        private val onItemClick: (Chat) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(chat : Chat) {
            binding.run {
                tvUsername.text = chat.besedaId

                root.setOnClickListener {
                    onItemClick(chat)
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
        return chats.size
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.onBind(chats[position])
    }
}