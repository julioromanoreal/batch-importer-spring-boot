package com.julioromano.batchimporterspringboot.processing.sales.utils;

import com.julioromano.batchimporterspringboot.entities.*;
import com.julioromano.batchimporterspringboot.processing.sales.SalesBatchResult;
import com.julioromano.batchimporterspringboot.processing.sales.SalesBatchType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class SalesBatchUtils {

    public String getWorstSalesman(List<Sale> sales) {
        Map<String, BigDecimal> amountBySalesman = sales.stream().collect(Collectors.toMap(Sale::getSalesmanName, sale -> {
            BigDecimal salePrice = BigDecimal.ZERO;
            for (SaleItem saleItem : sale.getItems()) {
                salePrice = salePrice.add(saleItem.getPrice().multiply(BigDecimal.valueOf(saleItem.getQuantity())));
            }

            return salePrice;
        }, BigDecimal::add));
        return amountBySalesman.keySet().stream().min(Comparator.comparing(amountBySalesman::get)).orElse("");
    }

    public String getMostExpensiveSale(List<Sale> sales) {
        return sales.stream().max(Comparator.comparing(sale -> {
            BigDecimal salePrice = BigDecimal.ZERO;
            for (SaleItem saleItem : sale.getItems()) {
                salePrice = salePrice.add(saleItem.getPrice().multiply(BigDecimal.valueOf(saleItem.getQuantity())));
            }

            return salePrice;
        })).map(Sale::getSaleId).orElse("");
    }

    public String getFormattedOutput(SalesBatchResult salesBatchResult) {
        String outputTpl = """
                Clientes: %s
                Vendedores: %s
                Venda mais cara: %s
                Pior vendedor: %s
                """;
        return String.format(outputTpl, salesBatchResult.getNumberOfCustomers(), salesBatchResult.getNumberOfSalesman(), salesBatchResult.getMostExpensiveSale(), salesBatchResult.getWorstSalesman());
    }

    public Sale handleSaleData(String fileDelimiter, String line) {
        String regexSplitter = SalesBatchType.SALE.getRegexSplitter().replaceAll("DEL", fileDelimiter);
        Pattern p = Pattern.compile(regexSplitter);
        Matcher m = p.matcher(line);
        if (m.matches()) {
            String saleId = m.group(2);
            String items = m.group(3);
            String salesmanName = m.group(4);

            return Sale.builder()
                    .saleId(saleId).salesmanName(salesmanName).items(items).build();
        }

        return null;
    }

    public Customer handleCustomerData(String fileDelimiter, String line) {
        String regexSplitter = SalesBatchType.CUSTOMER.getRegexSplitter().replaceAll("DEL", fileDelimiter);
        Pattern p = Pattern.compile(regexSplitter);
        Matcher m = p.matcher(line);
        if (m.matches()) {
            Long cnpj = Long.parseLong(m.group(2));
            String name = m.group(3);
            String businessArea = m.group(4);

            return Customer.builder()
                    .cnpj(cnpj).name(name).businessArea(businessArea).build();
        }

        return  null;
    }

    public Salesman handleSalesmanData(String fileDelimiter, String line) {
        String regexSplitter = SalesBatchType.SALESMAN.getRegexSplitter().replaceAll("DEL", fileDelimiter);
        Pattern p = Pattern.compile(regexSplitter);
        Matcher m = p.matcher(line);
        if (m.matches()) {
            Long cpf = Long.parseLong(m.group(2));
            String name = m.group(3);
            BigDecimal salary = new BigDecimal(m.group(4));

            return Salesman.builder()
                    .cpf(cpf).name(name).salary(salary).build();
        }

        return null;
    }

}
