import java.time.LocalDate;
import java.lang.String;
import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


class User {

    // Attributes
    private int userId;
    private String name;
    private String email;
    private String passwordHash;
    private String currency;

    // Relationships
    private List<Transaction> transactions;   // records (0..*)
    private List<Notification> notifications; // receives (0..*)
    private List<Budget> budgets;             // manages (0..*)
    private List<Report> reports;             // generates (0..*)
    private List<Goal> goals;                 // has (0..*)

    // Constructor
    public User(int userId, String name, String email, String passwordHash, String currency) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.currency = currency;
        this.transactions = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.budgets = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.goals = new ArrayList<>();
    }

    // Methods
    public void register() {
        System.out.println("User registered: " + this.email);
    }

    public boolean login(String passwordHash) {
        if (this.passwordHash.equals(passwordHash)) {
            System.out.println("User logged in: " + this.email);
            return true;
        }
        System.out.println("Login failed for: " + this.email);
        return false;
    }

    public void updateProfile(String newName, String newEmail, String newCurrency) {
        this.name = newName;
        this.email = newEmail;
        this.currency = newCurrency;
        System.out.println("Profile updated for user: " + this.userId);
    }

    // Relationship helpers
    public void addTransaction(Transaction transaction) { this.transactions.add(transaction); }
    public void removeTransaction(Transaction transaction) { this.transactions.remove(transaction); }

    public void addNotification(Notification notification) { this.notifications.add(notification); }

    public void addBudget(Budget budget) { this.budgets.add(budget); }
    public void removeBudget(Budget budget) { this.budgets.remove(budget); }

    public void addReport(Report report) { this.reports.add(report); }

    public void addGoal(Goal goal) { this.goals.add(goal); }
    public void removeGoal(Goal goal) { this.goals.remove(goal); }

    // Getters
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getCurrency() { return currency; }
    public List<Transaction> getTransactions() { return transactions; }
    public List<Notification> getNotifications() { return notifications; }
    public List<Budget> getBudgets() { return budgets; }
    public List<Report> getReports() { return reports; }
    public List<Goal> getGoals() { return goals; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setCurrency(String currency) { this.currency = currency; }

    @Override
    public String toString() {
        // passwordHash intentionally excluded for security
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", currency='" + currency + '\'' +
                ", transactions=" + transactions.size() +
                ", notifications=" + notifications.size() +
                ", budgets=" + budgets.size() +
                ", reports=" + reports.size() +
                ", goals=" + goals.size() +
                '}';
    }
}


class Category {

    // Attributes
    private int categoryId;
    private String name;
    private String type;

    // Relationships
    private List<Transaction> transactions; // one Category has many Transactions

    // Constructor
    public Category(int categoryId, String name, String type) {
        this.categoryId = categoryId;
        this.name = name;
        this.type = type;
        this.transactions = new ArrayList<>();
    }

    // Relationship methods
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        System.out.println("Transaction added to category: " + this.name);
    }

    public void removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        System.out.println("Transaction removed from category: " + this.name);
    }

    // Getters
    public int getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public List<Transaction> getTransactions() { return transactions; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", transactions=" + transactions.size() +
                '}';
    }
}

class Transaction {

    // Attributes
    private int transactionId;
    private String type;
    private BigDecimal amount;
    private LocalDate date;
    private String description;

    // Relationships
    private User user;         // belongs to a User (records)
    private Category category; // belongs to a Category

