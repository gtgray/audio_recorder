package tk.atna.audiorecorder.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tk.atna.audiorecorder.R
import tk.atna.audiorecorder.databinding.RecordItemBinding
import tk.atna.audiorecorder.domain.model.Record

class RecordViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    private val onPlay: ((Record) -> Unit),
    private val onForward: ((Record) -> Unit),
    private val binding: RecordItemBinding = RecordItemBinding.inflate(inflater, parent, false)
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var item: Record

    init {
        binding.btnPlay.setOnClickListener { onPlay.invoke(item) }
        binding.btnForward.setOnClickListener { onForward.invoke(item) }
    }

    fun bind(newItem: Record) {
        item = newItem
        with(binding) {
            tvTitle.text = item.title
            btnForward.isEnabled = item.enabled
            btnPlay.isEnabled = item.enabled || item.playing
            btnPlay.setImageResource(
                if (item.playing) R.drawable.svg_stop else R.drawable.svg_play
            )
        }
    }
}