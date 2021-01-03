package com.julioromano.batchimporterspringboot;


import com.julioromano.batchimporterspringboot.entities.Customer;
import com.julioromano.batchimporterspringboot.entities.Sale;
import com.julioromano.batchimporterspringboot.entities.Salesman;
import com.julioromano.batchimporterspringboot.processing.sales.SalesBatchResult;
import com.julioromano.batchimporterspringboot.processing.sales.utils.SalesBatchUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SalesBatchUtilsTest {

    @Autowired
    private SalesBatchUtils salesBatchUtils;

    @Test
    void givenEmptyCustomerLineThenNoErrorShouldBeThrown() {
        Customer parsed = salesBatchUtils.handleCustomerData("ç", "");
        assertDoesNotThrow(() -> Exception.class);
    }

    @Test
    void givenCorrectLineThenCustomerShouldBeReturned() {
        Customer parsed = salesBatchUtils.handleCustomerData("ç", "002ç2345675434544345çJose da SilvaçRural");
        Customer expected = Customer.builder().cnpj(2345675434544345L).name("Jose da Silva").businessArea("Rural").build();
        assertEquals(parsed, expected);
    }

    @Test
    void givenCustomerNameContainingTheFileDelimiterThenItsNameShouldNotBeTruncated() {
        Customer parsed = salesBatchUtils.handleCustomerData("ç", "002ç2345675434544345çJose da Silva GonçalvesçRural");
        Customer expected = Customer.builder().cnpj(2345675434544345L).name("Jose da Silva Gonçalves").businessArea("Rural").build();
        assertEquals(parsed, expected);
    }

    @Test
    void givenCustomerNameContainingTheFileDelimiterThenNoErrorShouldBeThrown() {
        salesBatchUtils.handleCustomerData("ç", "002ç2345675434544345çJose da Silva GonçalvesçRural");
        assertDoesNotThrow(() -> Exception.class);
    }

    @Test
    void givenEmptySalesmanLineThenNoErrorShouldBeThrown() {
        salesBatchUtils.handleSalesmanData("ç", "001ç1234567891234çPedroç50000");
        assertDoesNotThrow(() -> Exception.class);
    }

    @Test
    void givenCorrectLineThenSalesmanShouldBeReturned() {
        Salesman parsed = salesBatchUtils.handleSalesmanData("ç", "001ç1234567891234çPedroç50000");
        Salesman expected = Salesman.builder().cpf(1234567891234L).name("Pedro").salary(new BigDecimal("50000")).build();
        assertEquals(parsed, expected);
    }

    @Test
    void givenSalesmanNameContainingTheFileDelimiterThenItsNameShouldNotBeTruncated() {
        Salesman parsed = salesBatchUtils.handleSalesmanData("ç", "001ç1234567891234çPedro Gonçalvesç50000");
        Salesman expected = Salesman.builder().cpf(1234567891234L).name("Pedro Gonçalves").salary(new BigDecimal("50000")).build();
        assertEquals(parsed, expected);
    }

    @Test
    void givenSalesmanNameContainingTheFileDelimiterThenNoErrorShouldBeThrown() {
        salesBatchUtils.handleSalesmanData("ç", "001ç1234567891234çPedro Gonçalvesç50000");
        assertDoesNotThrow(() -> Exception.class);
    }

    @Test
    void givenEmptySaleLineThenNoErrorShouldBeThrown() {
        salesBatchUtils.handleSaleData("ç", "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro");
        assertDoesNotThrow(() -> Exception.class);
    }

    @Test
    void givenCorrectLineThenSaleShouldBeReturned() {
        Sale parsed = salesBatchUtils.handleSaleData("ç", "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro");
        Sale expected = Sale.builder().saleId("10").salesmanName("Pedro").items("[1-10-100,2-30-2.50,3-40-3.10]").build();
        assertEquals(parsed, expected);
    }

    @Test
    void givenSalesmanNameInSaleContainingTheFileDelimiterThenItsNameShouldNotBeTruncated() {
        Sale parsed = salesBatchUtils.handleSaleData("ç", "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro Gonçalves");
        Sale expected = Sale.builder().saleId("10").salesmanName("Pedro Gonçalves").items("[1-10-100,2-30-2.50,3-40-3.10]").build();
        assertEquals(parsed, expected);
    }

    @Test
    void givenSalesmanNameInSaleContainingTheFileDelimiterThenNoErrorShouldBeThrown() {
        salesBatchUtils.handleSaleData("ç", "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro Gonçalves");
        assertDoesNotThrow(() -> Exception.class);
    }

    @Test
    void givenFileContentThenCorrectResultShouldBeReturned() {
        String output = salesBatchUtils.getFormattedOutput(new SalesBatchResult(2, 2, "10", "Paulo"));
        assertEquals(output, """
                Clientes: 2
                Vendedores: 2
                Venda mais cara: 10
                Pior vendedor: Paulo
                """);
    }

    @Test
    void givenSaleListThenGetTheCorrectWorstSalesman() {
        String worstSalesman = salesBatchUtils.getWorstSalesman(
                List.of(
                        Sale.builder().saleId("10").items("[1-10-100,2-30-2.50,3-40-3.10]").salesmanName("Pedro").build(),
                        Sale.builder().saleId("08").items("[1-34-10,2-33-1.50,3-40-0.10]").salesmanName("Paulo").build()
                )
        );
        assertEquals(worstSalesman, "Paulo");
    }

    @Test
    void givenSaleListThenGetTheCorrectMostExpensiveSale() {
        String mostExpensiveSale = salesBatchUtils.getMostExpensiveSale(
                List.of(
                        Sale.builder().saleId("10").items("[1-10-100,2-30-2.50,3-40-3.10]").salesmanName("Pedro").build(),
                        Sale.builder().saleId("08").items("[1-34-10,2-33-1.50,3-40-0.10]").salesmanName("Paulo").build()
                )
        );
        assertEquals(mostExpensiveSale, "10");
    }

}
