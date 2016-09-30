package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.*
import org.junit.Assert
import org.junit.Test
import java.util.*

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
        Assert.assertTrue(relation.isAPartialOrder)

        val opt = orderPreservingTransformationMonoid(5)
        Assert.assertTrue(isAssociative(TransformationComposition, opt.elements()))
        Assert.assertTrue(isClosedUnderComposition(opt.elements(), TransformationComposition))

        println("opt size: " + opt.size())

        val sgp = Semigroup(opt.elements(), TransformationComposition)
        val greens = GreensRelations(sgp)

        val lClasses = greens.lClasses()
        println("Number of lClasses = " + lClasses.subsets().size)
        lClasses.subsets().forEach { lClass ->
            println()
            println("lClass with size: " + lClass.size)
            lClass.forEach { tuple -> println(tuple.toString()) }
        }

        val rClasses = greens.rClasses()
        println("NUmber of rClasses = " + rClasses.subsets().size)
        rClasses.subsets().forEach { rClass ->
            println()
            println("rClass with size: " + rClass.size)
            rClass.forEach { tuple -> println(tuple.toString()) }
        }
    }
}