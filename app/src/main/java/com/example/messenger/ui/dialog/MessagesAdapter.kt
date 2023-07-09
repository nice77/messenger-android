package com.example.messenger.ui.dialog

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.messenger.databinding.MyMessageItemBinding
import com.example.messenger.databinding.OtherMessageItemBinding
import com.google.firebase.database.FirebaseDatabase

class MessagesAdapter(
    private val messages: List<Message>,
    private val fbDB: FirebaseDatabase
) : RecyclerView.Adapter<ViewHolder>() {

    class MyMessagesViewHolder(
        private val binding : MyMessageItemBinding
    ) : ViewHolder(binding.root) {
        fun onBind(message : Message) {
            binding.run {
                tvUsername.text = getUsername(message.fromId) //message.toId.toString() // Нужно решить!
                tvMessage.text = "MyMessagesViewHolder"
            }
        }

        fun getUsername(id : Int) : String {
            //По-хорошему тут нужен запрос в БД, а не хранение в оперативке пользователей
            //Нужно доработатб
            return "noice"
        }
    }

    class OtherMessagesViewHolder(
        private val binding : OtherMessageItemBinding
    ) : ViewHolder(binding.root) {
        fun onBind(message : Message) {
            binding.run {
                tvUsername.text = getUsername(message.fromId) //message.toId.toString() // Нужно решить!
                tvMessage.text = "OtherMessagesViewHolder"
            }
        }

        fun getUsername(id : Int) : String {
            //По-хорошему тут нужен запрос в БД, а не хранение в оперативке пользователей
            //Нужно доработатб
            return "noice"
        }
    }

    override fun getItemViewType(position: Int) : Int {
        if (messages[position].fromId == 1) {
            return 0
        }
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == 0) {
            return MyMessagesViewHolder(
                MyMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
        return OtherMessagesViewHolder(OtherMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        println("Position: " + messages[position].fromId + " " + messages[position])
//        if (messages[position].fromId == 0) {
//            println("uno")
//            (holder as MyMessagesViewHolder).onBind(messages[position])
//        }
//        else {
//            (holder as OtherMessagesViewHolder).onBind(messages[position])
//        }

        if (holder is MyMessagesViewHolder) {
            holder.onBind(messages[position])
        }
        else if (holder is OtherMessagesViewHolder) {
            holder.onBind(messages[position])
        }
    }
}
