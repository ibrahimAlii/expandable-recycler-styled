package com.expandable.recycler.interfaces

import com.expandable.recycler.model.ExpandableModel

interface OnExpandableClick {

    fun onItemClicked(item: ExpandableModel, position: Int)

    fun onItemExpanded(item: ExpandableModel, position: Int)

    fun onItemCollapsed(item: ExpandableModel, position: Int)

}