package org.grandtestauto.maths.monoid.test

import org.junit.Assert
import org.grandtestauto.maths.monoid.*
import org.junit.Test

import java.util.HashSet

/**
 * @author Tim Lavers
 */
class SemigroupTest : TestBase() {

    @Test
    fun isHomomorphismTest() {
        val cyc2 = cyclicGroup(2)
        val cyc3 = cyclicGroup(3)
        val all_cyc3_cyc2 = allFunctionsFromTo(cyc3.elements(), cyc2.elements())
        Assert.assertEquals(8, all_cyc3_cyc2.size)

        val homomorphisms_cyc3_cyc2 = all_cyc3_cyc2.filter { isHomomorphism(it, cyc3, cyc2) }
        Assert.assertEquals(1, homomorphisms_cyc3_cyc2.size)
        val homomorphism = homomorphisms_cyc3_cyc2.iterator().next()
        cyc3.forEach { t -> Assert.assertEquals(homomorphism.invoke(t), unit(2)) }

        val o2 = orderPreservingTransformationMonoid(2)
        val all_o2_o2 = allFunctionsFromTo(o2.elements(), o2.elements())
        Assert.assertEquals(27, all_o2_o2.size)
        val homomorphisms_o2_o2 = all_o2_o2.filter({ isHomomorphism(it, o2, o2) })
        all_o2_o2.forEach {
            if (it in homomorphisms_o2_o2) {
                Assert.assertTrue(checkHomomorphism(it, o2))
            } else {
                Assert.assertFalse(checkHomomorphism(it, o2))
            }
        }
    }

    fun checkHomomorphism(function: FiniteFunction<Transformation, Transformation>, o2: Semigroup<Transformation>) : Boolean  {
        o2.forEach({
            s ->
            o2.forEach { t ->
                val st = o2.composition()(s, t)
                val fsft = o2.composition()(function.invoke(s), function.invoke(t))
                val fst = function.invoke(st)
                if (fst != fsft) {
                    return false
                }
            }
        })
        return true
    }

    @Test
    fun rightZeroSemigroupTest() {
        for (i in 1..9) {
            val sg = rightZeroSemigroup(i)
            Assert.assertTrue(isAssociative(sg.composition(), sg.elements()))
            Assert.assertTrue(isClosedUnderComposition(sg.elements(), sg.composition()))
            sg.forEach { t -> sg.forEach { u -> Assert.assertEquals(u, sg.composition()(t, u)) } }
        }
    }

    @Test
    fun leftZeroSemigroupTest() {
        for (i in 1..9) {
            val sg = leftZeroSemigroup(i)
            Assert.assertTrue(isAssociative(sg.composition(), sg.elements()))
            Assert.assertTrue(isClosedUnderComposition(sg.elements(), sg.composition()))
            sg.forEach { t -> sg.forEach { u -> Assert.assertEquals(t, sg.composition()(t, u)) } }
        }
    }

    @Test
    fun rightIdealTest() {
        val t3 = transformationMonoid(3)
        var expected = set(t(1, 1, 1), t(2, 2, 2), t(3, 3, 3))
        Assert.assertEquals(expected, rightIdeal(t3, t(1, 1, 1)))

        expected = set(t(2, 2, 1), t(1, 1, 2), t(2, 2, 3), t(3, 3, 2), t(3, 3, 1), t(1, 1, 3), t(1, 1, 1), t(2, 2, 2), t(3, 3, 3))
        Assert.assertEquals(expected, rightIdeal(t3, t(2, 2, 1)))
    }

    @Test
    fun leftIdealTest() {
        val t3 = transformationMonoid(3)
        var expected = set(t(1, 1, 1))
        Assert.assertEquals(expected, leftIdeal(t3, t(1, 1, 1)))

        expected = set(t(2, 2, 2), t(1, 1, 2), t(1, 2, 1), t(2, 1, 1), t(1, 2, 2), t(2, 1, 2), t(2, 2, 1), t(1, 1, 1))
        Assert.assertEquals(expected, leftIdeal(t3, t(1, 1, 2)))

        expected = set(t(1, 1, 3), t(1, 3, 1), t(3, 1, 1), t(1, 3, 3), t(3, 1, 3), t(3, 3, 1), t(1, 1, 1), t(3, 3, 3))
        Assert.assertEquals(expected, leftIdeal(t3, t(1, 1, 3)))

        Assert.assertEquals(t3.elements(), leftIdeal(t3, t(2, 1, 3)))
    }

