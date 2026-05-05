package com.example.software_project;

import javafx.application.Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// ─────────────────────────────────────────────
//  MAIN APPLICATION ENTRY POINT
// ─────────────────────────────────────────────

public class Controllers extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        new LoginController(primaryStage).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// ─────────────────────────────────────────────
//  LOGIN CONTROLLER
// ─────────────────────────────────────────────

class LoginController {

    private LoginView view;
    private Stage     stage;

    public LoginController(Stage stage) {
        this.stage = stage;
        this.view  = new LoginView(stage);
    }

    public void show() {
        stage.setScene(view.getScene());
        stage.setTitle("Finance Tracker - Login");
        stage.show();
        bindEvents();
    }

    private void bindEvents() {
        view.getLoginBtn().setOnAction(e -> handleLogin());
        view.getPasswordField().setOnAction(e -> handleLogin());

        // BUG FIX 1: registerLink was never wired — clicking "Create an account" did nothing
        view.getRegisterLink().setOnAction(e -> new RegistrationController(stage).show());
    }

    private void handleLogin() {
        String email    = view.getEmailField().getText().trim();
        String password = view.getPasswordField().getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Missing Fields", "Please enter email and password.");
            return;
        }

        // BUG FIX 2: was using a hardcoded demo User and only checking password,
        //            completely ignoring the email field.
        //            Now authenticates properly via UserStore (checks both email + password).
        User user = UserStore.authenticate(email, password);
        if (user != null) {
            new DashboardController(stage, user).show();
        } else {
            alert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
        }
    }

    private void alert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}

// ─────────────────────────────────────────────
//  REGISTRATION CONTROLLER
// ─────────────────────────────────────────────

class RegistrationController {

    private RegistrationView view;
    private Stage            stage;

    public RegistrationController(Stage stage) {
        this.stage = stage;
        this.view  = new RegistrationView(stage);
    }

    public void show() {
        stage.setScene(view.getScene());
        stage.setTitle("Finance Tracker - Register");
        bindEvents();
    }

    private void bindEvents() {
        view.getRegisterBtn()    .setOnAction(e -> handleRegister());
        view.getBackToLoginLink().setOnAction(e -> new LoginController(stage).show());
    }

    private void handleRegister() {
        String name     = view.getNameField().getText().trim();
        String email    = view.getEmailField().getText().trim();
        String password = view.getPasswordField().getText();
        String confirm  = view.getConfirmField().getText();
        String currency = view.getCurrencyBox().getValue();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Missing Fields", "Please fill in all fields.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            alert(Alert.AlertType.WARNING, "Invalid Email", "Please enter a valid email address.");
            return;
        }
        if (password.length() < 6) {
            alert(Alert.AlertType.WARNING, "Weak Password", "Password must be at least 6 characters.");
            return;
        }
        if (!password.equals(confirm)) {
            alert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return;
        }

        boolean ok = UserStore.register(name, email, password, currency);
        if (ok) {
            alert(Alert.AlertType.INFORMATION, "Registered!", "Account created. You can now sign in.");
            new LoginController(stage).show();
        } else {
            alert(Alert.AlertType.ERROR, "Email Taken", "An account with this email already exists.");
        }
    }

    private void alert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}

// ─────────────────────────────────────────────
//  DASHBOARD CONTROLLER
// ─────────────────────────────────────────────

class DashboardController {

    private DashboardView view;
    private Stage         stage;
    private User          currentUser;

    public DashboardController(Stage stage, User user) {
        this.stage       = stage;
        this.currentUser = user;
        this.view        = new DashboardView(stage);
    }

    public void show() {
        stage.setScene(view.getScene());
        stage.setTitle("Dashboard - " + currentUser.getName());
        bindEvents();
        loadData();
        loadCharts();
    }

    public void loadData() {
        BigDecimal income  = currentUser.getTransactions().stream()
                .filter(t -> "income".equalsIgnoreCase(t.getType()))
                .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = currentUser.getTransactions().stream()
                .filter(t -> "expense".equalsIgnoreCase(t.getType()))
                .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        view.getBalanceLabel().setText(currentUser.getCurrency() + " " + income.subtract(expense).toPlainString());
    }

