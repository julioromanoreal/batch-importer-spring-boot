package com.julioromano.batchimporterspringboot.batch.files;

import com.julioromano.batchimporterspringboot.batch.BatchAnalyzer;
import com.julioromano.batchimporterspringboot.batch.files.utils.FileUtils;
import com.julioromano.batchimporterspringboot.entities.BatchProcess;
import com.julioromano.batchimporterspringboot.exceptions.FileWatchingException;
import com.julioromano.batchimporterspringboot.exceptions.ProcessTypeException;
import com.julioromano.batchimporterspringboot.processing.BatchProcessing;
import com.julioromano.batchimporterspringboot.processing.ProcessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service("FileBatchAnalyzer")
public class FileBatchAnalyzer implements BatchAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileBatchAnalyzer.class);

    private final BatchProcessing databaseBatchProcessing;
    private final FileUtils fileUtils;

    public FileBatchAnalyzer(@Qualifier("DatabaseBatchProcessing") BatchProcessing databaseBatchProcessing, FileUtils fileUtils) {
        this.databaseBatchProcessing = databaseBatchProcessing;
        this.fileUtils = fileUtils;
    }

    @Override
    @Async
    public CompletableFuture<String> analyzeFiles(String path, ProcessType processType) throws FileWatchingException {
        try {
            List<Path> files = getFiles(path);

            databaseBatchProcessing.setProcessType(processType);
            CompletableFuture<BatchProcess> futureProcess = databaseBatchProcessing.process(files);
            BatchProcess process = futureProcess.get();

            String report = databaseBatchProcessing.produceOutput(process);
            return CompletableFuture.completedFuture(report);
        } catch (IOException | InterruptedException | ExecutionException | ProcessTypeException e) {
            LOGGER.error("Error watching for new files in " + path, e);
            throw new FileWatchingException(e);
        }
    }

    private List<Path> getFiles(String path) throws IOException {
        return Files.walk(Paths.get(path))
                .filter(Files::isRegularFile)
                .filter(f -> {
                    Optional<String> extension = fileUtils.getExtensionByStringHandling(f.toString());
                    return extension.isPresent() && extension.get().equals("dat");
                })
                .collect(Collectors.toList());
    }

}