    @Test
    fun powerSetIntersectionTest() {
        val p2 = powerSetIntersection(2)
        Assert.assertEquals(4, p2.size())
        Assert.assertTrue(p2.contains(i(1, 2)))
        Assert.assertTrue(p2.contains(i(2)))
        Assert.assertTrue(p2.contains(i(1)))
        Assert.assertTrue(p2.contains(i()))
        Assert.assertEquals(p2.composition()(i(1, 2), i(1, 2)), i(1, 2))
        Assert.assertEquals(p2.composition()(i(1, 2), i(1)), i(1))
        Assert.assertEquals(p2.composition()(i(1, 2), i(2)), i(2))
        Assert.assertEquals(p2.composition()(i(1), i(2)), i())

        val p3 = powerSetIntersection(3)
        Assert.assertEquals(8, p3.size())
        Assert.assertTrue(p3.contains(i(1, 2, 3)))
        Assert.assertTrue(p3.contains(i(2, 3)))
        Assert.assertTrue(p3.contains(i(1, 3)))
        Assert.assertTrue(p3.contains(i(3)))
        Assert.assertTrue(p3.contains(i(1, 2)))
        Assert.assertTrue(p3.contains(i(2)))
        Assert.assertTrue(p3.contains(i(1)))
        Assert.assertTrue(p3.contains(i()))
        Assert.assertEquals(p3.composition()(i(1, 2), i(1, 2)), i(1, 2))
        Assert.assertEquals(p3.composition()(i(1, 2), i(1)), i(1))
        Assert.assertEquals(p3.composition()(i(1, 2), i(2)), i(2))
        Assert.assertEquals(p3.composition()(i(1), i(2)), i())
        Assert.assertEquals(p3.composition()(i(1, 2, 3), i(1, 2, 3)), i(1, 2, 3))
        Assert.assertEquals(p3.composition()(i(1, 3), i(1)), i(1))
        Assert.assertEquals(p3.composition()(i(1, 3), i(3)), i(3))
        Assert.assertEquals(p3.composition()(i(1), i(3)), i())
        Assert.assertTrue(isClosedUnderComposition(p3.elements(), p3.composition()))

        val p4 = powerSetIntersection(4)
        Assert.assertEquals(16, p4.size())
        Assert.assertTrue(p4.contains(i(1, 2, 3, 4)))
        Assert.assertTrue(p4.contains(i(2, 3, 4)))
        Assert.assertTrue(p4.contains(i(1, 3, 4)))
        Assert.assertTrue(p4.contains(i(3, 4)))
        Assert.assertTrue(p4.contains(i(1, 2, 4)))
        Assert.assertTrue(p4.contains(i(2, 4)))
        Assert.assertTrue(p4.contains(i(1, 4)))
        Assert.assertTrue(p4.contains(i(4)))
        Assert.assertTrue(p4.contains(i(1, 2, 3)))
        Assert.assertTrue(p4.contains(i(2, 3)))
        Assert.assertTrue(p4.contains(i(1, 3)))
        Assert.assertTrue(p4.contains(i(3)))
        Assert.assertTrue(p4.contains(i(1, 2)))
        Assert.assertTrue(p4.contains(i(2)))
        Assert.assertTrue(p4.contains(i(1)))
        Assert.assertTrue(p4.contains(i()))
        Assert.assertEquals(p4.composition()(i(1, 2), i(1, 2)), i(1, 2))
        Assert.assertEquals(p4.composition()(i(1, 2), i(1)), i(1))
        Assert.assertEquals(p4.composition()(i(1, 2), i(2)), i(2))
        Assert.assertEquals(p4.composition()(i(1), i(2)), i())
        Assert.assertEquals(p4.composition()(i(1, 2, 3), i(1, 2, 3)), i(1, 2, 3))
        Assert.assertEquals(p4.composition()(i(1, 3), i(1)), i(1))
        Assert.assertEquals(p4.composition()(i(1, 3), i(3)), i(3))
        Assert.assertEquals(p4.composition()(i(1), i(3)), i())
        Assert.assertEquals(p4.composition()(i(1, 2, 3, 4), i(1, 2, 3, 4)), i(1, 2, 3, 4))
        Assert.assertEquals(p4.composition()(i(1, 2, 4), i(1, 4)), i(1, 4))
        Assert.assertEquals(p4.composition()(i(1, 2), i(2)), i(2))
        Assert.assertEquals(p4.composition()(i(1), i(4)), i())
        Assert.assertEquals(p4.composition()(i(1, 2, 3), i(1, 4, 3)), i(1, 3))
        Assert.assertEquals(p4.composition()(i(4, 3), i(4)), i(4))
        Assert.assertTrue(isClosedUnderComposition(p4.elements(), p4.composition()))
    }

