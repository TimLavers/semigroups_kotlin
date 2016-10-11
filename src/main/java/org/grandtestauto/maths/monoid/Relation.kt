package org.grandtestauto.maths.monoid

import java.util.*

fun <T> createRelation(baseSet: Set<T>, criterion: ((T, T) -> Boolean)): Relation<T> {
    val resultBase = mutableSetOf<Tuple<T, T>>()
    baseSet.forEach { s ->
        baseSet.forEach { t ->
            if (criterion(s, t)) {
                resultBase.add(Tuple<T, T>(s, t))
            }
        }
    }
    return Relation<T>(baseSet, resultBase)
}

/**
 * A set of pairs of elements of the same set.

 * @author Tim Lavers
 */
class Relation<T>(private val baseSet: Set<T>, private val elements: Set<Tuple<T, T>>) {

    init {
        this.elements.forEach { ttTuple ->
            assert(baseSet.contains(ttTuple.left())) { "Not in base set: " + ttTuple.left() }
            assert(baseSet.contains(ttTuple.right())) { "Not in base set: " + ttTuple.right() }
        }
    }

    val isSymmetric: Boolean by lazy { _isSymmetric() }
    val isReflexive: Boolean by lazy { _isReflexive() }
    val isAPartialOrder: Boolean by lazy { _isAPartialOrder() }
    val isAnEquivalence: Boolean by lazy { _isAnEquivalence() }
    val isTransitive: Boolean by lazy { _isTransitive() }

    operator fun contains(element: Tuple<T, T>): Boolean {
        return elements.contains(element)
    }

    fun transitiveClosure(): Relation<T> {
        return generate(TransitiveClosureGenerator())
    }

    fun size(): Int {
        return elements.size
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Relation<*>

        if (baseSet != other.baseSet) return false
        if (elements != other.elements) return false

        return true
    }

    override fun hashCode(): Int {
        var result = baseSet.hashCode()
        result += 31 * result + elements.hashCode()
        return result
    }

    override fun toString(): String {
        return "{${elements.toString()}}"
    }

    fun generateEquivalenceRelation(): Relation<T> {
        return generate(object : TransitiveClosureGenerator() {
            override fun createNew(t: Tuple<T, T>): Tuple<T, T>? {
                return t.flip()
            }
        })
    }

    private open inner class TransitiveClosureGenerator : Generation.CreateNew<Tuple<T, T>> {
        override fun createNew(s: Tuple<T, T>, t: Tuple<T, T>): Tuple<T, T>? {
            if (s.right() == t.left()) {
                return Tuple(s.left(), t.right())
            }
            return null
        }
    }

    private fun generate(tupleGenerator: Generation.CreateNew<Tuple<T, T>>): Relation<T> {
        val generation = Generation(elements, tupleGenerator)
        val generated = generation.generate()
        val base = HashSet<T>()
        generated.forEach { ttTuple ->
            base.add(ttTuple.left())
            base.add(ttTuple.right())
        }
        return Relation(base, generated)
    }

    private fun _isReflexive(): Boolean {
        for (x in baseSet) {
            if (!elements.contains(Tuple(x, x))) return false
        }
        return true
    }

    private fun _isSymmetric(): Boolean {
        for (t in elements) {
            if (!elements.contains(t.flip())) return false
        }
        return true
    }

    private fun _isAPartialOrder(): Boolean {
        if (!isReflexive) return false
        if (!isTransitive) return false
        elements.forEach{ ttTuple ->
            if (elements.contains(ttTuple.flip())) {
                if (ttTuple.right() != ttTuple.left()) return false
            }
        }
        return true
    }

    private fun _isAnEquivalence(): Boolean {
        if (!isReflexive) return false
        if (!isSymmetric) return false
        if (!isTransitive) return false
        return true
    }

    private fun _isTransitive(): Boolean {
        elements.forEach{ ttTuple ->
            elements.forEach{ ssTuple ->
                if (ttTuple.right() == ssTuple.left()) {
                    val composite = Tuple(ttTuple.left(), ssTuple.right())
                    if (!elements.contains(composite)) return false
                }
            }
        }
        return true
    }
}