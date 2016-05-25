package com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase;

import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class ProperCommentOnTableRuleToApply extends RuleToApply {

    public static final String ERROR_MESSAGE="This table creation statement should have a comment on it";


    @Override
    public String performChecksOn(File fileToCheck) throws IOException {

        String fileContentAsString= readFileAsString(fileToCheck.getAbsolutePath(), StandardCharsets.UTF_8);

        String[] sqlStatements=fileContentAsString.split("/");

        try {
            for(int i=0; i < sqlStatements.length ; i++){

                System.out.println("parsing statement "+i+" : "+sqlStatements[i]);

                Statement sqlStmt = CCJSqlParserUtil.parse(sqlStatements[i]);

                if(sqlStmt instanceof Select){

                    Select selectStatement=(Select)sqlStmt;


                }

            }


        } catch (JSQLParserException e) {
            e.printStackTrace();
        }

        return null;

    }

}
