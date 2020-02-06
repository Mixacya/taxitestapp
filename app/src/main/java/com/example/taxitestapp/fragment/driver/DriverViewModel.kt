package com.example.taxitestapp.fragment.driver

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taxitestapp.data.Driver
import com.example.taxitestapp.rest.ApiRepository
import com.example.taxitestapp.utils.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

private const val TAG = "DriverViewModel"

class DriverViewModel : ViewModel() {

    var start = 0
    var step = 25

    val driverArray = MutableLiveData<ArrayList<Driver>>().apply {
//        val driver1 = Driver(1,1,"One Title", "", "")
//        val driver2 = Driver(2,2,"Two Title", "", "")
//        val driver3 = Driver(3,3,"Three Title", "", "")
//        postValue(arrayListOf(driver1, driver2, driver3))
    }

    private val compositeDisposable = CompositeDisposable()



    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun loadMoreDrivers() {
        ApiRepository.service.getDriverArray(start, step)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val list = driverArray.value ?: arrayListOf()
                list.addAll(it)
                driverArray.postValue(list)
                start = step;
                step += 25
            }, {
                Log.e(TAG, "Can't load more drivers", it)
            }).addTo(compositeDisposable)
    }
}