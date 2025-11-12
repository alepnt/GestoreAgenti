# Offline build instructions

The Maven build fails in the sandbox because it cannot contact Maven Central to
resolve the Spring Boot BOM (`spring-boot-dependencies:3.3.5`). To build the
project you need a local Maven repository that already contains the
Spring Boot BOM and all of the dependencies declared by the client and server
modules.

## 1. Prime a local repository on a machine with internet access

```bash
./scripts/prime-maven-cache.sh
```

The helper script wraps the two `dependency:go-offline` invocations for the
server and client modules, storing the resulting repository snapshot under
`offline/repository` and packaging it into `offline/maven-repo.tar.gz`. If you
prefer to run the Maven goals manually you can still use:

```bash
./mvnw -pl server -am dependency:go-offline -DskipTests
./mvnw -pl client -am dependency:go-offline -DskipTests
```

Both approaches download the Spring Boot BOM together with the dependencies
declared in `server/pom.xml` and `client/pom.xml`.

## 2. Copy the populated repository into the sandbox

After the previous step completes you will have both a populated repository at
`offline/repository` and a compressed archive at `offline/maven-repo.tar.gz`.
Copy the archive to the restricted environment and unpack it with the companion
script:

```bash
# On the offline machine
./scripts/install-maven-cache.sh offline/maven-repo.tar.gz /workspace/.m2/repository
```

If you need to customise the paths, pass the archive location and the target
repository directory as arguments. Any file transfer tool (rsync, scp, zip +
unzip, etc.) is fine as long as the resulting repository keeps the Maven
directory structure.

## 3. Run Maven offline inside the sandbox

Once the repository is in place you can invoke Maven with the offline flag so it
never tries to reach Maven Central:

```bash
mvn -pl server -am -DskipTests package -o -Dmaven.repo.local=/workspace/.m2/repository
```

With the cache in place you can also execute targeted unit tests such as the
JWT regression suite that guards against the weak-secret regression fixed in
this change:

```bash
mvn -pl server -Dtest=JwtUtilTest test -o -Dmaven.repo.local=/workspace/.m2/repository
```

The `-o` switch enforces offline mode and `-Dmaven.repo.local` points Maven to
the repository you copied in step 2. Repeat the command for the client module if
needed.

## 4. (Optional) Install only the missing parent POM

If you only lack the Spring Boot BOM but already have the other artifacts,
download `spring-boot-dependencies-3.3.5.pom` once and install it manually:

```bash
mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file \
    -Dfile=spring-boot-dependencies-3.3.5.pom \
    -DpomFile=spring-boot-dependencies-3.3.5.pom \
    -DgroupId=org.springframework.boot \
    -DartifactId=spring-boot-dependencies \
    -Dversion=3.3.5 \
    -Dpackaging=pom
```

After the parent is installed locally, the regular build command succeeds even
without internet access.
