package org.grandtestauto.maths.monoid

import java.util.Collections
import java.util.HashSet

fun powerSetIntersection(rank_atLeast2: Int): Semigroup<IntSet> {
    val generators = HashSet<IntSet>()
    val dataForId = HashSet<Int>()
    for (i in 1..rank_atLeast2) {
        val data = HashSet<Int>()
        for (j in 1..rank_atLeast2) {
            if (j != i) data.add(j)
        }
        generators.add(IntSet(data))
        dataForId.add(i)
    }
    generators.add(IntSet(dataForId))
    return generateFrom({ s, t -> s.intersection(t)}, generators)
}

fun <T> isClosedUnderComposition(elements: Set<T>, composition: ((T,T) -> (T))): Boolean {
    val result = booleanArrayOf(true)
    for (t in elements) {
        if (!result[0]) break
        for (s in elements) {
            if (!result[0]) break
            val product = composition(s, t)
            if (!elements.contains(product)) {
                result[0] = false
                break
            }
        }
    }
    return result[0]
}

fun <T> isAssociative(composition: ((T,T) -> (T)), elements: Set<T>): Boolean {
    val result = booleanArrayOf(true)
    for (t in elements) {
        if (!result[0]) break
        for (s in elements) {
            if (!result[0]) break
            for (u in elements) {
                if (!result[0]) break
                val st_u = composition(composition(s, t), u)
                val s_tu = composition(s, composition(t, u))
                if (st_u != s_tu) {
                    result[0] = false
                }
            }
        }
    }
    return result[0]
}

fun <T, U> isHomomorphism(function: FiniteFunction<T, U>, domain: Semigroup<T>, range: Semigroup<U>): Boolean {
    assert(function.domain() == domain.elements())
    assert(range.elements().containsAll(function.range()))
    val result = booleanArrayOf(true)
    for (t1 in domain.elements()) {
        if (!result[0]) break
        for (t2 in domain.elements()) {
            val t1t2 = domain.composition()(t1, t2)
            val t1t2f = function.apply(t1t2)
            val t1ft2f = range.composition()(function.apply(t1), function.apply(t2))
            if (t1ft2f != t1t2f) {
                result[0] = false
            }
        }
    }
    return result[0]
}

fun rightZeroSemigroup(rank: Int): Semigroup<String> {
    val elements = HashSet<String>()
    for (i in 0..rank - 1) {
        elements.add("rz" + i)
    }
    return Semigroup<String>(elements, { s, t -> t})
}

fun leftZeroSemigroup(rank: Int): Semigroup<String> {
    val elements = HashSet<String>()
    for (i in 0..rank - 1) {
        elements.add("lz" + i)
    }
    return Semigroup<String>(elements, { s, t -> s})
}

fun <T> leftIdeal(semigroup: Semigroup<T>, t: T): Set<T> {
    val result = mutableSetOf<T>()
    for (s in semigroup) {
        result.add(semigroup.composition()(s, t))
    }
    return result
}

fun <T> rightIdeal(semigroup: Semigroup<T>, t: T): Set<T> {
    val result = mutableSetOf<T>()
    for (s in semigroup) {
        result.add(semigroup.composition()(t, s))
    }
    return result
}

fun <T> generateFrom(composition: ((T,T) -> T), generators: Set<T>): Semigroup<T> {
    val generation = Generation(generators, object : Generation.CreateNew<T> {
        override fun createNew(s: T, t: T): T {
            return composition(s, t)
        }
    })
    return Semigroup(generation.generate(), composition)
}

fun transformationMonoid(rank_atLeast2: Int): Semigroup<Transformation> {
    val elements = HashSet<Transformation>()
    val symn = symmetricGroup(rank_atLeast2).elements()
    val on = orderPreservingTransformationMonoid(rank_atLeast2).elements()
    for (s in symn) {
        for (o in on) {
            elements.add(s.compose(o))
        }
    }
    return Semigroup(elements, TransformationComposition)
}

