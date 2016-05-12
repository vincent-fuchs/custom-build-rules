package com.github.vincent_fuchs.custom_build_rules.files_provider;

import java.io.File;
import java.util.List;

public interface FilesProvider {

    List<File> findFiles();

}
