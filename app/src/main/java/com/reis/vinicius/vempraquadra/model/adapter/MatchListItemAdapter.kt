package com.reis.vinicius.vempraquadra.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchListItemBinding
import com.reis.vinicius.vempraquadra.model.entity.Match
import com.reis.vinicius.vempraquadra.view.home.MainMenuFragmentDirections

class MatchListItemAdapter(
    private val navController: NavController,
    private val matches: List<Match>
) : RecyclerView.Adapter<MatchListItemAdapter.Holder>() {
    inner class Holder(itemBinding: FragmentMatchListItemBinding):
        RecyclerView.ViewHolder(itemBinding.root){
            val name = itemBinding.textMatchListItemName
            val address = itemBinding.textMatchListItemAddress
            val startDate = itemBinding.textMatchListItemStartDate

            init {
                itemBinding.root.setOnClickListener {
                    navController.navigate(
                        MainMenuFragmentDirections.openMatchDetails(getId(bindingAdapterPosition))
                    )
                }
            }
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