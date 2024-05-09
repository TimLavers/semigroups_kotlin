package org.grandtestauto.maths.monoid.test

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.grandtestauto.maths.monoid.*
import org.junit.Assert
import org.junit.Test

/**
 * @author Tim Lavers
 */
class TransformationTest {

    @Test
    fun isRightZeroTest() {
     assert(t(1,1,1).isRightZero)
     assert(t(5,5,5,5,5,5,5).isRightZero)
     assert(!t(1,1,2).isRightZero)
     assert(!t(6,6,6,6,6,6,6,1).isRightZero)
    }

    @Test
    fun imageTest(){
        Assert.assertEquals(set(1,2,3), t(1,2,3).image)
        Assert.assertEquals(set(1), t(1,1,1, 1).image)
    }

    @Test
    fun subsemigroupOfOrderPreservingTransformationsTest() {
        val filtered = subsemigroupOfOrderPreservingTransformations(transformationMonoid(5))
        val expected = orderPreservingTransformationMonoid(5)
        expected.elements shouldBe filtered.elements
    }

    @Test
    fun isOrderPreservingTest() {
        Assert.assertFalse(isOrderPreserving(t(2,1)))
        Assert.assertFalse(isOrderPreserving(t(1,2,1)))
        Assert.assertFalse(isOrderPreserving(t(1,1,1,2,1)))
        Assert.assertTrue(isOrderPreserving(t(1,1,1,1,1)))
        Assert.assertTrue(isOrderPreserving(t(1,1,1,2,2)))
        Assert.assertTrue(isOrderPreserving(t(1,1,1,2,3,4,4,4,6)))
    }

    @Test
    fun cycle2_3___n_1Test() {
        cycle2_3___n_1(1) shouldBe t(1)
        cycle2_3___n_1(2) shouldBe t(2, 1)
        cycle2_3___n_1(3)  shouldBe t(2, 3, 1)
        cycle2_3___n_1(9) shouldBe t(2, 3, 4, 5, 6, 7, 8, 9, 1)
    }

    @Test
    fun fixedPointsTest() {
        cycle2_3___n_1(9).fixedPoints shouldBe emptySet()
        t(1,1,1,2,1).fixedPoints shouldBe setOf(1)
        t(1,2,1,4,1).fixedPoints shouldBe setOf(1, 2, 4)
    }

    @Test
    fun movedPointsTest() {
        cycle2_3___n_1(5).movedPoints shouldBe setOf(1, 2, 3, 4, 5)
        t(1,1,1,2,1).movedPoints shouldBe setOf(2, 3, 4, 5)
    }

    @Test
    fun preImageTest() {
        cycle2_3___n_1(8).preImage(1) shouldBe setOf(8)
        cycle2_3___n_1(7).preImage(7) shouldBe setOf(6)
        t(1, 1, 1).preImage(2) shouldBe emptySet()
        t(1, 1, 1).preImage(1) shouldBe setOf(1, 2, 3)
        t(1, 2, 1, 5, 2).preImage(2) shouldBe setOf(5, 2)
    }

    @Test
    fun numberOfFixedPointsTest(){
        Assert.assertEquals(1, Transformation(intArrayOf(1)).numberOfFixedPoints)
        Assert.assertEquals(1, Transformation(intArrayOf(1, 1)).numberOfFixedPoints)
        Assert.assertEquals(1, Transformation(intArrayOf(1, 1, 2)).numberOfFixedPoints)
        Assert.assertEquals(1, Transformation(intArrayOf(1, 1, 2, 1)).numberOfFixedPoints)
        Assert.assertEquals(2, Transformation(intArrayOf(1, 1, 2, 4)).numberOfFixedPoints)
        Assert.assertEquals(2, Transformation(intArrayOf(1, 1, 3, 3)).numberOfFixedPoints)
        Assert.assertEquals(3, Transformation(intArrayOf(1, 2, 3, 3)).numberOfFixedPoints)
        Assert.assertEquals(0, Transformation(intArrayOf(2, 3, 4, 1)).numberOfFixedPoints)
    }

