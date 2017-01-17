/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.dlms.cucumber;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features", 
        tags = { "~@Skip" }, 
        glue = {
                "classpath:com.alliander.osgp.platform.cucumber.glue" }, 
        plugin = { "pretty",
                "html:target/output/Cucumber-report", 
                "html:target/output/Cucumber-html-report.html",
                "json:target/output/cucumber.json" }, 
        snippets = SnippetType.CAMELCASE)
public class AcceptanceTests extends AbstractTransactionalJUnit4SpringContextTests {

}