fun orderPreservingTransformationMonoid(rank_atLeast2: Int): Semigroup<Transformation> {
    val generators = HashSet<Transformation>()
    generators.add(unit(rank_atLeast2))
    for (i in 1..rank_atLeast2 - 1) {
        val generatorLeftData = IntArray(rank_atLeast2)
        val generatorRightData = IntArray(rank_atLeast2)
        for (j in 1..i - 1) {
            generatorLeftData[j - 1] = j
            generatorRightData[j - 1] = j
        }
        generatorLeftData[i - 1] = i
        generatorRightData[i - 1] = i + 1
        generatorLeftData[i] = i
        generatorRightData[i] = i + 1
        for (j in i + 1..rank_atLeast2 - 1) {
            generatorLeftData[j] = j + 1
            generatorRightData[j] = j + 1
        }
        generators.add(Transformation(generatorLeftData))
        generators.add(Transformation(generatorRightData))
    }
    return generateFrom(TransformationComposition, generators)
}

fun symmetricGroup(rank_atLeast2: Int): Monoid<Transformation> {
    val result: Semigroup<Transformation>
    if (rank_atLeast2 < 5) {
        result = generateSymmetricGroup(rank_atLeast2)
    } else {
        val resultForLowerRank = symmetricGroup(rank_atLeast2 - 1)
        val symElements = HashSet<Transformation>()
        for (g in resultForLowerRank) {
            symElements.add(g.embed(rank_atLeast2))
        }
        val cyclicGroup = cyclicGroup(rank_atLeast2)
        val products = HashSet<Transformation>()
        for (cycle in cyclicGroup) {
            for (permutation in symElements) {
                products.add(permutation.compose(cycle))
            }
        }
        result = Semigroup(products, TransformationComposition)
    }
    return asMonoid(result, unit(rank_atLeast2))
}

private fun generateSymmetricGroup(rank_atLeast2: Int): Semigroup<Transformation> {
    val generators = HashSet<Transformation>()
    for (i in 1..rank_atLeast2 - 1) {
        val generatorData = IntArray(rank_atLeast2)
        for (j in 1..i - 1) {
            generatorData[j - 1] = j
        }
        generatorData[i - 1] = i + 1
        generatorData[i] = i
        for (j in i + 1..rank_atLeast2 - 1) {
            generatorData[j] = j + 1
        }
        generators.add(Transformation(generatorData))
    }
    return generateFrom(TransformationComposition, generators)
}

fun cyclicGroup(rank_atLeast2: Int): Monoid<Transformation> {
    val generators = HashSet<Transformation>()
    val gamma = cycle2_3___n_1(rank_atLeast2)
    generators.add(gamma)
    return asMonoid(generateFrom(TransformationComposition, generators), unit(rank_atLeast2))
}

fun <S,T> doubleProduct(left: Semigroup<S>,
                        right: Semigroup<T>,
                        actionOfLeftOnRight: ((S) -> ((T) -> T)),
                        actionOfRightOnLeft: ((T) -> ((S) -> S))) : Semigroup<Tuple<S,T>> {
    fun elements(): Set<Tuple<S, T>> {
        val result = HashSet<Tuple<S, T>>()
        left.forEach { s -> right.forEach { t -> result.add(Tuple(s, t)) } }
        return result
    }

    fun compose() : ((Tuple<S,T>, Tuple<S,T>) -> (Tuple<S,T>)) = {
        x, y -> Tuple(left.composition()(x.left(), actionOfRightOnLeft(x.right())(y.left())),
            right.composition()(actionOfLeftOnRight(y.left())(x.right()),y.right()))
    }

    return Semigroup(elements(), compose());
}

fun <T> asMonoid(semigroup: Semigroup<T>,identity: T  ) : Monoid<T> {
    return Monoid(semigroup.elements(), semigroup.composition(), identity)
}

/**
 * An immutable set of elements and an associative binary operation under which that set is closed.

 * @author Tim Lavers
 */
open class Semigroup<T>(private val elements: Set<T>, private val composition: ((T, T) -> T)) : Iterable<T> {

    fun size() : Int = elements.size

    operator fun contains(t: T): Boolean = elements.contains(t)

    fun composition(): ((T, T) -> T) = composition

    open fun powerOf(t: T, r: Int): T {
        if (r < 1) throw IllegalArgumentException("Strictly positive indices here, please.")
        var result = t
        for (i in 1..r - 1) result = composition(result, t)
        return result
    }

    fun elements(): Set<T> = elements

    override fun iterator(): Iterator<T> = elements().iterator()
}

class Monoid<T>(elements: Set<T>, composition: (T, T) -> T, val identity : T) : Semigroup<T>(elements, composition) {
    override fun powerOf(t: T, r: Int): T {
        if (r == 0) return identity
        return super.powerOf(t, r)
    }
}