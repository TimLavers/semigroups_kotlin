package org.grandtestauto.maths.monoid.test

import io.kotest.matchers.shouldBe
import org.grandtestauto.maths.monoid.*
import org.junit.Test

/**
 * Generation of some small semigroups.

 * @author Tim Lavers
 */
class SmallSemigroupsTest : TestBase() {
    @Test
    fun size3Test() {
        val transformationsOf3 = transformationMonoid(3).elements
        println("transformationsOf3.size() = " + transformationsOf3.size)

        val setsOf3Trannies = SubsetsOfSizeN(transformationsOf3, 3)
        val threeElementSemis = setsOf3Trannies.subsets().filter { subset -> isClosedUnderComposition(subset, TransformationComposition) }

        println("threeElementSemis.size() = " + threeElementSemis.size)
        threeElementSemis.forEach { println(it) }
    }

    @Test
    fun e1() {
        val t1 = t(2, 4, 1, 2, 1)
        val t2 = t(2, 5, 2, 3, 1)
        val generators = set(t1, t2)
        val semigroup = generateFrom(TransformationComposition, generators)

        val size = semigroup.size
        println("size = $size")
        val idempotents = semigroup.idempotents
        println("idempotents: ${idempotents.size}")
//        val greens = GreensRelations(semigroup)
//        printLClasses(greens)
//        printRClasses(greens)
        idempotents.forEach {
            val sub = semigroup.filter { t -> it.image == t.image && it.kernel == t.kernel }.toSet()
            if (isClosedUnderComposition(sub, TransformationComposition)) {
                val subsemigroup = Semigroup(sub, TransformationComposition)
                if (subsemigroup.isGroup) {
                    println("Subgroup: $sub")
                }

            }
        }


        val orderPreservers = subsemigroupOfOrderPreservingTransformations(semigroup)
        println("orderPreservers = $orderPreservers")
    }

    @Test
    fun o4() {
        val o4 = orderPreservingTransformationMonoid(4)
        println("o4.size = ${o4.size}")
        val o3 = orderPreservingTransformationMonoid(3)
        println("o3.size = ${o3.size}")
//        val allFunctions = allFunctionsFromTo(o4.elements, o3.elements)
        val allHomomorphisms = allHomomorphisms(o4, o3)
        val nonTrivial = allHomomorphisms.filter { it.range().size > 2 }
        println("allHomomorphisms = ${allHomomorphisms.size}")
        println("non trivials: ${nonTrivial.size}")
        nonTrivial.forEach { println("it = $it") }
    }

    @Test
    fun o4o4() {
        val o4 = orderPreservingTransformationMonoid(4)
        val allHomomorphisms = allHomomorphisms(o4, o4)
        val nonTrivial = allHomomorphisms.filter { it.range().size >= 2 }
        println("allHomomorphisms = ${allHomomorphisms.size}")
        println("non trivials: ${nonTrivial.size}")
        nonTrivial.forEach { println("it = ${it.range().size}=> $it") }
    }

    @Test
    fun o3o3() {
        val o3 = orderPreservingTransformationMonoid(3)
        println("o3.size = ${o3.size}")
        val allHomomorphisms = allHomomorphisms(o3, o3)
        val nonTrivial = allHomomorphisms.filter { it.range().size >= 4 }
        println("allHomomorphisms = ${allHomomorphisms.size}")
        println("non trivials: ${nonTrivial.size}")
        nonTrivial.forEach { println("it = ${it.range().size}=> $it") }
    }

    @Test
    fun `endomorphisms of a right zero semigroup`() {
        val r4 = rightZeroSemigroup(4)
        val allHomomorphisms = allHomomorphisms(r4, r4)
        allHomomorphisms.size shouldBe 4 * 4 * 4 * 4 // Anything goes!
    }

    @Test
    fun `image-defined sub-monoids of O5`() {
        val o5 = orderPreservingTransformationMonoid(5)

        val im135 = setOf(1, 3, 5)
        val s = o5.filter {it.image.isASubsetOf(im135)}.toSet()
        val semigroup = generateFrom(TransformationComposition, s)
        println(semigroup)

        val allHomomorphisms = allHomomorphisms(semigroup, semigroup)
    }

//    @Test
//    fun o5o5() {
//        val o5 = orderPreservingTransformationMonoid(5)
//        val allIsomorphisms = allInjectiveHomomorphisms(o5, o5)
//        allIsomorphisms.forEach { println(it) }
//    }

    @Test
    fun `non injective kernel of tn`() {

        val t4 = transformationMonoid(4)
        val o4 = orderPreservingTransformationMonoid(4)
        val filtered = t4.filter {
            it.isRightZero
        }
    }

    @Test
    fun chainSubs() {
        val c4 = chainSemigroup(3)
        val subs = c4.allSubsemigroups()
        println( "subs: ${subs.size}")
    }
}