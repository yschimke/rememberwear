/*
 * Copyright 2021 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.wear.soyted.app.api

import com.google.wear.soyted.app.api.model.auth.AuthRsp
import com.google.wear.soyted.app.api.model.auth.FrobRsp
import com.google.wear.soyted.app.api.model.lists.ListsRsp
import com.google.wear.soyted.app.api.model.post.PostRsp
import com.google.wear.soyted.app.api.model.tags.TagsRsp
import com.google.wear.soyted.app.api.model.tasks.TasksRsp
import com.google.wear.soyted.app.api.model.timeline.TimelineRsp
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// https://api.rememberthemilk.com/services/rest/\?method\=rtm.lists.getList
interface RememberTheMilkService {
    @GET("/services/rest/?method=rtm.auth.getFrob")
    suspend fun frob(): FrobRsp

    @GET("/services/rest/?method=rtm.auth.checkToken")
    suspend fun auth(): AuthRsp

    @GET("/services/rest/?method=rtm.auth.getToken")
    suspend fun token(
        @Query("frob") frob: String,
    ): AuthRsp

    @GET("/services/rest/?method=rtm.lists.getList")
    suspend fun lists(): ListsRsp

    @GET("/services/rest/?method=rtm.tasks.getList")
    suspend fun tasks(@Query("filter") tag: String? = null): TasksRsp

    @GET("/services/rest/?method=rtm.tags.getList")
    suspend fun tags(): TagsRsp

    @POST("/services/rest/?method=rtm.timelines.create")
    suspend fun timeline(): TimelineRsp

    @POST("/services/rest/?method=rtm.tasks.setTags")
    suspend fun setTags(
        @Query("timeline") timeline: String,
        @Query("list_id") list_id: String,
        @Query("taskseries_id") taskseries_id: String,
        @Query("task_id") task_id: String,
        @Query("tags") tags: String,
    ): PostRsp

    @POST("/services/rest/?method=rtm.tasks.add")
    suspend fun addTask(
        @Query("timeline") timeline: String,
        @Query("name") name: String,
        @Query("parse") parse: String,
        @Query("list_id") list_id: String,
    ): PostRsp

    @POST("/services/rest/?method=rtm.tasks.complete")
    suspend fun complete(
        @Query("timeline") timeline: String,
        @Query("list_id") list_id: String,
        @Query("taskseries_id") taskseries_id: String,
        @Query("task_id") task_id: String
    ): PostRsp

    @POST("/services/rest/?method=rtm.tasks.uncomplete")
    suspend fun uncomplete(
        @Query("timeline") timeline: String,
        @Query("list_id") list_id: String,
        @Query("taskseries_id") taskseries_id: String,
        @Query("task_id") task_id: String
    ): PostRsp
}