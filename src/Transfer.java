class Transfer {
    private final Account senderAccount;
    private final Account receiverAccount;
    private final double amount;

    public Transfer(Account senderAccount, Account receiverAccount, double amount) {
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.amount = amount;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public Account getReceiverAccount() {
        return receiverAccount;
    }

    public double getAmount() {
        return amount;
    }
}