    public void loadCharts() {
        // Pie chart — spending by category
        Map<String, BigDecimal> byCategory = currentUser.getTransactions().stream()
                .filter(t -> "expense".equalsIgnoreCase(t.getType()) && t.getCategory() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

        if (!byCategory.isEmpty())
            view.getPieChart().setData(FXCollections.observableArrayList(
                    byCategory.entrySet().stream()
                            .map(e -> new PieChart.Data(e.getKey(), e.getValue().doubleValue()))
                            .collect(Collectors.toList())));

        // Line chart — expenses grouped by month
        Map<String, Double> monthly = currentUser.getTransactions().stream()
                .filter(t -> "expense".equalsIgnoreCase(t.getType()))
                .collect(Collectors.groupingBy(
                        t -> t.getDate().getMonth().toString().substring(0, 3),
                        Collectors.summingDouble(t -> t.getAmount().doubleValue())));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Expenses");
        monthly.forEach((month, amt) -> series.getData().add(new XYChart.Data<>(month, amt)));

        view.getLineChart().getData().clear();
        if (!series.getData().isEmpty()) view.getLineChart().getData().add(series);
    }

    private void bindEvents() {
        view.getNavTransactions().setOnAction(e -> new TransactionController(stage, currentUser).show());
        view.getNavBudget()      .setOnAction(e -> new BudgetController(stage, currentUser).show());
        view.getNavGoals()       .setOnAction(e -> new GoalController(stage, currentUser).show());
        view.getNavReports()     .setOnAction(e -> new ReportController(stage, currentUser).show());
        view.getNavLogout()      .setOnAction(e -> new LoginController(stage).show());
    }
}

// ─────────────────────────────────────────────
//  TRANSACTION CONTROLLER
// ─────────────────────────────────────────────

class TransactionController {

    private TransactionView              view;
    private Stage                        stage;
    private User                         currentUser;
    private ObservableList<Transaction>  data;
    private FilteredList<Transaction>    filtered;

    public TransactionController(Stage stage, User user) {
        this.stage       = stage;
        this.currentUser = user;
        this.view        = new TransactionView(stage);
    }

    public void show() {
        stage.setScene(view.getScene());
        stage.setTitle("Transactions");
        data = FXCollections.observableArrayList(currentUser.getTransactions());

        // BUG FIX 3: FilteredList must wrap `data` BEFORE setItems.
        filtered = new FilteredList<>(data, t -> true);
        view.getTableView().setItems(filtered);

        bindEvents();
    }

    private void bindEvents() {
        // Live search — now also matches amount, date, and category
        view.getFilterField().textProperty().addListener((obs, old, val) ->
                filtered.setPredicate(t -> {
                    if (val == null || val.isEmpty()) return true;
                    String lower = val.toLowerCase();
                    return t.getDescription().toLowerCase().contains(lower)
                            || t.getType().toLowerCase().contains(lower)
                            || t.getAmount().toPlainString().contains(lower)
                            || t.getDate().toString().contains(lower)
                            || (t.getCategory() != null && t.getCategory().getName().toLowerCase().contains(lower));
                }));

        view.getAddBtn().setOnAction(e -> showAddDialog());

        view.getDeleteBtn().setOnAction(e -> {
            Transaction sel = view.getTableView().getSelectionModel().getSelectedItem();
            if (sel == null) { alert("No Selection", "Please select a transaction to delete."); return; }
            // BUG FIX 4: remove from `data` (not from filtered) so the ObservableList
            //            backing the FilteredList stays in sync with the user's list.
            currentUser.removeTransaction(sel);
            data.remove(sel);
            // PERSIST: save after deletion
            UserStore.saveTransactions(currentUser);
        });

        view.getBackBtn().setOnAction(e -> new DashboardController(stage, currentUser).show());
    }

