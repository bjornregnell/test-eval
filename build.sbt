ThisBuild / scalaVersion := "3.3.1"
libraryDependencies += ("com.eed3si9n.eval" % "eval" % "0.3.0").cross(CrossVersion.full)
fork := true
run / connectInput := true