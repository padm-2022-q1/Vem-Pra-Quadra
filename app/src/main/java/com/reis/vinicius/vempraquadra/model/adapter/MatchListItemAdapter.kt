package com.reis.vinicius.vempraquadra.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchListItemBinding
import com.reis.vinicius.vempraquadra.model.entity.Match

class MatchListItemAdapter(
    private val matches: List<Match>
) : RecyclerView.Adapter<MatchListItemAdapter.Holder>() {
    inner class Holder(itemBinding: FragmentMatchListItemBinding):
        RecyclerView.ViewHolder(itemBinding.root){
            val name = itemBinding.textMatchListItemName
            val address = itemBinding.textMatchListItemAddress
            val startDate = itemBinding.textMatchListItemStartDate
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(FragmentMatchListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val match = matches[position]

        holder.name.text = match.name
        holder.address.text = match.courtId.toString()
        holder.startDate.text = match.date.toString()
    }

    override fun getItemCount(): Int = matches.size

    fun getId(position: Int): String = matches[position].id
}