    private void showAddDialog() {
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.setTitle("Add Transaction");
        dialog.setHeaderText("Enter transaction details");

        // FIX 5: type was a free TextField — any string could be entered.
        //        Replaced with ComboBox to enforce only "income" or "expense".
        ComboBox<String> typeBox = new ComboBox<>(FXCollections.observableArrayList("income", "expense"));
        typeBox.setPromptText("Select type");
        typeBox.setPrefWidth(200);

        TextField amountField = new TextField(); amountField.setPromptText("Amount (e.g. 100.00)");
        TextField descField   = new TextField(); descField.setPromptText("Description");

        // FIX: Category is now a ComboBox with predefined options instead of a free TextField.
        //      This ensures consistent category names for budget grouping and pie chart display.
        ComboBox<String> catBox = new ComboBox<>(FXCollections.observableArrayList(
                "Food", "Transport", "Bills", "Shopping", "Health",
                "Entertainment", "Education", "Savings", "Income", "General"));
        catBox.setPromptText("Select category");
        catBox.setPrefWidth(200);
        catBox.setValue("General");

        javafx.scene.layout.GridPane g = new javafx.scene.layout.GridPane();
        g.setHgap(10); g.setVgap(10);
        g.setPadding(new javafx.geometry.Insets(15));
        g.add(new Label("Type:"),        0, 0); g.add(typeBox,     1, 0);
        g.add(new Label("Amount:"),      0, 1); g.add(amountField, 1, 1);
        g.add(new Label("Description:"), 0, 2); g.add(descField,   1, 2);
        g.add(new Label("Category:"),    0, 3); g.add(catBox,      1, 3);
        dialog.getDialogPane().setContent(g);

        ButtonType addType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addType, ButtonType.CANCEL);

        // Prevent JavaFX from auto-disabling the Add button
        javafx.scene.Node addButton = dialog.getDialogPane().lookupButton(addType);
        addButton.setDisable(false);
        typeBox.valueProperty().addListener((obs, o, n) -> addButton.setDisable(false));
        amountField.textProperty().addListener((obs, o, n) -> addButton.setDisable(false));

        dialog.setResultConverter(btn -> {
            if (btn == addType) {
                String type = typeBox.getValue();
                if (type == null) {
                    alert("Missing Type", "Please select income or expense.");
                    return null;
                }
                if (amountField.getText().trim().isEmpty()) {
                    alert("Missing Amount", "Please enter an amount.");
                    return null;
                }
                try {
                    BigDecimal amount = new BigDecimal(amountField.getText().trim());
                    // FIX 7: negative or zero amounts were silently accepted.
                    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                        alert("Invalid Amount", "Amount must be greater than zero.");
                        return null;
                    }
                    int newId = currentUser.getTransactions().size() + 1;
                    // FIX: read category from ComboBox, never null — defaults to "General"
                    String catName = catBox.getValue() == null ? "General" : catBox.getValue();
                    Category cat   = new Category(newId, catName, type);
                    String   desc  = descField.getText().trim().isEmpty() ? "-" : descField.getText().trim();
                    return new Transaction(newId, type, amount, LocalDate.now(), desc, currentUser, cat);
                } catch (NumberFormatException ex) {
                    alert("Invalid Amount", "Please enter a valid number (e.g. 100.00).");
                }
            }
            return null;
        });

        Optional<Transaction> result = dialog.showAndWait();
        result.ifPresent(t -> {
            currentUser.addTransaction(t);
            data.add(t);

            // FIX: Auto-update the active budget when an expense is added.
            //      Recalculate total spent across ALL expense transactions so the
            //      budget stays correct even after deletions or multiple additions.
            if ("expense".equalsIgnoreCase(t.getType()) && !currentUser.getBudgets().isEmpty()) {
                Budget activeBudget = currentUser.getBudgets().get(0);
                BigDecimal totalSpent = currentUser.getTransactions().stream()
                        .filter(tx -> "expense".equalsIgnoreCase(tx.getType()))
                        .map(Transaction::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                activeBudget.setSpentAmount(totalSpent);

                // FIX: Show an alert if budget is now exceeded
                if (activeBudget.getStatus() == BudgetStatus.EXCEEDED) {
                    Alert warn = new Alert(Alert.AlertType.WARNING);
                    warn.setTitle("Budget Exceeded");
                    warn.setHeaderText(null);
                    warn.setContentText("⚠ Your budget limit of "
                            + activeBudget.getTotalExpense().toPlainString()
                            + " has been exceeded! Spent: "
                            + activeBudget.getSpentAmount().toPlainString());
                    warn.showAndWait();
                } else if (activeBudget.getStatus() == BudgetStatus.NEAR_LIMIT) {
                    Alert warn = new Alert(Alert.AlertType.WARNING);
                    warn.setTitle("Budget Near Limit");
                    warn.setHeaderText(null);
                    warn.setContentText("⚠ You are near your budget limit! Spent: "
                            + activeBudget.getSpentAmount().toPlainString()
                            + " / Limit: " + activeBudget.getTotalExpense().toPlainString());
                    warn.showAndWait();
                }
            }

            // PERSIST: save transactions to disk immediately
            UserStore.saveTransactions(currentUser);
        });
    }

    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}

