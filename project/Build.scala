import sbt._
import Keys._

object TestBuild extends Build {
  val akey = AttributeKey[String]("TestKey")
  val t = TaskKey[String]("test-task")

  val a =
    Project("a", file("a"))
      .settings(sett("a"): _*)
  val b =
    Project("b", file("b"))
      .settings(sett("b"): _*)

  val root = Project("root", file(".")).aggregate(a, b)

  def sett(id: String) = seq(
    t <<= state.map { state =>
      println("Testtask running for "+id+" key value is "+state.get(akey))
      "Result for "+id
    }.updateState(updater).dependsOn(compile in Compile)
  )

  def updater(s: State, str: String): State = {
    s.update(akey)(_.getOrElse("") + "+")
  }
}
