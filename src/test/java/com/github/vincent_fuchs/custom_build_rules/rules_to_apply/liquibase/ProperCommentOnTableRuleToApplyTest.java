package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.LiquibaseScriptRulesToApplyTest;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class ProperCommentOnTableRuleToApplyTest {


    ProperCommentOnTableRuleToApply properCommentOnTableRuleToApply = new ProperCommentOnTableRuleToApply();

    @Test
    @Ignore
    public void shouldHaveProperCommentsOnTableCreation_forSynonymCreationLater() throws Exception {

        String checkResult=properCommentOnTableRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"createTableWithProperComment.sql"));

        assertThat(checkResult).isNotEmpty();

        //TODO more assertions on the content of checkResult
    }

}