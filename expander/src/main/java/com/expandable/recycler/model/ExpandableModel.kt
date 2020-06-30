package com.expandable.recycler.model

import com.expandable.recycler.PARENT

data class ExpandableModel(
    val id: Int = 0,
    val title: String = "",
    val type: Int = PARENT,
    val url: String = ""
)