package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.github.vincent_fuchs.custom_build_rules.rules_to_apply.LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


public class SkipMarkedStatementsRuleToApplyTest {

    SkipMarkedStatementsRuleToApply skipMarkedStatementsRuleToApply=new SkipMarkedStatementsRuleToApply();

    @Test
    public void statementsMarkedAsSkippedShouldNotBeInOutput() throws Exception {

        skipMarkedStatementsRuleToApply.setSkipPattern("\n-- skipMavenCheck");

        String contentToParse="DELETE FROM FEATURES where feature_name='someFeatureName1'\n" +
                "/\n" +
                "-- skipMavenCheck\n" +
                "DELETE FROM FEATURES where feature_name='someFeatureName2'\n" +
                "/";

        skipMarkedStatementsRuleToApply.performChecksOn(new ByteArrayInputStream(contentToParse.getBytes(StandardCharsets.UTF_8)),new File(""));

        assertThat(skipMarkedStatementsRuleToApply.getNotSkippedStatements()).hasSize(1);
        assertThat(skipMarkedStatementsRuleToApply.getNotSkippedStatements().get(0)).isEqualTo("DELETE FROM FEATURES where feature_name='someFeatureName1'\n");

    }

}