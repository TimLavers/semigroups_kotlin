package org.grandtestauto.maths.monoid

import java.util.*

fun <S, T> allFunctionsFromTo(domain: Set<S>, range: Set<T>): Set<FiniteFunction<S, T>> {
    val result = HashSet<FiniteFunction<S, T>>()
    val mapsSoFar = HashSet<Map<S, T>>()
    mapsSoFar.add(HashMap())
    domain.forEach { d ->
        val mapsWith_d_InDomain = HashSet<Map<S, T>>()
        mapsSoFar.forEach { m ->
            range.forEach { r ->
                val newMap = HashMap(m)
                newMap[d] = r
                mapsWith_d_InDomain.add(newMap)
            }
        }
        mapsSoFar.clear()
        mapsSoFar.addAll(mapsWith_d_InDomain)
    }
    mapsSoFar.forEach { m -> result.add(FiniteFunction(m)) }
    return result
}

/**
 * A function between two finite sets.
 */
class FiniteFunction<S, T>(val data: Map<S, T>) : (S)->(T) {

    operator fun plus(other: Pair<S,T>) : FiniteFunction<S,T> {
        val map =mutableMapOf<S,T>()
        map.putAll(data)
        map[other.left()] = other.right()
        return FiniteFunction(map)
    }

    override fun invoke(s: S): T {
        return data[s] ?: error("$s ∉ domain")
    }

    fun domain(): Set<S> {
        return data.keys
    }

    fun range(): Set<T> {
        return HashSet(data.values)
    }

    override fun toString(): String {
        return "{$data}"
    }

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as FiniteFunction<*, *>

        return data == other.data
    }

    override fun hashCode(): Int{
        return data.hashCode()
    }

    class Builder<S, T> {
        private val beingAddedTo = HashMap<S, T>()

        fun add(domainElement: S, rangeElement: T): Builder<S, T> {
            beingAddedTo[domainElement] = rangeElement
            return this
        }

        fun build(): FiniteFunction<S, T> {
            return FiniteFunction(beingAddedTo)
        }
    }
}