package com.julioromano.batchimporterspringboot;


import com.julioromano.batchimporterspringboot.entities.BatchProcess;
import com.julioromano.batchimporterspringboot.entities.Customer;
import com.julioromano.batchimporterspringboot.entities.Sale;
import com.julioromano.batchimporterspringboot.entities.Salesman;
import com.julioromano.batchimporterspringboot.processing.ProcessStrategy;
import com.julioromano.batchimporterspringboot.processing.ProcessType;
import com.julioromano.batchimporterspringboot.processing.impl.DatabaseBatchProcessing;
import com.julioromano.batchimporterspringboot.processing.sales.utils.SalesBatchUtils;
import com.julioromano.batchimporterspringboot.repositories.BatchProcessRepository;
import com.julioromano.batchimporterspringboot.repositories.CustomerRepository;
import com.julioromano.batchimporterspringboot.repositories.SaleRepository;
import com.julioromano.batchimporterspringboot.repositories.SalesmanRepository;
import org.apache.commons.io.LineIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SalesBatchProcessingTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SalesmanRepository salesmanRepository;

    @Autowired
    private BatchProcessRepository batchProcessRepository;

    @Autowired
    @Qualifier("SalesProcessStrategy")
    private ProcessStrategy salesProcessStrategy;

    @Mock
    private SalesBatchUtils salesBatchUtils;

    private DatabaseBatchProcessing databaseBatchProcessing;

    @BeforeEach
    void init() {
        mockSalesmanData();
        mockCustomerData();
        mockSaleData();

        databaseBatchProcessing = new DatabaseBatchProcessing(
                batchProcessRepository, salesProcessStrategy
        );
        databaseBatchProcessing.setProcessType(ProcessType.SALES);
    }

    private void mockSaleData() {
        when(salesBatchUtils.handleSaleData("ç", "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro"))
                .thenReturn(Sale.builder()
                        .saleId("10").items("[1-10-100,2-30-2.50,3-40-3.10]").salesmanName("Pedro").build());
        when(salesBatchUtils.handleSaleData("ç", "003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo"))
                .thenReturn(Sale.builder()
                        .saleId("08").items("[1-34-10,2-33-1.50,3-40-0.10]").salesmanName("Paulo").build());
    }

    private void mockCustomerData() {
        when(salesBatchUtils.handleCustomerData("ç", "002ç2345675434544345çJose da SilvaçRural"))
                .thenReturn(Customer.builder()
                        .cnpj(2345675434544345L).name("Jose da Silva").businessArea("Rural").build());
        when(salesBatchUtils.handleCustomerData("ç", "002ç2345675433444345çEduardo PereiraçRural"))
                .thenReturn(Customer.builder()
                        .cnpj(2345675433444345L).name("Eduardo Pereira").businessArea("Rural").build());
        when(salesBatchUtils.handleCustomerData("ç", "002ç2345675987544345çPedro GonçalvesçRural"))
                .thenReturn(Customer.builder()
                        .cnpj(2345675987544345L).name("Pedro Gonçalves").businessArea("Rural").build());
    }

    private void mockSalesmanData() {
        when(salesBatchUtils.handleSalesmanData("ç", "001ç1234567891234çPedro Gonçalvesç50000"))
                .thenReturn(Salesman.builder()
                        .cpf(1234567891234L).name("Pedro Gonçalves").salary(new BigDecimal("50000")).build());
        when(salesBatchUtils.handleSalesmanData("ç", "001ç1234567891234çPedroç50000"))
                .thenReturn(Salesman.builder()
                        .cpf(1234567891234L).name("Pedro").salary(new BigDecimal("50000")).build());
        when(salesBatchUtils.handleSalesmanData("ç", "001ç3245678865434çPauloç40000.99"))
                .thenReturn(Salesman.builder()
                        .cpf(3245678865434L).name("Paulo").salary(new BigDecimal("40000.99")).build());
        when(salesBatchUtils.handleSalesmanData("ç", "001ç9745670165474çGonçalvesç40000.98"))
                .thenReturn(Salesman.builder()
                        .cpf(9745670165474L).name("Gonçalves").salary(new BigDecimal("40000.98")).build());
    }

    @Test
    public void givenCorrectFileContentThenCustomersShouldBeSaved() {
        BatchProcess process = batchProcessRepository.save(BatchProcess.builder().startedAt(new Date()).build());

        StringReader reader = new StringReader("""
                001ç1234567891234çPedroç50000
                001ç3245678865434çPauloç40000.99
                002ç2345675434544345çJose da SilvaçRural
                002ç2345675433444345çEduardo PereiraçRural
                003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
                003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
                """);
        LineIterator it = new LineIterator(reader);
        databaseBatchProcessing.processFile("ç", it, process);
        assertEquals(customerRepository.countByBatchProcessId(process.getId()), 2);
    }

    @Test
    public void givenCorrectFileContentThenSalesmansShouldBeSaved() {
        BatchProcess process = batchProcessRepository.save(BatchProcess.builder().startedAt(new Date()).build());

        StringReader reader = new StringReader("""
                001ç1234567891234çPedroç50000
                001ç3245678865434çPauloç40000.99
                002ç2345675434544345çJose da SilvaçRural
                002ç2345675433444345çEduardo PereiraçRural
                003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
                003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
                """);
        LineIterator it = new LineIterator(reader);
        databaseBatchProcessing.processFile("ç", it, process);
        assertEquals(salesmanRepository.countByBatchProcessId(process.getId()), 2);
    }

    @Test
    public void givenCorrectFileContentThenSalesShouldBeSaved() {
        BatchProcess process = batchProcessRepository.save(BatchProcess.builder().startedAt(new Date()).build());

        StringReader reader = new StringReader("""
                001ç1234567891234çPedroç50000
                001ç3245678865434çPauloç40000.99
                002ç2345675434544345çJose da SilvaçRural
                002ç2345675433444345çEduardo PereiraçRural
                003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
                003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
                """);
        LineIterator it = new LineIterator(reader);
        databaseBatchProcessing.processFile("ç", it, process);
        assertEquals(saleRepository.findByBatchProcessId(process.getId()).size(), 2);
    }


    @Test
    public void givenCustomerNameContainingTheFileDelimiterThenItShouldNotBeTruncated() {
        BatchProcess process = batchProcessRepository.save(BatchProcess.builder().startedAt(new Date()).build());

        StringReader reader = new StringReader("""
                001ç1234567891234çPedroç50000
                001ç3245678865434çPauloç40000.99
                002ç2345675434544345çJose da SilvaçRural
                002ç2345675987544345çPedro GonçalvesçRural
                002ç2345675433444345çEduardo PereiraçRural
                003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
                003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
                """);
        LineIterator it = new LineIterator(reader);
        databaseBatchProcessing.processFile("ç", it, process);

        Customer customer = customerRepository.findByNameAndBatchProcessId("Pedro Gonçalves", process.getId());
        assertNotNull(customer);
    }

    @Test
    public void givenCustomerNameContainingTheFileDelimiterThenNoErrorShouldBeThrown() {
        BatchProcess process = batchProcessRepository.save(BatchProcess.builder().startedAt(new Date()).build());

        StringReader reader = new StringReader("""
                001ç1234567891234çPedroç50000
                001ç3245678865434çPauloç40000.99
                002ç2345675434544345çJose da SilvaçRural
                002ç2345675987544345çPedro GonçalvesçRural
                002ç2345675433444345çEduardo PereiraçRural
                003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
                003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
                """);
        LineIterator it = new LineIterator(reader);
        databaseBatchProcessing.processFile("ç", it, process);
        assertDoesNotThrow(() -> Exception.class);
    }

    @Test
    public void givenSalesmanNameContainingTheFileDelimiterThenItShouldNotBeTruncated() {
        BatchProcess process = batchProcessRepository.save(BatchProcess.builder().startedAt(new Date()).build());

        StringReader reader = new StringReader("""
                001ç1234567891234çPedro Gonçalvesç50000
                001ç3245678865434çPauloç40000.99
                001ç9745670165474çGonçalvesç40000.98
                002ç2345675434544345çJose da SilvaçRural
                002ç2345675433444345çEduardo PereiraçRural
                003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
                003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
                """);
        LineIterator it = new LineIterator(reader);
        databaseBatchProcessing.processFile("ç", it, process);

        Salesman salesman = salesmanRepository.findByNameAndBatchProcessId("Pedro Gonçalves", process.getId());
        assertNotNull(salesman);
    }

    @Test
    public void givenSalesmanNameContainingTheFileDelimiterThenNoErrorShouldBeThrown() {
        BatchProcess process = batchProcessRepository.save(BatchProcess.builder().startedAt(new Date()).build());

        StringReader reader = new StringReader("""
                001ç1234567891234çPedro Gonçalvesç50000
                001ç3245678865434çPauloç40000.99
                001ç9745670165474çGonçalvesç40000.98
                002ç2345675434544345çJose da SilvaçRural
                002ç2345675433444345çEduardo PereiraçRural
                003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro
                003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo
                """);
        LineIterator it = new LineIterator(reader);
        databaseBatchProcessing.processFile("ç", it, process);

        assertDoesNotThrow(() -> Exception.class);
    }

    @Test
    public void givenAnEmptyFileThenNoErrorShouldBeThrown() {
        BatchProcess process = batchProcessRepository.save(BatchProcess.builder().startedAt(new Date()).build());

        StringReader reader = new StringReader("");
        LineIterator it = new LineIterator(reader);
        databaseBatchProcessing.processFile("ç", it, process);

        assertDoesNotThrow(() -> Exception.class);
    }
}
