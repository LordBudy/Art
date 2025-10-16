package com.example.artphotoframe.core.data

import android.util.LruCache
import com.example.artphotoframe.core.data.models.metropolitan.MetObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext

class MetRepository (
    private val api: MetApi,
    private val io: CoroutineDispatcher = Dispatchers.IO
) {
    private val semaphore = Semaphore(permits = 8)
    private val memory = LruCache<Int, MetObject>(500)

    suspend fun searchIds(query: String): List<Int> =
        withContext(io) { api.search(q = query, hasImages = true).objectIDs.orEmpty() }

    suspend fun getObjectsBatched(ids: List<Int>): List<MetObject> = coroutineScope {
        ids.map { id ->
            async(io) {
                memory.get(id)?.let { return@async it }
                semaphore.withPermit {
                    runCatching { api.objectDetails(id) }.getOrNull()
                        ?.takeIf { !it.primaryImageSmall.isNullOrBlank() }
                        ?.also { memory.put(id, it) }
                }
            }
        }.awaitAll().filterNotNull()
    }
}