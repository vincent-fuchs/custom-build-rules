package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.LiquibaseScriptRulesToApplyTest;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import org.junit.Test;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProperCommentOnTableRuleToApplyTest {

    ProperCommentOnTableRuleToApply properCommentOnTableRuleToApply = new ProperCommentOnTableRuleToApply();

    File dummyFile=new File("");

    @Test
    public void shouldHaveProperCommentsOnTableCreation_forSynonymCreationLater() throws Exception {

        properCommentOnTableRuleToApply.setPatternThatCommentMustFollow("^!S=ABC!.*");

        String fileContent="CREATE TABLE IF NOT EXISTS \"TABLE_OK\"  (\"SOME_FIELD\"    VARCHAR2(256 BYTE));\n" +
                "comment on table TABLE_OK is '!S=ABC! some table';\n" +
                "/\n" +
                "CREATE TABLE IF NOT EXISTS \"TABLE_WITH_NO_COMMENT\"  (\"SOME_FIELD\"    VARCHAR2(256 BYTE));\n" +
                "/\n" +
                "CREATE TABLE IF NOT EXISTS \"TABLE_WITH_INCORRECT_COMMENT\"  (\"SOME_FIELD\"    VARCHAR2(256 BYTE));\n" +
                "comment on table TABLE_OK is 'some comment not matching configured regexp';\n" +
                "/";


        List<ParsingIssue> parsingIssues=properCommentOnTableRuleToApply.performChecksOn(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)),
                                                                                        dummyFile);

        assertThat(parsingIssues).hasSize(2);

        assertThat(parsingIssues.get(0).getMessage()).startsWith(ProperCommentOnTableRuleToApply.NO_COMMENT_AT_ALL);
        assertThat(parsingIssues.get(1).getMessage()).startsWith(ProperCommentOnTableRuleToApply.COMMENT_NOT_MATCHING_CONFIGURED_PATTERN);

    }

}