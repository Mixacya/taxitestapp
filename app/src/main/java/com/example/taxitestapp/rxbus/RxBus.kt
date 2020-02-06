package com.example.taxitestapp.rxbus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object RxBus {

    private val publisher = PublishSubject.create<Any>()

    fun publish(any: Any) {
        publisher.onNext(any)
    }

    fun <T> listen(eventType: Class<T>) : Observable<T> = publisher.ofType(eventType)

}