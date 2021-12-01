package com.google.wear.rememberwear.api

import com.google.wear.rememberwear.api.model.lists.ListsRsp
import com.google.wear.rememberwear.api.model.tags.TagsRsp
import com.google.wear.rememberwear.api.model.tasks.TasksRsp
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryName

// https://api.rememberthemilk.com/services/rest/\?method\=rtm.lists.getList
interface RememberTheMilkService {
    @GET("/services/rest/?method=rtm.lists.getList")
    suspend fun lists(): ListsRsp

    @GET("/services/rest/?method=rtm.tasks.getList")
    suspend fun tasks(@Query("filter") tag: String? = null): TasksRsp

    @GET("/services/rest/?method=rtm.tags.getList")
    suspend fun tags(): TagsRsp
}