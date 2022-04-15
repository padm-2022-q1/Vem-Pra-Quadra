package com.reis.vinicius.vempraquadra.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reis.vinicius.vempraquadra.databinding.FragmentFeedItemBinding

class FeedItemAdapter : RecyclerView.Adapter<FeedItemAdapter.FeedItemHolder>() {
    inner class FeedItemHolder(itemBinding: FragmentFeedItemBinding):
        RecyclerView.ViewHolder(itemBinding.root){
            val profilePicImage = itemBinding.imageFeedItemProfilePic
            val title = itemBinding.textFeedItemTitle
            val content = itemBinding.textFeedItemContent
            val createdDate = itemBinding.textFeedItemCreatedDate
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemHolder {
        return FeedItemHolder(FragmentFeedItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: FeedItemHolder, position: Int) {
        return
    }

    override fun getItemCount(): Int {
        return 10
    }
}