package org.faronovama.application.repo

import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ListRepo<E> : Repo<E> {
    private val list = ConcurrentHashMap<String, E>()

    override fun create(element: E): Boolean =
        true.apply {
            list[UUID.randomUUID().toString()] =
                element
        }

    override fun read(): List<E> =
        list.map {
            it.value
        }

    override fun read(id: String): E? =
        list[id]

    override fun read(ids: List<String>): List<E> =
        ids.mapNotNull { id ->
            list[id]
        }

    override fun delete(id: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun update(item: E): Boolean {
        TODO("Not yet implemented")
    }
}