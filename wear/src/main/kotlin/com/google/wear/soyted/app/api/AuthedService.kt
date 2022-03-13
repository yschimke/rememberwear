/*
 * Copyright 2021-2022 Google Inc. All rights reserved.
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

import com.google.wear.soyted.app.api.model.Rsp
import com.google.wear.soyted.app.api.model.auth.AuthRsp
import com.google.wear.soyted.app.api.model.auth.FrobRsp
import com.google.wear.soyted.app.api.model.lists.ListsRsp
import com.google.wear.soyted.app.api.model.tags.TagsRsp
import com.google.wear.soyted.app.api.model.tasks.TasksRsp
import com.google.wear.soyted.app.api.model.timeline.TimelineRsp
import com.google.wear.soyted.ui.login.AuthRepository
import java.io.IOException

class AuthedService(val delegate: RememberTheMilkService, val authRepository: AuthRepository) :
    RememberTheMilkService by delegate {

    override suspend fun tasks(tag: String?): TasksRsp {
        return delegate.tasks(tag).also {
            it.validate()
        }
    }

    override suspend fun auth(): AuthRsp {
        return delegate.auth().also {
            it.validate()
        }
    }

    override suspend fun frob(): FrobRsp {
        return delegate.frob().also {
            it.validate()
        }
    }

    override suspend fun token(frob: String): AuthRsp {
        return delegate.token(frob).also {
            it.validate()
        }
    }

    override suspend fun lists(): ListsRsp {
        return delegate.lists().also {
            it.validate()
        }
    }

    override suspend fun tags(): TagsRsp {
        return delegate.tags().also {
            it.validate()
        }
    }

    override suspend fun timeline(): TimelineRsp {
        return delegate.timeline().also {
            it.validate()
        }
    }

    fun Rsp.validate() {
        if (stat != "ok") {
            if (err?.code == "98") {
                println("401: Logging out")
                authRepository.setToken(null)
            }
            throw IOException("Error: ${err?.msg}")
        }
    }
}