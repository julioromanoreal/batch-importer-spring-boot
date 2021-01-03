package com.julioromano.batchimporterspringboot.processing.impl;

import com.julioromano.batchimporterspringboot.entities.BatchProcess;
import com.julioromano.batchimporterspringboot.exceptions.DirectoryCreationException;
import com.julioromano.batchimporterspringboot.exceptions.ProcessOutputException;
import com.julioromano.batchimporterspringboot.exceptions.ProcessTypeException;
import com.julioromano.batchimporterspringboot.exceptions.ProcessingException;
import com.julioromano.batchimporterspringboot.processing.BatchProcessing;
import com.julioromano.batchimporterspringboot.processing.BatchResult;
import com.julioromano.batchimporterspringboot.processing.ProcessStrategy;
import com.julioromano.batchimporterspringboot.processing.ProcessType;
import com.julioromano.batchimporterspringboot.repositories.BatchProcessRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Service("DatabaseBatchProcessing")
public class DatabaseBatchProcessing implements BatchProcessing {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseBatchProcessing.class);

    @Value("${application.file-delimiter}")
    private String fileDelimiter;

    private final BatchProcessRepository batchProcessRepository;
    private final ProcessStrategy salesProcessStrategy;

    private ProcessStrategy processStrategy;

    public DatabaseBatchProcessing(BatchProcessRepository batchProcessRepository,
                                   @Qualifier("SalesProcessStrategy") ProcessStrategy salesProcessStrategy) {
        this.batchProcessRepository = batchProcessRepository;
        this.salesProcessStrategy = salesProcessStrategy;
    }

    @Override
    public void setProcessType(ProcessType processType) throws ProcessTypeException {
        if (processType == ProcessType.SALES) {
            processStrategy = salesProcessStrategy;
        } else {
            throw new ProcessTypeException("Process Type undefined: " + processType);
        }
    }

    @Override
    @Async
    public CompletableFuture<BatchProcess> process(List<Path> files) throws ProcessingException {
        BatchProcess process = BatchProcess.builder().startedAt(new Date()).build();
        process = batchProcessRepository.save(process);
        List<CompletableFuture<Void>> threads = new ArrayList<>();

        for (Path file : files) {
            try (LineIterator it = FileUtils.lineIterator(file.toFile(), "UTF-8")) {
                LOGGER.info("Processing file " + file);
                CompletableFuture<Void> p = processFile(fileDelimiter, it, process);
                threads.add(p);
            } catch (IOException e) {
                LOGGER.error("Error processing file " + file);
                throw new ProcessingException(e);
            }
        }

        CompletableFuture.allOf(threads.toArray(new CompletableFuture[0])).join();
        return CompletableFuture.completedFuture(process);
    }

    @Async
    public CompletableFuture<Void> processFile(String fileDelimiter, LineIterator it, BatchProcess process) {
        Map<String, Consumer<String>> parsers = processStrategy.getParsersMap(fileDelimiter, process);

        while (it.hasNext()) {
            String line = it.nextLine();
            String identifier = line.substring(0, line.indexOf(fileDelimiter));

            if (parsers.containsKey(identifier)) {
                parsers.get(identifier).accept(line);
            }
        }

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public String produceOutput(BatchProcess process) throws ProcessOutputException, DirectoryCreationException {
        LOGGER.info("Processing the output of the process");

        processStrategy.createDirIfDoesNotExist();

        try {
            BatchResult batchResult = processStrategy.getBatchResult(process);
            return createOutputFile(batchResult);
        } catch (IOException e) {
            LOGGER.error("Error producing output file");
            throw new ProcessOutputException(e);
        }
    }

    private String createOutputFile(BatchResult batchResult) throws IOException {
        String fileName = new SimpleDateFormat("yyyyMMddHHmm'.done.dat'").format(new Date());
        FileWriter fileWriter = new FileWriter(processStrategy.getOutputFileDir() + fileName);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            String output = processStrategy.getOutput(batchResult);
            printWriter.print(output);
        }

        return fileName;
    }



}
