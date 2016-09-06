package org.grandtestauto.maths.monoid.test

import org.grandtestauto.assertion.Assert
import org.grandtestauto.maths.monoid.*
import org.grandtestauto.test.tools.Waiting
import org.junit.Test

import java.util.HashSet
import java.util.function.Predicate
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * @author Tim Lavers
 */
class POPreserveTest : TestBase() {
    @Test
    fun runTest() {
        val tuples = HashSet<Tuple<Int, Int>>()
        tuples.add(tu(1, 2))
        tuples.add(tu(1, 3))
        tuples.add(tu(1, 4))
        tuples.add(tu(1, 5))
        tuples.add(tu(2, 5))
        tuples.add(tu(3, 5))
        tuples.add(tu(4, 5))
        tuples.add(tu(1, 1))
        tuples.add(tu(2, 2))
        tuples.add(tu(3, 3))
        tuples.add(tu(4, 4))
        tuples.add(tu(5, 5))
        val baseSet = HashSet<Int>()
        baseSet.add(1)
        baseSet.add(2)
        baseSet.add(3)
        baseSet.add(4)
        baseSet.add(5)
        val relation = Relation(baseSet, tuples)
        Assert.azzert(relation.isAPartialOrder)

        val opt = orderPreservingTransformationMonoid(5)
        Assert.azzert(isAssociative(TransformationComposition, opt.elements()))
        Assert.azzert(isClosedUnderComposition(opt.elements(), TransformationComposition))

        println("opt size: " + opt.size())

        val sgp = Semigroup<Transformation>(opt.elements(), TransformationComposition)
        val greens = GreensRelations(sgp)

        val lClasses = greens.lClasses()
        Waiting.pause(2000)
        println("Number of lClasses = " + lClasses.subsets().size)
        lClasses.subsets().forEach { lClass ->
            println()
            println("lClass with size: " + lClass.size)
            lClass.forEach { tuple -> println(tuple.toString()) }
        }

        Waiting.pause(2000)
        val rClasses = greens.rClasses()
        println("NUmber of rClasses = " + rClasses.subsets().size)
        rClasses.subsets().forEach { rClass ->
            println()
            println("rClass with size: " + rClass.size)
            rClass.forEach { tuple -> println(tuple.toString()) }
        }
        Waiting.pause(2000)
    }
}