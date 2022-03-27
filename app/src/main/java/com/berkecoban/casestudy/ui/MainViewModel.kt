package com.berkecoban.casestudy.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.berkecoban.casestudy.model.ApiModelPost
import com.berkecoban.casestudy.service.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val service: Service
) : ViewModel() {

    var mData = MutableLiveData<List<ApiModelPost>>()

    private var compositeDisposable = CompositeDisposable()


    fun fetchPosts() {
        compositeDisposable.add(service.getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { posts ->
                    mData.value = posts
                },
                { throwable ->
                    Log.e("Api Error", throwable.message ?: "onError")
                }
            ))
    }

    fun cancelDisposable() {
        compositeDisposable.dispose()
    }
}