    // Constructor
    public Transaction(int transactionId, String type, BigDecimal amount,
                       LocalDate date, String description, User user, Category category) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.user = user;
        this.category = category;
    }

    // Methods
    public void addTransaction() {
        System.out.println("Transaction added: [" + type + "] " + amount + " on " + date);
    }

    public void deleteTransaction() {
        System.out.println("Transaction deleted: ID " + transactionId);
    }

    // Getters
    public int getTransactionId() {
        return transactionId;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public User getUser() {
        return user;
    }

    public Category getCategory() {
        return category;
    }

    // Setters
    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", user=" + (user != null ? user.getUserId() : "null") +
                ", category=" + (category != null ? category.getName() : "null") +
                '}';
    }
}
    class Budget {

    // Attributes
    private int budgetId;
    private String period;
    private BigDecimal spentAmount;
    private BigDecimal totalExpense;

    // Relationships
    private User user;                     // managed by a User
    private List<BudgetAlert> alerts;      // generates BudgetAlerts
    private BudgetStatus status;           // has status (enum)

    // Constructor
    public Budget(int budgetId, String period, BigDecimal totalExpense, User user) {
        this.budgetId = budgetId;
        this.period = period;
        this.spentAmount = BigDecimal.ZERO;
        this.totalExpense = totalExpense;
        this.user = user;
        this.alerts = new ArrayList<>();
        this.status = BudgetStatus.ON_TRACK;
    }

    // Methods
    public void checkLimit() {
        BigDecimal remaining = totalExpense.subtract(spentAmount);
        BigDecimal nearLimitThreshold = totalExpense.multiply(BigDecimal.valueOf(0.10));

        if (spentAmount.compareTo(totalExpense) >= 0) {
            this.status = BudgetStatus.EXCEEDED;
            System.out.println("Budget EXCEEDED for period: " + period);
            BudgetAlert alert = new BudgetAlert(alerts.size() + 1,
                    "Budget exceeded for period: " + period, this, NotificationType.BUDGET_EXCEEDED);
            alerts.add(alert);
        } else if (remaining.compareTo(nearLimitThreshold) <= 0) {
            this.status = BudgetStatus.NEAR_LIMIT;
            System.out.println("Budget NEAR LIMIT for period: " + period + ". Remaining: " + remaining);
            BudgetAlert alert = new BudgetAlert(alerts.size() + 1,
                    "Budget near limit for period: " + period, this, NotificationType.BUDGET_NEAR_LIMIT);
            alerts.add(alert);
        } else {
            this.status = BudgetStatus.ON_TRACK;
            System.out.println("Budget ON TRACK. Remaining: " + remaining);
        }
    }

    public void addSpending(BigDecimal amount) {
        this.spentAmount = this.spentAmount.add(amount);
        checkLimit();
    }

    // Getters
    public int getBudgetId() { return budgetId; }
    public String getPeriod() { return period; }
    public BigDecimal getSpentAmount() { return spentAmount; }
    public BigDecimal getTotalExpense() { return totalExpense; }
    public User getUser() { return user; }
    public List<BudgetAlert> getAlerts() { return alerts; }
    public BudgetStatus getStatus() { return status; }

    // Setters
    public void setPeriod(String period) { this.period = period; }
    public void setSpentAmount(BigDecimal spentAmount) { this.spentAmount = spentAmount; }
    public void setTotalExpense(BigDecimal totalExpense) { this.totalExpense = totalExpense; }
    public void setUser(User user) { this.user = user; }
    public void setStatus(BudgetStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Budget{" +
                "budgetId=" + budgetId +
                ", period='" + period + '\'' +
                ", spentAmount=" + spentAmount +
                ", totalExpense=" + totalExpense +
                ", status=" + status +
                ", userId=" + (user != null ? user.getUserId() : "null") +
                ", alerts=" + alerts.size() +
                '}';
    }
}
class Report {

    // Attributes
    private int reportId;
    private String period;
    private BigDecimal totalExpense;

    // Relationships
    private User user;                      // generated by a User
    private List<Transaction> transactions; // summarizes transactions (0..*)

    // Constructor
    public Report(int reportId, String period, User user) {
        this.reportId = reportId;
        this.period = period;
        this.user = user;
        this.transactions = new ArrayList<>();
        this.totalExpense = BigDecimal.ZERO;
    }

    // Relationship helpers
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        this.totalExpense = this.totalExpense.add(transaction.getAmount());
    }

    public void removeTransaction(Transaction transaction) {
        if (this.transactions.remove(transaction)) {
            this.totalExpense = this.totalExpense.subtract(transaction.getAmount());
        }
    }

    // Methods
    public void generateReport() {
        System.out.println("=== Report for period: " + period + " ===");
        System.out.println("User         : " + (user != null ? user.getName() : "N/A"));
        System.out.println("Transactions : " + transactions.size());
        System.out.println("Total Expense: " + totalExpense + " " +
                (user != null ? user.getCurrency() : ""));
        System.out.println("=====================================");
    }

    public String getReportSummary() {
        return "Period: " + period +
                " | User: " + (user != null ? user.getName() : "N/A") +
                " | Transactions: " + transactions.size() +
                " | Total: " + totalExpense +
                " " + (user != null ? user.getCurrency() : "");
    }

    public String exportToCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("transactionId,type,amount,date,description,category\n");
        for (Transaction t : transactions) {
            sb.append(t.getTransactionId()).append(",")
                    .append(t.getType()).append(",")
                    .append(t.getAmount()).append(",")
                    .append(t.getDate()).append(",")
                    .append(t.getDescription()).append(",")
                    .append(t.getCategory() != null ? t.getCategory().getName() : "").append("\n");
        }
        System.out.println("CSV export generated for report: " + reportId);
        return sb.toString();
    }

    // Getters
    public int getReportId() { return reportId; }
    public String getPeriod() { return period; }
    public BigDecimal getTotalExpense() { return totalExpense; }
    public User getUser() { return user; }
    public List<Transaction> getTransactions() { return transactions; }

    // Setters
    public void setPeriod(String period) { this.period = period; }
    public void setTotalExpense(BigDecimal totalExpense) { this.totalExpense = totalExpense; }
    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        // userId used instead of full User object to avoid circular references
        // transactions shown as count to avoid recursive print loops
        return "Report{" +
                "reportId=" + reportId +
                ", period='" + period + '\'' +
                ", totalExpense=" + totalExpense +
                ", transactionCount=" + transactions.size() +
                ", userId=" + (user != null ? user.getUserId() : "null") +
                '}';
    }
}


class Goal {

        // Attributes
        private int goalId;
        private String name;
        private BigDecimal targetAmount;
        private BigDecimal currentAmount;
        private LocalDate deadline;

