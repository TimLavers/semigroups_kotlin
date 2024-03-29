package org.grandtestauto.maths.monoid

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Collections

object UnionComposition : (Set<Int>, Set<Int>) -> Set<Int>  {
    override fun invoke(s: Set<Int>, t: Set<Int>) = s + t
}
object IntersectionComposition : (Set<Int>, Set<Int>) -> Set<Int> {
    override fun invoke(s: Set<Int>, t: Set<Int>) = s intersect t
}
fun intsFrom1To(n: Int) = (1..n).toSet()

infix fun Set<Int>.transform(t: Transformation) = filter { t.domain.contains(it) }.map { t.apply(it) }.toSet()

fun Set<Int>.allSubsets() =  this.allSubsets{true}

/**
 * Find all subsets of the given set that satisfy the given predicate.
 * Each subset of the set is tested precisely once.
 */
fun Set<Int>.allSubsets(predicate: (Set<Int>) -> Boolean): Set<Set<Int>> {
    suspend fun recurseSubsets(result: MutableSet<Set<Int>>, set: Set<Int>, eliminationLimit: Int, predicate: (Set<Int>) -> Boolean) {
        coroutineScope {
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
    }
    val result = Collections.synchronizedSet( mutableSetOf<Set<Int>>())
    if (predicate(this)) {
        result.add(this)
    }
    val elementsInOrder = this.toList().sorted()
    val theSet = this
    runBlocking {
        elementsInOrder.forEach {
            val subset = theSet - it
            launch(Dispatchers.Default) {
                recurseSubsets(result, subset, it, predicate)
            }
        }
    }
    return result
}
