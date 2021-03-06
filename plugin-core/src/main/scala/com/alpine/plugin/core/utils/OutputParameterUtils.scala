package com.alpine.plugin.core.utils

import com.alpine.plugin.core.OperatorParameters
import com.alpine.plugin.core.dialog.{DialogElement, OperatorDialog}

/**
 * Utility for the standard parameters for use by operators which
 * produce output.
 */
trait OutputParameterUtils {

  val operatorNameUUIDVariable = "@operator_name_uuid"

  private val trueStr = "true"
  private val falseStr = "false"

  private val yesStr = "Yes"
  private val noStr = "No"

  val overwriteParameterID = "overwrite"
  val dropIfExists = "dropIfExist"

  def addOverwriteParameter(operatorDialog: OperatorDialog, defaultValue: Boolean = true): DialogElement = {
    // May replace this with a more natural representation of a boolean parameter
    // e.g. a checkbox
    // in the future.
    val overwrite = operatorDialog.addRadioButtons(
      overwriteParameterID,
      "Overwrite Output",
      Array(trueStr, falseStr).toSeq,
      defaultValue.toString
    )
    overwrite
  }

  /**
   * Gets the value of the overwrite parameter, returning true if the String value is "true",
   * false if it is a different value or missing.
   * @param parameters OperatorParameters instance [containing the overwrite parameter].
   * @return Boolean representation of the overwrite parameter.
   */
  def getOverwriteParameterValue(parameters: OperatorParameters): Boolean = {
    if (parameters.contains(overwriteParameterID)) {
      trueStr == parameters.getStringValue(overwriteParameterID)
    } else {
      false
    }
  }

  /**
    * For use with database, not HDFS.
    *
    * @param operatorDialog From the 'onExecution' method's parameters. This method
    *                       adds the parameters to this object.
    * @param defaultValue boolean indicating default value of the parameter. True for "Yes", false for "No".
    * @return The dialog element added.
    */
  def addDropIfExistsParameter(operatorDialog: OperatorDialog, defaultValue: Boolean = true): DialogElement = {
    val overwrite = operatorDialog.addRadioButtons(
      dropIfExists,
      "Drop If Exists",
      Seq(yesStr, noStr),
      if (defaultValue) yesStr else noStr
    )
    overwrite
  }

  /**
    * Gets the value of the dropIfExists parameter, returning true if the String value is "Yes",
    * false if it is a different value or missing.
    * @param parameters OperatorParameters instance [containing the dropIfExists parameter].
    * @return Boolean representation of the dropIfExists parameter.
    */
  def getDropIfExistsParameterValue(parameters: OperatorParameters): Boolean = {
    if (parameters.contains(dropIfExists)) {
      yesStr == parameters.getStringValue(dropIfExists)
    } else {
      false
    }
  }

}

object OutputParameterUtils extends OutputParameterUtils {
  @deprecated("Just use bool.toString")
  def toTrueFalseString(bool: Boolean): String = {
    bool.toString
  }
}
