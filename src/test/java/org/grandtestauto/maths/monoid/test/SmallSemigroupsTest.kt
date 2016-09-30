package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.SubsetsOfSizeN
import org.grandtestauto.maths.monoid.TransformationComposition
import org.grandtestauto.maths.monoid.isClosedUnderComposition
import org.grandtestauto.maths.monoid.transformationMonoid
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
}