        // Relationships
        private User user;         // has a User
        private GoalStatus status; // has status (enum)

        // Constructor
        public Goal(int goalId, String name, BigDecimal targetAmount,
                    LocalDate deadline, User user) {
            this.goalId = goalId;
            this.name = name;
            this.targetAmount = targetAmount;
            this.currentAmount = BigDecimal.ZERO;
            this.deadline = deadline;
            this.user = user;
            this.status = GoalStatus.IN_PROGRESS;
        }

        // Methods
        public void updateProgress(BigDecimal contribution) {
            this.currentAmount = this.currentAmount.add(contribution);
            System.out.println("Progress updated: " + currentAmount + " / " + targetAmount);

            if (currentAmount.compareTo(targetAmount) >= 0) {
                this.status = GoalStatus.COMPLETED;
                System.out.println("Goal '" + name + "' completed!");
            } else if (LocalDate.now().isAfter(deadline)) {
                this.status = GoalStatus.CANCELLED;
                System.out.println("Goal '" + name + "' passed deadline and was cancelled.");
            } else {
                BigDecimal remaining = targetAmount.subtract(currentAmount);
                System.out.println("Remaining to reach goal: " + remaining);
            }
        }

        public BigDecimal getProgressPercentage() {
            if (targetAmount.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
            return currentAmount.multiply(BigDecimal.valueOf(100))
                    .divide(targetAmount, 2, java.math.RoundingMode.HALF_UP);
        }

        // Getters
        public int getGoalId() { return goalId; }
        public String getName() { return name; }
        public BigDecimal getTargetAmount() { return targetAmount; }
        public BigDecimal getCurrentAmount() { return currentAmount; }
        public LocalDate getDeadline() { return deadline; }
        public User getUser() { return user; }
        public GoalStatus getStatus() { return status; }

        // Setters
        public void setName(String name) { this.name = name; }
        public void setTargetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; }
        public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
        public void setUser(User user) { this.user = user; }
        public void setStatus(GoalStatus status) { this.status = status; }

        @Override
        public String toString() {
            return "Goal{" +
                    "goalId=" + goalId +
                    ", name='" + name + '\'' +
                    ", targetAmount=" + targetAmount +
                    ", currentAmount=" + currentAmount +
                    ", deadline=" + deadline +
                    ", status=" + status +
                    ", userId=" + (user != null ? user.getUserId() : "null") +
                    '}';
        }
    }
    class Notification {

    // Attributes
    private int notifId;
    private String message;
    private boolean isRead;
    private LocalDateTime timestamp;

    // Relationships
    private User user;                 // received by a User
    private NotificationType type;     // categorized by NotificationType enum

    // Constructor
    public Notification(int notifId, String message, User user, NotificationType type) {
        this.notifId = notifId;
        this.message = message;
        this.isRead = false;
        this.timestamp = LocalDateTime.now();
        this.user = user;
        this.type = type;
    }

    // Mark as read
    public void markAsRead() {
        this.isRead = true;
        System.out.println("Notification " + notifId + " marked as read.");
    }

    // Getters
    public int getNotifId() { return notifId; }
    public String getMessage() { return message; }
    public boolean isRead() { return isRead; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public User getUser() { return user; }
    public NotificationType getType() { return type; }

    // Setters
    public void setMessage(String message) { this.message = message; }
    public void setRead(boolean read) { isRead = read; }
    public void setUser(User user) { this.user = user; }
    public void setType(NotificationType type) { this.type = type; }

    @Override
    public String toString() {
        return "Notification{" +
                "notifId=" + notifId +
                ", message='" + message + '\'' +
                ", type=" + type +
                ", isRead=" + isRead +
                ", timestamp=" + timestamp +
                ", userId=" + (user != null ? user.getUserId() : "null") +
                '}';
    }
}
class BudgetAlert {

    // Attributes
    private int alertId;
    private String message;
    private LocalDateTime triggeredAt;

    // Relationships
    private Budget budget;             // generated by a Budget
    private NotificationType notificationType; // categorized by NotificationType enum

    // Constructor
    public BudgetAlert(int alertId, String message, Budget budget, NotificationType notificationType) {
        this.alertId = alertId;
        this.message = message;
        this.triggeredAt = LocalDateTime.now();
        this.budget = budget;
        this.notificationType = notificationType;
    }

    // Getters
    public int getAlertId() { return alertId; }
    public String getMessage() { return message; }
    public LocalDateTime getTriggeredAt() { return triggeredAt; }
    public Budget getBudget() { return budget; }
    public NotificationType getNotificationType() { return notificationType; }

    // Setters
    public void setMessage(String message) { this.message = message; }
    public void setBudget(Budget budget) { this.budget = budget; }
    public void setNotificationType(NotificationType notificationType) { this.notificationType = notificationType; }

    @Override
    public String toString() {
        return "BudgetAlert{" +
                "alertId=" + alertId +
                ", message='" + message + '\'' +
                ", notificationType=" + notificationType +
                ", triggeredAt=" + triggeredAt +
                ", budgetId=" + (budget != null ? budget.getBudgetId() : "null") +
                '}';
    }
}
