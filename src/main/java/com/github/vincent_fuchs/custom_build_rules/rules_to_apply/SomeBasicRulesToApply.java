package com.github.vincent_fuchs.custom_build_rules.rules_to_apply;

import java.io.File;

public class SomeBasicRulesToApply implements RuleToApply {
    @Override
    public String performChecksOn(File fileToCheck) {
        return null;
    }
}
