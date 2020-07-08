package com.expandable.recycler.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.expandable.recycler.*
import com.expandable.recycler.interfaces.OnExpandableClick
import com.expandable.recycler.model.ExpandableModel

class ExpandableAdapter(
    val data: List<ExpandableModel>,
    val onExpandableClick: OnExpandableClick
) :
    ListAdapter<ExpandableModel, RecyclerView.ViewHolder>(ItemDiffCallback()) {


    private var currentData = ArrayList<ExpandableModel>()
    private var parentTextColor = 0
    private var childTextColor = 0
    private var parentDividerColor = 0
    private var childDividerColor = 0
    private var childDividerVisibility = View.GONE
    private var parentDividerVisibility = 0
    private var parentTextSize = 0F
    private var childTextSize = 0F

    init {
        currentData = generalizingData(data)
    }

    private fun generalizingData(
        data: List<ExpandableModel>,
        index: Int = 0
    ): ArrayList<ExpandableModel> {
        var i = 0
        while (i < data.size) {
            var count = 0
            currentData.add(data[i++])

            for (k in i..i + 3) {
                if (k < data.size && data[k].type == CHILD)
                    count++
                else break
            }

            if (count > 3) {
                for (d in data.subList(i, i + 3))
                    currentData.add(data[i++])

                currentData.add(ExpandableModel(-1, "none", EXPAND))
                i++

                while (i < data.size && data[i].type != PARENT)
                    i++
            }
        }

        return currentData
    }

    private fun expandGroup(item: ExpandableModel, position: Int) {
        var pos = position
        var p = 0
        var inserted = 0
        while (p < data.size) {
            if (data[p].id == item.id) {
                p++
                while (p < data.size && data[p].type == CHILD) {
                    currentData.add(pos, data[p])
                    notifyItemInserted(pos++)
                    p++
                    inserted++
                }
                break
            }
            p++
        }

        currentData[position + inserted] = ExpandableModel(-1, "none", COLLAPSE)
        notifyItemChanged(position + inserted)
    }

    private fun collapseGroup(item: ExpandableModel) {
        var collapseTo = 0
        var index = currentData.size - 1
        var pos = 0
        while (index >= 0) {
            if (index >= 0 && currentData[index].id == item.id) {
                pos = index
                while (index >= 0 && currentData[index].type == CHILD) {
                    collapseTo++
                    index--
                }

                break
            }
            index--

        }

        collapseTo -= 4
        while (collapseTo >= 0) {
            currentData.removeAt(pos)
            notifyItemRemoved(pos--)
            collapseTo--
        }

        pos++
        currentData[pos] = ExpandableModel(-1, "none", EXPAND)
        notifyItemChanged(pos)

    }


    inner class ParentItemViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private var text: TextView? = null
        private var divider: View? = null

        init {
            text = view.findViewById(R.id.title)
            divider = view.findViewById(R.id.view)

            if (parentTextColor != 0) {
                text!!.setTextColor(parentTextColor)
            }

            if (parentDividerColor != 0) {
                divider!!.setBackgroundColor(parentDividerColor)
            }

            if (parentDividerVisibility != -1) {
                divider!!.visibility = parentDividerVisibility
            }

            if (parentTextSize != 0F) {
                text!!.textSize = parentTextSize
            }
        }

        fun bind(item: ExpandableModel) {
            text!!.text = item.title
            text!!.setOnClickListener {
                onExpandableClick.onItemClicked(item, adapterPosition)

            }
        }
    }


    inner class ItemViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private var text: TextView? = null
        private var divider: View? = null

        init {
            text = view.findViewById(R.id.title)
            divider = view.findViewById(R.id.view)

            if (childTextColor != 0) {
                text!!.setTextColor(childTextColor)
            }

            if (childDividerColor != 0) {
                divider!!.setBackgroundColor(childDividerColor)
            }

            if (childDividerVisibility != -1) {
                divider!!.visibility = childDividerVisibility
            }

            if (childTextSize != 0F) {
                text!!.textSize = childTextSize
            }
        }

        fun bind(item: ExpandableModel) {
            text!!.text = item.title
            text!!.setOnClickListener {
                onExpandableClick.onItemClicked(item, adapterPosition)

            }
        }
    }

    inner class ExpandViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private var expand: ImageView? = null

        init {
            expand = view.findViewById(R.id.expand)
        }

        fun bind(item: ExpandableModel) {

            expand!!.setOnClickListener {
                onExpandableClick.onItemExpanded(item, adapterPosition)
                expandGroup(item, adapterPosition)
            }

        }
    }

    inner class CollapseViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        private var collapse: ImageView? = null

        init {
            collapse = view.findViewById(R.id.collapse)
        }

        fun bind(item: ExpandableModel) {
            collapse!!.setOnClickListener {
                onExpandableClick.onItemCollapsed(item, adapterPosition)
                collapseGroup(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        when (viewType) {
            PARENT -> {

                return ParentItemViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_parent, parent, false)
                )
            }
            CHILD -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_child, parent, false)

                val params = view.layoutParams as ViewGroup.MarginLayoutParams
                params.marginStart = 30

                return ItemViewHolder(
                    view
                )
            }
            EXPAND -> {
                return ExpandViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_expand, parent, false)
                )
            }
            else -> {
                return CollapseViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_collapse, parent, false)
                )
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return currentData[position].type
    }

    override fun getItemCount(): Int {
        return currentData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ParentItemViewHolder -> {
                currentData[position].let { holder.bind(it) }
            }
            is ItemViewHolder -> {
                currentData[position].let { holder.bind(it) }
            }
            is ExpandViewHolder -> {
                currentData[position - 1].let { holder.bind(it) }
            }
            is CollapseViewHolder -> {
                currentData[position - 1].let { holder.bind(it) }
            }
        }
    }




    fun setParentTextColor(color: Int) {
        parentTextColor = color
    }

    fun setChildTextColor(color: Int) {
        childTextColor = color
    }

    fun setParentDividerColor(color: Int) {
        parentDividerColor = color
    }

    fun setChildDividerColor(color: Int) {
        childDividerColor = color
    }

    fun setParentDividerVisibility(visibility: Int) {
        parentDividerVisibility = visibility
    }

    fun setChildDividerVisibility(visibility: Int) {
        childDividerVisibility = visibility
    }

    fun setParentTextSize(size: Float) {
        parentTextSize = size
    }

    fun setChildTextSize(size: Float) {
        childTextSize = size
    }
}

private class ItemDiffCallback : DiffUtil.ItemCallback<ExpandableModel>() {

    override fun areItemsTheSame(oldItem: ExpandableModel, newItem: ExpandableModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ExpandableModel, newItem: ExpandableModel): Boolean {
        return oldItem == newItem
    }
}

