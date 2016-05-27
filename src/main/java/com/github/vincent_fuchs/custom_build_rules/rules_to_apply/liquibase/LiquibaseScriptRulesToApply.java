package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LiquibaseScriptRulesToApply extends RuleToApply {

    List sqlStatements=new ArrayList();

    @Override
    public List<ParsingIssue> performChecksOn(File fileToCheck) throws IOException {

        String fileContent=readFileAsString(fileToCheck.getAbsolutePath(), StandardCharsets.UTF_8);

        sqlStatements= Arrays.asList(fileContent.split("/"));


        return Collections.emptyList();
    }

    public List getSqlStatements() {
        return sqlStatements;
    }

}
