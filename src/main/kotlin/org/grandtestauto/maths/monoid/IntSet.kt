package org.grandtestauto.maths.monoid

import java.util.*

object UnionComposition : (Set<Int>, Set<Int>) -> Set<Int>  {
    override fun invoke(s: Set<Int>, t: Set<Int>) = s + t
}
object IntersectionComposition : (Set<Int>, Set<Int>) -> Set<Int> {
    override fun invoke(s: Set<Int>, t: Set<Int>) = s intersect t
}
fun intsFrom1To(n: Int) = (1..n).toSet()

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

infix fun <T> Set<T>.isASubsetOf(other: Set<T>) = other.containsAll(this)

infix fun Set<Int>.transform(t: Transformation) = filter { t.domain.contains(it) }.map { t.apply(it) }.toSet()