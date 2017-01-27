/*
 * COPYRIGHT (C) 2017 Alpine Data Labs Inc. All Rights Reserved.
*/
package com.alpine.model.pack.preprocess

import com.alpine.model.RowModel
import com.alpine.model.export.pfa.modelconverters.RenamingPFAConverter
import com.alpine.model.export.pfa.{PFAConverter, PFAConvertible}
import com.alpine.model.pack.UnitTransformer
import com.alpine.model.pack.sql.SimpleSQLTransformer
import com.alpine.plugin.core.io.ColumnDef
import com.alpine.sql.SQLGenerator
import com.alpine.transformer.Transformer
import com.alpine.transformer.sql.{ColumnarSQLExpression, SQLTransformer}

/**
  * Added in SDK 1.9 (Alpine-Chorus 6.3).
  *
  * This is like the unit model, except that it takes a list of names to be used for the output features.
  * It passes along the content of its input as output.
  *
  * Created by Jennifer Thompson on 1/5/17.
  */
case class RenamingModel(inputFeatures: Seq[ColumnDef], outputNames: Seq[String], override val identifier: String = "")  extends RowModel with PFAConvertible {
  override def transformer: Transformer = UnitTransformer
  override def outputFeatures: Seq[ColumnDef] = (inputFeatures zip outputNames).map {
    case (ColumnDef(_, inputType), outputName) => ColumnDef(outputName, inputType)
  }

  override def sqlTransformer(sqlGenerator: SQLGenerator): Option[SQLTransformer] = {
    Some(RenamingSQLTransformer(this, sqlGenerator))
  }

  override def getPFAConverter: PFAConverter = new RenamingPFAConverter(this)
}

case class RenamingSQLTransformer(model : RenamingModel, sqlGenerator: SQLGenerator) extends SimpleSQLTransformer {
  override def getSQLExpressions: Seq[ColumnarSQLExpression] = {
    inputColumnNames.map(_.asColumnarSQLExpression(sqlGenerator))
  }
}