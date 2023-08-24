package org.grandtestauto.maths.monoid.test

import io.kotest.matchers.shouldBe
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
        val tuples = HashSet<Pair<Int, Int>>()
        tuples.add(1 to 1)
        tuples.add(1 to 2)
        tuples.add(1 to 3)
        tuples.add(1 to 4)
        tuples.add(1 to 5)
        tuples.add(2 to 2)
        tuples.add(2 to 3)
        tuples.add(2 to 4)
        tuples.add(2 to 5)
        tuples.add(3 to 3)
        tuples.add(3 to 4)
        tuples.add(3 to 5)
        tuples.add(4 to 4)
        tuples.add(4 to 5)
        tuples.add(5 to 5)
        val baseSet = intsFrom1To(5).toSet()
        val relation = Relation(baseSet, tuples)
        Assert.assertTrue(relation.isAPartialOrder)

        val opt = orderPreservingTransformationMonoid(5)
        isAssociative(TransformationComposition, opt.elements) shouldBe true
        isClosedUnderComposition(opt.elements, TransformationComposition) shouldBe true
        println("opt size: " + opt.size)

        val sgp = Semigroup(opt.elements, TransformationComposition)
        val greens = GreensRelations(sgp)

        printLClasses(greens)
        printRClasses(greens)
    }
}