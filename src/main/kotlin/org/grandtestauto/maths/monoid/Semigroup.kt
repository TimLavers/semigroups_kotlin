package org.grandtestauto.maths.monoid

import java.util.*

/**
 * The numbers 1..n with i*j = max(i, j).
 */
fun chainSemigroup(n: Int): Semigroup<Int> {
    require(n >= 1)
    return Semigroup((1..n).toSet()) { s, t -> s.coerceAtLeast(t) }
}

fun powerSetIntersection(rank_atLeast2: Int): Semigroup<Set<Int>> {
    val generators = mutableSetOf<Set<Int>>()
    val dataForId = HashSet<Int>()
    for (i in 1..rank_atLeast2) {
        val data = HashSet<Int>()
        for (j in 1..rank_atLeast2) {
            if (j != i) data.add(j)
        }
        generators.add(HashSet(data))
        dataForId.add(i)
    }
    generators.add(HashSet(dataForId))
    return generateFrom({ s, t -> s intersect t }, generators)
}

fun <T> isClosedUnderComposition(elements: Set<T>, composition: ((T, T) -> (T))): Boolean {
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

fun <T> isAssociative(composition: ((T, T) -> (T)), elements: Set<T>): Boolean {
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
    require(function.domain() == domain.elements)
    require(range.elements.containsAll(function.range()))
    return isPartialHomomorphism(function, domain, range)
}

fun <T, U> findIsomorphism(domain: Semigroup<T>, range: Semigroup<U>): FiniteFunction<T, U>? =
    allBijectionsFromTo(domain, range).firstOrNull { isHomomorphism(it, domain, range) }

fun <T,U> areIsomorphic(domain: Semigroup<T>, range: Semigroup<U>) = findIsomorphism(domain, range) != null

/**
 * Returns true if the given function, which is defined on perhaps
 * a subset of the elements of the domain semigroup,
 * is consistent with being a homomorphism.
 */
private fun <T, U> isPartialHomomorphism(
    function: FiniteFunction<T, U>,
    domain: Semigroup<T>,
    range: Semigroup<U>
): Boolean {
    val result = booleanArrayOf(true)
    for (t1 in function.domain()) {
        if (!result[0]) break
        for (t2 in function.domain()) {
            val t1t2 = domain.composition(t1, t2)
            if (t1t2 in function.domain()) {
                val t1t2f = function.invoke(t1t2)
                val t1ft2f = range.composition(function.invoke(t1), function.invoke(t2))
                if (t1ft2f != t1t2f) {
                    result[0] = false
                }
            }
        }
    }
    return result[0]
}

fun rightZeroSemigroup(rank: Int): Semigroup<String> {
    val elements = (1..rank).map { "rz$it" }.toSet()
    return Semigroup(elements) { _, t -> t }
}

fun leftZeroSemigroup(rank: Int): Semigroup<String> {
    val elements = (1..rank).map { "lz$it" }.toSet()
    return Semigroup(elements) { s, _ -> s }
}

fun <T> generateFrom(composition: ((T, T) -> T), generators: Set<T>): Semigroup<T> {
    val generation = Generation(generators, object : Generation.CreateNew<T> {
        override fun createNew(s: T, t: T): T {
            return composition(s, t)
        }
    })
    return Semigroup(generation.generate(), composition)
}

fun transformationMonoid(rank_atLeast2: Int): Semigroup<Transformation> {
    val elements = HashSet<Transformation>()
    val symN = symmetricGroup(rank_atLeast2)
    val on = orderPreservingTransformationMonoid(rank_atLeast2)
    for (s in symN) {
        for (o in on) {
            elements.add(s.compose(o))
        }
    }
    return Semigroup(elements, TransformationComposition)
}

fun orderPreservingTransformationMonoid(n: Int) = generateTransformationMonoid(n) {a, b -> setOf(nudgeUp(a, b), nudgeDown(a, b)) }

fun nudgeUp(n: Int, i: Int) = nudge(n, i, true)

fun nudgeDown(n: Int, i: Int) = nudge(n, i, false)

private fun nudge(n: Int, i: Int, up: Boolean): Transformation {
    require(n > 1)
    require(i > 0)
    require(i < n)
    val data = IntArray(n)
    (1 until i).forEach { data[it - 1] = it}
    data[i - 1] = if (up) i + 1 else i
    data[i] = if (up) i + 1 else i
    (i + 1 until n).forEach { data[it] = it + 1 }
    return Transformation(data)
}

fun monoidOfNonDecreasingTransformations(n: Int) = generateTransformationMonoid(n) { a, b -> setOf(nudgeUp(a, b)) }

fun monoidOfNonIncreasingTransformations(n: Int) = generateTransformationMonoid(n) {a, b -> setOf(nudgeDown(a, b)) }

private fun generateTransformationMonoid(n: Int, generatorGenerator: ((Int, Int) -> Set<Transformation>)): Monoid<Transformation> {
    val unit = unit(n)
    val generators = mutableSetOf(unit)
    (1 until n).forEach { generators.addAll(generatorGenerator( n, it )) }
    return asMonoid( generateFrom(TransformationComposition, generators), unit)
}

fun symmetricGroup(n: Int): Monoid<Transformation> {
    require(n >= 1)
    if (n == 1) return transformationMonoid1()
    val result: Semigroup<Transformation> = if (n < 5) {
        generateSymmetricGroup(n)
    } else {
        val resultForLowerRank = symmetricGroup(n - 1)
        val symElements = resultForLowerRank.map { it.embed(n) }
        val cyclicGroup = cyclicGroup(n)
        val products = HashSet<Transformation>()
        for (cycle in cyclicGroup) {
            for (permutation in symElements) {
                products.add(permutation.compose(cycle))
            }
        }
        Semigroup(products, TransformationComposition)
    }
    return asMonoid(result, unit(n))
}

private fun generateSymmetricGroup(n: Int): Semigroup<Transformation> {
    require(n >= 2)
    val generators = HashSet<Transformation>()
    for (i in 1 until n) {
        val generatorData = IntArray(n)
        for (j in 1 until i) {
            generatorData[j - 1] = j
        }
        generatorData[i - 1] = i + 1
        generatorData[i] = i
        for (j in i + 1 until n) {
            generatorData[j] = j + 1
        }
        generators.add(Transformation(generatorData))
    }
    return generateFrom(TransformationComposition, generators)
}

fun cyclicGroup(rank: Int): Monoid<Transformation> {
    require(rank > 0)
    return if (rank == 1) {
        transformationMonoid1()
    } else asMonoid(generateFrom(TransformationComposition, setOf(cycle2_3___n_1(rank))), unit(rank))
}

private fun transformationMonoid1(): Monoid<Transformation> {
    val identity = unit(1)
    return Monoid(setOf(identity), TransformationComposition, identity)
}

fun <S, T> doubleProduct(
    left: Semigroup<S>,
    right: Semigroup<T>,
    actionOfLeftOnRight: ((S) -> ((T) -> T)),
    actionOfRightOnLeft: ((T) -> ((S) -> S))
): Semigroup<Pair<S, T>> {
    fun elements(): Set<Pair<S, T>> {
        val result = HashSet<Pair<S, T>>()
        left.forEach { s -> right.forEach { t -> result.add(Pair(s, t)) } }
        return result
    }

    fun compose(): ((Pair<S, T>, Pair<S, T>) -> (Pair<S, T>)) = { x, y ->
        Pair(
            left.composition(x.left(), actionOfRightOnLeft(x.right())(y.left())),
            right.composition(actionOfLeftOnRight(y.left())(x.right()), y.right())
        )
    }

    return Semigroup(elements(), compose())
}

fun <T> asMonoid(semigroup: Semigroup<T>, identity: T): Monoid<T> {
    return Monoid(semigroup.elements, semigroup.composition, identity)
}

fun <S, T> allHomomorphisms(s: Semigroup<S>, t: Semigroup<T>): Set<FiniteFunction<S, T>> {
    var result = mutableSetOf<FiniteFunction<S, T>>()

    //Start with the single empty partial map from s.elements to t.elements
    val seed = FiniteFunction(mapOf<S, T>())

    //Add this to a set of all partial maps of s.elements to t.elements
    result.add(seed)

    //For each element x of s:
    s.forEach { x ->
        //Create a new result set
        val newResults = mutableSetOf<FiniteFunction<S, T>>()
        //For each partial map p in the result set
        result.forEach { p ->
            //For each element y of t form a new map by extending p with the pair (x,y)
            t.forEach { y ->
                val newMap = p + Pair(x, y)
                if (isPartialHomomorphism(newMap, s, t)) {
                    //For each of these possible partial maps,
                    //add those that respect composition to the new result set
                    newResults.add(newMap)
                }
            }
        }
        //Replace the new result set with the old
        result = newResults
    }
    return result
}
fun allInjectiveHomomorphisms(s: Semigroup<Transformation>, t: Semigroup<Transformation>): Set<FiniteFunction<Transformation, Transformation>> {
    var result = mutableSetOf<FiniteFunction<Transformation, Transformation>>()

    //Start with the single empty partial map from s.elements to t.elements
    val seed = FiniteFunction(mapOf<Transformation, Transformation>())

    //Add this to a set of all partial maps of s.elements to t.elements
    result.add(seed)

    //For each element x of s:
    s.forEach { x ->
        println("working with $x..., number partials = ${result.size}")
        val imageSizeX = x.image.size
        //Create a new result set
        val newResults = mutableSetOf<FiniteFunction<Transformation, Transformation>>()
        //For each partial map p in the result set
        result.forEach { p ->
            //For each element y of t form a new map by extending p with the pair (x,y)
            t.forEach { y ->
                if (y.image.size == imageSizeX && !p.range().contains(y)) {
                    val newMap = p + Pair(x, y)
                    if (isPartialHomomorphism(newMap, s, t)) {
                        //For each of these possible partial maps,
                        //add those that respect composition to the new result set
                        newResults.add(newMap)
                    }
                }
            }
        }
        //Replace the new result set with the old
        result = newResults
    }
    return result
}

/**
 * An immutable set of elements and an associative binary operation under which that set is closed.

 * @author Tim Lavers
 */
open class Semigroup<T>(val elements: Set<T>, val composition: ((T, T) -> T)) : Set<T> by elements {

    val isGroup: Boolean by lazy {
        find { leftIdeal(it) != elements || rightIdeal(it) != elements } == null
    }

    val idempotents: Set<T> by lazy {
        filter { composition(it, it) == it }.toSet()
    }

    open fun powerOf(t: T, r: Int): T {
        if (r < 1) throw IllegalArgumentException("Strictly positive indices here, please.")
        if (r == 1) return t
        return composition(t, powerOf(t, r - 1))
    }

    fun leftIdeal(t: T): Set<T> {
        return map { composition(it, t) }.toSet()
    }

    fun rightIdeal(t: T): Set<T> {
        return map { composition(t, it) }.toSet()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Semigroup<*>

        if (elements != other.elements) return false
        return composition == other.composition
    }

    override fun hashCode(): Int {
        var result = elements.hashCode()
        result = 31 * result + composition.hashCode()
        return result
    }

    override fun toString(): String {
        return elements.toString()
    }

    fun isCongruence(equivalenceRelation: Relation<T>): Boolean {
        equivalenceRelation.forEach { tuple ->
            forEach { s ->
                val leftMultiple = Pair(composition(s, tuple.left()), composition(s, tuple.right()))
                if (!equivalenceRelation.contains(leftMultiple)) return false
                val rightMultiple = Pair(composition(tuple.left(), s), composition(tuple.right(), s))
                if (!equivalenceRelation.contains(rightMultiple)) return false
            }
        }
        return true
    }

    fun isSubsemigroup(subset: Set<T>) = if (!elements.containsAll(subset)) {
        false
    } else isClosedUnderComposition(subset, composition)

    fun allSubsemigroups() = elements
        .subsetsSatisfying{ isSubsemigroup(it) }
        .filter { it.isNotEmpty() }
        .map { Semigroup(it, composition) }
//    fun allSubsemigroups() = elements.powerSette().filter { it.isNotEmpty() }.filter { isSubsemigroup(it) }.map { Semigroup(it, composition) }
}
//fun allSubsemigroups() = elements.subsetsSatisfying{ isSubsemigroup(it) }.filter { it.isNotEmpty() }.map { Semigroup(it, composition) }
//
class Monoid<T>(elements: Set<T>, composition: (T, T) -> T, private val identity: T) :
    Semigroup<T>(elements, composition) {
    override fun powerOf(t: T, r: Int): T {
        if (r == 0) return identity
        return super.powerOf(t, r)
    }
}