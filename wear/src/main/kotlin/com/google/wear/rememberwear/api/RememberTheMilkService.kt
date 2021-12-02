package com.google.wear.rememberwear.api

import com.google.wear.rememberwear.api.model.ListsRsp
import retrofit2.http.GET

// https://api.rememberthemilk.com/services/rest/\?method\=rtm.lists.getList
interface RememberTheMilkService {
    @GET("/services/rest/?method=rtm.lists.getList")
    suspend fun todos(): ListsRsp
}