package com.reis.vinicius.vempraquadra.model.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.reis.vinicius.vempraquadra.model.entity.Court

class CourtDropdownListAdapter(
    appContext: Context,
    res: Int,
    private val textViewResId: Int,
    private val courts: List<Court>
) : ArrayAdapter<Court>(appContext, res, textViewResId, courts), Filterable {
    private var courtsFiltered = courts

    override fun getCount(): Int = courtsFiltered.size

    override fun getItem(position: Int): Court = courtsFiltered[position]

    override fun getItemId(position: Int): Long = courtsFiltered[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        view.findViewById<TextView>(textViewResId).text = getItem(position).name

        return view
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            val results = Filter.FilterResults()
            val prefix = constraint.toString().lowercase()
            courtsFiltered = if (prefix.isEmpty()) courts else {
                courts.filter {
                    it.name.contains(constraint.toString(), true)
                }
            }

            results.values = courtsFiltered
            results.count = courtsFiltered.size

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults?) {
            courtsFiltered = results?.values as? List<Court> ?: courts

            notifyDataSetChanged()
        }
    }
}