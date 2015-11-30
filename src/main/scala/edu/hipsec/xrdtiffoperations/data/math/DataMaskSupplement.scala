package edu.hipsec.xrdtiffoperations.data.math

import edu.hipsec.xrdtiffoperations.constants.FileExtensions

class DataMaskSupplement {

  // Protected Methods

  protected def generateFilename(fileName: String): String = stripFilename(fileName) + FileExtensions.DEFAULT

  protected def maskValue(value: Int, max: Int, min: Int): Int = value match {
    case x if x < min => min
    case x if x > max => max
    case _ => value
  }

  protected def stripFilename(name: String): String = name.replace(FileExtensions.DEFAULT, "")

}
