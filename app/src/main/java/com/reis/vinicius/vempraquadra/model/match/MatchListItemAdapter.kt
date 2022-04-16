package com.reis.vinicius.vempraquadra.model.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reis.vinicius.vempraquadra.databinding.FragmentFeedItemBinding
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchListBinding
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchListItemBinding

class MatchListItemAdapter : RecyclerView.Adapter<MatchListItemAdapter.Holder>() {
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
        return
    }

    override fun getItemCount(): Int {
        return 10
    }
}