package org.grandtestauto.maths.monoid

/**
 * @author Tim Lavers
 */
class SubsetsOfSizeN<out T>(private val originatingSet: Set<T>, cardinalityOfSubsets: Int) {
    private val subsets = mutableSetOf<Set<T>>()

    init {
        if (cardinalityOfSubsets == 0) {
            subsets.add(mutableSetOf())
        } else {
            val recursionMap = mutableMapOf<T, SubsetsOfSizeN<T>>()
            originatingSet.forEach { t -> recursionMap[t] = SubsetsOfSizeN(without(t), cardinalityOfSubsets - 1) }
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

    private fun without(excluded: T) = originatingSet.filter { it != excluded }.toSet()
}
