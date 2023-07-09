# BankProject
The code you provided is a Java program that implements a money transfer program. It reads transfer files, updates account balances, and generates a report.

Here are explanations for some important parts of the code:

1. `parseTransferFiles()` method: 
   - This method is responsible for parsing the transfer files and performing the money transfers.
   - It reads the account information from the "accounts.txt" file using the `readAccountsFromFile()` method.
   - It then iterates over the transfer files found in the "transfer_files" directory.
   - For each transfer file, it reads the contents line by line and checks if the line matches the pattern of "senderCard|receiverCard|amount".
   - If a line matches, it extracts the sender card, receiver card, and transfer amount.
   - It finds the respective sender and receiver accounts from the list of accounts obtained from the "accounts.txt" file.
   - It creates a new `Transfer` object and adds it to the `transfers` list.
   - Finally, it calls the `updateAccountBalances()` method to perform the actual money transfers and the `generateReport()` method to create a report.
   
2. `updateAccountBalances()` method:
   - This method updates the account balances based on the transfers performed.
   - It takes the `transfers` list as input.
   - For each transfer in the list, it subtracts the transfer amount from the sender's account balance and adds it to the receiver's account balance.
   - If any account has insufficient funds, it throws an `InsufficientFundsException`.
   - If the transfer amount is negative, it throws a `NegativeAmountException`.

3. `generateReport()` method:
   - This method generates a report file containing information about the transfers made.
   - It takes the `transfers` list as input.
   - It creates a new file named "report.txt" in the specified directory.
   - It writes the transfer details into the report file, including the sender's card, receiver's card, and transfer amount.
   
4. `printAllTransfers()` method:
   - This method prints all the transfers made by reading from the generated report file.
   - It reads the lines from the "report.txt" file and prints them to the console.

5. `readAccountsFromFile()` method:
   - This method reads the account information from the "accounts.txt" file.
   - It creates a list of `Account` objects.
   - It reads each line from the file, splits it using the "|" delimiter, and extracts the account number and balance.
   - It creates a new `Account` object with the extracted information and adds it to the accounts list.

Note:
- The paths specified in the code are hardcoded, so make sure to change them if needed.
- The code refers to some custom exception classes (`InsufficientFundsException` and `NegativeAmountException`) that are not present in the provided code. You may need to implement or import them separately.
  
# Files required for the program to work
When working with the program, you need to use several files:

1. accounts.txt - file with information about accounts. The format of each line in the file is "account number|balance". Example string: "12345-67890|1000.0".

2. //transferfiles// - directory containing files with information about transfers. Format of each file: each line contains information about the transfer in the format "sender|recipient|amount". Example string: "12345-67890|98765-43210|500.0". The files in the directory must have the .txt extension.

3. report.txt - the file where the translation report will be written. This file will be created automatically by the program.

# Please note that the paths to the accounts.txt, report.txt files and the transfer_files directory are hardcoded in the program code. Before running the program, make sure that the paths are correct and match the structure of your file system. 
![Безымянный](https://github.com/Fuzzylolpro/BankProject/assets/132467383/141ead5d-a20a-4a61-8a1f-9d79009dc1c7)


