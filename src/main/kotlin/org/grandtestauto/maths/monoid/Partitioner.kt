package org.grandtestauto.maths.monoid

import java.util.*
import java.util.function.BiPredicate

/**
 * Breaks a set into equivalence classes according to some predicate.

 * @author Tim Lavers
 */
class Partitioner<T>(private val baseSet: Set<T>, private val predicate: BiPredicate<T, T>) {

    fun equivalenceClasses(): SetPartition<T> {
        val result = SetPartition(baseSet)
        val exemplarToEquivalenceClass = mutableMapOf<T, MutableSet<T>>()
        baseSet.forEach { t ->
            val classified = booleanArrayOf(false)
            exemplarToEquivalenceClass.entries.forEach { e ->
                if (!classified[0]) {
                    if (predicate.test(t, e.key)) {
                        classified[0] = true
                        e.value.add(t)
                    }
                }
            }
            if (!classified[0]) {
                val equivalenceClass = HashSet<T>()
                equivalenceClass.add(t)
                exemplarToEquivalenceClass[t] = equivalenceClass
            }
        }
        exemplarToEquivalenceClass.values.forEach { e -> result.addSubset(e) }
        return result
    }
}