// ─────────────────────────────────────────────
//  BUDGET CONTROLLER
// ─────────────────────────────────────────────

class BudgetController {

    private BudgetView view;
    private Stage      stage;
    private User       currentUser;
    private Budget     budget;

    public BudgetController(Stage stage, User user) {
        this.stage       = stage;
        this.currentUser = user;
        this.view        = new BudgetView(stage);
    }

    public void show() {
        stage.setScene(view.getScene());
        stage.setTitle("Budget Manager");
        if (!currentUser.getBudgets().isEmpty()) {
            budget = currentUser.getBudgets().get(0);
            view.getLimitField().setText(budget.getTotalExpense().toPlainString());
            // FIX: Always recalculate spentAmount from current transactions when the
            //      view is opened, so any expenses added in TransactionController are
            //      immediately reflected here without needing a manual "Save".
            BigDecimal totalSpent = currentUser.getTransactions().stream()
                    .filter(t -> "expense".equalsIgnoreCase(t.getType()))
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            budget.setSpentAmount(totalSpent);
            checkLimit();
        }
        bindEvents();
    }

    private void bindEvents() {
        view.getSaveBtn().setOnAction(e -> saveBudget());
        view.getBackBtn().setOnAction(e -> new DashboardController(stage, currentUser).show());
    }

    public void saveBudget() {
        String text = view.getLimitField().getText().trim();
        if (text.isEmpty()) { alert("Missing Input", "Please enter a budget limit."); return; }
        try {
            BigDecimal limit = new BigDecimal(text);

            // BUG FIX 8: zero or negative budget limit was accepted silently.
            if (limit.compareTo(BigDecimal.ZERO) <= 0) {
                alert("Invalid Input", "Budget limit must be greater than zero.");
                return;
            }

            if (budget == null) {
                // BUG FIX 9: budget ID was always hardcoded as 1.
                int newId = currentUser.getBudgets().size() + 1;
                budget = new Budget(newId, "Monthly", limit, currentUser);
                currentUser.addBudget(budget);
            } else {
                budget.setTotalExpense(limit);
            }

            BigDecimal totalExpenses = currentUser.getTransactions().stream()
                    .filter(t -> "expense".equalsIgnoreCase(t.getType()))
                    .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            budget.setSpentAmount(totalExpenses);
            checkLimit();
        } catch (NumberFormatException ex) {
            alert("Invalid Input", "Please enter a valid number.");
        }
    }

    public void checkLimit() {
        if (budget == null) return;
        double ratio = budget.getTotalExpense().doubleValue() > 0
                ? budget.getSpentAmount().doubleValue() / budget.getTotalExpense().doubleValue() : 0;
        view.getProgressBar().setProgress(Math.min(ratio, 1.0));

        // Update the spent vs limit label
        view.getSpentLabel().setText(
                "Spent: " + budget.getSpentAmount().toPlainString()
                        + " / Limit: " + budget.getTotalExpense().toPlainString());

        switch (budget.getStatus()) {
            case ON_TRACK:
                view.getStatusLabel().setText("Status: ON TRACK");
                view.getStatusLabel().setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                view.getProgressBar().setStyle("-fx-accent: #27ae60;"); break;
            case NEAR_LIMIT:
                view.getStatusLabel().setText("Status: NEAR LIMIT ⚠");
                view.getStatusLabel().setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                view.getProgressBar().setStyle("-fx-accent: #f39c12;"); break;
            case EXCEEDED:
                view.getStatusLabel().setText("Status: EXCEEDED ✗");
                view.getStatusLabel().setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                view.getProgressBar().setStyle("-fx-accent: #e74c3c;"); break;
        }
    }

    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}

