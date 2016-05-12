package com.github.vincent_fuchs.custom_build_rules.files_provider;

import com.github.vincent_fuchs.custom_build_rules.model.Parameters;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class VersionBasedFilesProviderTest {

    VersionBasedFilesProvider myFileProvider;

    @Test
    public void shouldReturnOnlyTheFilesForCurrentVersion() throws IOException {

        Parameters parameters = new Parameters();
        parameters.setVersion("2016.AAA.R6");
        parameters.setDirectory(targetDir() + File.separator + "myLiquibaseScripts" + File.separator);
        parameters.setFileExtension("sql");

        myFileProvider=new VersionBasedFilesProvider(parameters);

        //creating files for previous version
        createFileIfNotExist("/myLiquibaseScripts/","myApp-RELEASE-2016.AAA.R5.V0-01-blablabla.sql");
        createFileIfNotExist("/myLiquibaseScripts/","myApp-RELEASE-2016.AAA.R5.V0-02-blablabla.sql");

        //creating files for current version
        File expectedFile1=createFileIfNotExist("/myLiquibaseScripts/","myApp-RELEASE-2016.AAA.R6.V0-01-blablabla.sql");
        File expectedFile2= createFileIfNotExist("/myLiquibaseScripts/","myApp-RELEASE-2016.AAA.R6.V0-02-blablabla.sql");
        File expectedFile3=createFileIfNotExist("/myLiquibaseScripts/","myApp-RELEASE-2016.AAA.R6.V0-03-blablabla.sql");

        //creating files for next version
        createFileIfNotExist("/myLiquibaseScripts/","myApp-RELEASE-2016.AAA.R7.V0-01-blablabla.sql");
        createFileIfNotExist("/myLiquibaseScripts/","myApp-RELEASE-2016.AAA.R7.V0-02-blablabla.sql");

        List<File> foundFiles= myFileProvider.findFiles();
        assertThat(foundFiles).containsExactlyInAnyOrder(expectedFile1,expectedFile2,expectedFile3);
    }

    private File createFileIfNotExist(String path, String fileName) throws IOException {

        System.out.println(targetDir()+path+fileName);

        new File(targetDir()+path).mkdirs();

        File fileToCreate=new File(targetDir()+path+fileName);

        if(!fileToCreate.exists()){
            fileToCreate.createNewFile();
        }

        return fileToCreate;

    }

    private File targetDir(){
        String relPath = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        File targetDir = new File(relPath);
        if(!targetDir.exists()) {
            targetDir.mkdir();
        }
        return targetDir;
    }

}