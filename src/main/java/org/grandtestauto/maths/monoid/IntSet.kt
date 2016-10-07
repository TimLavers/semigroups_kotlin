package org.grandtestauto.maths.monoid

import java.util.*

object UnionComposition : (Set<Int>, Set<Int>) -> Set<Int>  {
    override fun invoke(s: Set<Int>, t: Set<Int>): Set<Int>  {
        return s + t
    }
}
object IntersectionComposition : (Set<Int>, Set<Int>) -> Set<Int> {
    override fun invoke(s: Set<Int>, t: Set<Int>): Set<Int> {
        return s intersect t
    }
}
fun powerSet(n: Int): Set<Set<Int>> {
    val result = HashSet<Set<Int>>()
    if (n == 0) {
        result.add(HashSet(HashSet<Int>()))
    } else {
        val intSets = powerSet(n - 1)
        for (intSet in intSets) {
            result.add(intSet)
            result.add(intSet + n )
        }
    }
    return result
}

infix fun <T> Set<T>.isASubsetOf(other: Set<T>) : Boolean {
    return other.containsAll(this)
}

infix fun Set<Int>.transform(t: Transformation): Set<Int> {
    val transformed = HashSet<Int>()
    this.forEach { i ->
        if (t.domain.contains(i)) {
            transformed.add(t.apply(i))
        }
    }
    return transformed
}