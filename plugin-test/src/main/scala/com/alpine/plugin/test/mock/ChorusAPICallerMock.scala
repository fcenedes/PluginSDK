package com.alpine.plugin.test.mock

import java.io.{File, FileInputStream, InputStream}

import com.alpine.plugin.core.dialog.{ChorusFile, PythonNotebook}
import com.alpine.plugin.core.utils.ChorusAPICaller

import scala.util.{Failure, Try}

class ChorusAPICallerMock(val workfiles: Seq[ChorusFileInWorkspaceMock]) extends ChorusAPICaller {

  val workfileMap: Map[String, ChorusFileInWorkspaceMock] = {
    workfiles.map(w => (w.wf.fileId, w)).toMap
  }

  /**
    *
    * Mocks the download workfile method, but reading the local workflow associated with the workfileID
    */
  override def getWorkfileAsInputStream(workFileId: String): Try[InputStream] = {
    try {
      val mockWf = this.workfileMap(workFileId)
      val workfilePath = mockWf.workfilePath.get
      val f = new File(workfilePath)
      val stream = new FileInputStream(f)
      Try(stream)
    } catch {
      case (e: Exception) => Failure[InputStream](e)
    }
  }

  /**
    * Runs a workfile and returns the workfile object if successful
    * Note: this will not fail if the workfile exists but cannot be run (e.g. if the notebook server
    * is down, the query may appear successful).
    * Hoping to change this behavior in future releases.
    */
  override def runWorkfile(workfileId: String): Try[ChorusFile] = {
    Try(this.workfileMap(workfileId).wf)
  }

  override def runNotebook(workfileId: String): Try[PythonNotebook] = ???

  override def createOrUpdateChorusFile(workspaceId: String, file: File, overwrite: Boolean): Try[ChorusFile] = ???
}

object ChorusAPICallerMock {
  def apply(): ChorusAPICallerMock = {
    val emptyWorkfiles = Seq[ChorusFileInWorkspaceMock]()
    new ChorusAPICallerMock(emptyWorkfiles)
  }

}

case class ChorusFileInWorkspaceMock(wf: ChorusFile, workfilePath: Option[String])
