package com.example.repetitiontest.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.annotation.RequiresApi
import com.example.repetitiontest.R
import com.example.repetitiontest.databinding.UserRoleItemBinding

class UserRoleAdapter(
    var roleList: ArrayList<String>,
    private val context: Context
) : BaseAdapter() {
    override fun getCount(): Int = roleList.size

    override fun getItem(p0: Int): Any = roleList[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val item: UserRoleItemBinding = if (view == null) {
            UserRoleItemBinding.inflate(
                LayoutInflater.from(parent?.context),
                parent,
                false
            )
        } else {
            UserRoleItemBinding.bind(view)
        }
        item.tv.text = roleList[position]
        if (position == 0)
            item.tv.setTextColor(context.getColor(R.color.hint_color))
        return item.root
    }

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }
}