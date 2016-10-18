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
        Assert.assertEquals(6 * 24, product.size)
        for (t3 in s3) {
            for (t4 in s4) {
                val tuple = Tuple(t3, t4)
                Assert.assertTrue(product.contains(tuple))
            }
        }
    }

    @Test
    fun compositionTest() {
        for (tupleA in product) {
            for (tupleB in product) {
                val product = this.product.composition(tupleA, tupleB)
                Assert.assertEquals(s3.composition(tupleA.left(), tupleB.left()), product.left())
                Assert.assertEquals(s4.composition(tupleA.right(), tupleB.right()), product.right())
            }
        }
    }
}