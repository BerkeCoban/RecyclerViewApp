package com.berkecoban.casestudy.service

import com.berkecoban.casestudy.model.ApiModelPost
import io.reactivex.Observable
import retrofit2.http.GET




interface Service {
    @GET("posts")
    fun getPosts(): Observable<List<ApiModelPost>>
}