    @Test
    fun orderPreservingTransformationMonoidTest() {
        val o2 = orderPreservingTransformationMonoid(2)
        Assert.assertEquals(3, o2.size())
        Assert.assertTrue(o2.contains(t(1, 1)))
        Assert.assertTrue(o2.contains(t(2, 2)))
        Assert.assertTrue(o2.contains(t(1, 2)))

        val o3 = orderPreservingTransformationMonoid(3)
        Assert.assertEquals(10, o3.size())
        Assert.assertTrue(o3.contains(t(1, 1, 1)))
        Assert.assertTrue(o3.contains(t(2, 2, 2)))
        Assert.assertTrue(o3.contains(t(3, 3, 3)))

        Assert.assertTrue(o3.contains(t(1, 1, 2)))
        Assert.assertTrue(o3.contains(t(1, 1, 3)))
        Assert.assertTrue(o3.contains(t(2, 2, 3)))

        Assert.assertTrue(o3.contains(t(1, 2, 2)))
        Assert.assertTrue(o3.contains(t(1, 3, 3)))
        Assert.assertTrue(o3.contains(t(2, 3, 3)))

        Assert.assertTrue(o3.contains(t(1, 2, 3)))
        o3.elements().forEach { t -> Assert.assertTrue("Not order-preserving: " + t,isOrderPreserving(t)) }

        val o4 = orderPreservingTransformationMonoid(4)
        Assert.assertEquals(35, o4.size())
        o4.elements().forEach { t -> Assert.assertTrue(isOrderPreserving(t)) }
    }

    private fun isOrderPreserving(t: Transformation): Boolean {
        t.domain().forEach {
            if (it > 1) {
                if (t.apply(it - 1) > t.apply(it)) return false
            }
        }
        return true
    }

    @Test
    fun transformationMonoidTest() {
        Assert.assertEquals(4, transformationMonoid(2).size())
        Assert.assertEquals(27, transformationMonoid(3).size())
        Assert.assertEquals(256, transformationMonoid(4).size())
        Assert.assertEquals(125 * 25, transformationMonoid(5).size())
    }

