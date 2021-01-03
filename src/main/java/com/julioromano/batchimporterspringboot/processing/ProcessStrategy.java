package com.julioromano.batchimporterspringboot.processing;

import com.julioromano.batchimporterspringboot.entities.BatchProcess;
import com.julioromano.batchimporterspringboot.exceptions.DirectoryCreationException;

import java.util.Map;
import java.util.function.Consumer;

/**
 * {@code ProcessStrategy} interface represents the contract in which a valid
 * Processing Strategy should comply in order to keep the application working.
 *
 * {@code ProcessStrategy} interface represents the methods to process
 * and analyze the result of a specific strategy.
 *
 * @author  Julio Romano
 * @since   1.0
 */
public interface ProcessStrategy {

    Map<String, Consumer<String>> getParsersMap(String fileDelimiter, BatchProcess process);

    void createDirIfDoesNotExist() throws DirectoryCreationException;

    BatchResult getBatchResult(BatchProcess process);

    String getOutput(BatchResult batchResult);

    String getOutputFileDir();
}
