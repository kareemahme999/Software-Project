package com.example.software_project;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

// ─────────────────────────────────────────────
//  ENUMS
// ─────────────────────────────────────────────

enum GoalStatus         { IN_PROGRESS, COMPLETED, CANCELLED }
enum NotificationType   { BUDGET_NEAR_LIMIT, BUDGET_EXCEEDED, GOAL_COMPLETED }
enum BudgetStatus       { ON_TRACK, NEAR_LIMIT, EXCEEDED }

// ─────────────────────────────────────────────
//  USER
// ─────────────────────────────────────────────

class User {

    private int userId;
    private String name;
    private String email;
    private String passwordHash;
    private String currency;

    private List<Transaction>  transactions  = new ArrayList<>();
    private List<Notification> notifications = new ArrayList<>();
    private List<Budget>       budgets       = new ArrayList<>();
    private List<Report>       reports       = new ArrayList<>();
    private List<Goal>         goals         = new ArrayList<>();

    public User(int userId, String name, String email, String passwordHash, String currency) {
        this.userId       = userId;
        this.name         = name;
        this.email        = email;
        this.passwordHash = passwordHash;
        this.currency     = currency;
    }

    public void register()                                           { System.out.println("User registered: " + email); }
    public boolean login(String password)                           { return this.passwordHash.equals(password); }
    public void updateProfile(String n, String e, String c)         { name = n; email = e; currency = c; }

    public void addTransaction(Transaction t)    { transactions.add(t); }
    public void removeTransaction(Transaction t) { transactions.remove(t); }
    public void addNotification(Notification n)  { notifications.add(n); }
    public void addBudget(Budget b)              { budgets.add(b); }
    public void removeBudget(Budget b)           { budgets.remove(b); }
    public void addReport(Report r)              { reports.add(r); }
    public void addGoal(Goal g)                  { goals.add(g); }
    public void removeGoal(Goal g)               { goals.remove(g); }

    // Getters
    public int               getUserId()       { return userId; }
    public String            getName()         { return name; }
    public String            getEmail()        { return email; }
    public String            getPasswordHash() { return passwordHash; }
    public String            getCurrency()     { return currency; }
    public List<Transaction> getTransactions() { return transactions; }
    public List<Notification>getNotifications(){ return notifications; }
    public List<Budget>      getBudgets()      { return budgets; }
    public List<Report>      getReports()      { return reports; }
    public List<Goal>        getGoals()        { return goals; }

    // Setters
    public void setName(String n)         { name = n; }
    public void setEmail(String e)        { email = e; }
    public void setPasswordHash(String p) { passwordHash = p; }
    public void setCurrency(String c)     { currency = c; }

    @Override
    public String toString() {
        return "User{userId=" + userId + ", name='" + name + "', email='" + email +
                "', currency='" + currency + "'}";
    }
}

// ─────────────────────────────────────────────
//  CATEGORY
// ─────────────────────────────────────────────

class Category {

    private int    categoryId;
    private String name;
    private String type;
    private List<Transaction> transactions = new ArrayList<>();

    public Category(int id, String name, String type) {
        this.categoryId = id;
        this.name       = name;
        this.type       = type;
    }

    public void addTransaction(Transaction t)    { transactions.add(t);    System.out.println("Transaction added to category: " + name); }
    public void removeTransaction(Transaction t) { transactions.remove(t); System.out.println("Transaction removed from category: " + name); }

    public int               getCategoryId()   { return categoryId; }
    public String            getName()         { return name; }
    public String            getType()         { return type; }
    public List<Transaction> getTransactions() { return transactions; }

    public void setName(String n) { name = n; }
    public void setType(String t) { type = t; }

    @Override
    public String toString() {
        return "Category{categoryId=" + categoryId + ", name='" + name + "', type='" + type + "'}";
    }
}

// ─────────────────────────────────────────────
//  TRANSACTION
// ─────────────────────────────────────────────

class Transaction {

    private int        transactionId;
    private String     type;
    private BigDecimal amount;
    private LocalDate  date;
    private String     description;
    private User       user;
    private Category   category;

    public Transaction(int id, String type, BigDecimal amount, LocalDate date,
                       String description, User user, Category category) {
        this.transactionId = id;
        this.type          = type;
        this.amount        = amount;
        this.date          = date;
        this.description   = description;
        this.user          = user;
        this.category      = category;
    }

    public void addTransaction()    { System.out.println("Transaction added: [" + type + "] " + amount + " on " + date); }
    public void deleteTransaction() { System.out.println("Transaction deleted: ID " + transactionId); }

    public int        getTransactionId() { return transactionId; }
    public String     getType()          { return type; }
    public BigDecimal getAmount()        { return amount; }
    public LocalDate  getDate()          { return date; }
    public String     getDescription()   { return description; }
    public User       getUser()          { return user; }
    public Category   getCategory()      { return category; }

