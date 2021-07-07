package com.speechpeach.arestmanager.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.speechpeach.arestmanager.databinding.ItemUserBinding
import com.speechpeach.arestmanager.models.User
import com.speechpeach.arestmanager.utils.view.QuickCalendar
import com.speechpeach.arestmanager.utils.view.day
import com.speechpeach.arestmanager.utils.view.month
import com.speechpeach.arestmanager.utils.view.year

class UsersAdapter(private val itemClickListener: ItemClickListener) : ListAdapter<User, UsersAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.apply {
                setOnClickListener {
                    itemClickListener.onItemClick(getItem(bindingAdapterPosition))
                }
                setOnLongClickListener {
                    itemClickListener.onItemLongClick(getItem(bindingAdapterPosition))
                }
            }
        }

        fun bind(user: User) {
            binding.apply {

                val calendar = QuickCalendar.get(user.date)

                userDate.text = ("${calendar.day()}/${calendar.month()}/${calendar.year()}")
                userFullName.text = ("${user.secondName} ${user.name}")
                userPassport.text = ("${user.number} ${user.set}")
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User) =
            oldItem == newItem
    }

    interface ItemClickListener {
        fun onItemClick(user: User)
        fun onItemLongClick(user: User): Boolean
    }
}