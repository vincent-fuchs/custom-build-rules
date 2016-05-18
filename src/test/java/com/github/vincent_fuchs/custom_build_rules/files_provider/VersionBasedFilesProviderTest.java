package com.github.vincent_fuchs.custom_build_rules.files_provider;

import com.github.vincent_fuchs.custom_build_rules.model.Parameters;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class VersionBasedFilesProviderTest {

    VersionBasedFilesProvider myFileProvider;

    File expectedFile1;
    File expectedFile2;
    File expectedFile3;

    Parameters parameters = new Parameters();

    @Before
    public void setup() throws IOException {
        parameters.setDirectory(targetDir() + File.separator + "myLiquibaseScripts" + File.separator);
        parameters.setFileExtension("sql");

        createDummyFiles();
    }

    @Test
    public void shouldReturnOnlyTheFilesForCurrentVersion_whenVersionMatchesExactly() {

        parameters.setVersion("2016.AAA.R6");

        myFileProvider=new VersionBasedFilesProvider(parameters);

        List<File> foundFiles= myFileProvider.findFiles();
        assertThat(foundFiles).containsExactlyInAnyOrder(expectedFile1,expectedFile2,expectedFile3);
    }

    @Test
    public void shouldReturnOnlyTheFilesForCurrentVersion_whenVersionIsRcSnapshot() throws IOException {
        parameters.setVersion("2016.AAA.R6.RC-SNAPSHOT");

        myFileProvider=new VersionBasedFilesProvider(parameters);

        List<File> foundFiles= myFileProvider.findFiles();
        assertThat(foundFiles).containsExactlyInAnyOrder(expectedFile1,expectedFile2,expectedFile3);
    }

    @Test
    public void shouldReturnOnlyTheFilesForCurrentVersion_whenVersionIsMinorVersion() throws IOException {
        parameters.setVersion("2016.AAA.R6.RC00");

        myFileProvider=new VersionBasedFilesProvider(parameters);

        List<File> foundFiles= myFileProvider.findFiles();
        assertThat(foundFiles).containsExactlyInAnyOrder(expectedFile1,expectedFile2,expectedFile3);
    }


    private void createDummyFiles() throws IOException {
        //creating files for previous version
        createFileIfNotExist("/myLiquibaseScripts/","myApp-RELEASE-2016.AAA.R5.V0-01-blablabla.sql");
        createFileIfNotExist("/myLiquibaseScripts/","myApp-RELEASE-2016.AAA.R5.V0-02-blablabla.sql");

        //creating files for current version
        expectedFile1=createFileIfNotExist("/myLiquibaseScripts/","myApp-RELEASE-2016.AAA.R6.V0-01-blablabla.sql");
        expectedFile2= createFileIfNotExist("/myLiquibaseScripts/","myApp-RELEASE-2016.AAA.R6.V0-02-blablabla.sql");
        expectedFile3=createFileIfNotExist("/myLiquibaseScripts/","myApp-RELEASE-2016.AAA.R6.V0-03-blablabla.sql");

        //creating files for next version
        createFileIfNotExist("/myLiquibaseScripts/","myApp-RELEASE-2016.AAA.R7.V0-01-blablabla.sql");
        createFileIfNotExist("/myLiquibaseScripts/","myApp-RELEASE-2016.AAA.R7.V0-02-blablabla.sql");
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