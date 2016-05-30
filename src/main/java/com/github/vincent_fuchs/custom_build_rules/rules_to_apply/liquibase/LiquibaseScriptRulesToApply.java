package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LiquibaseScriptRulesToApply extends RuleToApply {

    List sqlStatements=new ArrayList();

    @Override
    public List<ParsingIssue> performChecksOn(InputStream fileToCheckAsStream, File file) throws IOException {

        String fileContent= IOUtils.toString(fileToCheckAsStream, "UTF-8");

        sqlStatements= Arrays.asList(fileContent.split("/"));


        return Collections.emptyList();
    }

    public List getSqlStatements() {
        return sqlStatements;
    }

}
