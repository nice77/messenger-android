package com.example.messenger.ui.dialog

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.messenger.databinding.MyMessageItemBinding
import com.example.messenger.databinding.OtherMessageItemBinding
import com.example.messenger.messanger.Beseda
import com.example.messenger.messanger.DataRepository
import com.example.messenger.messanger.Messeng
import com.google.firebase.database.FirebaseDatabase

class MessagesAdapter(
    var messages: List<Messeng>,
) : RecyclerView.Adapter<ViewHolder>() {

    // следующие два класса надо бы как-нибудь закинуть в один тырфейс
    // а хотя...

    class MyMessagesViewHolder(
        private val binding : MyMessageItemBinding
    ) : ViewHolder(binding.root) {
        fun onBind(message : Messeng) {
            binding.run {
                tvMessage.text = message.msg
                tvTime.text = message.date_time
            }
        }

        fun getUsername(id: String, callback: (String) -> Unit) {
            DataRepository.getInstance().getDB().child("users").child(id).get().addOnSuccessListener {
                callback(it.child("login").getValue(String::class.java).toString())
            }
        }
    }

    class OtherMessagesViewHolder(
        private val binding : OtherMessageItemBinding
    ) : ViewHolder(binding.root) {
        fun onBind(message : Messeng) {
            binding.run {
                getUsername(message.from) {
                    tvUsername.setText(it)
                }
                tvMessage.text = message.msg
                tvTime.text = message.date_time
            }
        }

        fun getUsername(id: String, callback: (String) -> Unit) {
            DataRepository.getInstance().getDB().child("users").child(id).get().addOnSuccessListener {
                callback(it.child("login").getValue(String::class.java).toString())
            }
        }
    }

    override fun getItemViewType(position: Int) : Int {
        if (messages[position].from == DataRepository.getInstance().getUser()) {
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
        if (holder is MyMessagesViewHolder) {
            holder.onBind(messages[position])
        }
        else if (holder is OtherMessagesViewHolder) {
            holder.onBind(messages[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newMessages: List<Messeng>?) {
        if (newMessages!!.size != 0) {
            messages = newMessages
            notifyDataSetChanged()
        }
    }
}
