package com.chiore.notesapp.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chiore.noteapp.R
import com.chiore.noteapp.databinding.HomeItemBinding
import com.chiore.notesapp.data.model.Notes
import com.chiore.notesapp.ui.fragments.HomeFragmentDirections

class SearchFragmentRvAdapter() :
    ListAdapter<Notes, SearchFragmentRvAdapter.SearchViewHolder>(DiffUtilCallBack()) {

    class SearchViewHolder(val binding: HomeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notes: Notes) {
            with(binding) {
                titleTv.text = notes.title
                noteTv.text = notes.notes

                if (notes.noteImage != null) {
                    homeRvItemIv.visibility = View.VISIBLE
                    Glide.with(root).load(notes.noteImage).into(homeRvItemIv)
                }

                adapterPosition

                when (notes.colors) {
                    1 -> {
                        homeParentLayout.setBackgroundResource(R.color.blue)
                    }
                    2 -> {
                        homeParentLayout.setBackgroundResource(R.color.yellow)
                    }
                    3 -> {
                        homeParentLayout.setBackgroundResource(R.color.crane_red)
                    }
                    4 -> {
                        homeParentLayout.setBackgroundResource(R.color.teal)
                    }
                    5 -> {
                        homeParentLayout.setBackgroundResource(R.color.crane_purple)
                    }
                    else -> {
                        homeParentLayout.setBackgroundResource(R.color.txt_color)
                    }
                }

//                itemView.setOnClickListener { view ->
//                    val action = HomeFragmentDirections
//                        .actionHomeFragmentToAddFragment(notes)
//                    Navigation.findNavController(view).navigate(action)
//                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            HomeItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)

//        holder.binding.deleteNote.setOnClickListener {
//            onItemClickListener?.let { it(position) }
//        }
    }

//    private var onItemClickListener: ((Int) -> Unit)? = null
//
//    fun setOnItemClickListener(listener: (Int) -> Unit) {
//        onItemClickListener = listener
//    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Notes>() {
        override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem == newItem
        }

    }

}