// ─────────────────────────────────────────────
//  GOAL CONTROLLER
// ─────────────────────────────────────────────

class GoalController {

    private GoalView view;
    private Stage    stage;
    private User     currentUser;
    private Goal     currentGoal;

    public GoalController(Stage stage, User user) {
        this.stage       = stage;
        this.currentUser = user;
        this.view        = new GoalView(stage);
    }

    public void show() {
        stage.setScene(view.getScene());
        stage.setTitle("Goals");
        // Populate the ListView with existing goals
        for (Goal g : currentUser.getGoals()) {
            view.getGoalListView().getItems().add(g.getName() + " — Target: " + g.getTargetAmount() + " | " + g.getStatus());
        }
        // BUG FIX 10: always loaded index 0, now loads the most recent goal.
        if (!currentUser.getGoals().isEmpty()) {
            currentGoal = currentUser.getGoals().get(currentUser.getGoals().size() - 1);
            view.getGoalListView().getSelectionModel().selectLast();
            refreshProgress();
        }
        bindEvents();
    }

    private void bindEvents() {
        // Wire ListView selection → set currentGoal and refresh progress
        view.getGoalListView().getSelectionModel().selectedIndexProperty().addListener((obs, oldIdx, newIdx) -> {
            int idx = newIdx.intValue();
            if (idx >= 0 && idx < currentUser.getGoals().size()) {
                currentGoal = currentUser.getGoals().get(idx);
                refreshProgress();
            }
        });

        view.getAddGoalBtn().setOnAction(e -> addGoal());
        view.getUpdateBtn().setOnAction(e -> {
            String txt = view.getContributionField().getText().trim();
            if (txt.isEmpty()) { alert("Missing Input", "Please enter a contribution amount."); return; }
            try {
                BigDecimal contribution = new BigDecimal(txt);
                // BUG FIX 11: zero or negative contributions were silently accepted.
                if (contribution.compareTo(BigDecimal.ZERO) <= 0) {
                    alert("Invalid Amount", "Contribution must be greater than zero.");
                    return;
                }
                updateProgress(contribution);
                view.getContributionField().clear();
            } catch (NumberFormatException ex) {
                alert("Invalid Input", "Please enter a valid number.");
            }
        });
        view.getBackBtn().setOnAction(e -> new DashboardController(stage, currentUser).show());
    }

    private void addGoal() {
        String name   = view.getNameField().getText().trim();
        String target = view.getTargetField().getText().trim();
        if (name.isEmpty() || target.isEmpty() || view.getDeadlinePicker().getValue() == null) {
            alert("Missing Fields", "Please fill all goal fields."); return;
        }
        // BUG FIX 12: deadline in the past was accepted for a new goal.
        if (view.getDeadlinePicker().getValue().isBefore(LocalDate.now())) {
            alert("Invalid Deadline", "Deadline must be a future date."); return;
        }
        try {
            BigDecimal targetAmt = new BigDecimal(target);
            if (targetAmt.compareTo(BigDecimal.ZERO) <= 0) {
                alert("Invalid Target", "Target amount must be greater than zero."); return;
            }
            currentGoal = new Goal(currentUser.getGoals().size() + 1, name,
                    targetAmt, view.getDeadlinePicker().getValue(), currentUser);
            currentUser.addGoal(currentGoal);

            // Update ListView with the new goal
            view.getGoalListView().getItems().add(currentGoal.getName() + " — Target: " + targetAmt + " | " + currentGoal.getStatus());
            view.getGoalListView().getSelectionModel().selectLast();

            // BUG FIX 13: form fields were never cleared after adding a goal.
            view.getNameField().clear();
            view.getTargetField().clear();
            view.getDeadlinePicker().setValue(null);

            refreshProgress();
            alertInfo("Goal Added!", "Goal '" + name + "' created successfully.");
        } catch (NumberFormatException ex) {
            alert("Invalid Input", "Target amount must be a number.");
        }
    }

