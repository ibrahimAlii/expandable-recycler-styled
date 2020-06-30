package com.expandable.recycler

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.expandable.recycler.adapter.ExpandableAdapter
import com.expandable.recycler.interfaces.OnExpandableClick
import com.expandable.recycler.model.ExpandableModel


class MainActivity : AppCompatActivity(), OnExpandableClick {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val items = ArrayList<ExpandableModel>()
        items.add(ExpandableModel(0, "Tech", PARENT))
        items.add(ExpandableModel(1, "Prog", CHILD))
        items.add(ExpandableModel(2, "DDD", CHILD))
        items.add(ExpandableModel(3, "BBB", CHILD))
        items.add(ExpandableModel(4, "CCC", CHILD))
        items.add(ExpandableModel(5, "TTT", CHILD))
        items.add(ExpandableModel(6, "TTT", CHILD))
        items.add(ExpandableModel(6, "vvvv", CHILD))
        items.add(ExpandableModel(11, "Politics", PARENT))
        items.add(ExpandableModel(12, "Pol", CHILD))
        items.add(ExpandableModel(13, "it", CHILD))
        items.add(ExpandableModel(14, "ic", CHILD))
        items.add(ExpandableModel(15, "s", CHILD))
        items.add(ExpandableModel(16, "Fun", PARENT))
        items.add(ExpandableModel(17, "Football", CHILD))
        items.add(ExpandableModel(18, "Basket", CHILD))
        items.add(ExpandableModel(122, "123", CHILD))
        items.add(ExpandableModel(19, "Fill", PARENT))
        items.add(ExpandableModel(20, "STY", PARENT))

        val recyclerView = findViewById<RecyclerView>(R.id.recycler)


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ExpandableAdapter(this, items, this)

    }

    val TAG = this::class.java.simpleName
    override fun onItemClicked(item: ExpandableModel, position: Int) {
        Log.d(TAG, "Clicked $item $position")
    }

    override fun onItemExpanded(item: ExpandableModel, position: Int) {
        Log.d(TAG, "Expanded $item $position")
    }

    override fun onItemCollapsed(item: ExpandableModel, position: Int) {
        Log.d(TAG, "Collapsed $item $position")
    }

}