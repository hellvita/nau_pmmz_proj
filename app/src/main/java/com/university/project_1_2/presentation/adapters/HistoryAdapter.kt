package com.university.project_1_2.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.university.project_1_2.R
import com.university.project_1_2.databinding.ListItem2Binding

class HistoryAdapter(val listener: Listener): ListAdapter<QuoteModel, HistoryAdapter.Holder>(HistoryAdapter.Comparator()) {
    class Holder(view: View): RecyclerView.ViewHolder(view){
        val binding = ListItem2Binding.bind(view)
        fun bind(item: QuoteModel, listener: HistoryAdapter.Listener)= with(binding){
            tvHlist.text = "'${item.Quote}'"
            tvHtime.text = "${item.Time}"
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item2,parent,false)
        return HistoryAdapter.Holder(view)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.Holder, position: Int) {
        holder.bind(getItem(position), listener)
    }
    interface Listener{
        fun onClick(item: QuoteModel)
    }
}