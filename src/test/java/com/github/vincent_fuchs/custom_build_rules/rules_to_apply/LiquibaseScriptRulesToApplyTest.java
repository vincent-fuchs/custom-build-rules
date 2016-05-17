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


    private final String RESOURCES_FOLDER="src/test/resources/";


    @Test
    public void shouldReadExpectedNumberOfSqlStatements() throws Exception {

        liquibaseScriptRulesToApply.performChecksOn(new File(RESOURCES_FOLDER+"incorrectTableCreation.sql"));

        assertThat(liquibaseScriptRulesToApply.getSqlStatements()).hasSize(2);
    }

    @Test
    @Ignore
    public void shouldFindAnErrorInScript_cantCreateTableIfExistsAlready() throws Exception {

        String checkResult=liquibaseScriptRulesToApply.performChecksOn(new File(RESOURCES_FOLDER+"incorrectTableCreation.sql"));

        assertThat(checkResult).isNotEmpty();

        //TODO more assertions on the content of checkResult
    }

    @Test
    @Ignore
    public void shouldFindAnErrorInScript_cantInsertInReferenceTableWithoutProperCheckBefore() throws Exception {

        //not sure what would be the right way to create a table though.. create a "template" stored procedure that is mandatory to use ?
        String checkResult=liquibaseScriptRulesToApply.performChecksOn(new File(RESOURCES_FOLDER+"scriptWithCommentedStatement.sql"));

        assertThat(checkResult).isNotEmpty();

        //TODO more assertions on the content of checkResult
    }

    @Test
    @Ignore
    public void shouldSkipStatementsMarkedAsSuch() throws Exception {

        String checkResult=liquibaseScriptRulesToApply.performChecksOn(new File(RESOURCES_FOLDER+"insertInReferenceTableWithNoPriorCheck.sql"));

        //assuming the statement with -- skipMavenCheck comment is eliminated altogether from the list of statements
        // probably the comment should be configurable from XML (but with a defaul value)
        assertThat(liquibaseScriptRulesToApply.getSqlStatements()).hasSize(1);
    }

    @Test
    @Ignore
    public void scriptShouldEndWithForwardSlash() throws Exception {

        String checkResult=liquibaseScriptRulesToApply.performChecksOn(new File(RESOURCES_FOLDER+"scriptWithNoEndingSlash.sql"));

        assertThat(checkResult).isNotEmpty();

        //TODO more assertions on the content of checkResult
    }

    @Test
    @Ignore
    public void shouldHaveProperCommentsOnTableCreation_forSynonymCreationLater() throws Exception {

        String checkResult=liquibaseScriptRulesToApply.performChecksOn(new File(RESOURCES_FOLDER+"createTableWithProperComment.sql"));

        assertThat(checkResult).isNotEmpty();

        //TODO more assertions on the content of checkResult
    }

}