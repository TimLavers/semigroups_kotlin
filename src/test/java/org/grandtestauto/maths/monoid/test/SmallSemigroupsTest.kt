package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.*
import org.junit.Test

/**
 * Generation of some small semigroups.

 * @author Tim Lavers
 */
class SmallSemigroupsTest : TestBase() {
    @Test
    fun size3Test() {
        val transformationsOf3 = transformationMonoid(3).elements()
        println("transformationsOf3.size() = " + transformationsOf3.size)

        val setsOf3Trannies = SubsetsOfSizeN(transformationsOf3, 3)
        val threeElementSemis = setsOf3Trannies.subsets().filter({ subset -> isClosedUnderComposition(subset, TransformationComposition) })

        println("threeElementSemis.size() = " + threeElementSemis.size)
        threeElementSemis.forEach( {println(it)})
    }

    @Test
    fun e1() {
        val t1 = t(2,4,1,2,1)
        val t2 = t(2,5,2,3,1)
        val generators = set(t1, t2)
        val semigroup = generateFrom(TransformationComposition, generators)

        val size = semigroup.size()
        println("size = ${size}")
        val idempotents = semigroup.idempotents()
        println("idempotents: ${idempotents.size}")
        val greens = GreensRelations(semigroup)
        printLClasses(greens)
        printRClasses(greens)

        val orderPreservers = subsemigroupOfOrderPreservingTransformations(semigroup)
        println("orderPreservers = ${orderPreservers}")
    }
}