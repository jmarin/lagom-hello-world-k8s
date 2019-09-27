// The Lagom plugin
addSbtPlugin("com.lightbend.lagom" % "lagom-sbt-plugin" % "1.5.1")

addSbtPlugin("com.dwijnand" % "sbt-dynver" % "3.3.0")

addSbtPlugin("com.lightbend.cinnamon" % "sbt-cinnamon" % "2.11.4")
// Credentials and resolver to download the Cinnamon Telemetry libraries
credentials += Credentials(
  Path.userHome / ".lightbend" / "commercial.credentials"
)
resolvers += Resolver.url(
  "lightbend-commercial",
  url("https://repo.lightbend.com/commercial-releases")
)(Resolver.ivyStylePatterns)
