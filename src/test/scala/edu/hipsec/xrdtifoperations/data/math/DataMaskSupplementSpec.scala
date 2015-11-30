package edu.hipsec.xrdtifoperations.data.math

import edu.hipsec.xrdtiffoperations.constants.FileExtensions
import edu.hipsec.xrdtifoperations.data.math.mocks.DummyMask
import org.scalatest.FunSpec

class DataMaskSupplementSpec extends FunSpec {

  // Fields

  val targetImage: DummyMask = new DummyMask
  val inputMax = 10
  val inputMin = 2

  // Requirements

  describe("A data mask") {

    describe("when generating new filename") {

      it("should replace an existing extension with the application default") {
        val base = "thing"
        assert(this.targetImage.generateFilename(base + ".png") == base + FileExtensions.DEFAULT)
      }
    }

    describe("when applying a bounds filter") {

      it("should limit the outputted value to any upper bound given") {
        assert(this.targetImage.maskValue(14, this.inputMax, this.inputMin) == this.inputMax)
      }

      it("should limit the outputted value to any lower bound given") {
        assert(this.targetImage.maskValue(1, this.inputMax, this.inputMin) == this.inputMin)
      }

      it("should return the inputted value if within prescribed bounds") {
        assert(this.targetImage.maskValue(9, this.inputMax, this.inputMin) == 9)
      }

    }

  }

}
