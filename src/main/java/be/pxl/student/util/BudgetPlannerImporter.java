package be.pxl.student.util;

import be.pxl.student.entity.Account;
import be.pxl.student.entity.Payment;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Util class to import csv file
 */
public class BudgetPlannerImporter {
    protected static final Logger logger = LogManager.getLogger("BudgetPlannerImporter");

    private String _fileName;

    public BudgetPlannerImporter(String fileName) {
        _fileName = fileName;
    }

    public ArrayList<Account> loadAccounts() throws IOException {
        var accountList = new ArrayList<Account>();

        try (var reader = Files.newBufferedReader(Paths.get(_fileName))) {
            String line;

            // Skip header row.
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                try {
                    var lineSplit = line.split(",");

                    if (!lineSplit[1].startsWith("BE")) {
                        continue;
                    }

                    var existingAccount = accountList.stream().filter(account -> account.getIBAN().equals(lineSplit[1])).findFirst().orElse(null);

                    if (existingAccount == null) {
                        existingAccount = new Account();
                        existingAccount.setName(lineSplit[0]);
                        existingAccount.setIBAN(lineSplit[1]);
                        existingAccount.setPayments(new ArrayList<Payment>());

                        accountList.add(existingAccount);
                    }

                    var fmt = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                    var payment = new Payment(LocalDateTime.parse(lineSplit[3], fmt), Float.parseFloat(lineSplit[4]), lineSplit[5], lineSplit[6]);

                    existingAccount.getPayments().add(payment);
                } catch (Exception e) {
                    logger.error("Failed parsing a line: " + e.getMessage());
                }
            }
        }

        return accountList;
    }
}
