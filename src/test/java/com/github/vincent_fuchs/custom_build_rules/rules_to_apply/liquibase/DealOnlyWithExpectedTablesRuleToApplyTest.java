package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Ignore
public class DealOnlyWithExpectedTablesRuleToApplyTest {

    DealOnlyWithExpectedTablesRuleToApply dealOnlyWithExpectedTablesRuleToApply=new DealOnlyWithExpectedTablesRuleToApply();

    File dummyFile=new File("");

    @Test
    public void shouldAllowOnlyChangesOnTablesMatching() throws Exception {

        dealOnlyWithExpectedTablesRuleToApply.setPatternThatTablesMustFollow("^!S=ABC!.*");

        String fileContent="CREATE TABLE IF NOT EXISTS \"SOME_MATCHING_TABLE\"  (\"SOME_FIELD\"    VARCHAR2(256 BYTE));\n" +
                "/\n" +
                "CREATE TABLE IF NOT EXISTS \"SOME_NOT_MATCHING_TABLE\"  (\"SOME_FIELD\"    VARCHAR2(256 BYTE));\n" +
                "/\n" +
                "INSERT INTO \"SOME_MATCHING_TABLE\" values (\"SOME_VALUE\");\n" +
                "/\n" +
                "INSERT INTO \"SOME_NOT_MATCHING_TABLE\"  values (\"SOME_VALUE\");\n" +
                "/\n" +
                "DELETE TABLE IF NOT EXISTS \"SOME_MATCHING_TABLE\") ;\n" +
                "/\n" +
                "DELETE TABLE IF NOT EXISTS \"SOME_NOT_MATCHING_TABLE\");\n" +
                "/\n" +
                "/";


        List<ParsingIssue> parsingIssues=dealOnlyWithExpectedTablesRuleToApply.performChecksOn(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)),
                dummyFile);

        assertThat(parsingIssues).isNotEmpty();
    }

    @Test
    public void shouldAllowOnlyChangesOnSequencesMatching() throws Exception {

        dealOnlyWithExpectedTablesRuleToApply.setPatternThatTablesMustFollow("^!S=ABC!.*");

        String fileContent="CREATE SEQUENCE IF NOT EXISTS \"SOME_MATCHING_SEQUENCE\"  (\"SOME_FIELD\"    VARCHAR2(256 BYTE));\n" +
                "/\n" +
                "CREATE SEQUENCE IF NOT EXISTS \"SOME_NOT_MATCHING_SEQUENCE\"  (\"SOME_FIELD\"    VARCHAR2(256 BYTE));\n" +
                "/\n" +
                "/";


        List<ParsingIssue> parsingIssues=dealOnlyWithExpectedTablesRuleToApply.performChecksOn(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)),
                dummyFile);

        assertThat(parsingIssues).isNotEmpty();
    }

}