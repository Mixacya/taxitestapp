package com.example.taxitestapp.fragment.driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taxitestapp.R
import com.example.taxitestapp.fragment.driver.adapter.DriverAdapter
import com.example.taxitestapp.utils.LoadMoreScrollListener
import kotlinx.android.synthetic.main.fragment_driver.*

class DriverFragment : Fragment() {

    private lateinit var driverViewModel: DriverViewModel
    private val driverAdapter = DriverAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        driverViewModel = ViewModelProviders.of(this).get(DriverViewModel::class.java)
        return inflater.inflate(R.layout.fragment_driver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvDriverList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rvDriverList.addOnScrollListener(loadMoreScrollListener)
        rvDriverList.adapter = driverAdapter

        driverViewModel.driverArray.observe(this, Observer {
            driverAdapter.update(it)
            loadMoreScrollListener.loading = false
        })

        updateList()
    }

    private fun updateList() {
        driverViewModel.loadMoreDrivers()
    }

    private val loadMoreScrollListener = object : LoadMoreScrollListener() {
        override fun onLoadData() {
            updateList()
        }

    }

}