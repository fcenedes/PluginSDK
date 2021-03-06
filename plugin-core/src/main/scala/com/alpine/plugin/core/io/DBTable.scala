/**
  * COPYRIGHT (C) 2015 Alpine Data Labs Inc. All Rights Reserved.
  */

package com.alpine.plugin.core.io

import com.alpine.plugin.core.annotation.AlpineSdkApi

/**
  * :: AlpineSdkApi ::
  * Represents a Database Table.
  */
@AlpineSdkApi
trait DBTable extends IOBase {
  def schemaName: String

  def tableName: String

  def tabularSchema: TabularSchema

  @Deprecated
  @deprecated("Will always return false.")
  def isView: Boolean

  @Deprecated
  @deprecated("Will return empty string.")
  def dbName: String

  @Deprecated
  @deprecated("Will return empty string.")
  def dbURL: String
}
