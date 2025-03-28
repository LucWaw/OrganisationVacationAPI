package com.lucwaw.model.repository

import com.lucwaw.domain.Named

abstract class Repository<T : Named> {
    protected val items = mutableListOf<T>()

    fun upsertItem(item: T) {
        val index = items.indexOfFirst { it.name == item.name }
        if (index != -1) {
            items[index] = item
        } else {
            items.add(item)
        }
    }

    fun removeItem(itemName: String): Boolean {
        return items.removeIf { it.name == itemName }
    }
}