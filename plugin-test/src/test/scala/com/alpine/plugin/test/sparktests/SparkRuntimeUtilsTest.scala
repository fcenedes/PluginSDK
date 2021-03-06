package com.alpine.plugin.test.sparktests

import java.io.File

import com.alpine.plugin.core.io.defaults.HdfsDelimitedTabularDatasetDefault
import com.alpine.plugin.core.io.TSVAttributes
import com.alpine.plugin.core.spark.utils.{SparkMetadataWriter, SparkRuntimeUtils}
import com.alpine.plugin.core.utils.HdfsStorageFormatType
import com.alpine.plugin.test.utils.TestSparkContexts
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.scalatest.FunSuite

import scala.io.Source
import scala.util.Random


class SparkRuntimeUtilsTest extends FunSuite {

  import TestSparkContexts._

  private val testData = """2014,"",Volt,5000
                           |2015,,Volt,5000
                           |new,"",Volt,5000
                           |5.5,"",Volt,5000""".stripMargin

  private val testResultsFolder = "target/test-results"
  private val fishPath = testResultsFolder + "/FishData"

  val carsSchema = new StructType(
    Array(
      StructField("year", IntegerType, nullable = true),
      StructField("make", StringType, nullable = true),
      StructField("model", StringType, nullable = true),
      StructField("price", DoubleType, nullable = true)
    ))

  val sparkUtils = new SparkRuntimeUtils(sparkSession)

  test("Check read dirty data") {
    val testDataFilePath = new File(testResultsFolder, s"test-data${Random.nextInt()}.csv")
    FileUtils.write(testDataFilePath, testData)
    testDataFilePath.deleteOnExit()
    val f = HdfsDelimitedTabularDatasetDefault(testDataFilePath.getPath,
      sparkUtils.convertSparkSQLSchemaToTabularSchema(carsSchema), TSVAttributes.defaultCSV)
    val results = sparkUtils.getDataFrame(f)
    val resultRows = results.collect()
    assert(resultRows.toSet.equals(Set(
      Row.fromTuple(2014, null, "Volt", 5000.0),
      Row.fromTuple(2015, null, "Volt", 5000.0))))
    assert(resultRows.length == 2)
    testDataFilePath.deleteOnExit()
  }

  test("Write with nullValue as empty string, and delim as pipe AND trailing white space") {

    val pipeAttributes = TSVAttributes(
      delimiter = '|',
      escapeStr = TSVAttributes.DEFAULT_ESCAPE_CHAR,
      quoteStr = TSVAttributes.DEFAULT_QUOTE_CHAR,
      containsHeader = false)

    val originalFishData = Seq(
      FishColor("red", "fish"),
      FishColor("blue", "fish  "),
      FishColor("", "  fish"))

    val dataFrame = sparkSession.createDataFrame(sc.parallelize(originalFishData))

    val fishDataOutput = sparkUtils.saveDataFrame(fishPath + "_PipeDelim", dataFrame, HdfsStorageFormatType.CSV,
      overwrite = true, Map[String, AnyRef](), pipeAttributes)
    val readData = sparkUtils.getDataFrame(fishDataOutput).collect()
    val nulls = readData.filter(row => row.anyNull)
    readData.foreach(x => assert(x.get(1).toString == "fish"))
    assert(readData.length == 3)
    assert(nulls.length == 1)
    val metadata = new File(fishDataOutput.path + "/" + SparkMetadataWriter.METADATA_FILENAME)
    assert(metadata.isFile, "Failed to write metadata")
    val s: String = Source.fromFile(metadata).getLines().next()
    assert(s.equals(
      "{\"column_names\":[\"color\",\"fish\"],\"column_types\":[\"chararray\",\"chararray\"],\"delimiter\":\"|\",\"escape\":\"\\\\\",\"quote\":\"\\\"\",\"is_first_line_header\":false,\"total_number_of_rows\":-1}"), "Metadata is not correct")

    val asTextFile = sc.textFile(fishDataOutput.path)
    assert(asTextFile.first().split('|').length == 2)
  }

  test("Test Storage Utils") {

    val h: HdfsStorageFormatType = HdfsStorageFormatType.Avro
    val m = h match {
      case HdfsStorageFormatType.Avro => 1
      case HdfsStorageFormatType.Parquet => 2
      case HdfsStorageFormatType.CSV => 3
    }

    assert(m == 1)
  }

  test("Default delim is CSV") {
    val testDataFilePath = new File(testResultsFolder, s"test-data${Random.nextInt()}.csv")
    FileUtils.write(testDataFilePath, testData)
    testDataFilePath.deleteOnExit()
    val f = HdfsDelimitedTabularDatasetDefault(testDataFilePath.getPath,
      sparkUtils.convertSparkSQLSchemaToTabularSchema(carsSchema), TSVAttributes.defaultCSV)
    val results = sparkUtils.getDataFrame(f)

    val savedASDF: HdfsDelimitedTabularDatasetDefault = sparkUtils.saveDataFrameDefault(
      path = "plugin-test/testing-temp/TestSavingAsCSV",
      dataFrame = results).asInstanceOf[HdfsDelimitedTabularDatasetDefault]

    assert(savedASDF.tsvAttributes == TSVAttributes.defaultCSV,
      "Save Data Frame default should save data frame as a csv")
  }
}

case class FishColor(color: String, fish: String) extends Serializable