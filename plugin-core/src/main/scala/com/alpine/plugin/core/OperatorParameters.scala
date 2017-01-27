/**
 * COPYRIGHT (C) 2015 Alpine Data Labs Inc. All Rights Reserved.
 */

package com.alpine.plugin.core

import com.alpine.plugin.core.annotation.AlpineSdkApi
import com.alpine.plugin.core.dialog.ChorusFile
import com.alpine.plugin.core.io.OperatorInfo
import com.alpine.plugin.core.utils.ChorusAPICaller

/**
 * :: AlpineSdkApi ::
 * Interface for accessing the parameter values for an operator.
 * This is the argument to the 'onExecution' callback function.
 * The developers can extract operator parameter values through this interface.
 */
@AlpineSdkApi
trait OperatorParameters extends Serializable {

  /**
    * This object contains the session id. It exposes a few supported methods to interact with
    * the chorus API such as those to download a workfile.
    */
  def getChorusAPICaller: ChorusAPICaller

  def operatorInfo: OperatorInfo
  /**
   * Find out whether or not the given parameter Id is contained
   * in the object.
   * @param parameterId The Id of the parameter that we want to search for.
   * @return true if the parameter is contained. false otherwise.
   */
  def contains(parameterId: String): Boolean

  /**
   * Get the value of a parameter as a reference object.
   * @param parameterId The parameter Id that was used with an input field
   *                    in the OperatorDialog object.
   * @return The parameter value as a reference object.
   */
  def getValue(parameterId: String): AnyRef

  /**
   * Get the string array value of a parameter (a checkboxes parameter).
   * @param parameterId The parameter Id of the multi item selector (checkboxes).
   * @return An array of selected values.
   */
  def getStringArrayValue(parameterId: String): Array[String]

  /**
   * Get the selected columns from a tabular dataset output of an input operator.
   * NOTE: If the parameter was not required and the user did not input a value then this method
   * will return and empty array.
   * @param parameterId The parameter Id of the column checkboxes dialog element.
   * @return A tuple of a source operator name and an array of selected column
   *         names.
   */
  def getTabularDatasetSelectedColumns(parameterId: String): (String, Array[String])

  /**
    * Get the selected columns from a tabular dataset output of an input operator.
    * This is a Java-friendly version.
    * NOTE: If the parameter was not required and the user did not input a value then this method
    * will return and empty array.
    * @param parameterId The parameter Id of the column checkboxes dialog element.
    * @return An array of selected column names.
    */
  def getTabularDatasetSelectedColumnNames(parameterId: String): Array[String]

  /**
   * Get the selected column from a tabular dataset output of an input operator.
   * NOTE: If the parameter was not required and the user didn't select a column this will return
   * an empty string.
   * @param parameterId The parameter Id of the column dropdown dialog element.
   * @return A tuple of a source operator name and a selected column name.
   */
  def getTabularDatasetSelectedColumn(parameterId: String): (String, String)

  /**
    * Get the selected column from a tabular dataset output of an input operator.
    * This is a Java-friendly version.
    * NOTE: If the parameter was not required and the user didn't select a column this will return
    * an empty string.
    * @param parameterId The parameter Id of the column dropdown dialog element.
    * @return The selected column name.
    */
  def getTabularDatasetSelectedColumnName(parameterId: String): String

  /**
    * Get the value of a parameter as a string.
    * If the parameter is a TabularColumnDropDownBox then this returns the same value as
    * "getTabularDatasetSelectedColumn". If it is a chorus file object, then it will return the
    * WorkfileId (e.g. the same as getChorusFile.workfileId)
    * It will throw an exception if the param is one of the following types:
    * TabularColumnCheckboxes, Checkboxes.
    *
    * @param parameterId The parameter Id that was used with an input field
    *                    in the OperatorDialog object.
    * @return The parameter value as a string.
    */
  def getStringValue(parameterId: String): String

  /**
   * Get the value of a parameter as an integer.
   * @param parameterId The parameter Id that was used with an input field
   *                    in the OperatorDialog object.
   * @return The parameter value as an integer.
   */
  def getIntValue(parameterId: String): Int

  /**
   * Get the value of a parameter as a double.
   * @param parameterId The parameter Id that was used with an input field
   *                    in the OperatorDialog object.
   * @return The parameter value as a double.
   */
  def getDoubleValue(parameterId: String): Double

  /**
   * Get an iterator of parameter Ids.
   * @return An iterator of parameter Ids.
   */
  def getParameterIds: Iterator[String]

  /**
    * Get the chorus file stored added by a "ChorusFileSelector" parameter.
    * The return type is a case class with a fileId and a fileName field.
    */
  def getChorusFile(parameterId: String): ChorusFile

  def getAdvancedSparkParameters: scala.collection.mutable.Map[String, String]
}
