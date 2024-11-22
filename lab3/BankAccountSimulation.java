public class BankAccountSimulation {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(100); // 初始余额为100元

        // 创建取款和存款线程
        Thread withdrawThread1 = new Thread(new WithdrawTask(account), "Withdraw-1");
        Thread withdrawThread2 = new Thread(new WithdrawTask(account), "Withdraw-2");
        Thread withdrawThread3 = new Thread(new WithdrawTask(account), "Withdraw-3");

        Thread depositThread1 = new Thread(new DepositTask(account), "Deposit-1");
        Thread depositThread2 = new Thread(new DepositTask(account), "Deposit-2");

        // 启动线程
        withdrawThread1.start();
        withdrawThread2.start();
        withdrawThread3.start();
        depositThread1.start();
        depositThread2.start();
    }
}

class BankAccount {
    private int balance;

    public BankAccount(int balance) {
        this.balance = balance;
    }

    // 存款操作
    public void deposit(int amount) {
        synchronized (this) {
            if (amount > 0) {
                balance += amount;
                System.out.printf("%s deposited %d. Balance: %d\n", Thread.currentThread().getName(), amount, balance);
            }
        }
    }

    // 取款操作
    public void withdraw(int amount) {
        synchronized (this) {
            if (amount > 0 && balance >= amount) {
                balance -= amount;
                System.out.printf("%s withdrew %d. Balance: %d\n", Thread.currentThread().getName(), amount, balance);
            } else {
                System.out.printf("%s attempted to withdraw %d but failed. Balance: %d\n", Thread.currentThread().getName(), amount, balance);
            }
        }
    }
}

// 存款任务
class DepositTask implements Runnable {
    private final BankAccount account;

    public DepositTask(BankAccount account) {
        this.account = account;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) { // 每个存款线程存款5次，每次10元
            account.deposit(10);
            try {
                Thread.sleep(100); // 模拟存款操作需要一些时间
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

// 取款任务
class WithdrawTask implements Runnable {
    private final BankAccount account;

    public WithdrawTask(BankAccount account) {
        this.account = account;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) { // 每个取款线程取款5次，每次10元
            account.withdraw(10);
            try {
                Thread.sleep(100); // 模拟取款操作需要一些时间
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