    public void setType(String t)        { type = t; }
    public void setAmount(BigDecimal a)  { amount = a; }
    public void setDate(LocalDate d)     { date = d; }
    public void setDescription(String d) { description = d; }
    public void setUser(User u)          { user = u; }
    public void setCategory(Category c)  { category = c; }

    @Override
    public String toString() {
        return "Transaction{id=" + transactionId + ", type='" + type + "', amount=" + amount +
                ", date=" + date + ", desc='" + description + "'}";
    }
}

// ─────────────────────────────────────────────
//  BUDGET
// ─────────────────────────────────────────────

class Budget {

    private int        budgetId;
    private String     period;
    private BigDecimal spentAmount;
    private BigDecimal totalExpense;
    private User       user;
    private List<BudgetAlert> alerts = new ArrayList<>();
    private BudgetStatus      status;

    public Budget(int id, String period, BigDecimal totalExpense, User user) {
        this.budgetId     = id;
        this.period       = period;
        this.totalExpense = totalExpense;
        this.user         = user;
        this.spentAmount  = BigDecimal.ZERO;
        this.status       = BudgetStatus.ON_TRACK;
    }

    public void checkLimit() {
        BigDecimal remaining  = totalExpense.subtract(spentAmount);
        BigDecimal threshold  = totalExpense.multiply(BigDecimal.valueOf(0.10));
        if      (spentAmount.compareTo(totalExpense) >= 0)  status = BudgetStatus.EXCEEDED;
        else if (remaining.compareTo(threshold) <= 0)       status = BudgetStatus.NEAR_LIMIT;
        else                                                 status = BudgetStatus.ON_TRACK;
    }

    public void addSpending(BigDecimal amount) { spentAmount = spentAmount.add(amount); checkLimit(); }

    public int               getBudgetId()    { return budgetId; }
    public String            getPeriod()      { return period; }
    public BigDecimal        getSpentAmount() { return spentAmount; }
    public BigDecimal        getTotalExpense(){ return totalExpense; }
    public User              getUser()        { return user; }
    public List<BudgetAlert> getAlerts()      { return alerts; }
    public BudgetStatus      getStatus()      { return status; }

    public void setPeriod(String p)           { period = p; }
    public void setSpentAmount(BigDecimal s)  { spentAmount = s; checkLimit(); }
    public void setTotalExpense(BigDecimal t) { totalExpense = t; }
    public void setUser(User u)               { user = u; }
    public void setStatus(BudgetStatus s)     { status = s; }

    @Override
    public String toString() {
        return "Budget{id=" + budgetId + ", period='" + period + "', spent=" + spentAmount +
                ", total=" + totalExpense + ", status=" + status + "}";
    }
}

// ─────────────────────────────────────────────
//  BUDGET ALERT
// ─────────────────────────────────────────────

class BudgetAlert {

    private int              alertId;
    private String           message;
    private LocalDateTime    triggeredAt;
    private Budget           budget;
    private NotificationType notificationType;

    public BudgetAlert(int id, String message, Budget budget, NotificationType type) {
        this.alertId          = id;
        this.message          = message;
        this.triggeredAt      = LocalDateTime.now();
        this.budget           = budget;
        this.notificationType = type;
    }

    public int              getAlertId()          { return alertId; }
    public String           getMessage()          { return message; }
    public LocalDateTime    getTriggeredAt()       { return triggeredAt; }
    public Budget           getBudget()           { return budget; }
    public NotificationType getNotificationType() { return notificationType; }

    public void setMessage(String m)              { message = m; }
    public void setBudget(Budget b)               { budget = b; }
    public void setNotificationType(NotificationType t) { notificationType = t; }

    @Override
    public String toString() {
        return "BudgetAlert{id=" + alertId + ", type=" + notificationType + ", msg='" + message + "'}";
    }
}

// ─────────────────────────────────────────────
//  GOAL
// ─────────────────────────────────────────────

class Goal {

    private int        goalId;
    private String     name;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private LocalDate  deadline;
    private User       user;
    private GoalStatus status;

    public Goal(int id, String name, BigDecimal targetAmount, LocalDate deadline, User user) {
        this.goalId        = id;
        this.name          = name;
        this.targetAmount  = targetAmount;
        this.currentAmount = BigDecimal.ZERO;
        this.deadline      = deadline;
        this.user          = user;
        this.status        = GoalStatus.IN_PROGRESS;
    }

    public void updateProgress(BigDecimal contribution) {
        currentAmount = currentAmount.add(contribution);
        System.out.println("Progress: " + currentAmount + " / " + targetAmount);
        if      (currentAmount.compareTo(targetAmount) >= 0) { status = GoalStatus.COMPLETED; System.out.println("Goal '" + name + "' completed!"); }
        else if (LocalDate.now().isAfter(deadline))          { status = GoalStatus.CANCELLED;  System.out.println("Goal '" + name + "' cancelled (deadline passed)."); }
        else    System.out.println("Remaining: " + targetAmount.subtract(currentAmount));
    }

