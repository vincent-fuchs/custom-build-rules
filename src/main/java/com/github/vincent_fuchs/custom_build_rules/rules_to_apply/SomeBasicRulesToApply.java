package com.github.vincent_fuchs.custom_build_rules.rules_to_apply;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class SomeBasicRulesToApply extends RuleToApply {

    @Override
    public List<ParsingIssue> performChecksOn(InputStream fileToCheck, File file) throws IOException {
        return null;
    }
}
