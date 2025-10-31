# Offline build instructions

The Maven build fails in the sandbox because it cannot contact Maven Central to
resolve the Spring Boot parent POM (`spring-boot-starter-parent:3.5.6`). To
build the project you need a local Maven repository that already contains the
Spring Boot BOM and all of the dependencies declared by the client and server
modules.

## 1. Prime a local repository on a machine with internet access

```bash
./mvnw -pl server -am dependency:go-offline -DskipTests
./mvnw -pl client -am dependency:go-offline -DskipTests
```

Both commands download the Spring Boot parent POM together with the
dependencies declared in `server/pom.xml` and `client/pom.xml`.

## 2. Copy the populated repository into the sandbox

After the previous step completes you will have a filled Maven cache under
`~/.m2/repository`. Copy that directory to the restricted environment, for
example under `/workspace/.m2/repository`.

```bash
# On the online machine
rsync -a ~/.m2/repository/ user@sandbox:/workspace/.m2/repository/
```

Any file transfer tool (rsync, scp, zip + unzip, etc.) is fine as long as the
resulting repository keeps the Maven directory structure.

## 3. Run Maven offline inside the sandbox

Once the repository is in place you can invoke Maven with the offline flag so it
never tries to reach Maven Central:

```bash
mvn -pl server -am -DskipTests package -o -Dmaven.repo.local=/workspace/.m2/repository
```

The `-o` switch enforces offline mode and `-Dmaven.repo.local` points Maven to
the repository you copied in step 2. Repeat the command for the client module if
needed.

## 4. (Optional) Install only the missing parent POM

If you only lack the Spring Boot parent POM but already have the other
artifacts, download `spring-boot-starter-parent-3.5.6.pom` once and install it
manually:

```bash
mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file \
    -Dfile=spring-boot-starter-parent-3.5.6.pom \
    -DpomFile=spring-boot-starter-parent-3.5.6.pom \
    -DgroupId=org.springframework.boot \
    -DartifactId=spring-boot-starter-parent \
    -Dversion=3.5.6 \
    -Dpackaging=pom
```

After the parent is installed locally, the regular build command succeeds even
without internet access.
