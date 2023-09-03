package org.grandtestauto.maths.monoid

import java.util.*

fun subsemigroupOfOrderPreservingTransformations(semigroup: Semigroup<Transformation>): Semigroup<Transformation> {
    return Semigroup(semigroup.filter { isOrderPreserving(it) }.toSet(), TransformationComposition)
}

fun isOrderPreserving(t: Transformation): Boolean {
    t.domain.forEach {
        if (it > 1) {
            if (t.apply(it - 1) > t.apply(it)) return false
        }
    }
    return true
}

fun definesATransformation(map: IntArray): Boolean {
    for (i in map) {
        if (i < 1) return false
        if (i > map.size) return false
    }
    return true
}

fun cycle2_3___n_1(rank: Int): Transformation {
    require(rank > 0)
    val generatorData = IntArray(rank)
    for (i in 0 until rank - 1) {
        generatorData[i] = i + 2
    }
    generatorData[rank - 1] = 1
    return Transformation(generatorData)
}

fun unit(n: Int): Transformation {
    require(n > 0)
    val map = IntArray(n)
    map.indices.forEach { map[it] = it + 1 }
    return Transformation(map)
}

/**
 * An immutable function from the set of integers {1, 2, ... , n} to itself.

 * @author Tim Lavers
 */
class Transformation(private val map: IntArray) {

    val isRightZero: Boolean by lazy {
        val val0 = map[0]
        for (element in map) {
            if (element != val0) return@lazy false
        }
        return@lazy true
    }

    val kernel: Relation<Int> by lazy {
        val pairs = HashSet<Pair<Int, Int>>()
        for (i in 1..map.size) {
            for (j in 1..map.size) {
                if (apply(i) == apply(j)) pairs.add(Pair(i, j))
            }
        }
        Relation(domain, pairs)
    }

    val domain: Set<Int> by lazy {
        (1..map.size).toSet()
    }

    val numberOfFixedPoints: Int by lazy {
        (1..map.size).count { it == apply(it) }
    }

    val image: Set<Int> by lazy {
        (1..map.size).map { apply(it) }.toSet()
    }

    init {
        assert(definesATransformation(map))
    }

    fun inDomain(i: Int): Boolean {
        return i > 0 && i <= map.size
    }

    fun apply(i: Int): Int {
        assert(inDomain(i))
        return map[i - 1]
    }

    operator fun invoke(i: Int) = apply(i)

    fun embed(m_greaterThanRank: Int): Transformation {
        val newMap = IntArray(m_greaterThanRank)
        System.arraycopy(map, 0, newMap, 0, map.size)
        for (i in map.size until m_greaterThanRank) {
            newMap[i] = i + 1
        }
        return Transformation(newMap)
    }

    fun rank(): Int {
        return map.size
    }

    fun compose(t: Transformation): Transformation {
        assert(t.rank() == rank())
        if (t.isRightZero) return t
        val compositeMap = IntArray(map.size)
        for (i in 1..map.size) {
            compositeMap[i - 1] = t.map[map[i - 1] -1]
        }
        return Transformation(compositeMap)
    }

    operator fun times(other: Transformation): Transformation {
        return compose(other)
    }

    override fun hashCode(): Int {
        return map.contentHashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is Transformation && map.contentEquals(other.map)
    }

    override fun toString(): String {
        val sb = StringBuilder("[")
        var notFirst = false
        for (i in map) {
            if (notFirst) {
                sb.append(", ")
            } else {
                notFirst = true
            }
            sb.append(i)
        }
        sb.append("]")
        return sb.toString()
    }
}

object TransformationComposition : (Transformation, Transformation) -> Transformation {
    override fun invoke(p1: Transformation, p2: Transformation): Transformation {
        return p1 * p2
    }
}