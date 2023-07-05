
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoneyTransferProgram {
    private static final String ACCOUNTS_FILE = "B:\\FinalProject\\accounts.txt";
    private static final String REPORT_FILE = "B:\\FinalProject\\report.txt";

    public void parseTransferFiles() throws IOException {
        List<Transfer> transfers = new ArrayList<>();

        List<File> transferFiles = findTransferFiles();

        for (File transferFile : transferFiles) {
            try {
                List<Account> accounts = readAccountsFromFile();

                Scanner scanner = new Scanner(transferFile);

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();

                    Pattern pattern = Pattern.compile("\\d{5}-\\d{5}\\|\\d{5}-\\d{5}\\|\\d+");
                    Matcher matcher = pattern.matcher(line);

                    if (matcher.matches()) {
                        String[] parts = line.split("\\|");

                        String senderCard = parts[0].trim();
                        String receiverCard = parts[1].trim();
                        double amount = Double.parseDouble(parts[2].trim());

                        Account senderAccount = findAccount(accounts, senderCard);
                        Account receiverAccount = findAccount(accounts, receiverCard);

                        if (senderAccount == null || receiverAccount == null) {
                            System.out.println("Счёт отправителя или получателя не найден: " + line);
                            continue;
                        }

                        if (senderAccount.getBalance() < amount) {
                            System.out.println("Недостаточно средств на счёте отправителя: " + line);
                            continue;
                        }

                        transfers.add(new Transfer(senderAccount, receiverAccount, amount));
                    }
                }

                scanner.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        updateAccountBalances(transfers);
        generateReport(transfers);
    }

    public void printAllTransfers() {
        try {
            File reportFile = new File(REPORT_FILE);

            if (!reportFile.exists()) {
                System.out.println("Файл с отчетом не найден.");
                return;
            }

            Scanner scanner = new Scanner(reportFile);

            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Account> readAccountsFromFile() throws IOException {
        List<Account> accounts = new ArrayList<>();

        File accountsFile = new File(ACCOUNTS_FILE);

        if (!accountsFile.exists()) {
            System.out.println("Файл с номерами счетов не найден.");
            return accounts;
        }

        BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length == 2) {
                String accountNumber = parts[0].trim();
                double balance = Double.parseDouble(parts[1].trim());
                accounts.add(new Account(accountNumber, balance));
            }
        }

        reader.close();

        return accounts;
    }

    private void updateAccountBalances(List<Transfer> transfers) throws IOException {
        List<Account> accounts = readAccountsFromFile();

        for (Transfer transfer : transfers) {
            Account senderAccount = transfer.getSenderAccount();
            Account receiverAccount = transfer.getReceiverAccount();
            double amount = transfer.getAmount();

            for (Account account : accounts) {
                if (account.getAccountNumber().equals(senderAccount.getAccountNumber())) {
                    account.decreaseBalance(amount);
                }
            }

            for (Account account : accounts) {
                if (account.getAccountNumber().equals(receiverAccount.getAccountNumber())) {
                    account.increaseBalance(amount);
                }
            }
        }

        PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE));

        for (Account account : accounts) {
            writer.println(account.getAccountNumber() + "|" + account.getBalance());
        }

        writer.close();
    }
    private List<File> findTransferFiles() {
        List<File> transferFiles = new ArrayList<>();

        String directoryPath = "B:\\FinalProject\\transfer_files";

        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));

            if (files != null) {
                Collections.addAll(transferFiles, files);
            }
        } else {
            System.out.println("Указанный путь не является директорией: " + directoryPath);
        }

        return transferFiles;
    }


    public void generateReport(List<Transfer> transfers) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(REPORT_FILE));

        writer.println("Отчет о переводах:");
        writer.println("-----------------");

        for (Transfer transfer : transfers) {
            writer.println("Счет отправителя: " + transfer.getSenderAccount().getAccountNumber());
            writer.println("Счет получателя: " + transfer.getReceiverAccount().getAccountNumber());
            writer.println("Сумма перевода: " + transfer.getAmount());
            writer.println("-----------------");
        }

        writer.close();
    }

    private Account findAccount(List<Account> accounts, String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
}