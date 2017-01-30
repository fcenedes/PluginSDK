/**
  * COPYRIGHT (C) 2015 Alpine Data Labs Inc. All Rights Reserved.
  */

package com.alpine.plugin.core.io.defaults

import com.alpine.plugin.core.io.{IOBase, OperatorInfo, Tuple3}

/**
  * Abstract implementation of [[Tuple3]].
  * Can be extended by developers who want custom behaviour not provided by [[Tuple3Default]].
  *
  * @param _1       The first element of the tuple.
  * @param _2       The second element of the tuple.
  * @param _3       The third element of the tuple.
  * @param addendum Map containing additional information.
  * @tparam T1 Type of the first element of the tuple.
  * @tparam T2 Type of the second element of the tuple.
  * @tparam T3 Type of the third element of the tuple.
  */
abstract class AbstractTuple3[
T1 <: IOBase,
T2 <: IOBase,
T3 <: IOBase
](val _1: T1, val _2: T2, val _3: T3,
  val addendum: Map[String, AnyRef])
  extends Tuple3[T1, T2, T3] {
  def elements: Seq[IOBase] = Seq(_1, _2, _3)
}

/**
  * Default implementation of [[Tuple3]].
  *
  * @param _1       The first element of the tuple.
  * @param _2       The second element of the tuple.
  * @param _3       The third element of the tuple.
  * @param addendum Map containing additional information.
  * @tparam T1 Type of the first element of the tuple.
  * @tparam T2 Type of the second element of the tuple.
  * @tparam T3 Type of the third element of the tuple.
  */
case class Tuple3Default[
T1 <: IOBase,
T2 <: IOBase,
T3 <: IOBase
](override val _1: T1,
  override val _2: T2,
  override val _3: T3,
  override val addendum: Map[String, AnyRef])
  extends AbstractTuple3(_1, _2, _3, addendum) {

  @deprecated("Use constructor without displayName and sourceOperatorInfo.")
  def this(displayName: String, _1: T1, _2: T2, _3: T3,
           sourceOperatorInfo: Option[OperatorInfo],
           addendum: Map[String, AnyRef] = Map[String, AnyRef]()) = {
    this(_1, _2, _3, addendum)
  }
}

object Tuple3Default {

  @deprecated("Use constructor without displayName and sourceOperatorInfo.")
  def apply[
  T1 <: IOBase,
  T2 <: IOBase,
  T3 <: IOBase
  ](displayName: String, _1: T1, _2: T2, _3: T3,
    sourceOperatorInfo: Option[OperatorInfo],
    addendum: Map[String, AnyRef] = Map[String, AnyRef]()): Tuple3Default[T1, T2, T3] = {
    Tuple3Default(_1, _2, _3, addendum)
  }

  def apply[
  T1 <: IOBase,
  T2 <: IOBase,
  T3 <: IOBase
  ](_1: T1, _2: T2, _3: T3): Tuple3Default[T1, T2, T3] = {
    Tuple3Default(_1, _2, _3, Map[String, AnyRef]())
  }
}
