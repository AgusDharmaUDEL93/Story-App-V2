package com.udeldev.storyapp.helper.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.udeldev.storyapp.R
import com.udeldev.storyapp.model.entity.ListStoryItem

class StoryListAdapter : RecyclerView.Adapter<StoryListAdapter.StoryListViewHolder>() {

    private var _storyList : List<ListStoryItem?>? = emptyList()
    @SuppressLint("NotifyDataSetChanged")
    fun setStoryList (value : List<ListStoryItem?>?){
        _storyList = value
        notifyDataSetChanged()
    }


    inner class StoryListViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imageStoryList : ImageView = itemView.findViewById(R.id.image_story_list)
        val dateStoryList : TextView = itemView.findViewById(R.id.text_story_list_date)
        val titleStoryList :TextView = itemView.findViewById(R.id.text_story_list_title)
        val descStoryList :TextView = itemView.findViewById(R.id.text_story_list_desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_story, parent, false)
        return StoryListViewHolder(view)
    }

    override fun getItemCount(): Int = _storyList?.size ?: 0

    override fun onBindViewHolder(holder: StoryListViewHolder, position: Int) {
        holder.titleStoryList.text = _storyList?.get(position)?.name
        holder.descStoryList.text = _storyList?.get(position)?.description
        holder.dateStoryList.text = _storyList?.get(position)?.createdAt
        Glide.with(holder.itemView.context)
            .load(_storyList?.get(position)?.photoUrl ?: "https://i.stack.imgur.com/l60Hf.png")
            .into(holder.imageStoryList)
    }
}