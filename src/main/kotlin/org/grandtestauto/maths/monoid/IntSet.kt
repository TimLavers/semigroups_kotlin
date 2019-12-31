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
fun intsFrom1To(n: Int): Set<Int> {
    val result = HashSet<Int>()
    for (i in 1..n) result.add(i)
    return result
}

fun <T> Set<T>.powerSet(): Set<Set<T>> {
    val result = HashSet<Set<T>>()
    if (this.isEmpty()) {
        result.add(HashSet())
    } else {
        val anElement = this.iterator().next()
        val withoutTheElement = this.minusElement(anElement)
        val recurse = withoutTheElement.powerSet()
        recurse.forEach {
                result.add(it)
                result.add(it + anElement)
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