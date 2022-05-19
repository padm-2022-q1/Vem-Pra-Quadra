package com.reis.vinicius.vempraquadra.view.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentChatMessageReceivedBinding
import com.reis.vinicius.vempraquadra.databinding.FragmentChatMessageSentBinding
import com.reis.vinicius.vempraquadra.model.dto.MessageWithUserData
import java.text.SimpleDateFormat
import java.util.*

class MessageListItemAdapter(
    private val mMessages: MutableList<MessageWithUserData>,
    private val userId: String,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private enum class ViewHolderType(val value: Int) {
        SENT(1),
        RECEIVED(2);

        companion object {
            fun fromInt(value: Int) = values().first { it.value == value }
        }
    }

    inner class SentMessageViewHolder(itemBinding: FragmentChatMessageSentBinding):
        RecyclerView.ViewHolder(itemBinding.root){
            val content = itemBinding.textSentMessageContent
            val imgStatus = itemBinding.imageSentMessageStatus
        }

    inner class ReceivedMessageViewHolder(itemBinding: FragmentChatMessageReceivedBinding):
        RecyclerView.ViewHolder(itemBinding.root){
        val userName = itemBinding.textReceivedMessageAuthor
        val content = itemBinding.textReceivedMessageContent
        val date = itemBinding.textReceivedMessageDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (ViewHolderType.fromInt(viewType)){
            ViewHolderType.SENT -> SentMessageViewHolder(
                FragmentChatMessageSentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            ViewHolderType.RECEIVED -> ReceivedMessageViewHolder(
                FragmentChatMessageReceivedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = mMessages[position]

        when (holder) {
            is SentMessageViewHolder -> {
                holder.content.text = message.message.content
                holder.imgStatus.setImageResource(
                    if (message.message.sentIn == null) R.drawable.ic_pending
                    else R.drawable.ic_check
                )
            }
            is ReceivedMessageViewHolder -> {
                val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)

                holder.userName.text = message.user.name
                holder.content.text = message.message.content
                holder.date.text = message.message.sentIn?.let { dateFormatter.format(it) }
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (mMessages[position].user.id == userId) {
            true -> ViewHolderType.SENT.value
            false -> ViewHolderType.RECEIVED.value
        }

    override fun getItemCount(): Int = mMessages.size

    fun addMessage(message: MessageWithUserData){
        mMessages.add(0, message)
        notifyItemInserted(0)
    }
}