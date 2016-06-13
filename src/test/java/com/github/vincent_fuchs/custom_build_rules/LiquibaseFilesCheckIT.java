package com.github.vincent_fuchs.custom_build_rules;

import com.github.vincent_fuchs.custom_build_rules.files_provider.FilesProvider;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.LiquibaseScriptRulesToApplyTest;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.ParsingIssue;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase.EndingSlashRuleToApply;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase.ProperCommentOnTableRuleToApply;
import com.github.vincent_fuchs.custom_build_rules.rules_to_apply.liquibase.SkipMarkedStatementsRuleToApply;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class LiquibaseFilesCheckIT {

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Mock
    EnforcerRuleHelper mockHelper;

    @Mock
    FilesProvider filesProvider;

    private File file1;

    private File file2;

    LiquibaseFilesCheck liquibaseFilesCheck = new LiquibaseFilesCheck();

    @Rule
    public ExpectedException exceptionPolicy = ExpectedException.none();

    SkipMarkedStatementsRuleToApply skipMarkedStatements = new SkipMarkedStatementsRuleToApply("-- skipMavenCheck");
    EndingSlashRuleToApply endingSlash = new EndingSlashRuleToApply();
    ProperCommentOnTableRuleToApply properCommentOnTable = new ProperCommentOnTableRuleToApply("^!S=ABC!.*");

    @Mock
    Log mockLogger;

    @Before
    public void configureLiquibaseFilesCheck() throws IOException, ExpressionEvaluationException {

        liquibaseFilesCheck.setRulesToApply(Arrays.asList(skipMarkedStatements, endingSlash, properCommentOnTable));
        liquibaseFilesCheck.setFilesProvider(filesProvider);

        when(mockHelper.evaluate("${project.version}")).thenReturn("1.0.0");
        when(mockHelper.evaluate("${basedir}")).thenReturn(folder.getRoot().getAbsolutePath());
        when(mockHelper.getLog()).thenReturn(mockLogger);
      }


    @Test
    public void shouldFindTheExpectedIssues() throws EnforcerRuleException, IOException {

        exceptionPolicy.expect(EnforcerRuleException.class);

        exceptionPolicy.expectMessage(ProperCommentOnTableRuleToApply.COMMENT_NOT_MATCHING_CONFIGURED_PATTERN);

        when(filesProvider.findFiles()).thenReturn(Arrays.asList(new File(LiquibaseScriptRulesToApplyTest.RESOURCES_FOLDER+"scriptWithAllKindsOfIssues.sql")));

        liquibaseFilesCheck.execute(mockHelper);

    }
}