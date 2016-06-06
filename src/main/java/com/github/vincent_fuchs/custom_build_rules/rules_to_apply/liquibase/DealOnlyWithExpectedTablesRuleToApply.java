package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;


import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.Collections;
import java.util.List;

public class DealOnlyWithExpectedTablesRuleToApply extends RuleToApply {

    private String patternThatTablesMustFollow=".*";

    @Override
    public List<ParsingIssue> performChecksOn(InputStream fileToCheckAsStream, File file) throws IOException {

     return Collections.emptyList();
    }

    public void setPatternThatTablesMustFollow(String patternThatTablesMustFollow) {
        this.patternThatTablesMustFollow = patternThatTablesMustFollow;
    }
}
