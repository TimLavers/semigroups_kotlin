package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.*
import org.junit.Assert.*
import org.junit.Test
import java.util.*

/**
 * @author Tim Lavers
 */
class SemigroupTest : TestBase() {

    @Test
    fun chainSemigroupTest() {
        val i5 = chainSemigroup(5)
        assert(i5.size == 5)
        assert(i5.idempotents.size == 5)
        for (i in 1..5) {
            assert(i5.contains(i))
        }
        assert(isClosedUnderComposition(i5.elements, i5.composition))
        assert(isAssociative(i5.composition, i5.elements))
        i5.forEach { s ->
            i5.forEach { t -> assert(i5.composition(s, t) == s.coerceAtLeast(t)) }
        }
    }

    @Test
    fun isCongruenceTest() {
        val o3 = orderPreservingTransformationMonoid(3)
        val silly = createRelation(o3.elements, { a, b -> a.apply(1) == b.apply(1) })
        assert(!o3.isCongruence(silly))

        val closer = createRelation(o3.elements, { a, b -> a.image == b.image })
        assert(!o3.isCongruence(closer))

        val o5 = orderPreservingTransformationMonoid(5)
        val almost = createRelation(o5.elements, { a, b -> a.kernel == b.kernel })
        assert(!o5.isCongruence(almost))

        val identityCongruence = createRelation(o5.elements, { a, b -> a.kernel == b.kernel && a.image == b.image })
        assert(o5.isCongruence(identityCongruence))

        /*
        If we map On to {0, 1} by id -> 1 and all other elements -> 0, we shold
        get a homomorphism with kernel equivalent to the following congruence.
         */
        val zeroOrId = createRelation(o5.elements, { a, b -> (a.image.size < 5 && b.image.size < 5) || (a.image.size == 5 && b.image.size == 5) })
        assert(o5.isCongruence(zeroOrId))

        //todo tn examples
    }

    @Test
    fun isGroupTest() {
        assert(cyclicGroup(5).isGroup)
        assert(symmetricGroup(5).isGroup)
        assert(!orderPreservingTransformationMonoid(5).isGroup)
    }

    @Test
    fun idempotentsTest() {
        val cyc7 = cyclicGroup(7)
        assertEquals(1, cyc7.idempotents.size)

        val o2 = orderPreservingTransformationMonoid(2)
        val idempotentsO2 = o2.idempotents
        assertEquals(3, idempotentsO2.size)
        assertFalse(idempotentsO2.contains(t(2, 1)))
    }

    @Test
    fun isHomomorphismTest() {
        val cyc2 = cyclicGroup(2)
        val cyc3 = cyclicGroup(3)
        val all_cyc3_cyc2 = allFunctionsFromTo(cyc3.elements, cyc2.elements)
        assertEquals(8, all_cyc3_cyc2.size)

        val homomorphisms_cyc3_cyc2 = all_cyc3_cyc2.filter { isHomomorphism(it, cyc3, cyc2) }
        assertEquals(1, homomorphisms_cyc3_cyc2.size)
        val homomorphism = homomorphisms_cyc3_cyc2.iterator().next()
        cyc3.forEach { t -> assertEquals(homomorphism.invoke(t), unit(2)) }

        val o2 = orderPreservingTransformationMonoid(2)
        val all_o2_o2 = allFunctionsFromTo(o2.elements, o2.elements)
        assertEquals(27, all_o2_o2.size)
        val homomorphisms_o2_o2 = all_o2_o2.filter({ isHomomorphism(it, o2, o2) })
        all_o2_o2.forEach {
            if (it in homomorphisms_o2_o2) {
                assertTrue(checkHomomorphism(it, o2))
            } else {
                assertFalse(checkHomomorphism(it, o2))
            }
        }
    }

