import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        MoneyTransferProgram program = new MoneyTransferProgram();

        System.out.println("Выберите операцию:");
        System.out.println("1 - Парсинг файлов перевода");
        System.out.println("2 - Вывод списка всех переводов");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> program.parseTransferFiles();
            case 2 -> program.printAllTransfers();
            default -> System.out.println("Некорректный выбор операции");
        }

    }
}
