package com.github.vincent_fuchs.custom_build_rules.rules_to_apply;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase.LiquibaseScriptRulesToApply;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class LiquibaseScriptRulesToApplyTest {

    LiquibaseScriptRulesToApply liquibaseScriptRulesToApply=new LiquibaseScriptRulesToApply();
    String checkResult;

    @Before
    public void checkOnSampleFile() throws IOException {
        checkResult = liquibaseScriptRulesToApply.performChecksOn(new File("src/test/resources/verySimpleSqlScript.sql"));
    }

    @Test
    public void shouldReadExpectedNumberOfSqlStatements() throws Exception {
        assertThat(liquibaseScriptRulesToApply.getSqlStatements()).hasSize(2);
    }

    @Test
    @Ignore
    public void shouldFindAnErrorInScript_cantCreateTableIfExistsAlready() throws Exception {
        assertThat(checkResult).isNotEmpty();
    }

}