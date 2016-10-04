package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.*
import java.util.*
import java.util.stream.Stream


/**
 * @author Tim Lavers
 */

fun tset(vararg transformations: Transformation): Set<Transformation> {
    val result = HashSet<Transformation>()
    Collections.addAll(result, *transformations)
    return result
}

fun intsets(vararg sets: IntSet): Set<IntSet> {
    val result = HashSet<IntSet>()
    Collections.addAll(result, *sets)
    return result
}

fun t(vararg image: Int): Transformation {
    return Transformation(image)
}

fun i(vararg elements: Int): IntSet {
    val set = HashSet<Int>()
    for (i in elements) set.add(i)
    return IntSet(set)
}

fun s(vararg elements: Int): IntSet {
    val elementSet = HashSet<Int>()
    for (i in elements) elementSet.add(i)
    return IntSet(elementSet)
}

fun <T> set(vararg elements: T): Set<T> {
    val result = HashSet<T>()
    Stream.of(*elements).forEach({ result.add(it) })
    return result
}

fun <S,T> tu(i: S, j: T): Tuple<S, T> {
    return Tuple(i, j)
}

fun relation(vararg tuples: Tuple<Any, Any>): Relation<Any> {
    val elements = mutableSetOf<Tuple<Any, Any>>()
    val baseSet = HashSet<Any>()
    for (tuple in tuples) {
        elements.add(tuple)
        baseSet.add(tuple.left())
        baseSet.add(tuple.right())
    }
    return Relation(baseSet, elements)
}


fun printLClasses(greens: GreensRelations<Transformation>) {
    val lClasses = greens.lClasses()
    println("Number of lClasses = " + lClasses.subsets().size)
    lClasses.subsets().forEach { lClass ->
        println()
        println("lClass with size: " + lClass.size)
        lClass.forEach { tuple -> println(tuple.toString()) }
    }
}

fun printRClasses(greens: GreensRelations<Transformation>) {
    val rClasses = greens.rClasses()
    println("NUmber of rClasses = " + rClasses.subsets().size)
    rClasses.subsets().forEach { rClass ->
        println()
        println("rClass with size: " + rClass.size)
        rClass.forEach { tuple -> println(tuple.toString()) }
    }
}
open class TestBase {
}
