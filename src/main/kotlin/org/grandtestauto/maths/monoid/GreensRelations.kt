package org.grandtestauto.maths.monoid

import java.util.*

fun <T> are_L_Related(semigroup: Semigroup<T>, a: T, b: T): Boolean {
    return semigroup.leftIdeal(a) == semigroup.leftIdeal(b)
}

fun <T> are_R_Related(semigroup: Semigroup<T>, a: T, b: T): Boolean {
    return semigroup.rightIdeal(a) == semigroup.rightIdeal(b)
}
class GreensRelations<T>(private val semigroup: Semigroup<T>) {

    fun lClasses(): SetPartition<T> {
        val result = SetPartition(semigroup.elements)
        val leftIdealsToElementsThatProduceThem = mutableMapOf<Set<T>, MutableSet<T>>()
        semigroup.forEach { t ->
            val leftIdeal = semigroup.leftIdeal(t)
            var producers: MutableSet<T>? = leftIdealsToElementsThatProduceThem.remove(leftIdeal)
            if (producers == null) {
                producers = HashSet()
            }
            producers.add(t)
            leftIdealsToElementsThatProduceThem[leftIdeal] = producers
        }
        leftIdealsToElementsThatProduceThem.entries.forEach { e -> result.addSubset(e.value) }
        return result
    }

    fun rClasses(): SetPartition<T> {
        val result = SetPartition(semigroup.elements)
        val rightIdealsToElementsThatProduceThem = mutableMapOf<Set<T>, MutableSet<T>>()
        semigroup.forEach { t ->
            val rightIdeal = semigroup.rightIdeal(t)
            var producers: MutableSet<T>? = rightIdealsToElementsThatProduceThem.remove(rightIdeal)
            if (producers == null) {
                producers = HashSet()
            }
            producers.add(t)
            rightIdealsToElementsThatProduceThem[rightIdeal] = producers
        }
        rightIdealsToElementsThatProduceThem.entries.forEach { e -> result.addSubset(e.value) }
        return result
    }
}
