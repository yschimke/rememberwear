package com.google.wear.rememberwear.api

import com.google.wear.rememberwear.db.Todo
import retrofit2.http.GET

// https://api.rememberthemilk.com/services/rest/\?method\=rtm.lists.getList
interface RememberTheMilkService {
    @GET("v3/users/me")
    suspend fun todos(): List<Todo>
}