package org.grandtestauto.maths.monoid

import java.util.*

fun <S,T> Pair<S,T>.left() = this.first

fun <S,T> Pair<S,T>.right() =  this.second

fun <S,T> Pair<S,T>.flip() = Pair(second, first)

fun <T> createRelation(baseSet: Set<T>, criterion: ((T, T) -> Boolean)): Relation<T> {
    val resultBase = mutableSetOf<Pair<T, T>>()
    baseSet.forEach { s ->
        baseSet.forEach { t ->
            if (criterion(s, t)) {
                resultBase.add(Pair(s, t))
            }
        }
    }
    return Relation(baseSet, resultBase)
}

/**
 * A set of pairs of elements of the same set.

 * @author Tim Lavers
 */
class Relation<T>(private val baseSet: Set<T>, private val elements: Set<Pair<T, T>>) : Iterable<Pair<T, T>> {
    init {
        this.elements.forEach {
            assert(baseSet.contains(it.left())) { "Not in base set: " + it.left() }
            assert(baseSet.contains(it.right())) { "Not in base set: " + it.right() }
        }
    }

    val isSymmetric: Boolean by lazy { calculateIsSymmetric() }
    val isReflexive: Boolean by lazy { calculateIsReflexive() }
    val isAPartialOrder: Boolean by lazy { calculateIsAPartialOrder() }
    val isAnEquivalence: Boolean by lazy { calculateIsAnEquivalence() }
    val isTransitive: Boolean by lazy { calculateIsTransitive() }

    operator fun contains(element: Pair<T, T>) = elements.contains(element)

    fun transitiveClosure() = generate(TransitiveClosureGenerator())

    override fun iterator() = elements.iterator()

    fun size() = elements.size

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Relation<*>

        if (baseSet != other.baseSet) return false
        return elements == other.elements
    }

    override fun hashCode(): Int {
        var result = baseSet.hashCode()
        result += 31 * result + elements.hashCode()
        return result
    }

    override fun toString() = "{$elements}"

    fun generateEquivalenceRelation() =  generate(object : TransitiveClosureGenerator() {
            override fun createNew(t: Pair<T, T>): Pair<T, T> {
                return t.flip()
            }
        })

    private open inner class TransitiveClosureGenerator : Generation.CreateNew<Pair<T, T>> {
        override fun createNew(s: Pair<T, T>, t: Pair<T, T>): Pair<T, T>? {
            if (s.right() == t.left()) {
                return Pair(s.left(), t.right())
            }
            return null
        }
    }

    private fun generate(tupleGenerator: Generation.CreateNew<Pair<T, T>>): Relation<T> {
        val generation = Generation(elements, tupleGenerator)
        val generated = generation.generate()
        val base = HashSet<T>()
        generated.forEach { ttTuple ->
            base.add(ttTuple.left())
            base.add(ttTuple.right())
        }
        return Relation(base, generated)
    }

    private fun calculateIsReflexive(): Boolean {
        for (x in baseSet) {
            if (!elements.contains(Pair(x, x))) return false
        }
        return true
    }

    private fun calculateIsSymmetric(): Boolean {
        for (t in elements) {
            if (!elements.contains(t.flip())) return false
        }
        return true
    }

    private fun calculateIsAPartialOrder(): Boolean {
        if (!isReflexive) return false
        if (!isTransitive) return false
        elements.forEach{
            if (elements.contains(it.flip())) {
                if (it.right() != it.left()) return false
            }
        }
        return true
    }

    private fun calculateIsAnEquivalence(): Boolean {
        if (!isReflexive) return false
        if (!isSymmetric) return false
        return isTransitive
    }

    private fun calculateIsTransitive(): Boolean {
        elements.forEach{ ttTuple ->
            elements.forEach{ ssTuple ->
                if (ttTuple.right() == ssTuple.left()) {
                    val composite = Pair(ttTuple.left(), ssTuple.right())
                    if (!elements.contains(composite)) return false
                }
            }
        }
        return true
    }
}