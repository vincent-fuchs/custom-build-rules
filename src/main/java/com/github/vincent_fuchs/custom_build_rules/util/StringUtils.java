package com.github.vincent_fuchs.custom_build_rules.util;

import java.io.File;

public class StringUtils {

    public static String addLeadingTrailingFileSeparatorIfRequired(String input){

        if(input==null){
            return null;
        }

        String fileSeparator= File.separator;

        if(!input.startsWith(fileSeparator)){
            input=fileSeparator+input;
        }

        if(!input.endsWith(fileSeparator)){
            input=input+fileSeparator;
        }



        return input;
    }


}
