# md5 checksum
java

------------------------------------

```
java -jar md5checksum -s string  --> calculate md5 of string
```

```
java -jar md5checksum file       --> calculate md5 of the file
```
----------------------------------

--- how to enable sonar within jenkins by maven plugin

** docker:

```
docker search sonarqube
docker pull sonarqube
docker run -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube
```

** jenkins:

```
manage -> plugin manage -> install below plugins
SonarQube Plugin
Environment Injector Plugin

manage -> system management
SonarQube servers       -> enable
maven configuration     -> enable maven settings.xml

project -> setting

build environment -> enable "Prepare SonarQube Scanner environment"
build tasks:
1. invoke top-level Maven targets: clean verify
2. invoke top-level Maven targets: $SONAR_MAVEN_GOAL -Dsonar.host.url=$SONAR_HOST_URL
```

** project code:

within pom.xml, add a plugin:

```
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>test/com/**/*</exclude>
					</excludes>
				</configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>post-unit-test</id>
                        <phase>test</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```

