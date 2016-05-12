package com.github.vincent_fuchs.custom_build_rules.rules_to_apply;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase.LiquibaseScriptRulesToApply;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class LiquibaseScriptRulesToApplyTest {

    LiquibaseScriptRulesToApply liquibaseScriptRulesToApply=new LiquibaseScriptRulesToApply();

    @Test
    public void shouldReadExpectedNumberOfSqlStatements() throws Exception {

        liquibaseScriptRulesToApply.performChecksOn(new File("verySimpleSqlScript.sql"));

        assertThat(liquibaseScriptRulesToApply.getSqlStatements()).hasSize(2);

    }

}