    @Test
    fun symmetricGroupTest() {
        val s2 = symmetricGroup(2)
        Assert.assertEquals(2, s2.size())
        Assert.assertTrue(s2.contains(t(1, 2)))
        Assert.assertTrue(s2.contains(t(2, 1)))

        val s3 = symmetricGroup(3)
        Assert.assertEquals(6, s3.size())
        Assert.assertTrue(s3.contains(t(1, 2, 3)))
        Assert.assertTrue(s3.contains(t(2, 1, 3)))
        Assert.assertTrue(s3.contains(t(1, 3, 2)))
        Assert.assertTrue(s3.contains(t(3, 2, 1)))
        Assert.assertTrue(s3.contains(t(2, 3, 1)))
        Assert.assertTrue(s3.contains(t(3, 1, 2)))

        val s4 = symmetricGroup(4)
        Assert.assertEquals(24, s4.size())
        Assert.assertTrue(s4.contains(t(1, 2, 3, 4)))
        Assert.assertTrue(s4.contains(t(2, 1, 3, 4)))
        Assert.assertTrue(s4.contains(t(1, 3, 2, 4)))
        Assert.assertTrue(s4.contains(t(3, 2, 1, 4)))
        Assert.assertTrue(s4.contains(t(2, 3, 1, 4)))
        Assert.assertTrue(s4.contains(t(3, 1, 2, 4)))//6

        Assert.assertTrue(s4.contains(t(1, 2, 4, 3)))
        Assert.assertTrue(s4.contains(t(1, 2, 4, 3)))
        Assert.assertTrue(s4.contains(t(1, 4, 3, 2)))
        Assert.assertTrue(s4.contains(t(1, 3, 4, 2)))
        Assert.assertTrue(s4.contains(t(1, 4, 2, 3)))//5

        Assert.assertTrue(s4.contains(t(3, 2, 1, 4)))
        Assert.assertTrue(s4.contains(t(3, 2, 4, 1)))
        Assert.assertTrue(s4.contains(t(4, 2, 3, 1)))
        Assert.assertTrue(s4.contains(t(4, 2, 1, 3)))//4

        Assert.assertTrue(s4.contains(t(4, 1, 3, 2)))
        Assert.assertTrue(s4.contains(t(4, 2, 3, 1)))
        Assert.assertTrue(s4.contains(t(2, 4, 3, 1)))//3

        Assert.assertTrue(s4.contains(t(2, 1, 4, 3)))
        Assert.assertTrue(s4.contains(t(2, 4, 1, 3)))
        Assert.assertTrue(s4.contains(t(2, 3, 4, 1)))
        Assert.assertTrue(s4.contains(t(3, 1, 4, 2)))
        Assert.assertTrue(s4.contains(t(3, 4, 1, 2)))
        Assert.assertTrue(s4.contains(t(3, 4, 2, 1)))
        Assert.assertTrue(s4.contains(t(4, 3, 2, 1)))
        Assert.assertTrue(s4.contains(t(4, 1, 2, 3)))
        Assert.assertTrue(s4.contains(t(4, 3, 1, 2)))

        val s5 = symmetricGroup(5)
        Assert.assertEquals(120, s5.size())
        for (t in s5) {
            Assert.assertEquals(5, t.rank())
            Assert.assertTrue(t.apply(1) != t.apply(2))
            Assert.assertTrue(t.apply(1) != t.apply(3))
            Assert.assertTrue(t.apply(1) != t.apply(4))
            Assert.assertTrue(t.apply(1) != t.apply(5))
            Assert.assertTrue(t.apply(2) != t.apply(3))
            Assert.assertTrue(t.apply(2) != t.apply(4))
            Assert.assertTrue(t.apply(2) != t.apply(5))
            Assert.assertTrue(t.apply(3) != t.apply(4))
            Assert.assertTrue(t.apply(3) != t.apply(5))
            Assert.assertTrue(t.apply(4) != t.apply(5))
        }
    }

    @Test
    fun symmetricGroupLargeTest() {
        val s3 = symmetricGroup(3)
        Assert.assertEquals(6, s3.size())
        Assert.assertEquals(24, symmetricGroup(4).size())
        Assert.assertEquals(120, symmetricGroup(5).size())
        Assert.assertEquals(720, symmetricGroup(6).size())
        Assert.assertEquals(5040, symmetricGroup(7).size())
        Assert.assertEquals(40320, symmetricGroup(8).size())
        Assert.assertEquals(40320 * 9, symmetricGroup(9).size())
        //        Assert.assertEquals(40320 * 90, Semigroup.symmetricGroup(10).size());
    }

    @Test
    fun cyclicGroupTest() {
        val c2 = cyclicGroup(2)
        Assert.assertEquals(2, c2.size())
        Assert.assertTrue(c2.contains(t(1, 2)))
        Assert.assertTrue(c2.contains(t(2, 1)))

        val c3 = cyclicGroup(3)
        Assert.assertEquals(3, c3.size())
        Assert.assertTrue(c3.contains(t(1, 2, 3)))
        Assert.assertTrue(c3.contains(t(2, 3, 1)))
        Assert.assertTrue(c3.contains(t(3, 1, 2)))

        val c4 = cyclicGroup(4)
        Assert.assertEquals(4, c4.size())
        Assert.assertTrue(c4.contains(t(1, 2, 3, 4)))
        Assert.assertTrue(c4.contains(t(2, 3, 4, 1)))
        Assert.assertTrue(c4.contains(t(3, 4, 1, 2)))
        Assert.assertTrue(c4.contains(t(4, 1, 2, 3)))
    }

    @Test
    fun isClosedUnderCompositionTest() {
        val compo: (Transformation, Transformation) -> Transformation = { x, y -> x.compose(y) }

        Assert.assertTrue(isClosedUnderComposition(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), compo))
        Assert.assertTrue(isClosedUnderComposition(tset(t(2, 2, 2), t(1, 1, 1), t(1, 1, 2)), compo))
        fun dumb(x: Transformation, y: Transformation): Transformation {
            val dumb = IntArray(x.rank())
            for (i in 1..x.rank()) {
                dumb[i - 1] = Math.max(x.apply(i), y.apply(i))
            }
            return Transformation(dumb)
        }

