package org.grandtestauto.maths.monoid

/**
 * @author Tim Lavers
 */
class SubsetsOfSizeN<T>(private val originatingSet: Set<T>, cardinalityOfSubsets: Int) {
    private val subsets = mutableSetOf<Set<T>>()

    init {
        if (cardinalityOfSubsets == 0) {
            subsets.add(mutableSetOf<T>())
        } else {
            val recursionMap = mutableMapOf<T, SubsetsOfSizeN<T>>()
            originatingSet.forEach { t -> recursionMap.put(t, SubsetsOfSizeN(without(t), cardinalityOfSubsets - 1)) }
            recursionMap.entries.forEach { k ->
                val recursiveSubsets = k.value.subsets()
                recursiveSubsets.forEach { subset ->
                    val withKey = mutableSetOf<T>()
                    withKey.addAll(subset)
                    withKey.add(k.key)
                    subsets.add(withKey)
                }
            }
        }
    }

    fun subsets(): Set<Set<T>> {
        return subsets
    }

    private fun without(excluded: T): Set<T> {
        val result = mutableSetOf<T>()
        originatingSet.forEach{t ->
            if (t != excluded) result.add(t)
        }
        return result;
    }
}
