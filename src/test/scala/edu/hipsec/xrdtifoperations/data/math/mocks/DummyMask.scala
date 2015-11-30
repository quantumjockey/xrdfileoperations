package edu.hipsec.xrdtifoperations.data.math.mocks

import edu.hipsec.xrdtiffoperations.data.math.DataMaskSupplement

class DummyMask extends DataMaskSupplement {

  // Public Methods

  override def generateFilename(fileName: String): String = super.generateFilename(fileName)

  override def maskValue(value: Int, max: Int, min: Int): Int = super.maskValue(value, max, min)

  override def stripFilename(name: String): String = super.stripFilename(name)

}
