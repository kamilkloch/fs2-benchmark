val fs2Version = "3.9.4"
val logbackVersion = "1.5.3"

// compiler options explicitly disabled from https://github.com/DavidGregory084/sbt-tpolecat
val disabledScalacOptionsCompile = Set(
  "-Xfatal-warnings",
  "-Wunused:privates",
)

lazy val fs2_benchmark = (project in file("."))
  .enablePlugins(JmhPlugin)
  .settings(
    name := "fs2-benchmark",
    version := "0.1.0-SNAPSHOT",
    fork := true,
    scalaVersion := "2.13.13",
    scalacOptions ++= Seq("-release", "21"),
    javacOptions ++= Seq("-source", "21", "-target", "21"),
    libraryDependencies ++= Seq(
      "co.fs2" %% "fs2-core" % fs2Version
    ),
    Compile / scalacOptions ~= ((options: Seq[String]) => options.filterNot(disabledScalacOptionsCompile)),
    Compile / scalacOptions ++= Seq(
      "-Wconf:any:warning-verbose", // print warnings with their category, site, and (for deprecations) origin and since-version
      "-Xsource:3", // disabled until IJ Scala plugin has stable support
      "-Vimplicits", // makes the compiler print implicit resolution chains when no implicit value can be found
      "-Vtype-diffs", // turns type error messages into colored diffs between the two types
      "-Wconf:cat=other-match-analysis:error", // report incomplete case match as error
      "-Wconf:cat=other-pure-statement:silent", // silence "unused value of type [???] (add `: Unit` to discard silently)"
      "-Wnonunit-statement",
    ),
    javaOptions := Seq(
      "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED",
      "--add-opens", "java.base/java.util.zip=ALL-UNNAMED",
      "-Dcats.effect.tracing.mode=none",
      "-Dcats.effect.tracing.exceptions.enhanced=false",
      "-Dcats.effect.tracing.buffer.size=64",
      "-Djava.lang.Integer.IntegerCache.high=65536",
      "-Djava.net.preferIPv4Stack=true",
      "-XX:+UnlockExperimentalVMOptions",
      "-XX:+TrustFinalNonStaticFields",
      "-Xms8g",
      "-Xmx8g",
      "-XX:+UseZGC",
      "-XX:+ZGenerational",
      "-XX:+AlwaysPreTouch",
      "-XX:TLABSize=1m",
      "-XX:-ResizeTLAB",
      "-XX:InitialCodeCacheSize=256m",
      "-XX:ReservedCodeCacheSize=256m",
      "-XX:NonNMethodCodeHeapSize=16m",
      "-XX:NonProfiledCodeHeapSize=120m",
      "-XX:ProfiledCodeHeapSize=120m"
    )
  )
  