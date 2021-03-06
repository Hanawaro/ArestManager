package com.speechpeach.arestmanager.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.speechpeach.arestmanager.databinding.ItemArestBinding
import com.speechpeach.arestmanager.models.Arest
import com.speechpeach.arestmanager.utils.*

class ArestsAdapter(private val itemClickListener: ItemClickListener) : ListAdapter<Arest, ArestsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: ItemArestBinding) : RecyclerView.ViewHolder(binding.root){

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

        fun bind(arest: Arest) {
            binding.apply {

                val calendar = QuickCalendar.get(arest.registrationDate)

                arestDate.text = ("${calendar.day()}/${calendar.month()}/${calendar.year()}")
                arestName.text = ValueConstants.Organization.codes[arest.organizationID]
                arestOwner.text = ("${arest.userSecondName} ${arest.userName}")

                arestStatus.text = when(arest.status.toArestStatusType()) {
                    Arest.Type.Active    -> ValueConstants.ArestStatus.active
                    Arest.Type.Canceled  -> ValueConstants.ArestStatus.canceled
                    Arest.Type.Completed -> ValueConstants.ArestStatus.completed
                }
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<Arest>() {
        override fun areItemsTheSame(oldItem: Arest, newItem: Arest) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Arest, newItem: Arest) =
            oldItem == newItem
    }

    interface ItemClickListener {
        fun onItemClick(arest: Arest)
        fun onItemLongClick(arest: Arest): Boolean
    }
}