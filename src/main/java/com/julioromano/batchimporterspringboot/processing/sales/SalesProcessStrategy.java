package com.julioromano.batchimporterspringboot.processing.sales;

import com.julioromano.batchimporterspringboot.entities.BatchProcess;
import com.julioromano.batchimporterspringboot.entities.Customer;
import com.julioromano.batchimporterspringboot.entities.Sale;
import com.julioromano.batchimporterspringboot.entities.Salesman;
import com.julioromano.batchimporterspringboot.exceptions.DirectoryCreationException;
import com.julioromano.batchimporterspringboot.processing.BatchResult;
import com.julioromano.batchimporterspringboot.processing.ProcessStrategy;
import com.julioromano.batchimporterspringboot.processing.sales.utils.SalesBatchUtils;
import com.julioromano.batchimporterspringboot.repositories.CustomerRepository;
import com.julioromano.batchimporterspringboot.repositories.SaleRepository;
import com.julioromano.batchimporterspringboot.repositories.SalesmanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component("SalesProcessStrategy")
public class SalesProcessStrategy implements ProcessStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesProcessStrategy.class);

    @Value("${application.file-delimiter}")
    private String fileDelimiter;

    @Value("${application.sales-data-out-dir}")
    private String outputFileDir;

    private final CustomerRepository customerRepository;
    private final SalesmanRepository salesmanRepository;
    private final SaleRepository saleRepository;
    private final SalesBatchUtils salesBatchUtils;

    public SalesProcessStrategy(CustomerRepository customerRepository, SalesmanRepository salesmanRepository, SaleRepository saleRepository, SalesBatchUtils salesBatchUtils) {
        this.customerRepository = customerRepository;
        this.salesmanRepository = salesmanRepository;
        this.saleRepository = saleRepository;
        this.salesBatchUtils = salesBatchUtils;
    }

    @Override
    public Map<String, Consumer<String>> getParsersMap(String fileDelimiter, BatchProcess process) {
        Map<String, Consumer<String>> parsers = new HashMap<>();
        parsers.put(SalesBatchType.SALESMAN.getIdentifier(), line -> {
            Salesman salesman = salesBatchUtils.handleSalesmanData(fileDelimiter, line);
            salesman.setBatchProcess(process);
            salesmanRepository.save(salesman);

        });
        parsers.put(SalesBatchType.CUSTOMER.getIdentifier(), line -> {
            Customer customer = salesBatchUtils.handleCustomerData(fileDelimiter, line);
            customer.setBatchProcess(process);
            customerRepository.save(customer);
        });
        parsers.put(SalesBatchType.SALE.getIdentifier(), line -> {
            Sale sale = salesBatchUtils.handleSaleData(fileDelimiter, line);
            sale.setBatchProcess(process);
            saleRepository.save(sale);
        });
        return parsers;
    }

    @Override
    public void createDirIfDoesNotExist() throws DirectoryCreationException {
        File output = new File(outputFileDir);
        if (!output.exists()) {
            if (!output.mkdirs()) {
                LOGGER.error("Error creating directories " + outputFileDir);
                throw new DirectoryCreationException("Error creating directories" + outputFileDir);
            }
        }
    }

    @Override
    public BatchResult getBatchResult(BatchProcess process) {
        List<Sale> sales = saleRepository.findByBatchProcessId(process.getId());

        int numberOfCustomers = customerRepository.countByBatchProcessId(process.getId());
        int numberOfSalesman = salesmanRepository.countByBatchProcessId(process.getId());
        String mostExpensiveSale = salesBatchUtils.getMostExpensiveSale(sales);
        String worstSalesman = salesBatchUtils.getWorstSalesman(sales);

        return new SalesBatchResult(numberOfCustomers, numberOfSalesman, mostExpensiveSale, worstSalesman);
    }

    @Override
    public String getOutput(BatchResult batchResult) {
        return salesBatchUtils.getFormattedOutput((SalesBatchResult) batchResult);
    }

    @Override
    public String getOutputFileDir() {
        return outputFileDir;
    }
}
