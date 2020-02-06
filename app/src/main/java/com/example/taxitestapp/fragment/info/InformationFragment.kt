package com.example.taxitestapp.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.taxitestapp.R
import com.example.taxitestapp.rxbus.RxBus
import com.example.taxitestapp.rxbus.RxDistance
import com.example.taxitestapp.rxbus.RxEvent
import com.example.taxitestapp.utils.LocationPreferences
import com.example.taxitestapp.utils.addTo
import com.example.taxitestapp.utils.string
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_info.*

class InformationFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onStart() {
        super.onStart()
        context?.let {
            tvInfoCoordinates.text = LocationPreferences.getLastLocation(it)
            tvInfoDistance.text = String.format("%.2f m", LocationPreferences.getDistance(it))
        }
        RxBus.listen(RxEvent.EventLocation::class.java).subscribe {
            val location = it.location
            location?.let {
                tvInfoCoordinates.text = it.string()
            }

        }.addTo(compositeDisposable)
        RxBus.listen(RxDistance.EventDistance::class.java).subscribe {
            tvInfoDistance.text = String.format("%.2f m", it.distance)
        }.addTo(compositeDisposable)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

}