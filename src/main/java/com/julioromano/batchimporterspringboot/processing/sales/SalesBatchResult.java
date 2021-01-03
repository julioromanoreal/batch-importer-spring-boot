package com.julioromano.batchimporterspringboot.processing.sales;

import com.julioromano.batchimporterspringboot.processing.BatchResult;

public class SalesBatchResult extends BatchResult {
    private final int numberOfCustomers;
    private final int numberOfSalesman;
    private final String mostExpensiveSale;
    private final String worstSalesman;

    public SalesBatchResult(int numberOfCustomers, int numberOfSalesman, String mostExpensiveSale, String worstSalesman) {
        this.numberOfCustomers = numberOfCustomers;
        this.numberOfSalesman = numberOfSalesman;
        this.mostExpensiveSale = mostExpensiveSale;
        this.worstSalesman = worstSalesman;
    }

    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    public int getNumberOfSalesman() {
        return numberOfSalesman;
    }

    public String getMostExpensiveSale() {
        return mostExpensiveSale;
    }

    public String getWorstSalesman() {
        return worstSalesman;
    }
}
