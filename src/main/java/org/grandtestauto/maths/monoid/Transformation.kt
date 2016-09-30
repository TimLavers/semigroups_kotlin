package org.grandtestauto.maths.monoid

import java.util.*

fun definesATransformation(map: IntArray): Boolean {
    for (i in map) {
        if (i < 1) return false
        if (i > map.size) return false
    }
    return true
}
fun cycle2_3___n_1(rank_atLeast2: Int): Transformation {
    val generatorData = IntArray(rank_atLeast2)
    for (i in 0..rank_atLeast2 - 1 - 1) {
        generatorData[i] = i + 2
    }
    generatorData[rank_atLeast2 - 1] = 1
    return Transformation(generatorData)
}

fun unit(rank: Int): Transformation {
    val map = IntArray(rank)
    for (i in map.indices) {
        map[i] = i + 1
    }
    return Transformation(map)
}
/**
 * An immutable function from the set of integers {1, 2, ... , n} to itself.

 * @author Tim Lavers
 */
class Transformation(private val map: IntArray) {

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

    fun kernel(): Relation<Int> {
        val pairs = HashSet<Tuple<Int, Int>>()
        for (i in 1..map.size) {
            for (j in 1..map.size) {
                if (apply(i) == apply(j)) pairs.add(Tuple(i, j))
            }
        }
        return Relation(domain(), pairs)
    }

    fun numberOfFixedPoints(): Int {
        var result = 0
        for (i in 1..map.size) {
            if (i == apply(i)) result++
        }
        return result
    }

    fun embed(m_greaterThanRank: Int): Transformation {
        val newMap = IntArray(m_greaterThanRank)
        System.arraycopy(map, 0, newMap, 0, map.size)
        for (i in map.size..m_greaterThanRank - 1) {
            newMap[i] = i + 1
        }
        return Transformation(newMap)
    }

    fun rank(): Int {
        return map.size
    }

    fun compose(t: Transformation): Transformation {
        assert(t.rank() == rank())
        val compositeMap = IntArray(map.size)
        for (i in 1..map.size) {
            compositeMap[i - 1] = t.apply(map[i - 1])
        }
        return Transformation(compositeMap)
    }

    operator fun times(other : Transformation) : Transformation {
        return compose(other)
    }

    fun domain(): Set<Int> {
        val result = HashSet<Int>(map.size)
        for (i in 1..map.size) result.add(i)
        return result
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(map)
    }

    override fun equals(other: Any?): Boolean {
        return other is Transformation && Arrays.equals(map, other.map)
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
//object TransformationComposition : Composition<Transformation> {
//    override fun compose(x: Transformation, y: Transformation): Transformation {
//        return x.compose(y)
//    }
//}
