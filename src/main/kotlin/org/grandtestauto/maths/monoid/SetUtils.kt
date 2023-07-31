package org.grandtestauto.maths.monoid

fun <T> Set<T>.powerSet(): Set<Set<T>> = subsetsSatisfying { true }

fun <T> Set<T>.subsetsSatisfying(predicate: (Set<T>) -> Boolean): Set<Set<T>> {
    val elementList = this.toList()
    val intSetsSatisfying = intsFrom1To(elementList.size)
        .allSubsets {
            intSet -> predicate(intSet.map { elementList[it - 1] }.toSet())
        }
    return intSetsSatisfying.map {
        intSet -> intSet.map { elementList[it - 1] }.toSet()
    }.toSet()
}

infix fun <T> Set<T>.isASubsetOf(other: Set<T>) = other.containsAll(this)