/*
 * Copyright 2021 Stream.IO, Inc. All Rights Reserved.
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

package io.getstream.avengerschat.view.user

import androidx.annotation.WorkerThread
import io.getstream.avengerschat.extensions.extraData
import io.getstream.avengerschat.model.Avenger
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.await
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.utils.onSuccessSuspend
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class UserProfileEditRepository @Inject constructor(
    private val chatClient: ChatClient,
    private val dispatcher: CoroutineDispatcher,
) {

    init {
        Timber.d("injection UserProfileEditRepository")
    }

    /**
     * Updates a specific user for updating the new profile image.
     */
    @WorkerThread
    fun updateUser(avenger: Avenger, newProfileUrl: String) = flow {
        val user = User(
            id = avenger.id,
            extraData = avenger.extraData(newProfileUrl)
        )
        val result = chatClient.updateUser(user).await()
        result.onSuccessSuspend {
            emit(result.data())
        }
    }.flowOn(dispatcher)
}
