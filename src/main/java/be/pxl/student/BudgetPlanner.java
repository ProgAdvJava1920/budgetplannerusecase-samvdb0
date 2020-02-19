package be.pxl.student;

import be.pxl.student.entity.Account;
import be.pxl.student.util.BudgetPlannerImporter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class BudgetPlanner {
    protected static final Logger logger = LogManager.getLogger("BudgetPlanner");

    public static void main(String[] args) throws IOException {
        var importer = new BudgetPlannerImporter("src/main/resources/account_payments.csv");
        var accounts = importer.loadAccounts();

        for (var account : accounts) {
            logger.info(account.toString());
        }
    }
}
