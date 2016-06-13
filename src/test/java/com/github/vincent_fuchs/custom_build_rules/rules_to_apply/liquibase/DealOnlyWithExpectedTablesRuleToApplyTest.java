package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import org.assertj.core.api.ListAssert;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



public class DealOnlyWithExpectedTablesRuleToApplyTest {

    DealOnlyWithExpectedTablesRuleToApply dealOnlyWithExpectedTablesRuleToApply = new DealOnlyWithExpectedTablesRuleToApply();

    File dummyFile = new File("");

    String configuredPattern = ".{0,2}SOME_MATCHING_TABLE.{0,2}";

    @Before
    public void configure() {
        dealOnlyWithExpectedTablesRuleToApply.setPatternThatTablesMustFollow(configuredPattern);
    }


    @Test
    public void shouldAllowOnlyChangesOnTablesMatching() throws Exception {

        String fileContent = "CREATE TABLE IF NOT EXISTS \"SOME_MATCHING_TABLE\"  (\"SOME_FIELD\"    VARCHAR2(256 BYTE));\n" +
                "/\n" +
                "CREATE TABLE IF NOT EXISTS \"SOME_NOT_MATCHING_TABLE\"  (\"SOME_FIELD\"    VARCHAR2(256 BYTE));\n" +
                "/\n" +
                "INSERT INTO \"A_SOME_MATCHING_TABLE_1\" values (\"SOME_VALUE\");\n" +
                "/\n" +
                "INSERT INTO \"A_SOME_NOT_MATCHING_TABLE_1\"  values (\"SOME_VALUE\");\n" +
                "/\n" +
                "DELETE TABLE IF NOT EXISTS \"1_SOME_MATCHING_TABLE_A\") ;\n" +
                "/\n" +
                "DELETE TABLE IF NOT EXISTS \"1_SOME_NOT_MATCHING_TABLE_A\");\n" +
                "/\n" +
                "/";


        List<ParsingIssue> parsingIssues = dealOnlyWithExpectedTablesRuleToApply.performChecksOn(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)),
                dummyFile);

        List<ParsingIssue> expectedIssues = new ArrayList<>();
        expectedIssues.add(new ParsingIssue(buildExpectedMessageForObject("SOME_NOT_MATCHING_TABLE"), dummyFile));
        expectedIssues.add(new ParsingIssue(buildExpectedMessageForObject("A_SOME_NOT_MATCHING_TABLE_1"), dummyFile));
        expectedIssues.add(new ParsingIssue(buildExpectedMessageForObject("1_SOME_NOT_MATCHING_TABLE_A"), dummyFile));

        assertThat(parsingIssues).containsExactlyElementsOf(expectedIssues);
    }

    private String buildExpectedMessageForObject(String dbObject) {

        return DealOnlyWithExpectedTablesRuleToApply.NOT_ALLOWED_TO_TOUCH_THAT_OBJECT + " - object : " + dbObject + ". configured pattern : " + configuredPattern;

    }

    @Test
    public void shouldAllowOnlyALTEROnTablesMatching() throws Exception {


        String fileContent = "alter table SOME_NOT_MATCHING_TABLE add NEW_COLUMN NUMBER(1,0) DEFAULT null\n" +
                "/\n" +
                "alter table SOME_MATCHING_TABLE add NEW_COLUMN NUMBER(1,0) DEFAULT null\n" +
                "/";

        List<ParsingIssue> parsingIssues = dealOnlyWithExpectedTablesRuleToApply.performChecksOn(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)),
                dummyFile);

        assertThat((parsingIssues.get(0).getMessage())).isEqualTo(buildExpectedMessageForObject("SOME_NOT_MATCHING_TABLE"));
        assertThat(parsingIssues).hasSize(1);
    }

    @Test
    @Ignore
    public void shouldAllowChangesOnlyOnSequencesMatching() {


    }
}