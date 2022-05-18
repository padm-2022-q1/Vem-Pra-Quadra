package com.reis.vinicius.vempraquadra.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.reis.vinicius.vempraquadra.R
import com.reis.vinicius.vempraquadra.databinding.FragmentMatchListItemBinding
import com.reis.vinicius.vempraquadra.model.dto.MatchWithCourt
import com.reis.vinicius.vempraquadra.model.entity.Match
import com.reis.vinicius.vempraquadra.view.home.MainMenuFragmentDirections
import java.text.SimpleDateFormat
import java.util.*

class MatchListItemAdapter(
    private val context: Context,
    private val navController: NavController,
    private val matches: List<MatchWithCourt>
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
        val dateFormatter = SimpleDateFormat(context.getString(R.string.date_format), Locale.US)

        holder.name.text = match.match.name
        holder.address.text = match.court.name
        holder.startDate.text = dateFormatter.format(match.match.date)

        holder.itemView.setOnClickListener {
            navController.navigate(MainMenuFragmentDirections.openMatchDetails(getId(position)))
        }
    }

    override fun getItemCount(): Int = matches.size

    fun getId(position: Int): String = matches[position].match.id
}