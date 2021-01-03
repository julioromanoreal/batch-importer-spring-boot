package com.julioromano.batchimporterspringboot.controllers;

import com.julioromano.batchimporterspringboot.batch.BatchAnalyzer;
import com.julioromano.batchimporterspringboot.processing.ProcessType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/batch-processing")
public class BatchProcessingController {

    private final BatchAnalyzer fileBatchAnalyzer;

    @Value("${application.sales-data-in-dir}")
    private String salesDataInDir;

    public BatchProcessingController(@Qualifier("FileBatchAnalyzer") BatchAnalyzer fileBatchAnalyzer) {
        this.fileBatchAnalyzer = fileBatchAnalyzer;
    }

    @RequestMapping(path = "/{processTypeValue}", method = RequestMethod.POST)
    public ResponseEntity<String> process(@PathVariable String processTypeValue) {
        try {
            ProcessType processType = ProcessType.valueOf(processTypeValue.toUpperCase());

            String inDir = getInDir(processType);
            if (inDir == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

            CompletableFuture<String> report = fileBatchAnalyzer.analyzeFiles(inDir, processType);
            return ResponseEntity.ok(report.get());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String getInDir(ProcessType processType) {
        String inDir = null;
        if (processType == ProcessType.SALES) {
            inDir = salesDataInDir;
        }
        return inDir;
    }

}