    @Test
    fun kernelTest(){
        var t = Transformation(intArrayOf(1))
        Assert.assertEquals(relation(tu(1, 1)), t.kernel)

        Transformation(intArrayOf(1, 2)).kernel shouldBe relation(tu(1, 1), tu(2, 2))

        Transformation(intArrayOf(1, 1)).kernel shouldBe relation(tu(1, 1), tu(2, 2), tu(1, 2), tu(2, 1))

        Transformation(intArrayOf(1, 1, 2)).kernel shouldBe relation(tu(1, 1), tu(2, 2), tu(1, 2), tu(2, 1), tu(3, 3))

        Transformation(intArrayOf(2, 1, 2)).kernel shouldBe  relation(tu(1, 1), tu(2, 2), tu(1, 3), tu(3, 1), tu(3, 3))

        Transformation(intArrayOf(2, 2, 2)).kernel shouldBe relation(tu(1, 1), tu(1, 2), tu(1, 3), tu(2, 1), tu(2, 2), tu(2, 3), tu(3, 1), tu(3, 2), tu(3, 3))

        Transformation(intArrayOf(2, 2, 1, 1)).kernel shouldBe relation(tu(1, 1), tu(1, 2), tu(2, 1), tu(2, 2), tu(3, 3), tu(3, 4), tu(4, 3), tu(4, 4))

        Transformation(intArrayOf(1, 2, 1, 1)).kernel shouldBe relation(tu(1, 1), tu(1, 3), tu(1, 4), tu(3, 1), tu(3, 3), tu(3, 4), tu(4, 1), tu(4, 3), tu(4, 4), tu(2, 2))

        Transformation(intArrayOf(1, 2, 3, 4)).kernel shouldBe relation(tu(1, 1), tu(2, 2), tu(3, 3), tu(4, 4))
    }

    @Test
    fun embedTest(){
        var t = Transformation(intArrayOf(1, 1, 1, 1))
        var embedded = t.embed(5)
        Assert.assertEquals(Transformation(intArrayOf(1, 1, 1, 1, 5)), embedded)

        embedded = t.embed(12)
        Assert.assertEquals(Transformation(intArrayOf(1, 1, 1, 1, 5, 6, 7, 8, 9, 10, 11, 12)), embedded)

        t = Transformation(intArrayOf(4, 3, 2, 1))
        embedded = t.embed(12)
        Assert.assertEquals(Transformation(intArrayOf(4, 3, 2, 1, 5, 6, 7, 8, 9, 10, 11, 12)), embedded)
    }

    @Test
    fun toStringTest(){
        val t = Transformation(intArrayOf(1, 3, 4, 1))
        Assert.assertEquals("[1, 3, 4, 1]", t.toString())
    }

    @Test
    fun definesATransformationTest(){
        Assert.assertTrue(definesATransformation(intArrayOf(1)))
        Assert.assertTrue(definesATransformation(intArrayOf(1, 2)))
        Assert.assertTrue(definesATransformation(intArrayOf(2, 1)))
        Assert.assertTrue(definesATransformation(intArrayOf(2, 1, 3)))
        Assert.assertTrue(definesATransformation(intArrayOf(2, 1, 4, 3)))
        Assert.assertTrue(definesATransformation(intArrayOf(5, 2, 1, 4, 3)))
        Assert.assertTrue(definesATransformation(intArrayOf(5, 5, 5, 5, 5, 5, 5, 5)))

        Assert.assertFalse(definesATransformation(intArrayOf(0)))
        Assert.assertFalse(definesATransformation(intArrayOf(2)))
        Assert.assertFalse(definesATransformation(intArrayOf(2, 1, 6)))
        Assert.assertFalse(definesATransformation(intArrayOf(2, 3)))
        Assert.assertFalse(definesATransformation(intArrayOf(2, 1, 0, 3)))
        Assert.assertFalse(definesATransformation(intArrayOf(5, 2, -1, 4, 3)))
    }

