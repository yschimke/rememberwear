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

package com.google.wear.soyted.api

import com.google.wear.soyted.api.model.tasks.TasksRsp
import com.google.wear.soyted.login.AuthRepository
import java.io.IOException

class AuthedService(val delegate: RememberTheMilkService, val authRepository: AuthRepository) :
    RememberTheMilkService by delegate {

    override suspend fun tasks(tag: String?): TasksRsp {
        return delegate.tasks(tag).also {
            if (it.stat != "ok") {
                if (it.err?.code == "98") {
                    println("401: Logging out")
                    authRepository.setToken(null)
                }
                throw IOException("Error: ${it.err?.msg}")
            }
        }
    }
}