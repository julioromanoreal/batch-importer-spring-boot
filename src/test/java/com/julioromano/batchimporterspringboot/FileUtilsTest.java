package com.julioromano.batchimporterspringboot;

import com.julioromano.batchimporterspringboot.batch.files.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class FileUtilsTest {

    @Autowired
    FileUtils fileUtils;

    @Test
    void givenFileNameThenReturnExtension() {
        Optional<String> extension = fileUtils.getExtensionByStringHandling("file.dat");
        assertEquals(extension.get(), "dat");
    }

    @Test
    void givenFileNameWithNoExtensionThenReturnShouldBeEmpty() {
        Optional<String> extension = fileUtils.getExtensionByStringHandling("file");
        assertFalse(extension.isPresent());
    }

}