    fun checkHomomorphism(function: FiniteFunction<Transformation, Transformation>, o2: Semigroup<Transformation>): Boolean {
        o2.forEach({
            s ->
            o2.forEach { t ->
                val st = o2.composition(s, t)
                val fsft = o2.composition(function.invoke(s), function.invoke(t))
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
            assertTrue(isAssociative(sg.composition, sg.elements))
            assertTrue(isClosedUnderComposition(sg.elements, sg.composition))
            sg.forEach { t -> sg.forEach { u -> assertEquals(u, sg.composition(t, u)) } }
        }
    }

    @Test
    fun leftZeroSemigroupTest() {
        for (i in 1..9) {
            val sg = leftZeroSemigroup(i)
            assertTrue(isAssociative(sg.composition, sg.elements))
            assertTrue(isClosedUnderComposition(sg.elements, sg.composition))
            sg.forEach { t -> sg.forEach { u -> assertEquals(t, sg.composition(t, u)) } }
        }
    }

    @Test
    fun rightIdealTest() {
        val t3 = transformationMonoid(3)
        var expected = set(t(1, 1, 1), t(2, 2, 2), t(3, 3, 3))
        assertEquals(expected, t3.rightIdeal(t(1, 1, 1)))

        expected = set(t(2, 2, 1), t(1, 1, 2), t(2, 2, 3), t(3, 3, 2), t(3, 3, 1), t(1, 1, 3), t(1, 1, 1), t(2, 2, 2), t(3, 3, 3))
        assertEquals(expected, t3.rightIdeal(t(2, 2, 1)))
    }

    @Test
    fun leftIdealTest() {
        val t3 = transformationMonoid(3)
        var expected = set(t(1, 1, 1))
        assertEquals(expected, t3.leftIdeal(t(1, 1, 1)))

        expected = set(t(2, 2, 2), t(1, 1, 2), t(1, 2, 1), t(2, 1, 1), t(1, 2, 2), t(2, 1, 2), t(2, 2, 1), t(1, 1, 1))
        assertEquals(expected, t3.leftIdeal(t(1, 1, 2)))

        expected = set(t(1, 1, 3), t(1, 3, 1), t(3, 1, 1), t(1, 3, 3), t(3, 1, 3), t(3, 3, 1), t(1, 1, 1), t(3, 3, 3))
        assertEquals(expected, t3.leftIdeal(t(1, 1, 3)))

        assertEquals(t3.elements, t3.leftIdeal(t(2, 1, 3)))
    }

    @Test
    fun powerSetIntersectionTest() {
        val p2 = powerSetIntersection(2)
        assertEquals(4, p2.size)
        assertTrue(p2.contains(i(1, 2)))
        assertTrue(p2.contains(i(2)))
        assertTrue(p2.contains(i(1)))
        assertTrue(p2.contains(i()))
        assertEquals(p2.composition(i(1, 2), i(1, 2)), i(1, 2))
        assertEquals(p2.composition(i(1, 2), i(1)), i(1))
        assertEquals(p2.composition(i(1, 2), i(2)), i(2))
        assertEquals(p2.composition(i(1), i(2)), i())

        val p3 = powerSetIntersection(3)
        assertEquals(8, p3.size)
        assertTrue(p3.contains(i(1, 2, 3)))
        assertTrue(p3.contains(i(2, 3)))
        assertTrue(p3.contains(i(1, 3)))
        assertTrue(p3.contains(i(3)))
        assertTrue(p3.contains(i(1, 2)))
        assertTrue(p3.contains(i(2)))
        assertTrue(p3.contains(i(1)))
        assertTrue(p3.contains(i()))
        assertEquals(p3.composition(i(1, 2), i(1, 2)), i(1, 2))
        assertEquals(p3.composition(i(1, 2), i(1)), i(1))
        assertEquals(p3.composition(i(1, 2), i(2)), i(2))
        assertEquals(p3.composition(i(1), i(2)), i())
        assertEquals(p3.composition(i(1, 2, 3), i(1, 2, 3)), i(1, 2, 3))
        assertEquals(p3.composition(i(1, 3), i(1)), i(1))
        assertEquals(p3.composition(i(1, 3), i(3)), i(3))
        assertEquals(p3.composition(i(1), i(3)), i())
        assertTrue(isClosedUnderComposition(p3.elements, p3.composition))

        val p4 = powerSetIntersection(4)
        assertEquals(16, p4.size)
        assertTrue(p4.contains(i(1, 2, 3, 4)))
        assertTrue(p4.contains(i(2, 3, 4)))
        assertTrue(p4.contains(i(1, 3, 4)))
        assertTrue(p4.contains(i(3, 4)))
        assertTrue(p4.contains(i(1, 2, 4)))
        assertTrue(p4.contains(i(2, 4)))
        assertTrue(p4.contains(i(1, 4)))
        assertTrue(p4.contains(i(4)))
        assertTrue(p4.contains(i(1, 2, 3)))
        assertTrue(p4.contains(i(2, 3)))
        assertTrue(p4.contains(i(1, 3)))
        assertTrue(p4.contains(i(3)))
        assertTrue(p4.contains(i(1, 2)))
        assertTrue(p4.contains(i(2)))
        assertTrue(p4.contains(i(1)))
        assertTrue(p4.contains(i()))
        assertEquals(p4.composition(i(1, 2), i(1, 2)), i(1, 2))
        assertEquals(p4.composition(i(1, 2), i(1)), i(1))
        assertEquals(p4.composition(i(1, 2), i(2)), i(2))
        assertEquals(p4.composition(i(1), i(2)), i())
        assertEquals(p4.composition(i(1, 2, 3), i(1, 2, 3)), i(1, 2, 3))
        assertEquals(p4.composition(i(1, 3), i(1)), i(1))
        assertEquals(p4.composition(i(1, 3), i(3)), i(3))
        assertEquals(p4.composition(i(1), i(3)), i())
        assertEquals(p4.composition(i(1, 2, 3, 4), i(1, 2, 3, 4)), i(1, 2, 3, 4))
        assertEquals(p4.composition(i(1, 2, 4), i(1, 4)), i(1, 4))
        assertEquals(p4.composition(i(1, 2), i(2)), i(2))
        assertEquals(p4.composition(i(1), i(4)), i())
        assertEquals(p4.composition(i(1, 2, 3), i(1, 4, 3)), i(1, 3))
        assertEquals(p4.composition(i(4, 3), i(4)), i(4))
        assertTrue(isClosedUnderComposition(p4.elements, p4.composition))
    }

    @Test
    fun toStringTest() {
        val o3 = orderPreservingTransformationMonoid(3)
        val toString = o3.toString()
        o3.forEach { assert(toString.contains(it.toString())) }
    }

    @Test
    fun orderPreservingTransformationMonoidTest() {
        val o2 = orderPreservingTransformationMonoid(2)
        assertEquals(3, o2.size)
        assertTrue(o2.contains(t(1, 1)))
        assertTrue(o2.contains(t(2, 2)))
        assertTrue(o2.contains(t(1, 2)))

        val o3 = orderPreservingTransformationMonoid(3)
        assertEquals(10, o3.size)
        assertTrue(o3.contains(t(1, 1, 1)))
        assertTrue(o3.contains(t(2, 2, 2)))
        assertTrue(o3.contains(t(3, 3, 3)))

        assertTrue(o3.contains(t(1, 1, 2)))
        assertTrue(o3.contains(t(1, 1, 3)))
        assertTrue(o3.contains(t(2, 2, 3)))

        assertTrue(o3.contains(t(1, 2, 2)))
        assertTrue(o3.contains(t(1, 3, 3)))
        assertTrue(o3.contains(t(2, 3, 3)))

        assertTrue(o3.contains(t(1, 2, 3)))
        o3.forEach { t -> assertTrue("Not order-preserving: " + t, isOrderPreserving(t)) }

        val o4 = orderPreservingTransformationMonoid(4)
        assertEquals(35, o4.size)
        o4.forEach { t -> assertTrue(isOrderPreserving(t)) }
    }

    @Test
    fun transformationMonoidTest() {
        assertEquals(4, transformationMonoid(2).size)
        assertEquals(27, transformationMonoid(3).size)
        assertEquals(256, transformationMonoid(4).size)
        assertEquals(125 * 25, transformationMonoid(5).size)
    }

    @Test
    fun symmetricGroupTest() {
        val s2 = symmetricGroup(2)
        assertEquals(2, s2.size)
        assertTrue(s2.contains(t(1, 2)))
        assertTrue(s2.contains(t(2, 1)))

        val s3 = symmetricGroup(3)
        assertEquals(6, s3.size)
        assertTrue(s3.contains(t(1, 2, 3)))
        assertTrue(s3.contains(t(2, 1, 3)))
        assertTrue(s3.contains(t(1, 3, 2)))
        assertTrue(s3.contains(t(3, 2, 1)))
        assertTrue(s3.contains(t(2, 3, 1)))
        assertTrue(s3.contains(t(3, 1, 2)))

        val s4 = symmetricGroup(4)
        assertEquals(24, s4.size)
        assertTrue(s4.contains(t(1, 2, 3, 4)))
        assertTrue(s4.contains(t(2, 1, 3, 4)))
        assertTrue(s4.contains(t(1, 3, 2, 4)))
        assertTrue(s4.contains(t(3, 2, 1, 4)))
        assertTrue(s4.contains(t(2, 3, 1, 4)))
        assertTrue(s4.contains(t(3, 1, 2, 4)))//6

        assertTrue(s4.contains(t(1, 2, 4, 3)))
        assertTrue(s4.contains(t(1, 2, 4, 3)))
        assertTrue(s4.contains(t(1, 4, 3, 2)))
        assertTrue(s4.contains(t(1, 3, 4, 2)))
        assertTrue(s4.contains(t(1, 4, 2, 3)))//5

        assertTrue(s4.contains(t(3, 2, 1, 4)))
        assertTrue(s4.contains(t(3, 2, 4, 1)))
        assertTrue(s4.contains(t(4, 2, 3, 1)))
        assertTrue(s4.contains(t(4, 2, 1, 3)))//4

        assertTrue(s4.contains(t(4, 1, 3, 2)))
        assertTrue(s4.contains(t(4, 2, 3, 1)))
        assertTrue(s4.contains(t(2, 4, 3, 1)))//3

        assertTrue(s4.contains(t(2, 1, 4, 3)))
        assertTrue(s4.contains(t(2, 4, 1, 3)))
        assertTrue(s4.contains(t(2, 3, 4, 1)))
        assertTrue(s4.contains(t(3, 1, 4, 2)))
        assertTrue(s4.contains(t(3, 4, 1, 2)))
        assertTrue(s4.contains(t(3, 4, 2, 1)))
        assertTrue(s4.contains(t(4, 3, 2, 1)))
        assertTrue(s4.contains(t(4, 1, 2, 3)))
        assertTrue(s4.contains(t(4, 3, 1, 2)))

        val s5 = symmetricGroup(5)
        assertEquals(120, s5.size)
        for (t in s5) {
            assertEquals(5, t.rank())
            assertTrue(t.apply(1) != t.apply(2))
            assertTrue(t.apply(1) != t.apply(3))
            assertTrue(t.apply(1) != t.apply(4))
            assertTrue(t.apply(1) != t.apply(5))
            assertTrue(t.apply(2) != t.apply(3))
            assertTrue(t.apply(2) != t.apply(4))
            assertTrue(t.apply(2) != t.apply(5))
            assertTrue(t.apply(3) != t.apply(4))
            assertTrue(t.apply(3) != t.apply(5))
            assertTrue(t.apply(4) != t.apply(5))
        }
    }

    @Test
    fun symmetricGroupLargeTest() {
        val s3 = symmetricGroup(3)
        assertEquals(6, s3.size)
        assertEquals(24, symmetricGroup(4).size)
        assertEquals(120, symmetricGroup(5).size)
        assertEquals(720, symmetricGroup(6).size)
        assertEquals(5040, symmetricGroup(7).size)
        assertEquals(40320, symmetricGroup(8).size)
        assertEquals(40320 * 9, symmetricGroup(9).size)
        //        Assert.assertEquals(40320 * 90, Semigroup.symmetricGroup(10).size);
    }

    @Test
    fun cyclicGroupTest() {
        val c2 = cyclicGroup(2)
        assertEquals(2, c2.size)
        assertTrue(c2.contains(t(1, 2)))
        assertTrue(c2.contains(t(2, 1)))

        val c3 = cyclicGroup(3)
        assertEquals(3, c3.size)
        assertTrue(c3.contains(t(1, 2, 3)))
        assertTrue(c3.contains(t(2, 3, 1)))
        assertTrue(c3.contains(t(3, 1, 2)))

        val c4 = cyclicGroup(4)
        assertEquals(4, c4.size)
        assertTrue(c4.contains(t(1, 2, 3, 4)))
        assertTrue(c4.contains(t(2, 3, 4, 1)))
        assertTrue(c4.contains(t(3, 4, 1, 2)))
        assertTrue(c4.contains(t(4, 1, 2, 3)))
    }

    @Test
    fun isClosedUnderCompositionTest() {
        val compo: (Transformation, Transformation) -> Transformation = { x, y -> x.compose(y) }

        assertTrue(isClosedUnderComposition(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), compo))
        assertTrue(isClosedUnderComposition(tset(t(2, 2, 2), t(1, 1, 1), t(1, 1, 2)), compo))
        fun dumb(x: Transformation, y: Transformation): Transformation {
            val dumb = IntArray(x.rank())
            for (i in 1..x.rank()) {
                dumb[i - 1] = Math.max(x.apply(i), y.apply(i))
            }
            return Transformation(dumb)
        }

        val stupid: (Transformation, Transformation) -> Transformation = { x, y -> dumb(x, y) }
        assertFalse(isClosedUnderComposition(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), stupid))
    }

