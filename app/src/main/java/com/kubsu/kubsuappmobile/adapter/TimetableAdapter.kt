package com.kubsu.kubsuappmobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kubsu.kubsuappmobile.R
import com.kubsu.kubsuappmobile.data.model.Timetable
import com.kubsu.kubsuappmobile.databinding.ListItemBinding

class TimetableAdapter : ListAdapter<Timetable, TimetableAdapter.Holder>(Comparator()) {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ListItemBinding.bind(view)

        fun bind(timetable: Timetable) = with(binding) {
            title.text = timetable.course.name
            description.text = timetable.course.courseType.name
        }
    }

    class Comparator : DiffUtil.ItemCallback<Timetable>() {
        override fun areItemsTheSame(oldItem: Timetable, newItem: Timetable): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Timetable, newItem: Timetable): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}