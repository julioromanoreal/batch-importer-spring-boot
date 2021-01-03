package com.julioromano.batchimporterspringboot.batch;

import com.julioromano.batchimporterspringboot.exceptions.FileWatchingException;
import com.julioromano.batchimporterspringboot.processing.ProcessType;

import java.util.concurrent.CompletableFuture;

/**
 * {@code BatchAnalyzer} interface represents the contract in which a valid
 * Watcher should comply in order to keep the application working.
 *
 * {@code BatchAnalyzer} interface represents only the entrypoint in which
 * the whole process start.
 *
 * @author  Julio Romano
 * @since   1.0
 */
public interface BatchAnalyzer {

    /**
     * Process the files in the path specified and return
     * the name of the file that was created.
     *
     * For performance reasons, this method should be executed
     * asynchronously and return a {@code CompletableFuture}
     * containing the name of the output file when the process
     * is done.
     *
     * @author  Julio Romano
     * @since   1.0
     */
    CompletableFuture<String> analyzeFiles(String path, ProcessType processType) throws FileWatchingException;

}
