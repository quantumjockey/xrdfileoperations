name := "xrdfileoperations"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.12",                             // for using jUnit
  "org.clojure" % "clojure" % "1.7.0",                    // for compiling and running Clojure sources
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"   // for using ScalaTest
)
