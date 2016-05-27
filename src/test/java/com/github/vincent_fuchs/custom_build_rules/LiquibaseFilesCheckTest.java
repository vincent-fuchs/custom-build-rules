package com.github.vincent_fuchs.custom_build_rules;

import com.github.vincent_fuchs.custom_build_rules.files_provider.FilesProvider;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.SomeBasicRulesToApply;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class LiquibaseFilesCheckTest {

    @Mock
    EnforcerRuleHelper mockHelper;

    @Mock
    FilesProvider filesProvider;

    @Mock
    private File file1;

    @Mock
    private File file2;

    ParsingIssue someParsingIssue1;
    ParsingIssue someParsingIssue2;

    LiquibaseFilesCheck liquibaseFilesCheck=new LiquibaseFilesCheck();

    @Mock
    RuleToApply ruleToApply=new SomeBasicRulesToApply();

    @Mock
    Log mockLogger;

    @Before
    public void configureLiquibaseFilesCheck() throws IOException, ExpressionEvaluationException {

        liquibaseFilesCheck.setRulesToApply(Arrays.asList(ruleToApply));
        liquibaseFilesCheck.setFilesProvider(filesProvider);

        someParsingIssue1=new ParsingIssue("Issue with file1",file1);
        someParsingIssue2=new ParsingIssue("Issue with file2",file2);

        when(mockHelper.evaluate("${project.version}")).thenReturn("1.0.0");
        when(mockHelper.getLog()).thenReturn(mockLogger);
        when(filesProvider.findFiles()).thenReturn(Arrays.asList(file1,file2));
    }


    @Test
    public void shouldAnalyzeTheFilesThatAreFound() throws EnforcerRuleException, IOException {

        liquibaseFilesCheck.execute(mockHelper);

        verify(ruleToApply).performChecksOn(file1);
        verify(ruleToApply).performChecksOn(file2);
    }

    @Test(expected = EnforcerRuleException.class)
    public void shouldFailWhenOneFileIsNotCompliant() throws EnforcerRuleException, IOException {

        when(ruleToApply.performChecksOn(file1)).thenReturn(Arrays.asList(someParsingIssue1));

        liquibaseFilesCheck.execute(mockHelper);

    }

    @Test
    public void shouldAggregateErrorMessagesWhenMultipleFailures() throws IOException {

        when(ruleToApply.performChecksOn(file1)).thenReturn(Arrays.asList(someParsingIssue1));
        when(ruleToApply.performChecksOn(file2)).thenReturn(Arrays.asList(someParsingIssue2));

        try {
            liquibaseFilesCheck.execute(mockHelper);
            fail("An exception should have been thrown !");
        } catch (EnforcerRuleException e) {

            assertThat(e).hasMessageContaining("file1");
            assertThat(e).hasMessageContaining("file2");
        }
    }

    private File createFileIfNotExist(String path, String fileName) throws IOException {

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