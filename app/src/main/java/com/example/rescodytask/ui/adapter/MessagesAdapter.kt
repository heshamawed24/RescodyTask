package com.example.rescodytask.ui.adapter

import android.content.Context
import android.graphics.Color
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rescodytask.data.models.Messages
import com.example.rescodytask.databinding.MessageItemListBinding

class MessagesAdapter(val context: Context, val messageClickListener: MessageClickListener): ListAdapter<Messages, MessagesAdapter.MyHolder>(MessageDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(messageClickListener,getItem(position),context)
    }

    class MyHolder(val binding: MessageItemListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(messageClickListener: MessageClickListener, item :Messages,context: Context){
            binding.message = item
            when(item.category){
                "Negative"->binding.tvMessageCategory.setTextColor(Color.RED)
                "Positive"->binding.tvMessageCategory.setTextColor(Color.BLUE)
                "Neutral"->binding.tvMessageCategory.setTextColor(Color.GREEN)
            }
            binding.card.setOnClickListener { messageClickListener.onClick(item) }
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup): MyHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MessageItemListBinding.inflate(layoutInflater,parent,false)
                return MyHolder(binding)
            }
        }

    }

    class MessageDiffCallback : DiffUtil.ItemCallback<Messages>(){
        override fun areItemsTheSame(
            oldItem: Messages,
            newItem: Messages
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Messages,
            newItem: Messages
        ): Boolean {
            return oldItem == newItem
        }

    }

    class MessageClickListener(val clickListener: (item: Messages) -> Unit){
        fun onClick(item: Messages) = clickListener(item)
    }




}