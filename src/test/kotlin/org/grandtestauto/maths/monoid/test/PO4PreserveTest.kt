package org.grandtestauto.maths.monoid.test

import io.kotest.matchers.shouldBe
import org.grandtestauto.maths.monoid.*
import org.junit.Test
import java.util.*

/**
 * @author Tim Lavers
 */
class PO4PreserveTest : TestBase() {

    @Test
    fun runTest() {
        val tuples = HashSet<Pair<Int, Int>>()
        tuples.add(1 to 2)
        tuples.add(1 to 3)
        tuples.add(1 to 4)
        tuples.add(2 to 3)
        tuples.add(2 to 4)
        tuples.add(1 to 1)
        tuples.add(2 to 2)
        tuples.add(3 to 3)
        tuples.add(4 to 4)
        val baseSet = intsFrom1To(4).toSet()
        val relation = Relation(baseSet, tuples)
        relation.isAPartialOrder shouldBe true

        val orderPreservers = orderPreservingTransformationMonoid(4)
        isAssociative(TransformationComposition, orderPreservers.elements) shouldBe true
        isClosedUnderComposition(orderPreservers.elements, TransformationComposition) shouldBe true

        println("opt size: " + orderPreservers.size)

        val greens = GreensRelations(orderPreservers)
        greens.lClasses()

        val lClasses = greens.lClasses()
        println("Number of lClasses = " + lClasses.subsets().size)
        val totalFixity = intArrayOf(0)
        lClasses.subsets().forEach { lClass ->
            println()
            println("lClass with size: " + lClass.size)
            lClass.forEach { tuple -> println(tuple.toString()) }
            val fixity = lClass.sumOf { it.numberOfFixedPoints }
            totalFixity[0] += fixity
            println("fixity = $fixity")
        }
        println("totalFixity = " + totalFixity[0])

        val rClasses = greens.rClasses()
        println("Number of rClasses = " + rClasses.subsets().size)
        totalFixity[0] = 0
        rClasses.subsets().forEach { rClass ->
            println()
            println("rClass with size: " + rClass.size)
            rClass.forEach { tuple -> println(tuple.toString()) }
            val fixity = rClass.sumOf { it.numberOfFixedPoints }
            totalFixity[0] += fixity
            println("fixity = $fixity")
        }
        println("totalFixity = " + totalFixity[0])
    }
}