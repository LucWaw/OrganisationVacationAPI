package com.lucwaw.model.repository

import com.lucwaw.domain.Named

abstract class Repository<T : Named> {
    open val items = mutableListOf<T>()

    fun itemByName(name: String) = items.find {
        it.name.equals(name, ignoreCase = true)
    }

    fun addItem(item: T) {
        if (itemByName(item.name) != null){
            throw IllegalArgumentException("Item with name ${item.name} already exists.")
        }
        items.add(item)
    }

    fun updateItem(item: T): Boolean {
        val index = items.indexOfFirst { it.name == item.name }
        return if (index != -1) {
            items[index] = item
            true
        } else {
            false
        }
    }

    fun removeItem(itemName: String): Boolean {
        return items.removeIf { it.name == itemName }
    }
}