package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LiquibaseScriptRulesToApply implements RuleToApply {

    List sqlStatements=new ArrayList();

    @Override
    public String performChecksOn(File fileToCheck) throws IOException {

        String fileContent=readFile(fileToCheck.getAbsolutePath(), StandardCharsets.UTF_8);

        sqlStatements= Arrays.asList(fileContent.split("/"));


        return null;
    }

    public List getSqlStatements() {
        return sqlStatements;
    }




    public static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
