package com.maryam.sample.ui.postList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maryam.sample.R
import com.maryam.sample.model.Post
import kotlinx.android.synthetic.main.post_item_rcy.view.*

class PostAdapter : ListAdapter<Post, PostAdapter.ViewHolder>(DiffCallback()) {

    private lateinit var clickListener: (item:Post) -> Unit


    fun setClickListenerRoot(clickListener: (item:Post) -> Unit) {
        this.clickListener = clickListener
    }
    inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item:Post) {
            itemView.txt_id.text=item.id.toString()
            itemView.txt_title.text=item.title
            itemView.setOnClickListener{
                clickListener(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.post_item_rcy, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun clearList() {
        submitList(ArrayList())
    }
}