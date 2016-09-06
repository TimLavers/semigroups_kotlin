package org.grandtestauto.maths.monoid

import java.util.*

object UnionComposition : (IntSet, IntSet) -> IntSet {
    override fun invoke(s: IntSet, t: IntSet): IntSet {
        return s.union(t)
    }
}
object IntersectionComposition : (IntSet, IntSet) -> IntSet {
    override fun invoke(s: IntSet, t: IntSet): IntSet {
        return s.intersection(t)
    }
}
fun powerSet(n: Int): Set<IntSet> {
    val result = HashSet<IntSet>()
    if (n == 0) {
        result.add(IntSet(HashSet<Int>()))
    } else {
        val intSets = powerSet(n - 1)
        for (intSet in intSets) {
            result.add(intSet)
            result.add(intSet.with(n))
        }
    }
    return result
}

/**
 * An immutable set of integers.
 */
class IntSet(private val data: Set<Int>) {

    operator fun contains(i: Int): Boolean {
        return data.contains(i)
    }

    fun transform(t: Transformation): IntSet {
        val transformed = HashSet<Int>()
        data.forEach { i ->
            if (t.domain().contains(i)) {
                transformed.add(t.apply(i))
            }
        }
        return IntSet(transformed)
    }

    fun union(other: IntSet): IntSet {
        val resultData = HashSet(data)
        resultData.addAll(other.data)
        return IntSet(resultData)
    }

    fun intersection(other: IntSet): IntSet {
        val resultData = HashSet<Int>()
        for (i in data) {
            if (other.contains(i)) resultData.add(i)
        }
        return IntSet(resultData)
    }

    fun isASubsetOf(other: IntSet): Boolean {
        for (i in data) {
            if (!other.data.contains(i)) return false
        }
        return true
    }

    fun with(i: Int): IntSet {
        val resultData = HashSet(data)
        resultData.add(i)
        return IntSet(resultData)
    }

    fun size(): Int {
        return data.size
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is IntSet && other.data == data
    }

    override fun toString(): String {
        val sb = StringBuilder("IntSet{")
        val sorted = TreeSet(data)
        var first = true
        for (t in sorted) {
            if (!first) {
                sb.append(",")
            } else {
                first = false
            }
            sb.append(t)
        }
        sb.append("}")
        return sb.toString()
    }
}