package org.grandtestauto.maths.monoid.test

import org.grandtestauto.assertion.Assert
import org.grandtestauto.maths.monoid.*
import org.junit.Test

/**
 * @author Tim Lavers
 */
public class C3S4DirectProduct {
    private val c3 = cyclicGroup(3)
    private val s4 = symmetricGroup(4)
    private val unit: ((Transformation) -> ((Transformation) -> Transformation)) = { t -> { s -> s } }
    private val product = doubleProduct(c3, s4, unit, unit)

    @Test
    fun elementsTest() {
        val elements = product.elements()
        Assert.aequals(3 * 24, elements.size);
        for (t3 in c3.elements()) {
            for (t4 in s4.elements()) {
                val tuple = Tuple(t3, t4);
                Assert.azzert(elements.contains(tuple));
            }
        }
    }

    @Test
    fun compositionTest() {
        val elements = product.elements();
        for (tupleA in elements) {
            for (tupleB in elements) {
                val product = product.composition()(tupleA, tupleB);
                Assert.aequals(c3.composition()(tupleA.left(), tupleB.left()), product.left());
                Assert.aequals(s4.composition()(tupleA.right(), tupleB.right()), product.right());
            }
        }
    }
}