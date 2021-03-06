package com.github.vincent_fuchs.custom_build_rules.rules_to_apply;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase.LiquibaseScriptRulesToApply;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class LiquibaseScriptRulesToApplyTest {

    LiquibaseScriptRulesToApply liquibaseScriptRulesToApply=new LiquibaseScriptRulesToApply();

    List<ParsingIssue> parsingIssues;

    public final static String RESOURCES_FOLDER="src/test/resources/";


    @Test
    public void shouldReadExpectedNumberOfSqlStatements() throws Exception {

        liquibaseScriptRulesToApply.performChecksOn(new File(RESOURCES_FOLDER+"incorrectTableCreation.sql"));

        assertThat(liquibaseScriptRulesToApply.getSqlStatements()).hasSize(2);
    }

    @Test
    @Ignore
    public void shouldFindAnErrorInScript_cantCreateTableIfExistsAlready() throws Exception {

       parsingIssues=liquibaseScriptRulesToApply.performChecksOn(new File(RESOURCES_FOLDER+"incorrectTableCreation.sql"));

        assertThat(parsingIssues).isNotEmpty();

        //TODO more assertions on the content of checkResult
    }

    @Test
    @Ignore
    public void shouldFindAnErrorInScript_cantInsertInReferenceTableWithoutProperCheckBefore() throws Exception {

        //not sure what would be the right way to create a table though.. create a "template" stored procedure that is mandatory to use ?
        parsingIssues=liquibaseScriptRulesToApply.performChecksOn(new File(RESOURCES_FOLDER+"scriptWithCommentedStatement.sql"));

        assertThat(parsingIssues).isNotEmpty();

        //TODO more assertions on the content of checkResult
    }

}