package org.tap4j.plugin.util;


import hudson.model.TopLevelItem;
import org.htmlunit.html.HtmlPage;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.recipes.LocalData;

import static org.junit.Assert.assertTrue;


public class GraphHelperTest {

    @Rule
    public JenkinsRule rule = new JenkinsRule();

    @Issue("JENKINS-37623")
    @LocalData
    @Test
    public void renderTooltipsWithFailedBuilds() throws Exception {

        TopLevelItem project = rule.jenkins.getItem("testPipeline-randomly-no-data");
        try (JenkinsRule.WebClient wc = rule.createWebClient()) {
            HtmlPage page = wc.getPage(project);

            //      there should be a TAP result trend graph
            rule.assertXPath(page, "//img[@src='tapResults/graph']");

            //      check that tooltip is rendered for the last build
            rule.assertXPath(page, "//area[@title='1 Failure(s)' and @href='16/tapResults/']");

            //      check that build without TAP action recorded is excluded from graph
            assertTrue(page.getByXPath("//area[@href='7/tapResults/']").isEmpty());
        }
    }
}
