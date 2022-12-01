package com.university.project_1_2.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.university.project_1_2.R
import com.university.project_1_2.databinding.ListItem1Binding
import com.university.project_1_2.presentation.fragments.GlistRandFragment

class QuotesListAdapter(val listener: Listener): ListAdapter<QuoteModel, QuotesListAdapter.Holder>(Comparator())
{
    class Holder(view: View): RecyclerView.ViewHolder(view){
        val binding = ListItem1Binding.bind(view)
        fun bind(item: QuoteModel, listener: Listener)= with(binding){
            tvHlist.text = "'${item.Quote}'"
            itemView.setOnClickListener {
                listener.onClick(item)
            }
        }
    }
    class Comparator: DiffUtil.ItemCallback<QuoteModel>(){
        override fun areItemsTheSame(oldItem: QuoteModel, newItem: QuoteModel): Boolean {
            return oldItem.ID == newItem.ID
        }

        override fun areContentsTheSame(oldItem: QuoteModel, newItem: QuoteModel): Boolean {
            return oldItem.ID == newItem.ID
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item1,parent,false)
        return Holder(view)
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), listener)
    }
    interface Listener{
        fun onClick(item: QuoteModel)
    }
}