package org.faronovama.application.repo

interface Repo<E> {
    fun create(element: E): Boolean
    fun read(): List<E>
    fun read(id: String): E?
    fun read(ids: List<String>): List<E>
    fun update(item: E): Boolean
    fun delete(id: String): Boolean
}