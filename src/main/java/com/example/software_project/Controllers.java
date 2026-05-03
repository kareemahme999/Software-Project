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
        UserStore.load();  // pre-load users from file
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
        stage.setTitle("Finance Tracker");
        stage.show();
        bindEvents();
    }

    private void bindEvents() {
        view.getLoginBtn()     .setOnAction(e -> handleLogin());
        view.getPasswordField().setOnAction(e -> handleLogin());
        view.getRegisterLink() .setOnAction(e -> new RegistrationController(stage).show());
    }

    private void handleLogin() {
        String email    = view.getEmailField().getText().trim();
        String password = view.getPasswordField().getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Missing Fields", "Please enter your email and password.");
            return;
        }

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
        stage.setTitle("Finance Tracker - Create Account");
        bindEvents();
    }

    private void bindEvents() {
        view.getRegisterBtn()    .setOnAction(e -> handleRegister());
        view.getBackToLoginLink().setOnAction(e -> new LoginController(stage).show());
    }

    private void handleRegister() {
        String name     = view.getNameField()    .getText().trim();
        String email    = view.getEmailField()   .getText().trim();
        String password = view.getPasswordField().getText().trim();
        String confirm  = view.getConfirmField() .getText().trim();
        String currency = view.getCurrencyBox()  .getValue();

        // Validation
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

        boolean success = UserStore.register(name, email, password, currency);
        if (success) {
            alert(Alert.AlertType.INFORMATION, "Account Created",
                    "Welcome, " + name + "! Your account has been created.\nYou can now sign in.");
            new LoginController(stage).show();
        } else {
            alert(Alert.AlertType.ERROR, "Email Taken",
                    "An account with this email already exists.");
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

        view.getBalanceLabel().setText(currentUser.getCurrency() + " " + income.subtract(expense));
    }

    public void loadCharts() {
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

    public TransactionController(Stage stage, User user) {
        this.stage       = stage;
        this.currentUser = user;
        this.view        = new TransactionView(stage);
    }

    public void show() {
        stage.setScene(view.getScene());
        stage.setTitle("Transactions");
        data = FXCollections.observableArrayList(currentUser.getTransactions());
        view.getTableView().setItems(data);
        bindEvents();
    }

    private void bindEvents() {
        FilteredList<Transaction> filtered = new FilteredList<>(data, t -> true);
        view.getFilterField().textProperty().addListener((obs, old, val) ->
                filtered.setPredicate(t -> val == null || val.isEmpty()
                        || t.getDescription().toLowerCase().contains(val.toLowerCase())
                        || t.getType().toLowerCase().contains(val.toLowerCase())));
        view.getTableView().setItems(filtered);

        view.getAddBtn().setOnAction(e -> showAddDialog());

        view.getDeleteBtn().setOnAction(e -> {
            Transaction sel = view.getTableView().getSelectionModel().getSelectedItem();
            if (sel == null) { alert("No Selection", "Please select a transaction to delete."); return; }
            currentUser.removeTransaction(sel);
            data.remove(sel);
        });

        view.getBackBtn().setOnAction(e -> new DashboardController(stage, currentUser).show());
    }

    private void showAddDialog() {
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.setTitle("Add Transaction");
        dialog.setHeaderText("Enter transaction details");

        TextField typeField   = new TextField(); typeField.setPromptText("income / expense");
        TextField amountField = new TextField(); amountField.setPromptText("Amount");
        TextField descField   = new TextField(); descField.setPromptText("Description");
        TextField catField    = new TextField(); catField.setPromptText("Category");

        javafx.scene.layout.GridPane g = new javafx.scene.layout.GridPane();
        g.setHgap(10); g.setVgap(10);
        g.setPadding(new javafx.geometry.Insets(15));
        g.add(new Label("Type:"),        0, 0); g.add(typeField,   1, 0);
        g.add(new Label("Amount:"),      0, 1); g.add(amountField, 1, 1);
        g.add(new Label("Description:"), 0, 2); g.add(descField,   1, 2);
        g.add(new Label("Category:"),    0, 3); g.add(catField,    1, 3);
        dialog.getDialogPane().setContent(g);

        ButtonType addType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addType, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == addType) {
                try {
                    Category cat = new Category(1, catField.getText(), typeField.getText());
                    return new Transaction(data.size() + 1, typeField.getText(),
                            new BigDecimal(amountField.getText()), LocalDate.now(),
                            descField.getText(), currentUser, cat);
                } catch (Exception ex) { alert("Invalid Input", "Please check your entries."); }
            }
            return null;
        });

        Optional<Transaction> result = dialog.showAndWait();
        result.ifPresent(t -> { currentUser.addTransaction(t); data.add(t); });
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
            if (budget == null) { budget = new Budget(1, "Monthly", limit, currentUser); currentUser.addBudget(budget); }
            else budget.setTotalExpense(limit);

            BigDecimal totalExpenses = currentUser.getTransactions().stream()
                    .filter(t -> "expense".equalsIgnoreCase(t.getType()))
                    .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            budget.setSpentAmount(totalExpenses);
            checkLimit();
        } catch (NumberFormatException ex) { alert("Invalid Input", "Please enter a valid number."); }
    }

    public void checkLimit() {
        if (budget == null) return;
        double ratio = budget.getTotalExpense().doubleValue() > 0
                ? budget.getSpentAmount().doubleValue() / budget.getTotalExpense().doubleValue() : 0;
        view.getProgressBar().setProgress(Math.min(ratio, 1.0));

        switch (budget.getStatus()) {
            case ON_TRACK:
                view.getStatusLabel().setText("Status: ON TRACK");
                view.getStatusLabel().setStyle("-fx-text-fill: " + Styles.SUCCESS + "; -fx-font-weight: bold;");
                view.getProgressBar().setStyle("-fx-accent: " + Styles.SUCCESS + ";"); break;
            case NEAR_LIMIT:
                view.getStatusLabel().setText("Status: NEAR LIMIT");
                view.getStatusLabel().setStyle("-fx-text-fill: " + Styles.WARNING + "; -fx-font-weight: bold;");
                view.getProgressBar().setStyle("-fx-accent: " + Styles.WARNING + ";"); break;
            case EXCEEDED:
                view.getStatusLabel().setText("Status: EXCEEDED");
                view.getStatusLabel().setStyle("-fx-text-fill: " + Styles.DANGER + "; -fx-font-weight: bold;");
                view.getProgressBar().setStyle("-fx-accent: " + Styles.DANGER + ";"); break;
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
        if (!currentUser.getGoals().isEmpty()) { currentGoal = currentUser.getGoals().get(0); refreshProgress(); }
        bindEvents();
    }

    private void bindEvents() {
        view.getAddGoalBtn().setOnAction(e -> addGoal());
        view.getUpdateBtn() .setOnAction(e -> {
            String txt = view.getContributionField().getText().trim();
            updateProgress(new BigDecimal(txt.isEmpty() ? "0" : txt));
        });
        view.getBackBtn().setOnAction(e -> new DashboardController(stage, currentUser).show());
    }

    private void addGoal() {
        String name   = view.getNameField().getText().trim();
        String target = view.getTargetField().getText().trim();
        if (name.isEmpty() || target.isEmpty() || view.getDeadlinePicker().getValue() == null) {
            alert("Missing Fields", "Please fill all goal fields."); return;
        }
        try {
            currentGoal = new Goal(currentUser.getGoals().size() + 1, name,
                    new BigDecimal(target), view.getDeadlinePicker().getValue(), currentUser);
            currentUser.addGoal(currentGoal);
            refreshProgress();
            alertInfo("Goal Added!", "Goal '" + name + "' created successfully.");
        } catch (NumberFormatException ex) { alert("Invalid Input", "Target amount must be a number."); }
    }

    public void updateProgress(BigDecimal contribution) {
        if (currentGoal == null) { alert("No Goal", "Please create a goal first."); return; }
        currentGoal.updateProgress(contribution);
        refreshProgress();
    }

    private void refreshProgress() {
        if (currentGoal == null) return;
        view.getProgressBar().setProgress(Math.min(currentGoal.getProgressPercentage().doubleValue() / 100.0, 1.0));
        view.getStatusLabel().setText("Progress: " + currentGoal.getProgressPercentage() + "% — " + currentGoal.getStatus());
        switch (currentGoal.getStatus()) {
            case COMPLETED: view.getStatusLabel().setStyle("-fx-text-fill: " + Styles.SUCCESS + "; -fx-font-weight: bold;"); view.getProgressBar().setStyle("-fx-accent: " + Styles.SUCCESS + ";"); break;
            case CANCELLED: view.getStatusLabel().setStyle("-fx-text-fill: " + Styles.DANGER  + "; -fx-font-weight: bold;"); view.getProgressBar().setStyle("-fx-accent: " + Styles.DANGER  + ";"); break;
            default:        view.getStatusLabel().setStyle("-fx-text-fill: " + Styles.PRIMARY + "; -fx-font-weight: bold;"); view.getProgressBar().setStyle("-fx-accent: " + Styles.PRIMARY + ";");
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
        currentUser.getTransactions().forEach(report::addTransaction);
        currentUser.addReport(report);

        view.getSummaryTable().setItems(FXCollections.observableArrayList(report.getTransactions()));
        view.getTotalLabel().setText("Total Expense: " + currentUser.getCurrency() + " " + report.getTotalExpense());
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