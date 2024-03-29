package com.reis.vinicius.vempraquadra.view.court

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.reis.vinicius.vempraquadra.databinding.FragmentCourtListItemBinding
import com.reis.vinicius.vempraquadra.model.entity.Court

class CourtListItemAdapter(
    private val fragment: CourtListFragment,
    private val courts: List<Court>
) : RecyclerView.Adapter<CourtListItemAdapter.Holder>() {
    inner class Holder(itemBinding: FragmentCourtListItemBinding):
        RecyclerView.ViewHolder(itemBinding.root){
            val name = itemBinding.textCourtItemName
            val address = itemBinding.textCourtItemAddress
            val btnNavigate = itemBinding.btnCourtNavigate
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            FragmentCourtListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val court = courts[position]

        holder.name.text = court.name
        holder.address.text = court.address

        bindNavigateEvent(holder.btnNavigate, court.address)
    }

    override fun getItemCount(): Int = courts.size

    override fun getItemId(position: Int): Long = courts[position].id

    private fun bindNavigateEvent(button: ImageButton, address: String){
        button.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=$address")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

            mapIntent.setPackage("com.google.android.apps.maps")

            fragment.startActivity(mapIntent)
        }
    }
}