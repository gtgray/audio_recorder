package tk.atna.audiorecorder.presentation.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import tk.atna.audiorecorder.domain.model.Record

class RecordsAdapter(
    context: Context,
    private val onPlay: ((Record) -> Unit),
    private val onForward: ((Record) -> Unit)
) : ListAdapter<Record, RecordViewHolder>(ItemDiffCallback()) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        return RecordViewHolder(inflater, parent, onPlay, onForward)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    private class ItemDiffCallback : DiffUtil.ItemCallback<Record>() {

        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem.link == newItem.link
        }

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem == newItem
        }
    }
}