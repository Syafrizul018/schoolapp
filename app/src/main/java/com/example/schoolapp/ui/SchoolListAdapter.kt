package com.example.schoolapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.model.School

class SchoolListAdapter(
    private val onItemClickListener: (School) -> Unit
): ListAdapter<School, SchoolListAdapter.SchoolViewModel>(WORDS_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolViewModel {
        return SchoolViewModel.create(parent)
    }

    override fun onBindViewHolder(holder: SchoolViewModel, position: Int) {
        val school = getItem(position)
        holder.bind(school)
        holder.itemView.setOnClickListener {
            onItemClickListener(school)
        }
    }

    class SchoolViewModel (itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nametextview: TextView = itemView.findViewById(R.id.nametextview)
        private val addrestextview: TextView = itemView.findViewById(R.id.addresstextview)
        private val streettextview: TextView = itemView.findViewById(R.id.streettextview)

        fun bind(school: School?) {
            nametextview.text = school?.name
            addrestextview.text = school?.address
            streettextview.text = school?.street

        }

        companion object {
            fun create(parent: ViewGroup): SchoolListAdapter.SchoolViewModel {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_school, parent, false)
                return SchoolViewModel(view)
            }
        }
    }
        companion object{
            private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<School>(){
                override fun areItemsTheSame(oldItem: School, newItem: School): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: School, newItem: School): Boolean {
                    return oldItem.id == newItem.id
                }
            }
        }
    }