    @Test
    fun isAssociativeTest() {
        assertTrue(isAssociative(TransformationComposition, tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3))))
        assertTrue(isAssociative(TransformationComposition, tset(t(2, 2, 2), t(1, 1, 1), t(1, 1, 2))))
        assertFalse(isClosedUnderComposition(tset(t(3, 1, 2), t(1, 2, 3)), TransformationComposition))
        assertFalse(isClosedUnderComposition(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3), t(1, 3, 2)), TransformationComposition))
    }

    @Test
    fun generateFromTest() {
        val c5generator = tset(t(2, 3, 4, 5, 1))
        val generated = generateFrom(TransformationComposition, c5generator)
        assertEquals(5, generated.size)
        assertTrue(generated.contains(t(1, 2, 3, 4, 5)))
        assertTrue(generated.contains(t(2, 3, 4, 5, 1)))
        assertTrue(generated.contains(t(3, 4, 5, 1, 2)))
        assertTrue(generated.contains(t(4, 5, 1, 2, 3)))
        assertTrue(generated.contains(t(5, 1, 2, 3, 4)))
    }

    @Test
    fun sizeTest() {
        val semigroup = Semigroup(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), TransformationComposition)
        assertEquals(3, semigroup.size)
        val semigroup2 = Semigroup(intsets(s(1), s(2), s(3), s(1, 2), s(1, 3), s(2, 3), s(1, 2, 3)), UnionComposition)
        assertEquals(7, semigroup2.size)
    }

    @Test
    fun elementsTest() {
        val semigroup = Semigroup(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), TransformationComposition)
        val elements = semigroup.elements
        assertEquals(3, elements.size)
        assertTrue(elements.contains(t(2, 3, 1)))
        assertTrue(elements.contains(t(3, 1, 2)))
        assertTrue(elements.contains(t(1, 2, 3)))
    }

    @Test
    fun powerOfTest() {
        val c = t(2, 3, 4, 1)
        val s4 = symmetricGroup(4)
        assertEquals(c, s4.powerOf(c, 1))
        assertEquals(t(3, 4, 1, 2), s4.powerOf(c, 2))
        assertEquals(t(4, 1, 2, 3), s4.powerOf(c, 3))
        assertEquals(t(1, 2, 3, 4), s4.powerOf(c, 4))
    }

    @Test
    fun iteratorTest() {
        val semigroup = Semigroup(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), TransformationComposition)
        val elements = HashSet<Transformation>()
        for (t in semigroup) elements.add(t)
        assertEquals(3, elements.size)
        assertTrue(elements.contains(t(2, 3, 1)))
        assertTrue(elements.contains(t(3, 1, 2)))
        assertTrue(elements.contains(t(1, 2, 3)))
    }

    @Test
    fun compositionTest() {
        val semigroup = Semigroup(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), TransformationComposition)
        assertTrue(TransformationComposition === semigroup.composition)
    }

    @Test
    fun containsTest() {
        val semigroup = Semigroup(tset(t(2, 3, 1), t(3, 1, 2), t(1, 2, 3)), TransformationComposition)
        assertTrue(semigroup.contains(t(2, 3, 1)))
        assertTrue(semigroup.contains(t(3, 1, 2)))
        assertTrue(semigroup.contains(t(1, 2, 3)))
        val semigroup2 = Semigroup(intsets(s(1), s(2), s(3), s(1, 2), s(1, 3), s(2, 3), s(1, 2, 3)), UnionComposition)
        assertTrue(semigroup2.contains(s(1)))
        assertTrue(semigroup2.contains(s(2)))
        assertTrue(semigroup2.contains(s(3)))
        assertTrue(semigroup2.contains(s(1, 2)))
        assertTrue(semigroup2.contains(s(1, 3)))
        assertTrue(semigroup2.contains(s(2, 3)))
        assertTrue(semigroup2.contains(s(1, 2, 3)))
    }
}