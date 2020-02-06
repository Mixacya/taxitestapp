package com.example.taxitestapp.fragment.driver.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.taxitestapp.data.Driver
import kotlinx.android.synthetic.main.item_driver.view.*
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.taxitestapp.R


class DriverAdapter : RecyclerView.Adapter<DriverViewHolder>() {

    private val driverArray = mutableListOf<Driver>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_driver, parent, false)
        return DriverViewHolder(view)
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        holder.bind(driverArray[position])
    }

    override fun getItemCount(): Int {
        return driverArray.size
    }

    fun update(newList: List<Driver>) {
        val diffResult = DiffUtil.calculateDiff(DriverDiffUtils(driverArray, newList))
        driverArray.clear()
        driverArray.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

}

class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(driver: Driver) = with(itemView) {
        tvDriverTitle.text = driver.id.toString()
        tvDriverDescription.text = driver.title

        val options = RequestOptions()
            .centerCrop()
            .circleCrop()
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)


        val url = driver.thumbnailUrl + ".png"

        Glide.with(this)
//            .load("https://img.huffingtonpost.com/asset/5dcc613f1f00009304dee539.jpeg")
            .load(url)
            .apply(options)
            .into(ivDriverIcon)
    }

}