    @Test
    fun inDomainTest(){
        val t = Transformation(intArrayOf(1, 1, 1, 1))
        Assert.assertTrue(t.inDomain(1))
        Assert.assertTrue(t.inDomain(2))
        Assert.assertTrue(t.inDomain(3))
        Assert.assertTrue(t.inDomain(4))
        Assert.assertFalse(t.inDomain(5))
    }

    @Test
    fun rankTest(){
        Assert.assertEquals(4, Transformation(intArrayOf(1, 1, 1, 1)).rank())
        Assert.assertEquals(6, Transformation(intArrayOf(1, 5, 5, 1, 1, 1)).rank())
    }

    @Test
    fun applyTest(){
        var t = Transformation(intArrayOf(1, 1, 1, 1))
        for (i in 1..4) {
            Assert.assertEquals(1, t.apply(i))
        }

        t = Transformation(intArrayOf(1, 4, 3, 1, 4))
        Assert.assertEquals(1, t.apply(1))
        Assert.assertEquals(4, t.apply(2))
        Assert.assertEquals(3, t.apply(3))
        Assert.assertEquals(1, t.apply(4))
        Assert.assertEquals(4, t.apply(5))
    }

    @Test
    fun unitTest(){
        shouldThrow<IllegalArgumentException> { unit(0) }
        Assert.assertEquals(Transformation(intArrayOf(1)), unit(1))
        Assert.assertEquals(Transformation(intArrayOf(1, 2)), unit(2))
        Assert.assertEquals(Transformation(intArrayOf(1, 2, 3, 4, 5)), unit(5))
    }

    @Test
    fun composeTest(){
        val s = Transformation(intArrayOf(2, 3, 4, 1))
        val t = Transformation(intArrayOf(2, 3, 4, 1))
        val st = s.compose(t)
        Assert.assertEquals(4, st.rank())
        Assert.assertEquals(3, st.apply(1))
        Assert.assertEquals(4, st.apply(2))
        Assert.assertEquals(1, st.apply(3))
        Assert.assertEquals(2, st.apply(4))

        val u = Transformation(intArrayOf(1, 1, 1, 1))
        val su = s.compose(u)
        Assert.assertEquals(4, su.rank())
        Assert.assertEquals(1, su.apply(1))
        Assert.assertEquals(1, su.apply(2))
        Assert.assertEquals(1, su.apply(3))
        Assert.assertEquals(1, su.apply(4))
    }

    @Test
    fun timesTest(){
        val s = Transformation(intArrayOf(2, 3, 4, 1))
        val t = Transformation(intArrayOf(2, 3, 4, 1))
        val st = s * t
        Assert.assertEquals(4, st.rank())
        Assert.assertEquals(3, st.apply(1))
        Assert.assertEquals(4, st.apply(2))
        Assert.assertEquals(1, st.apply(3))
        Assert.assertEquals(2, st.apply(4))

        val u = Transformation(intArrayOf(1, 1, 1, 1))
        val su = s * u
        Assert.assertEquals(4, su.rank())
        Assert.assertEquals(1, su.apply(1))
        Assert.assertEquals(1, su.apply(2))
        Assert.assertEquals(1, su.apply(3))
        Assert.assertEquals(1, su.apply(4))
    }

    @Test
    fun equalsTest(){
        val s = Transformation(intArrayOf(2, 3, 4, 1))
        val t = Transformation(intArrayOf(2, 3, 4, 1))
        val u = Transformation(intArrayOf(2, 3, 4, 1, 5))
        Assert.assertTrue(s == t)
        Assert.assertTrue(t == s)
        Assert.assertFalse(t == u)
        Assert.assertFalse(u == t)
    }

    @Test
    fun hashCodeTest(){
        val s = Transformation(intArrayOf(2, 3, 4, 1))
        val t = Transformation(intArrayOf(2, 3, 4, 1))
        Assert.assertEquals(s.hashCode(), t.hashCode())
    }

    @Test
    fun domainTest(){
        Assert.assertEquals(3, t(1, 2, 1).domain.size)
        Assert.assertTrue(t(1, 2, 1).domain.contains(1))
        Assert.assertTrue(t(1, 2, 1).domain.contains(2))
        Assert.assertTrue(t(1, 2, 1).domain.contains(3))
    }
}
