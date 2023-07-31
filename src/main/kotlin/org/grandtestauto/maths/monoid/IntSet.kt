package org.grandtestauto.maths.monoid

object UnionComposition : (Set<Int>, Set<Int>) -> Set<Int>  {
    override fun invoke(s: Set<Int>, t: Set<Int>) = s + t
}
object IntersectionComposition : (Set<Int>, Set<Int>) -> Set<Int> {
    override fun invoke(s: Set<Int>, t: Set<Int>) = s intersect t
}
fun intsFrom1To(n: Int) = (1..n).toSet()

infix fun Set<Int>.transform(t: Transformation) = filter { t.domain.contains(it) }.map { t.apply(it) }.toSet()

fun Set<Int>.allSubsets() =  this.allSubsets{true}

fun Set<Int>.allSubsets(predicate: (Set<Int>) -> Boolean): Set<Set<Int>> {
    fun recurseSubsets(result: MutableSet<Set<Int>>, set: Set<Int>, eliminationLimit: Int, predicate: (Set<Int>) -> Boolean) {
        if (predicate(set)) {
            result.add(set)
        }
        set.forEach {
            if (it > eliminationLimit) {
                val subset = set - it
                recurseSubsets(result, subset, it, predicate)
            }
        }
    }
    val result = mutableSetOf<Set<Int>>()
    if (predicate(this)) {
        result.add(this)
    }
    val elementsInOrder = this.toList().sorted()
    elementsInOrder.forEach {
        val subset = this - it
        recurseSubsets(result, subset, it, predicate)
    }
    return result
}
