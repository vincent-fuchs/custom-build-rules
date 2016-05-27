package com.github.vincent_fuchs.custom_build_rules.rules_to_apply;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class SomeBasicRulesToApply extends RuleToApply {
    @Override
    public List<ParsingIssue> performChecksOn(File fileToCheck) {
        return Collections.emptyList();
    }
}
