package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.LiquibaseScriptRulesToApplyTest;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProperCommentOnTableRuleToApplyTest {


    ProperCommentOnTableRuleToApply properCommentOnTableRuleToApply = new ProperCommentOnTableRuleToApply();

    @Test
    public void shouldHaveProperCommentsOnTableCreation_forSynonymCreationLater() throws Exception {

        properCommentOnTableRuleToApply.setPatternThatCommentMustFollow("^!S=ABC!.*");

        List<ParsingIssue> parsingIssues=properCommentOnTableRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"createTableWithProperComment.sql"));

        assertThat(parsingIssues).hasSize(2);

        assertThat(parsingIssues.get(0).getMessage()).startsWith(ProperCommentOnTableRuleToApply.NO_COMMENT_AT_ALL);
        assertThat(parsingIssues.get(1).getMessage()).startsWith(ProperCommentOnTableRuleToApply.COMMENT_NOT_MATCHING_CONFIGURED_PATTERN);

    }

}