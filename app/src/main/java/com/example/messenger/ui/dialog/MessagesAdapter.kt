package com.example.messenger.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.databinding.MessageItemBinding

class MessagesAdapter(
    private val messages: List<Message>
) : RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>() {
    class MessagesViewHolder(
        private val binding : MessageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(message : Message) {
            binding.run {
                tvUsername.text = getUsername(message.fromId) //message.toId.toString() // Нужно решить!
                tvMessage.text = message.msg
            }
        }

        fun getUsername(id : Int) : String {
            //По-хорошему тут нужен запрос в БД, а не хранение в оперативке пользователей
            //Нужно доработатб
            return "noice"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        return MessagesViewHolder(
            MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        holder.onBind(messages[position])
    }
}
