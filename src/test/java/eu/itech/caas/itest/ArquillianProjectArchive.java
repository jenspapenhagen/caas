/*
 * Copyright 2018 jay.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.itech.caas.itest;

import eu.itech.caas.RESTConfiguration;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.Coordinate;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencies;

import static org.jboss.shrinkwrap.api.Filters.exclude;
import static org.jboss.shrinkwrap.resolver.api.maven.ScopeType.RUNTIME;

/**
 *
 * @author jay
 */
public class ArquillianProjectArchive {

    @Deployment
    public static WebArchive createDeployment() {
        // Compile Safe Packages.
        Package projectPackage = RESTConfiguration.class.getPackage();
        Package itestPackage = Utils.class.getPackage();

        File[] libs = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .importRuntimeDependencies()
                .addDependency(MavenDependencies.createDependency("org.assertj:assertj-core", RUNTIME, false)) // AssertJ Fluent Assertions
                .resolve().withTransitivity().asFile();

        WebArchive war = ShrinkWrap.create(WebArchive.class, "caas-integrationtest.war")
                .addPackages(true, exclude(itestPackage), projectPackage)
                .addClass(Coordinate.class) // Need this cause of the maven resolver is part of the deployment
                .addClass(ArquillianProjectArchive.class)
                .addClass(Utils.class)
                .addClass(Gernator.class)
                .addAsResource(new ClassLoaderAsset("META-INF/test-persistence.xml"), "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(libs);
        return war;
    }
}
