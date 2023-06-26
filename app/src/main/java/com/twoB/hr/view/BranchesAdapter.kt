package com.twoB.hr.view
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.twoB.hr.R

class BranchesAdapter() :
    BaseAdapter() {
    private val fruitList: ArrayList<String> = ArrayList()
    override fun getCount(): Int {
        return fruitList.size
    }

    override fun getItem(i: Int): Any {
        return i
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }
    fun submitList(list: List<String>) {
        fruitList.addAll(list)
    }


    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View? {
        val rootView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.spinner_item, viewGroup, false)
        val txtName = rootView.findViewById<TextView>(R.id.name)
        txtName.text = fruitList[i]
        return rootView
    }
}