        val stupid: (Transformation, Transformation) -> Transformation = { x, y -> dumb(x, y) }
        Assert.assertFalse(isClosedUnderComposition(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), stupid))
    }

    @Test
    fun isAssociativeTest() {
        Assert.assertTrue(isAssociative(TransformationComposition, tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3))))
        Assert.assertTrue(isAssociative(TransformationComposition, tset(t(2, 2, 2), t(1, 1, 1), t(1, 1, 2))))
        Assert.assertFalse(isClosedUnderComposition(tset(t(3, 1, 2), t(1, 2, 3)), TransformationComposition))
        Assert.assertFalse(isClosedUnderComposition(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3), t(1, 3, 2)), TransformationComposition))
    }

    @Test
    fun generateFromTest() {
        val c5generator = tset(t(2, 3, 4, 5, 1))
        val generated = generateFrom(TransformationComposition, c5generator)
        Assert.assertEquals(5, generated.size())
        Assert.assertTrue(generated.contains(t(1, 2, 3, 4, 5)))
        Assert.assertTrue(generated.contains(t(2, 3, 4, 5, 1)))
        Assert.assertTrue(generated.contains(t(3, 4, 5, 1, 2)))
        Assert.assertTrue(generated.contains(t(4, 5, 1, 2, 3)))
        Assert.assertTrue(generated.contains(t(5, 1, 2, 3, 4)))
    }

    @Test
    fun sizeTest() {
        val semigroup = Semigroup(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), TransformationComposition)
        Assert.assertEquals(3, semigroup.size())
        val semigroup2 = Semigroup(intsets(s(1), s(2), s(3), s(1, 2), s(1, 3), s(2, 3), s(1, 2, 3)), UnionComposition)
        Assert.assertEquals(7, semigroup2.size())
    }

    @Test
    fun elementsTest() {
        val semigroup = Semigroup(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), TransformationComposition)
        val elements = semigroup.elements()
        Assert.assertEquals(3, elements.size)
        Assert.assertTrue(elements.contains(t(2, 3, 1)))
        Assert.assertTrue(elements.contains(t(3, 1, 2)))
        Assert.assertTrue(elements.contains(t(1, 2, 3)))
    }

    @Test
    fun powerOfTest() {
        val c = t(2, 3, 4, 1)
        val s4 = symmetricGroup(4)
        Assert.assertEquals(c, s4.powerOf(c, 1))
        Assert.assertEquals(t(3, 4, 1, 2), s4.powerOf(c, 2))
        Assert.assertEquals(t(4, 1, 2, 3), s4.powerOf(c, 3))
        Assert.assertEquals(t(1, 2, 3, 4), s4.powerOf(c, 4))
    }

    @Test
    fun iteratorTest() {
        val semigroup = Semigroup(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), TransformationComposition)
        val elements = HashSet<Transformation>()
        for (t in semigroup) elements.add(t)
        Assert.assertEquals(3, elements.size)
        Assert.assertTrue(elements.contains(t(2, 3, 1)))
        Assert.assertTrue(elements.contains(t(3, 1, 2)))
        Assert.assertTrue(elements.contains(t(1, 2, 3)))
    }

    @Test
    fun compositionTest() {
        val semigroup = Semigroup(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), TransformationComposition)
        Assert.assertTrue(TransformationComposition === semigroup.composition())
    }

    @Test
    fun containsTest() {
        val semigroup = Semigroup(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), TransformationComposition)
        Assert.assertTrue(semigroup.contains(t(2, 3, 1)))
        Assert.assertTrue(semigroup.contains(t(3, 1, 2)))
        Assert.assertTrue(semigroup.contains(t(1, 2, 3)))
        val semigroup2 = Semigroup(intsets(s(1), s(2), s(3), s(1, 2), s(1, 3), s(2, 3), s(1, 2, 3)), UnionComposition)
        Assert.assertTrue(semigroup2.contains(s(1)))
        Assert.assertTrue(semigroup2.contains(s(2)))
        Assert.assertTrue(semigroup2.contains(s(3)))
        Assert.assertTrue(semigroup2.contains(s(1, 2)))
        Assert.assertTrue(semigroup2.contains(s(1, 3)))
        Assert.assertTrue(semigroup2.contains(s(2, 3)))
        Assert.assertTrue(semigroup2.contains(s(1, 2, 3)))
    }
}