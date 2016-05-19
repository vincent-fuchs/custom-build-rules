package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.LiquibaseScriptRulesToApplyTest;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class EndingSlashRuleToApplyTest {

    SlashRuleToApply slashRuleToApply = new SlashRuleToApply();

    @Test
    public void scriptShouldEndWithForwardSlash_WhenLastLineIsAstatement() throws Exception {


        String checkResult=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"scriptWithNoEndingSlash.sql"));

        assertThat(checkResult).contains("scriptWithNoEndingSlash.sql - "+SlashRuleToApply.ERROR_MESSAGE);

    }


    @Test
    public void scriptShouldEndWithForwardSlash_WhenLastLinesAreBlank() throws Exception {

        String checkResult=slashRuleToApply.performChecksOn(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"scriptWithNoEndingSlashAndBlankLines.sql"));

        assertThat(checkResult).contains("scriptWithNoEndingSlashAndBlankLines.sql - "+SlashRuleToApply.ERROR_MESSAGE);
    }



}