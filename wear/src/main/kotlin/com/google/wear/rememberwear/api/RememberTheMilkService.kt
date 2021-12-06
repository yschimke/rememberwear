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

package com.google.wear.rememberwear.api

import com.google.wear.rememberwear.api.model.lists.ListsRsp
import com.google.wear.rememberwear.api.model.tags.TagsRsp
import com.google.wear.rememberwear.api.model.tasks.TasksRsp
import retrofit2.http.GET
import retrofit2.http.Query

// https://api.rememberthemilk.com/services/rest/\?method\=rtm.lists.getList
interface RememberTheMilkService {
    @GET("/services/rest/?method=rtm.lists.getList")
    suspend fun lists(): ListsRsp

    @GET("/services/rest/?method=rtm.tasks.getList")
    suspend fun tasks(@Query("filter") tag: String? = null): TasksRsp

    @GET("/services/rest/?method=rtm.tags.getList")
    suspend fun tags(): TagsRsp
}