package com.reis.vinicius.vempraquadra.model.court

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filterable
import android.widget.TextView

class CourtDropdownListAdapter(
    appContext: Context,
    res: Int,
    private val textViewResId: Int,
    private val courts: List<Court>
) : ArrayAdapter<Court>(appContext, res, textViewResId, courts), Filterable {

    override fun getItem(position: Int): Court = courts[position]

    override fun getItemId(position: Int): Long = courts[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        view.findViewById<TextView>(textViewResId).text = getItem(position).name

        return view
    }
}