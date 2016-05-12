package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LiquibaseScriptRulesToApply implements RuleToApply {

    List sqlStatements=new ArrayList();

    @Override
    public String performChecksOn(File fileToCheck) {
        return null;
    }

    public List getSqlStatements() {
        return sqlStatements;
    }
}
