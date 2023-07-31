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
        println("o4.size = ${o4.size}")
//        val allFunctions = allFunctionsFromTo(o4.elements, o3.elements)
        val allHomomorphisms = allHomomorphisms(o4, o4)
        val nonTrivial = allHomomorphisms.filter { it.range().size > 2 }
        println("allHomomorphisms = ${allHomomorphisms.size}")
        println("non trivials: ${nonTrivial.size}")
        nonTrivial.forEach { println("it = ${it.range().size}=> $it") }
    }

    @Test
    fun o3o3() {
        val o3 = orderPreservingTransformationMonoid(3)
        println("o3.size = ${o3.size}")
//        val allFunctions = allFunctionsFromTo(o4.elements, o3.elements)
        val allHomomorphisms = allHomomorphisms(o3, o3)
        val nonTrivial = allHomomorphisms.filter { it.range().size >= 4 }
        println("allHomomorphisms = ${allHomomorphisms.size}")
        println("non trivials: ${nonTrivial.size}")
        nonTrivial.forEach { println("it = ${it.range().size}=> $it") }
    }

//    @Test
    fun o5o5() {
        val o5 = orderPreservingTransformationMonoid(5)
        val subsemigroups = o5.allSubsemigroups()
        println("number of subs: ${subsemigroups.size}.")
    }

    @Test
    fun chainSubs() {
        val c4 = chainSemigroup(3)
        val subs = c4.allSubsemigroups()
        println( "subs: ${subs.size}")
    }
}