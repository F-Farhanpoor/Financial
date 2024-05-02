package controller.exceptions;

public class NoFinancialFoundException extends Exception{
    public NoFinancialFoundException(){
        super("No Record Found!!!");
    }
}