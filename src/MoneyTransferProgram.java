import Exeption.InsufficientFundsException;
import Exeption.NegativeAmountException;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoneyTransferProgram {
    private static final String ACCOUNTS_FILE = "C:\\Users\\Кит\\IdeaProjects\\BankProject\\accounts.txt";
    private static final String REPORT_FILE = "C:\\Users\\Кит\\IdeaProjects\\BankProject\\report.txt";
    private static final String directoryPath = "C:\\Users\\Кит\\IdeaProjects\\BankProject\\transfer_files\\";

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
            writer.println(LocalDateTime.now());
            writer.println("-----------------");
            try {
                processTransfer(transfer);
                writer.println("Статус: успешно обработан");
            } catch (Exception e) {
                writer.println("Статус: ошибка во время обработки - " + e.getMessage());
            }
            writer.println("-----------------");
        }
        writer.close();
    }

    private void processTransfer(Transfer transfer) throws InsufficientFundsException, NegativeAmountException {
        Account senderAccount = transfer.getSenderAccount();
        double amount = transfer.getAmount();
        if (senderAccount.getBalance() < amount) {
            throw new InsufficientFundsException("Недостаточно средств на счете отправителя: " + senderAccount.getAccountNumber());
        }
        if (amount < 0) {
            throw new NegativeAmountException("Отрицательная сумма отправки: " + amount);
        }
        if (amount == 0) {
            throw new NegativeAmountException("Сумма отправки равна : " + amount);
        }
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