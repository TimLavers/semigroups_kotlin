package org.grandtestauto.maths.monoid.test

import org.grandtestauto.assertion.Assert
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
        Assert.aequals(8, all_cyc3_cyc2.size)

        val homomorphisms_cyc3_cyc2 = all_cyc3_cyc2.filter { isHomomorphism(it, cyc3, cyc2) }
        Assert.aequals(1, homomorphisms_cyc3_cyc2.size)
        val homomorphism = homomorphisms_cyc3_cyc2.iterator().next()
        cyc3.forEach { t -> Assert.aequals(homomorphism.apply(t), unit(2)) }

        val o2 = orderPreservingTransformationMonoid(2)
        val all_o2_o2 = allFunctionsFromTo(o2.elements(), o2.elements())
        Assert.aequals(27, all_o2_o2.size)
        val homomorphisms_o2_o2 = all_o2_o2.filter({ isHomomorphism(it, o2, o2) })
        all_o2_o2.forEach {
            if (it in homomorphisms_o2_o2) {
                Assert.azzert(checkHomomorphism(it, o2))
            } else {
                Assert.azzertFalse(checkHomomorphism(it, o2))
            }
        }
    }

    fun checkHomomorphism(function: FiniteFunction<Transformation, Transformation>, o2: Semigroup<Transformation>) : Boolean  {
        o2.forEach({
            s ->
            o2.forEach { t ->
                val st = o2.composition()(s, t)
                val fsft = o2.composition()(function.apply(s), function.apply(t))
                val fst = function.apply(st)
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
            Assert.azzert(isAssociative(sg.composition(), sg.elements()))
            Assert.azzert(isClosedUnderComposition(sg.elements(), sg.composition()))
            sg.forEach { t -> sg.forEach { u -> Assert.aequals(u, sg.composition()(t, u)) } }
        }
    }

    @Test
    fun leftZeroSemigroupTest() {
        for (i in 1..9) {
            val sg = leftZeroSemigroup(i)
            Assert.azzert(isAssociative(sg.composition(), sg.elements()))
            Assert.azzert(isClosedUnderComposition(sg.elements(), sg.composition()))
            sg.forEach { t -> sg.forEach { u -> Assert.aequals(t, sg.composition()(t, u)) } }
        }
    }

    @Test
    fun rightIdealTest() {
        val t3 = transformationMonoid(3)
        var expected = set(t(1, 1, 1), t(2, 2, 2), t(3, 3, 3))
        Assert.aequals(expected, rightIdeal(t3, t(1, 1, 1)))

        expected = set(t(2, 2, 1), t(1, 1, 2), t(2, 2, 3), t(3, 3, 2), t(3, 3, 1), t(1, 1, 3), t(1, 1, 1), t(2, 2, 2), t(3, 3, 3))
        Assert.aequals(expected, rightIdeal(t3, t(2, 2, 1)))
    }

    @Test
    fun leftIdealTest() {
        val t3 = transformationMonoid(3)
        var expected = set(t(1, 1, 1))
        Assert.aequals(expected, leftIdeal(t3, t(1, 1, 1)))

        expected = set(t(2, 2, 2), t(1, 1, 2), t(1, 2, 1), t(2, 1, 1), t(1, 2, 2), t(2, 1, 2), t(2, 2, 1), t(1, 1, 1))
        Assert.aequals(expected, leftIdeal(t3, t(1, 1, 2)))

        expected = set(t(1, 1, 3), t(1, 3, 1), t(3, 1, 1), t(1, 3, 3), t(3, 1, 3), t(3, 3, 1), t(1, 1, 1), t(3, 3, 3))
        Assert.aequals(expected, leftIdeal(t3, t(1, 1, 3)))

        Assert.aequals(t3.elements(), leftIdeal(t3, t(2, 1, 3)))
    }

    @Test
    fun powerSetIntersectionTest() {
        val p2 = powerSetIntersection(2)
        Assert.aequals(4, p2.size())
        Assert.azzert(p2.contains(i(1, 2)))
        Assert.azzert(p2.contains(i(2)))
        Assert.azzert(p2.contains(i(1)))
        Assert.azzert(p2.contains(i()))
        Assert.aequals(p2.composition()(i(1, 2), i(1, 2)), i(1, 2))
        Assert.aequals(p2.composition()(i(1, 2), i(1)), i(1))
        Assert.aequals(p2.composition()(i(1, 2), i(2)), i(2))
        Assert.aequals(p2.composition()(i(1), i(2)), i())

        val p3 = powerSetIntersection(3)
        Assert.aequals(8, p3.size())
        Assert.azzert(p3.contains(i(1, 2, 3)))
        Assert.azzert(p3.contains(i(2, 3)))
        Assert.azzert(p3.contains(i(1, 3)))
        Assert.azzert(p3.contains(i(3)))
        Assert.azzert(p3.contains(i(1, 2)))
        Assert.azzert(p3.contains(i(2)))
        Assert.azzert(p3.contains(i(1)))
        Assert.azzert(p3.contains(i()))
        Assert.aequals(p3.composition()(i(1, 2), i(1, 2)), i(1, 2))
        Assert.aequals(p3.composition()(i(1, 2), i(1)), i(1))
        Assert.aequals(p3.composition()(i(1, 2), i(2)), i(2))
        Assert.aequals(p3.composition()(i(1), i(2)), i())
        Assert.aequals(p3.composition()(i(1, 2, 3), i(1, 2, 3)), i(1, 2, 3))
        Assert.aequals(p3.composition()(i(1, 3), i(1)), i(1))
        Assert.aequals(p3.composition()(i(1, 3), i(3)), i(3))
        Assert.aequals(p3.composition()(i(1), i(3)), i())
        Assert.azzert(isClosedUnderComposition(p3.elements(), p3.composition()))

        val p4 = powerSetIntersection(4)
        Assert.aequals(16, p4.size())
        Assert.azzert(p4.contains(i(1, 2, 3, 4)))
        Assert.azzert(p4.contains(i(2, 3, 4)))
        Assert.azzert(p4.contains(i(1, 3, 4)))
        Assert.azzert(p4.contains(i(3, 4)))
        Assert.azzert(p4.contains(i(1, 2, 4)))
        Assert.azzert(p4.contains(i(2, 4)))
        Assert.azzert(p4.contains(i(1, 4)))
        Assert.azzert(p4.contains(i(4)))
        Assert.azzert(p4.contains(i(1, 2, 3)))
        Assert.azzert(p4.contains(i(2, 3)))
        Assert.azzert(p4.contains(i(1, 3)))
        Assert.azzert(p4.contains(i(3)))
        Assert.azzert(p4.contains(i(1, 2)))
        Assert.azzert(p4.contains(i(2)))
        Assert.azzert(p4.contains(i(1)))
        Assert.azzert(p4.contains(i()))
        Assert.aequals(p4.composition()(i(1, 2), i(1, 2)), i(1, 2))
        Assert.aequals(p4.composition()(i(1, 2), i(1)), i(1))
        Assert.aequals(p4.composition()(i(1, 2), i(2)), i(2))
        Assert.aequals(p4.composition()(i(1), i(2)), i())
        Assert.aequals(p4.composition()(i(1, 2, 3), i(1, 2, 3)), i(1, 2, 3))
        Assert.aequals(p4.composition()(i(1, 3), i(1)), i(1))
        Assert.aequals(p4.composition()(i(1, 3), i(3)), i(3))
        Assert.aequals(p4.composition()(i(1), i(3)), i())
        Assert.aequals(p4.composition()(i(1, 2, 3, 4), i(1, 2, 3, 4)), i(1, 2, 3, 4))
        Assert.aequals(p4.composition()(i(1, 2, 4), i(1, 4)), i(1, 4))
        Assert.aequals(p4.composition()(i(1, 2), i(2)), i(2))
        Assert.aequals(p4.composition()(i(1), i(4)), i())
        Assert.aequals(p4.composition()(i(1, 2, 3), i(1, 4, 3)), i(1, 3))
        Assert.aequals(p4.composition()(i(4, 3), i(4)), i(4))
        Assert.azzert(isClosedUnderComposition(p4.elements(), p4.composition()))
    }

    @Test
    fun orderPreservingTransformationMonoidTest() {
        val o2 = orderPreservingTransformationMonoid(2)
        Assert.aequals(3, o2.size())
        Assert.azzert(o2.contains(t(1, 1)))
        Assert.azzert(o2.contains(t(2, 2)))
        Assert.azzert(o2.contains(t(1, 2)))

        val o3 = orderPreservingTransformationMonoid(3)
        Assert.aequals(10, o3.size())
        Assert.azzert(o3.contains(t(1, 1, 1)))
        Assert.azzert(o3.contains(t(2, 2, 2)))
        Assert.azzert(o3.contains(t(3, 3, 3)))

        Assert.azzert(o3.contains(t(1, 1, 2)))
        Assert.azzert(o3.contains(t(1, 1, 3)))
        Assert.azzert(o3.contains(t(2, 2, 3)))

        Assert.azzert(o3.contains(t(1, 2, 2)))
        Assert.azzert(o3.contains(t(1, 3, 3)))
        Assert.azzert(o3.contains(t(2, 3, 3)))

        Assert.azzert(o3.contains(t(1, 2, 3)))
        o3.elements().forEach { t -> Assert.azzert(isOrderPreserving(t), "Not order-preserving: " + t) }

        val o4 = orderPreservingTransformationMonoid(4)
        Assert.aequals(35, o4.size())
        o4.elements().forEach { t -> Assert.azzert(isOrderPreserving(t)) }
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
        Assert.aequals(4, transformationMonoid(2).size())
        Assert.aequals(27, transformationMonoid(3).size())
        Assert.aequals(256, transformationMonoid(4).size())
        Assert.aequals(125 * 25, transformationMonoid(5).size())
    }

    @Test
    fun symmetricGroupTest() {
        val s2 = symmetricGroup(2)
        Assert.aequals(2, s2.size())
        Assert.azzert(s2.contains(t(1, 2)))
        Assert.azzert(s2.contains(t(2, 1)))

        val s3 = symmetricGroup(3)
        Assert.aequals(6, s3.size())
        Assert.azzert(s3.contains(t(1, 2, 3)))
        Assert.azzert(s3.contains(t(2, 1, 3)))
        Assert.azzert(s3.contains(t(1, 3, 2)))
        Assert.azzert(s3.contains(t(3, 2, 1)))
        Assert.azzert(s3.contains(t(2, 3, 1)))
        Assert.azzert(s3.contains(t(3, 1, 2)))

        val s4 = symmetricGroup(4)
        Assert.aequals(24, s4.size())
        Assert.azzert(s4.contains(t(1, 2, 3, 4)))
        Assert.azzert(s4.contains(t(2, 1, 3, 4)))
        Assert.azzert(s4.contains(t(1, 3, 2, 4)))
        Assert.azzert(s4.contains(t(3, 2, 1, 4)))
        Assert.azzert(s4.contains(t(2, 3, 1, 4)))
        Assert.azzert(s4.contains(t(3, 1, 2, 4)))//6

        Assert.azzert(s4.contains(t(1, 2, 4, 3)))
        Assert.azzert(s4.contains(t(1, 2, 4, 3)))
        Assert.azzert(s4.contains(t(1, 4, 3, 2)))
        Assert.azzert(s4.contains(t(1, 3, 4, 2)))
        Assert.azzert(s4.contains(t(1, 4, 2, 3)))//5

        Assert.azzert(s4.contains(t(3, 2, 1, 4)))
        Assert.azzert(s4.contains(t(3, 2, 4, 1)))
        Assert.azzert(s4.contains(t(4, 2, 3, 1)))
        Assert.azzert(s4.contains(t(4, 2, 1, 3)))//4

        Assert.azzert(s4.contains(t(4, 1, 3, 2)))
        Assert.azzert(s4.contains(t(4, 2, 3, 1)))
        Assert.azzert(s4.contains(t(2, 4, 3, 1)))//3

        Assert.azzert(s4.contains(t(2, 1, 4, 3)))
        Assert.azzert(s4.contains(t(2, 4, 1, 3)))
        Assert.azzert(s4.contains(t(2, 3, 4, 1)))
        Assert.azzert(s4.contains(t(3, 1, 4, 2)))
        Assert.azzert(s4.contains(t(3, 4, 1, 2)))
        Assert.azzert(s4.contains(t(3, 4, 2, 1)))
        Assert.azzert(s4.contains(t(4, 3, 2, 1)))
        Assert.azzert(s4.contains(t(4, 1, 2, 3)))
        Assert.azzert(s4.contains(t(4, 3, 1, 2)))

        val s5 = symmetricGroup(5)
        Assert.aequals(120, s5.size())
        for (t in s5) {
            Assert.aequals(5, t.rank())
            Assert.azzert(t.apply(1) != t.apply(2))
            Assert.azzert(t.apply(1) != t.apply(3))
            Assert.azzert(t.apply(1) != t.apply(4))
            Assert.azzert(t.apply(1) != t.apply(5))
            Assert.azzert(t.apply(2) != t.apply(3))
            Assert.azzert(t.apply(2) != t.apply(4))
            Assert.azzert(t.apply(2) != t.apply(5))
            Assert.azzert(t.apply(3) != t.apply(4))
            Assert.azzert(t.apply(3) != t.apply(5))
            Assert.azzert(t.apply(4) != t.apply(5))
        }
    }

    @Test
    fun symmetricGroupLargeTest() {
        val s3 = symmetricGroup(3)
        Assert.aequals(6, s3.size())
        Assert.aequals(24, symmetricGroup(4).size())
        Assert.aequals(120, symmetricGroup(5).size())
        Assert.aequals(720, symmetricGroup(6).size())
        Assert.aequals(5040, symmetricGroup(7).size())
        Assert.aequals(40320, symmetricGroup(8).size())
        Assert.aequals(40320 * 9, symmetricGroup(9).size())
        //        Assert.aequals(40320 * 90, Semigroup.symmetricGroup(10).size());
    }

    @Test
    fun cyclicGroupTest() {
        val c2 = cyclicGroup(2)
        Assert.aequals(2, c2.size())
        Assert.azzert(c2.contains(t(1, 2)))
        Assert.azzert(c2.contains(t(2, 1)))

        val c3 = cyclicGroup(3)
        Assert.aequals(3, c3.size())
        Assert.azzert(c3.contains(t(1, 2, 3)))
        Assert.azzert(c3.contains(t(2, 3, 1)))
        Assert.azzert(c3.contains(t(3, 1, 2)))

        val c4 = cyclicGroup(4)
        Assert.aequals(4, c4.size())
        Assert.azzert(c4.contains(t(1, 2, 3, 4)))
        Assert.azzert(c4.contains(t(2, 3, 4, 1)))
        Assert.azzert(c4.contains(t(3, 4, 1, 2)))
        Assert.azzert(c4.contains(t(4, 1, 2, 3)))
    }

    @Test
    fun isClosedUnderCompositionTest() {
        val compo: (Transformation, Transformation) -> Transformation = { x, y -> x.compose(y) }

        Assert.azzert(isClosedUnderComposition(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), compo))
        Assert.azzert(isClosedUnderComposition(tset(t(2, 2, 2), t(1, 1, 1), t(1, 1, 2)), compo))
        fun dumb(x: Transformation, y: Transformation): Transformation {
            val dumb = IntArray(x.rank())
            for (i in 1..x.rank()) {
                dumb[i - 1] = Math.max(x.apply(i), y.apply(i))
            }
            return Transformation(dumb)
        }

        val stupid: (Transformation, Transformation) -> Transformation = { x, y -> dumb(x, y) }
        Assert.azzertFalse(isClosedUnderComposition(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), stupid))
    }

    @Test
    fun isAssociativeTest() {
        Assert.azzert(isAssociative(TransformationComposition, tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3))))
        Assert.azzert(isAssociative(TransformationComposition, tset(t(2, 2, 2), t(1, 1, 1), t(1, 1, 2))))
        Assert.azzertFalse(isClosedUnderComposition(tset(t(3, 1, 2), t(1, 2, 3)), TransformationComposition))
        Assert.azzertFalse(isClosedUnderComposition(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3), t(1, 3, 2)), TransformationComposition))
    }

    @Test
    fun generateFromTest() {
        val c5generator = tset(t(2, 3, 4, 5, 1))
        val generated = generateFrom(TransformationComposition, c5generator)
        Assert.aequals(5, generated.size())
        Assert.azzert(generated.contains(t(1, 2, 3, 4, 5)))
        Assert.azzert(generated.contains(t(2, 3, 4, 5, 1)))
        Assert.azzert(generated.contains(t(3, 4, 5, 1, 2)))
        Assert.azzert(generated.contains(t(4, 5, 1, 2, 3)))
        Assert.azzert(generated.contains(t(5, 1, 2, 3, 4)))
    }

    @Test
    fun sizeTest() {
        val semigroup = Semigroup(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), TransformationComposition)
        Assert.aequals(3, semigroup.size())
        val semigroup2 = Semigroup(intsets(s(1), s(2), s(3), s(1, 2), s(1, 3), s(2, 3), s(1, 2, 3)), UnionComposition)
        Assert.aequals(7, semigroup2.size())
    }

    @Test
    fun elementsTest() {
        val semigroup = Semigroup(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), TransformationComposition)
        val elements = semigroup.elements()
        Assert.aequals(3, elements.size)
        Assert.azzert(elements.contains(t(2, 3, 1)))
        Assert.azzert(elements.contains(t(3, 1, 2)))
        Assert.azzert(elements.contains(t(1, 2, 3)))
    }

    @Test
    fun powerOfTest() {
        val c = t(2, 3, 4, 1)
        val s4 = symmetricGroup(4)
        Assert.aequals(c, s4.powerOf(c, 1))
        Assert.aequals(t(3, 4, 1, 2), s4.powerOf(c, 2))
        Assert.aequals(t(4, 1, 2, 3), s4.powerOf(c, 3))
        Assert.aequals(t(1, 2, 3, 4), s4.powerOf(c, 4))
    }

    @Test
    fun iteratorTest() {
        val semigroup = Semigroup(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), TransformationComposition)
        val elements = HashSet<Transformation>()
        for (t in semigroup) elements.add(t)
        Assert.aequals(3, elements.size)
        Assert.azzert(elements.contains(t(2, 3, 1)))
        Assert.azzert(elements.contains(t(3, 1, 2)))
        Assert.azzert(elements.contains(t(1, 2, 3)))
    }

    @Test
    fun compositionTest() {
        val semigroup = Semigroup(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), TransformationComposition)
        Assert.azzert(TransformationComposition === semigroup.composition())
    }

    @Test
    fun containsTest() {
        val semigroup = Semigroup(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), TransformationComposition)
        Assert.azzert(semigroup.contains(t(2, 3, 1)))
        Assert.azzert(semigroup.contains(t(3, 1, 2)))
        Assert.azzert(semigroup.contains(t(1, 2, 3)))
        val semigroup2 = Semigroup(intsets(s(1), s(2), s(3), s(1, 2), s(1, 3), s(2, 3), s(1, 2, 3)), UnionComposition)
        Assert.azzert(semigroup2.contains(s(1)))
        Assert.azzert(semigroup2.contains(s(2)))
        Assert.azzert(semigroup2.contains(s(3)))
        Assert.azzert(semigroup2.contains(s(1, 2)))
        Assert.azzert(semigroup2.contains(s(1, 3)))
        Assert.azzert(semigroup2.contains(s(2, 3)))
        Assert.azzert(semigroup2.contains(s(1, 2, 3)))
    }
}