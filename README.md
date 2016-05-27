[![Build Status](https://travis-ci.org/vincent-fuchs/custom-build-rules.svg?branch=master)](https://travis-ci.org/vincent-fuchs/custom-build-rules) 

[![Coverage Status](https://coveralls.io/repos/github/vincent-fuchs/custom-build-rules/badge.svg?branch=master)](https://coveralls.io/github/vincent-fuchs/custom-build-rules?branch=master)


# Custom Build Rules
These are a set of rules that we can call to perform additional checks on our code / scripts at build time. 

They need to be used with [Maven Enforcer Plugin](https://maven.apache.org/enforcer/maven-enforcer-plugin/). Below is an example of how to use it in your project :

	        <plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-enforcer-plugin</artifactId>
				<version>1.4.1</version>
				<dependencies>
					
					<!-- THIS JAR WILL CONTAIN THE CUSTOM RULES--> 
					
					<dependency>
						<groupId>com.github.vincent-fuchs</groupId>
						<artifactId>custom-build-rules</artifactId>
						<version>1.0.0-SNAPSHOT</version>
					</dependency>
					
				</dependencies>

				<executions>
					<execution>
						<id>enforce</id>
						<goals>
							<goal>enforce</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
				
				    <!-- THIS IS WHERE SPECIFIC RULES CONFIG IS DONE--> 			
				
					<rules>
						<LiquibaseFilesCheck implementation="com.github.vincent_fuchs.custom_build_rules.LiquibaseFilesCheck">
							<directory>/scripts/sql/</directory>
                            <fileExtension>sql</fileExtension>
							<RulesToApply>
								<ruleToApply implementation="com.github.vincent_fuchs.custom_build_rules.rules_to_apply.SomeBasicRulesToApply"/>
							</RulesToApply>
						</LiquibaseFilesCheck>
					</rules>
				</configuration>
			</plugin>
