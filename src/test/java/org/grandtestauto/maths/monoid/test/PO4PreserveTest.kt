package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.*
import org.junit.Assert
import org.junit.Test
import java.util.*

/**


 * @author Tim Lavers
 */
class PO4PreserveTest : TestBase() {

    @Test
    fun runTest() {
        val tuples = HashSet<Tuple<Int, Int>>()
        tuples.add(tu(1, 2))
        tuples.add(tu(1, 3))
        tuples.add(tu(1, 4))
        tuples.add(tu(2, 4))
        tuples.add(tu(3, 4))
        tuples.add(tu(1, 1))
        tuples.add(tu(2, 2))
        tuples.add(tu(3, 3))
        tuples.add(tu(4, 4))
        val baseSet = HashSet<Int>()
        baseSet.add(1)
        baseSet.add(2)
        baseSet.add(3)
        baseSet.add(4)
        val relation = Relation(baseSet, tuples)
        Assert.assertTrue(relation.isAPartialOrder)

        val orderPreservers = orderPreservingTransformationMonoid(4)
        Assert.assertTrue(isAssociative(TransformationComposition, orderPreservers.elements()))
        Assert.assertTrue(isClosedUnderComposition(orderPreservers.elements(), TransformationComposition))

        println("opt size: " + orderPreservers.elements().size)

        val greens = GreensRelations(orderPreservers)
        greens.lClasses()

        val lClasses = greens.lClasses()
        println("Number of lClasses = " + lClasses.subsets().size)
        val totalFixity = intArrayOf(0)
        lClasses.subsets().forEach { lClass ->
            println()
            println("lClass with size: " + lClass.size)
            lClass.forEach { tuple -> println(tuple.toString()) }
            val fixity = lClass.sumBy { it.numberOfFixedPoints }
            totalFixity[0] += fixity
            println("fixity = " + fixity)
        }
        println("totalFixity = " + totalFixity[0])

        val rClasses = greens.rClasses()
        println("Number of rClasses = " + rClasses.subsets().size)
        totalFixity[0] = 0
        rClasses.subsets().forEach { rClass ->
            println()
            println("rClass with size: " + rClass.size)
            rClass.forEach { tuple -> println(tuple.toString()) }
            val fixity = rClass.sumBy { it.numberOfFixedPoints }
            totalFixity[0] += fixity
            println("fixity = " + fixity)
        }
        println("totalFixity = " + totalFixity[0])
    }
}