    public BigDecimal getProgressPercentage() {
        if (targetAmount.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return currentAmount.multiply(BigDecimal.valueOf(100))
                .divide(targetAmount, 2, java.math.RoundingMode.HALF_UP);
    }

    public int        getGoalId()        { return goalId; }
    public String     getName()          { return name; }
    public BigDecimal getTargetAmount()  { return targetAmount; }
    public BigDecimal getCurrentAmount() { return currentAmount; }
    public LocalDate  getDeadline()      { return deadline; }
    public User       getUser()          { return user; }
    public GoalStatus getStatus()        { return status; }

    public void setName(String n)           { name = n; }
    public void setTargetAmount(BigDecimal t){ targetAmount = t; }
    public void setDeadline(LocalDate d)    { deadline = d; }
    public void setUser(User u)             { user = u; }
    public void setStatus(GoalStatus s)     { status = s; }

    @Override
    public String toString() {
        return "Goal{id=" + goalId + ", name='" + name + "', progress=" +
                getProgressPercentage() + "%, status=" + status + "}";
    }
}

// ─────────────────────────────────────────────
//  REPORT
// ─────────────────────────────────────────────

class Report {

    private int        reportId;
    private String     period;
    private BigDecimal totalExpense;
    private User       user;
    private List<Transaction> transactions = new ArrayList<>();

    public Report(int id, String period, User user) {
        this.reportId     = id;
        this.period       = period;
        this.user         = user;
        this.totalExpense = BigDecimal.ZERO;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        totalExpense = totalExpense.add(t.getAmount());
    }

    public void removeTransaction(Transaction t) {
        if (transactions.remove(t)) totalExpense = totalExpense.subtract(t.getAmount());
    }

    public void generateReport() {
        System.out.println("=== Report: " + period + " ===");
        System.out.println("User: " + (user != null ? user.getName() : "N/A"));
        System.out.println("Transactions: " + transactions.size());
        System.out.println("Total Expense: " + totalExpense + " " + (user != null ? user.getCurrency() : ""));
    }

    public String getReportSummary() {
        return "Period:" + period + " | Transactions:" + transactions.size() + " | Total:" + totalExpense;
    }

    public String exportToCsv() {
        StringBuilder sb = new StringBuilder("transactionId,type,amount,date,description,category\n");
        for (Transaction t : transactions)
            sb.append(t.getTransactionId()).append(",")
                    .append(t.getType()).append(",")
                    .append(t.getAmount()).append(",")
                    .append(t.getDate()).append(",")
                    .append(t.getDescription()).append(",")
                    .append(t.getCategory() != null ? t.getCategory().getName() : "").append("\n");
        System.out.println("CSV export generated for report: " + reportId);
        return sb.toString();
    }

    public int               getReportId()     { return reportId; }
    public String            getPeriod()       { return period; }
    public BigDecimal        getTotalExpense()  { return totalExpense; }
    public User              getUser()         { return user; }
    public List<Transaction> getTransactions() { return transactions; }

    public void setPeriod(String p)           { period = p; }
    public void setTotalExpense(BigDecimal t) { totalExpense = t; }
    public void setUser(User u)               { user = u; }

    @Override
    public String toString() {
        return "Report{id=" + reportId + ", period='" + period + "', total=" + totalExpense + "}";
    }
}

// ─────────────────────────────────────────────
//  NOTIFICATION
// ─────────────────────────────────────────────

class Notification {

    private int              notifId;
    private String           message;
    private boolean          isRead;
    private LocalDateTime    timestamp;
    private User             user;
    private NotificationType type;

    public Notification(int id, String message, User user, NotificationType type) {
        this.notifId   = id;
        this.message   = message;
        this.isRead    = false;
        this.timestamp = LocalDateTime.now();
        this.user      = user;
        this.type      = type;
    }

    public void markAsRead() { isRead = true; System.out.println("Notification " + notifId + " marked as read."); }

    public int              getNotifId()   { return notifId; }
    public String           getMessage()   { return message; }
    public boolean          isRead()       { return isRead; }
    public LocalDateTime    getTimestamp() { return timestamp; }
    public User             getUser()      { return user; }
    public NotificationType getType()      { return type; }

    public void setMessage(String m)          { message = m; }
    public void setRead(boolean r)            { isRead = r; }
    public void setUser(User u)               { user = u; }
    public void setType(NotificationType t)   { type = t; }

    @Override
    public String toString() {
        return "Notification{id=" + notifId + ", type=" + type + ", isRead=" + isRead + ", msg='" + message + "'}";
    }
}