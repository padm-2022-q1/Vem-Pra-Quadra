package com.reis.vinicius.vempraquadra.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reis.vinicius.vempraquadra.databinding.FragmentFeedItemBinding

class FeedListItemAdapter : RecyclerView.Adapter<FeedListItemAdapter.Holder>() {
    inner class Holder(itemBinding: FragmentFeedItemBinding):
        RecyclerView.ViewHolder(itemBinding.root){
            val profilePicImage = itemBinding.imageFeedItemProfilePic
            val title = itemBinding.textFeedItemTitle
            val content = itemBinding.textFeedItemContent
            val createdDate = itemBinding.textFeedItemCreatedDate
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(FragmentFeedItemBinding.inflate(
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