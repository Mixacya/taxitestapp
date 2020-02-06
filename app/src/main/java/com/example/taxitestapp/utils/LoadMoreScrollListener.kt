package com.example.taxitestapp.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class LoadMoreScrollListener : RecyclerView.OnScrollListener() {

    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var pastVisibleItems = 0
    var loading = false

    abstract fun onLoadData()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy > 0) {
            val layoutManager = recyclerView.layoutManager
            if (layoutManager is LinearLayoutManager) {
                visibleItemCount = layoutManager.getChildCount()
                totalItemCount = layoutManager.getItemCount()
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                if (!loading) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loading = true;
                        onLoadData()
                    }
                }
            }
        }
    }
}