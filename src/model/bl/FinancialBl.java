package model.bl;

import controller.exceptions.NoFinancialFoundException;
import model.da.FinancialDa;
import model.entity.Financial;

import java.util.List;

public class FinancialBl {
    public static Financial save(Financial financial) throws Exception {
        try (FinancialDa financialDa = new FinancialDa()) {
            financialDa.save(financial);
            return financial;
        }
    }

    public static Financial edit(Financial financial) throws Exception {
        try (FinancialDa financialDa = new FinancialDa()) {
            if (financialDa.findById(financial.getId()) != null) {
                financialDa.edit(financial);
                return financial;
            } else {
                throw new NoFinancialFoundException();
            }
        }
    }

    public static Financial remove(int id) throws Exception {
        try (FinancialDa financialDa = new FinancialDa()) {
            Financial financial = financialDa.findById(id);
            if (financial != null) {
                financialDa.remove(id);
                return financial;
            } else {
                throw new NoFinancialFoundException();
            }
        }
    }

    public static List<Financial> findAll() throws Exception {
        try (FinancialDa financialDa = new FinancialDa()) {
            List<Financial> financialList = financialDa.findAll();
            if (!financialList.isEmpty()) {
                return financialList;
            } else {
                throw new NoFinancialFoundException();
            }
        }
    }

    public static Financial findById(int id) throws Exception {
        try (FinancialDa financialDa = new FinancialDa()) {
            Financial financial = financialDa.findById(id);
            if (financial != null) {
                return financial;
            } else {
                throw new NoFinancialFoundException();
            }
        }
    }

    public static List<Financial> findByFType(String fType) throws Exception {
        try (FinancialDa financialDa = new FinancialDa()) {
            List<Financial> financialList = financialDa.findByFType(fType);
            if (!financialList.isEmpty()) {
                return financialList;
            } else {
                throw new NoFinancialFoundException();
            }
        }
    }


}
