package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.*
import org.junit.Assert
import org.junit.Test

/**
 * @author Tim Lavers
 */
class DoubleProductTest {

    private val s3: Semigroup<Transformation> = symmetricGroup(3)
    private val s4: Semigroup<Transformation> = symmetricGroup(4)
    private val unit: ((Transformation) -> ((Transformation) -> Transformation)) = {t -> {s -> s}}
    private val product = doubleProduct(s3, s4, unit, unit)

    @Test
    fun elementsTest() {
        val elements = product.elements()
        Assert.assertEquals(6 * 24, elements.size)
        for (t3 in s3.elements()) {
            for (t4 in s4.elements()) {
                val tuple = Tuple(t3, t4)
                Assert.assertTrue(elements.contains(tuple))
            }
        }
    }

    @Test
    fun compositionTest() {
        val elements = product.elements()
        for (tupleA in elements) {
            for (tupleB in elements) {
                val product = this.product.composition()(tupleA, tupleB)
                Assert.assertEquals(s3.composition()(tupleA.left(), tupleB.left()), product.left())
                Assert.assertEquals(s4.composition()(tupleA.right(), tupleB.right()), product.right())
            }
        }
    }
}