    public void updateProgress(BigDecimal contribution) {
        if (currentGoal == null) { alert("No Goal", "Please create a goal first."); return; }

        // BUG FIX 14: updating a COMPLETED or CANCELLED goal had no guard.
        if (currentGoal.getStatus() == GoalStatus.COMPLETED) {
            alertInfo("Already Complete", "This goal is already completed!"); return;
        }
        if (currentGoal.getStatus() == GoalStatus.CANCELLED) {
            alert("Goal Cancelled", "This goal is cancelled. Please create a new one."); return;
        }

        currentGoal.updateProgress(contribution);
        refreshProgress();
        // Update the ListView entry to reflect new status
        int idx = currentUser.getGoals().indexOf(currentGoal);
        if (idx >= 0) {
            view.getGoalListView().getItems().set(idx,
                    currentGoal.getName() + " — Target: " + currentGoal.getTargetAmount() + " | " + currentGoal.getStatus());
        }
    }

    private void refreshProgress() {
        if (currentGoal == null) return;
        view.getProgressBar().setProgress(Math.min(currentGoal.getProgressPercentage().doubleValue() / 100.0, 1.0));
        view.getStatusLabel().setText("Progress: " + currentGoal.getProgressPercentage() + "% — " + currentGoal.getStatus());
        switch (currentGoal.getStatus()) {
            case COMPLETED:
                view.getStatusLabel().setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                view.getProgressBar().setStyle("-fx-accent: #27ae60;"); break;
            case CANCELLED:
                view.getStatusLabel().setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                view.getProgressBar().setStyle("-fx-accent: #e74c3c;"); break;
            default:
                view.getStatusLabel().setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
                view.getProgressBar().setStyle("-fx-accent: #3498db;");
        }
    }

    private void alert(String title, String msg)     { Alert a = new Alert(Alert.AlertType.WARNING);     a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait(); }
    private void alertInfo(String title, String msg) { Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait(); }
}

// ─────────────────────────────────────────────
//  REPORT CONTROLLER
// ─────────────────────────────────────────────

class ReportController {

    private ReportView view;
    private Stage      stage;
    private User       currentUser;

    public ReportController(Stage stage, User user) {
        this.stage       = stage;
        this.currentUser = user;
        this.view        = new ReportView(stage);
    }

    public void show() {
        stage.setScene(view.getScene());
        stage.setTitle("Reports");
        bindEvents();
    }

    public void generateReport(int userId) {
        Report report = new Report(currentUser.getReports().size() + 1, "Current Period", currentUser);

        // BUG FIX 15: was adding ALL transactions (income + expense) to the report total.
        //             Report total should only sum EXPENSE transactions.
        currentUser.getTransactions().stream()
                .filter(t -> "expense".equalsIgnoreCase(t.getType()))
                .forEach(report::addTransaction);



        currentUser.addReport(report);
        view.getSummaryTable().setItems(FXCollections.observableArrayList(report.getTransactions()));
        view.getTotalLabel().setText("Total Expense: " + currentUser.getCurrency() + " " + report.getTotalExpense().toPlainString());
    }

    public void exportToCsv(int userId) {
        if (currentUser.getReports().isEmpty()) { alert("No Report", "Please generate a report first."); return; }
        Report last = currentUser.getReports().get(currentUser.getReports().size() - 1);
        try {
            java.nio.file.Files.writeString(java.nio.file.Path.of("report_" + last.getReportId() + ".csv"), last.exportToCsv());
            alertInfo("Exported!", "Saved as report_" + last.getReportId() + ".csv");
        } catch (Exception ex) { alert("Export Error", ex.getMessage()); }
    }

    private void bindEvents() {
        view.getGenerateBtn().setOnAction(e -> generateReport(currentUser.getUserId()));
        view.getExportBtn()  .setOnAction(e -> exportToCsv(currentUser.getUserId()));
        view.getBackBtn()    .setOnAction(e -> new DashboardController(stage, currentUser).show());
    }

    private void alert(String title, String msg)     { Alert a = new Alert(Alert.AlertType.WARNING);     a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait(); }
    private void alertInfo(String title, String msg) { Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(title); a.setHeaderText(null); a.setContentText(msg); a.showAndWait(); }
}