Semigroups in Kotlin
=================
A Kotlin implementation of finite Semigroups in the Kotlin programming language.

The main abstraction is `Semigroup<T>`, which consists of a set of elements of type `T` and an associative map of
the elements of the set.

Abstractions
------------
The main abstract functions are:
* generation of a semigroup from a set of elements and a composition function,
* calculations of Green's relations on a semigroup,
* direct, semi-direct and bilateral semi-direct (or double, or Zappa) products of two semigroups.

Concrete Semigroups
-------------------
There are utility functions for generating:
* left- and right-zero semigroups,
* the full transformation monoid on a set `{1, ..., n}`,
* the monoid of order-preserving transformations on `{1, ..., n}`,
* cyclig groups,
* symmetric groups.

 