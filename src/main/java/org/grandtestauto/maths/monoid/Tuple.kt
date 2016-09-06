package org.grandtestauto.maths.monoid

/**
 * An ordered pair of elements.

 * @author Tim Lavers
 */
//@Deprecated("Pair")
class Tuple<S, T>(private val s: S, private val t: T) {

    fun flip(): Tuple<T, S> {
        return Tuple(t, s)
    }

    fun left(): S {
        return s
    }

    fun right(): T {
        return t
    }

    override fun toString(): String {
        return StringBuilder().append("<").append(s).append(", ").append(t).append(">").toString()
    }

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Tuple<*, *>

        if (s != other.s) return false
        if (t != other.t) return false

        return true
    }

    override fun hashCode(): Int{
        var result = s?.hashCode() ?: 0
        result += 31 * result + (t?.hashCode() ?: 0)
        return result
    }
}
