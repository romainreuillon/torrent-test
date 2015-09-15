organization := "fr.iscpif"

name := "torrent"

scalaVersion := "2.11.7"

resolvers += Resolver.mavenLocal

libraryDependencies += "com.turn" % "ttorrent-core" % "1.5-SNAPSHOT"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.12"

scalariformSettings

