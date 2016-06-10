package com.github.vincent_fuchs.custom_build_rules;

import com.github.vincent_fuchs.custom_build_rules.files_provider.FilesProvider;
import com.github.vincent_fuchs.custom_build_rules.model.ParsedFile;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.RuleToApply;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.SomeBasicRulesToApply;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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

    @Rule
    public ExpectedException exceptionPolicy = ExpectedException.none();

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

        verify(ruleToApply).performChecksOn(new ParsedFile(file1));
        verify(ruleToApply).performChecksOn(new ParsedFile(file2));
    }

    @Test(expected = EnforcerRuleException.class)
    public void shouldFailWhenOneFileIsNotCompliant() throws EnforcerRuleException, IOException {

        when(ruleToApply.performChecksOn(new ParsedFile(file1))).thenReturn(Arrays.asList(someParsingIssue1));

        liquibaseFilesCheck.execute(mockHelper);
    }

    @Test
    public void shouldAggregateErrorMessagesWhenMultipleFailures() throws IOException, EnforcerRuleException {

        exceptionPolicy.expect(EnforcerRuleException.class);
        exceptionPolicy.expectMessage("file1");
        exceptionPolicy.expectMessage("file2");

        when(ruleToApply.performChecksOn(new ParsedFile(file1))).thenReturn(Arrays.asList(someParsingIssue1));
        when(ruleToApply.performChecksOn(new ParsedFile(file2))).thenReturn(Arrays.asList(someParsingIssue2));

        liquibaseFilesCheck.execute(mockHelper);
    }

    @Test
    public void shouldProcessAsExpectedWithSeveral() throws IOException, EnforcerRuleException {

        exceptionPolicy.expect(EnforcerRuleException.class);
        exceptionPolicy.expectMessage("file1");
        exceptionPolicy.expectMessage("file2");

        when(ruleToApply.performChecksOn(new ParsedFile(file1))).thenReturn(Arrays.asList(someParsingIssue1));
        when(ruleToApply.performChecksOn(new ParsedFile(file2))).thenReturn(Arrays.asList(someParsingIssue2));

        liquibaseFilesCheck.execute(mockHelper);
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