package com.julioromano.batchimporterspringboot.processing;

import com.julioromano.batchimporterspringboot.entities.BatchProcess;
import com.julioromano.batchimporterspringboot.exceptions.DirectoryCreationException;
import com.julioromano.batchimporterspringboot.exceptions.ProcessOutputException;
import com.julioromano.batchimporterspringboot.exceptions.ProcessTypeException;
import com.julioromano.batchimporterspringboot.exceptions.ProcessingException;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * {@code BatchProcessing} interface represents the contract in which a valid
 * Processing component should comply in order to keep the application working.
 *
 * {@code BatchProcessing} interface represents the methods to process a list of files
 * and produce the output report.
 *
 * @author  Julio Romano
 * @since   1.0
 */
public interface BatchProcessing {

    /**
     * Define the type of processing so allowing
     * new types to be easily added in future as needed.
     *
     * It throws a {@code ProcessTypeException} in case the
     * {@code ProcessType} defined is not valid.
     *
     * @author  Julio Romano
     * @since   1.0
     */
    void setProcessType(ProcessType processType) throws ProcessTypeException;

    /**
     * Process the files in the path specified and return
     * the POJO object with information about the execution.
     *
     * For performance reasons, this method should be executed
     * asynchronously.
     *
     * @author  Julio Romano
     * @since   1.0
     */
    CompletableFuture<BatchProcess> process(List<Path> files) throws ProcessingException;

    /**
     * Given a POJO object with information about the execution,
     * this method should create the output file and return its name.
     *
     * @author  Julio Romano
     * @since   1.0
     */
    String produceOutput(BatchProcess process) throws ProcessOutputException, DirectoryCreationException;

}
