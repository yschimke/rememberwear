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
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

// https://api.rememberthemilk.com/services/rest/\?method\=rtm.lists.getList
class KtorRememberTheMilkService(val baseUrl: String, val client: HttpClient): RememberTheMilkService {
    override suspend fun frob(): FrobRsp {
        return client.get("$baseUrl/services/rest/?method=rtm.auth.getFrob").body()
    }

    override suspend fun auth(): AuthRsp {
        return client.get("$baseUrl/services/rest/?method=rtm.auth.checkToken").body()
    }

    override suspend fun token(frob: String): AuthRsp {
        return client.get("$baseUrl/services/rest/?method=rtm.auth.getToken"){
            parameter("frob", frob)
        }.body()
    }

    override suspend fun lists(): ListsRsp {
        return client.get("$baseUrl/services/rest/?method=rtm.lists.getList").body()
    }

    override suspend fun tasks(tag: String?): TasksRsp {
        return client.get("$baseUrl/services/rest/?method=rtm.tasks.getList").body()
    }

    override suspend fun tags(): TagsRsp {
        return client.get("$baseUrl/services/rest/?method=rtm.tags.getList").body()
    }

    override suspend fun timeline(): TimelineRsp {
        return client.post("$baseUrl/services/rest/?method=rtm.timelines.create").body()
    }

    override suspend fun setTags(
        timeline: String,
        list_id: String,
        taskseries_id: String,
        task_id: String,
        tags: String
    ): PostRsp {
        TODO("Not yet implemented")
    }

//
//    @POST("/services/rest/?method=rtm.tasks.setTags")
//    suspend fun setTags(
//        @Query("timeline") timeline: String,
//        @Query("list_id") list_id: String,
//        @Query("taskseries_id") taskseries_id: String,
//        @Query("task_id") task_id: String,
//        @Query("tags") tags: String,
//    ): PostRsp

    override suspend fun addTask(
        timeline: String,
        name: String,
        parse: String,
        list_id: String
    ): PostRsp {
        TODO("Not yet implemented")
    }
//
//    @POST("/services/rest/?method=rtm.tasks.add")
//    suspend fun addTask(
//        @Query("timeline") timeline: String,
//        @Query("name") name: String,
//        @Query("parse") parse: String,
//        @Query("list_id") list_id: String,
//    ): PostRsp

    override suspend fun complete(
        timeline: String,
        list_id: String,
        taskseries_id: String,
        task_id: String
    ): PostRsp {
        TODO("Not yet implemented")
    }
//
//    @POST("/services/rest/?method=rtm.tasks.complete")
//    suspend fun complete(
//        @Query("timeline") timeline: String,
//        @Query("list_id") list_id: String,
//        @Query("taskseries_id") taskseries_id: String,
//        @Query("task_id") task_id: String
//    ): PostRsp

    override suspend fun uncomplete(
        timeline: String,
        list_id: String,
        taskseries_id: String,
        task_id: String
    ): PostRsp {
        TODO("Not yet implemented")
    }
//
//    @POST("/services/rest/?method=rtm.tasks.uncomplete")
//    suspend fun uncomplete(
//        @Query("timeline") timeline: String,
//        @Query("list_id") list_id: String,
//        @Query("taskseries_id") taskseries_id: String,
//        @Query("task_id") task_id: String
//    ): PostRsp
}
