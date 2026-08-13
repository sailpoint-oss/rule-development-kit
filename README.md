[![Discourse Topics][discourse-shield]][discourse-url]
![Issues][issues-shield]
![Contributor Shield][contributor-shield]

[discourse-shield]: https://img.shields.io/discourse/topics?label=Discuss%20This%20Tool&server=https%3A%2F%2Fdeveloper.sailpoint.com%2Fdiscuss
[discourse-url]: https://developer.sailpoint.com/discuss/tag/rules
[issues-shield]:https://img.shields.io/github/issues/sailpoint-oss/rule-development-kit?label=Issues
[contributor-shield]:https://img.shields.io/github/contributors/sailpoint-oss/rule-development-kit?label=Contributors


[product-screenshot]: ./assets/images/intellij.png

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/othneildrew/Best-README-Template">
    <img src="https://avatars.githubusercontent.com/u/63106368?s=200&v=4" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Rule Development Kit</h3>

  <p align="center">
    A project setup for the development and testing of SailPoint rules.
    <br />
    <a href="https://developer.sailpoint.com/idn/tools/rule-development-kit"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <!-- <a href="https://github.com/sailpoint/repo-template">View Demo</a>
    ·
    <a href="https://github.com/sailpoint-oss/repo-template/issues">Report Bug</a>
    ·
    <a href="https://github.com/sailpoint-oss/repo-template/issueschoose">Request Feature</a> -->
  </p>
</div>

## Requirements

- **JDK 17.** This is not optional — see the warning below.
- **Maven 3.9+.**

### ⚠️ Make sure Maven is actually using JDK 17

Maven uses `JAVA_HOME`, *not* whichever `java` is first on your `PATH`. If `JAVA_HOME` is
unset, Maven falls back to its own bundled JDK — and a Homebrew-installed Maven pulls in
Homebrew's `openjdk` formula, which is likely much newer than 17.

Check what Maven is really using before you build:

```bash
mvn -v      # look at the "Java version:" line, not `java -version`
```

If it does not say 17, set it explicitly:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)   # macOS
# export JAVA_HOME=/usr/lib/jvm/temurin-17-jdk     # Linux, adjust to your install
```

**Why this matters:** this project pins `mockito-core` 5.2.0 and `byte-buddy` 1.14.5, which
predate recent JDKs. On a newer JDK the code still compiles, but most tests fail with
`Mockito cannot mock this class`. On JDK 17 all tests pass. If you see that error, you are
on the wrong JDK — it is not a problem with your rule.

## Running the tests

```bash
mvn test
```

You should get `Tests run: 13, Failures: 0, Errors: 0` and `BUILD SUCCESS`.

## Updating the SailPoint class stubs

The `sailpoint.*` classes your rules compile against (`IdnRuleUtil`, `Identity`, `Link`,
`ProvisioningPlan`, …) are **not** source files in this repo. They come from a prebuilt
`sailpoint:rule-java-docs` jar produced by the
[rule-javadoc](https://github.com/sailpoint-oss/rule-javadoc) repo.

That jar is vendored here as a committed local Maven repository:

```
lib/sailpoint/rule-java-docs/<version>/
    rule-java-docs-<version>.jar        # the compiled stubs
    rule-java-docs-<version>.pom
    *.md5 / *.sha1                      # checksums Maven requires
lib/sailpoint/rule-java-docs/maven-metadata.xml
```

`pom.xml` points at it with `<url>file://${project.basedir}/lib</url>` and depends on a
specific version. So when the rule API changes, you rebuild the jar in `rule-javadoc` and
deploy it into `lib/` here.

### Steps

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
RDK=$(pwd)                      # run this from the root of this repo
```

**1. Bump the version in `rule-javadoc/pom.xml`.**

Check what version this repo currently consumes (`grep -A2 rule-java-docs pom.xml`) and pick
the next one. Note that `rule-javadoc`'s own `pom.xml` has historically drifted out of sync
with the version actually shipped here, so verify rather than assume.

**2. Build the jar.**

```bash
cd /path/to/rule-javadoc
mvn package                     # produces target/rule-java-docs-<VERSION>.jar
```

**3. Deploy it into this repo's `lib/`.**

```bash
mvn deploy:deploy-file \
  -Durl=file://$RDK/lib \
  -DrepositoryId=data-local \
  -Dfile=target/rule-java-docs-<VERSION>.jar \
  -DpomFile=pom.xml
```

Use `deploy:deploy-file`, **not** `install:install-file` — only `deploy-file` writes the
`.md5`/`.sha1` checksums and updates `maven-metadata.xml` to match the existing layout.

**4. Point this repo at the new version.** Edit the dependency in `pom.xml`:

```xml
<dependency>
    <groupId>sailpoint</groupId>
    <artifactId>rule-java-docs</artifactId>
    <version><!-- new version --></version>
</dependency>
```

**5. Verify.**

```bash
cd $RDK
mvn test        # expect 13/13 passing
```

**6. Commit** the new `lib/sailpoint/rule-java-docs/<version>/` directory, the updated
`lib/sailpoint/rule-java-docs/maven-metadata.xml`, and `pom.xml`. The old version directory
can be deleted once you are confident in the new one.

### Checking what you built

```bash
# maven-metadata.xml should list the new version and set it as <release>
cat lib/sailpoint/rule-java-docs/maven-metadata.xml

# the jar should contain Java 8 bytecode (major version 52)
javap -verbose -cp lib/sailpoint/rule-java-docs/<VERSION>/rule-java-docs-<VERSION>.jar \
  sailpoint.server.IdnRuleUtil | grep major
```

Java 8 bytecode is expected and correct. `rule-javadoc` sets
`maven.compiler.target=8`, so the jar targets Java 8 **regardless of which JDK you build
with**, and JDK 17 reads it without issue. You do not need a JDK 8 to produce it.

## Troubleshooting

| Symptom | Cause |
| --- | --- |
| `Mockito cannot mock this class: ...` | Maven is running on a JDK newer than 17. Check `mvn -v` and set `JAVA_HOME`. |
| `Could not resolve dependencies for ... rule-java-docs:jar:<version>` | The version in `pom.xml` does not match a directory under `lib/sailpoint/rule-java-docs/`. |
| Dependency resolves to the old stubs after an update | Stale local cache. Run `rm -rf ~/.m2/repository/sailpoint/rule-java-docs` and rebuild. |
| `source value 8 is obsolete` warnings when building `rule-javadoc` | Expected, harmless. It targets Java 8 on purpose. |
