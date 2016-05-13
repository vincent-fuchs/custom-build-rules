package com.github.vincent_fuchs.custom_build_rules.util;

import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilsTest {

    @Test
    public void shouldAddLeadingTrailingSeparatorIfNotHere() throws Exception {
        assertThat(StringUtils.addLeadingTrailingFileSeparatorIfRequired("something"))
                .isEqualTo(File.separator+"something"+File.separator);
    }

    @Test
    public void shouldAddLeadingTrailingSeparatorIfOneInMiddle() throws Exception {
        assertThat(StringUtils.addLeadingTrailingFileSeparatorIfRequired("something"+File.separator+"somethingElse"))
                .isEqualTo(File.separator+"something"+File.separator+"somethingElse"+File.separator);
    }

    @Test
    public void shouldAddLeadingIfNotHere() throws Exception {

        assertThat(StringUtils.addLeadingTrailingFileSeparatorIfRequired("something"+File.separator))
                .isEqualTo(File.separator+"something"+File.separator);

    }

    @Test
    public void shouldAddTrailingIfNotHere() throws Exception {
        assertThat(StringUtils.addLeadingTrailingFileSeparatorIfRequired(File.separator+"something"))
                .isEqualTo(File.separator+"something"+File.separator);
    }
}