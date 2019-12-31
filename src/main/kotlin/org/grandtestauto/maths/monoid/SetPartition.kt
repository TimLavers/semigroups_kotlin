package org.grandtestauto.maths.monoid

import java.util.HashSet


fun <S> areDistinct(set1: Set<S>, set2: Set<S>): Boolean {
    set1.forEach { e -> if  (set2.contains(e)) return false }
    return true
}
/**
 * @author Tim Lavers
 */
class SetPartition<T>(private val originatingSet: Set<T>) {
    private val subsets = HashSet<Set<T>>()

    fun addSubset(subset: Set<T>) {
        assert(originatingSet.containsAll(subset))
        subsets.forEach{ts -> assert(areDistinct(subset, ts))}
        subsets.add(subset)
    }

    val isComplete: Boolean
        get() = unionSize() == originatingSet.size

    fun subsets(): Set<Set<T>> {
        return subsets
    }

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as SetPartition<*>

        if (originatingSet != other.originatingSet) return false
        if (subsets != other.subsets) return false

        return true
    }

    override fun hashCode(): Int{
        var result = originatingSet.hashCode()
        result += 31 * result + subsets.hashCode()
        return result
    }

    private fun unionSize(): Int {
        val result = intArrayOf(0)
        subsets.forEach { t -> result[0] += t.size }
        return result[0]
    }
}
