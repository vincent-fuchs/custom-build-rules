package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.LiquibaseScriptRulesToApplyTest;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class ProperCommentOnTableRuleToApplyTest {


    ProperCommentOnTableRuleToApply properCommentOnTableRuleToApply = new ProperCommentOnTableRuleToApply();

    @Test
    public void shouldHaveProperCommentsOnTableCreation_forSynonymCreationLater() throws Exception {

        properCommentOnTableRuleToApply.setPatternThatCommentMustFollow("^!S=ABC!.*");

        String checkResult=properCommentOnTableRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"createTableWithProperComment.sql"));

        assertThat(checkResult).contains(ProperCommentOnTableRuleToApply.NO_COMMENT_AT_ALL);
        assertThat(checkResult).contains(ProperCommentOnTableRuleToApply.COMMENT_NOT_MATCHING_CONFIGURED_PATTERN);

    }

}