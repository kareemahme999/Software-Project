package com.example.software_project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

// ─────────────────────────────────────────────
//  LOGIN VIEW
// ─────────────────────────────────────────────

class LoginView {

    private Stage         stage;
    private TextField     emailField;
    private PasswordField passwordField;
    private Button        loginBtn;

    public LoginView(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        Label title = new Label("💰 Finance Tracker");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));

        Label subtitle = new Label("Manage your money wisely");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#7f8c8d"));

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefWidth(300);
        emailField.setPrefHeight(40);
        styleField(emailField);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(300);
        passwordField.setPrefHeight(40);
        styleField(passwordField);

        loginBtn = new Button("Login");
        loginBtn.setPrefWidth(300);
        loginBtn.setPrefHeight(40);
        loginBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8;");

        VBox form = new VBox(12, emailField, passwordField, loginBtn);
        form.setAlignment(Pos.CENTER);

        VBox card = new VBox(20, title, subtitle, form);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setMaxWidth(380);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 5);");

        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: #ecf0f1;");
        root.setPrefSize(600, 450);

        return new Scene(root, 600, 450);
    }

    private void styleField(Control f) {
        f.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 8; " +
                "-fx-background-radius: 8; -fx-padding: 8; -fx-font-size: 13px;");
    }

    public TextField     getEmailField()    { return emailField; }
    public PasswordField getPasswordField() { return passwordField; }
    public Button        getLoginBtn()      { return loginBtn; }
    public Stage         getStage()         { return stage; }
}

// ─────────────────────────────────────────────
//  DASHBOARD VIEW
// ─────────────────────────────────────────────

class DashboardView {

    private Stage  stage;
    private Label  balanceLabel;
    private PieChart                    pieChart;
    private LineChart<String, Number>   lineChart;
    private Button navTransactions, navBudget, navGoals, navReports, navLogout;

    public DashboardView(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        // Sidebar
        Label appName = new Label("💰 Finance");
        appName.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        appName.setTextFill(Color.WHITE);

        navTransactions = sidebarBtn("📋  Transactions");
        navBudget       = sidebarBtn("📊  Budget");
        navGoals        = sidebarBtn("🎯  Goals");
        navReports      = sidebarBtn("📄  Reports");
        navLogout       = sidebarBtn("🚪  Logout");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox sidebar = new VBox(15, appName, new Separator(),
                navTransactions, navBudget, navGoals, navReports, spacer, navLogout);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(180);
        sidebar.setStyle("-fx-background-color: #2c3e50;");

        // Balance card
        balanceLabel = new Label("$0.00");
        balanceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        balanceLabel.setTextFill(Color.web("#27ae60"));

        Label balTitle = new Label("Current Balance");
        balTitle.setFont(Font.font("Arial", 13));
        balTitle.setTextFill(Color.web("#7f8c8d"));

        VBox balCard = card(new VBox(5, balTitle, balanceLabel));

        // Pie chart
        pieChart = new PieChart(FXCollections.observableArrayList(
                new PieChart.Data("Food", 30), new PieChart.Data("Transport", 20),
                new PieChart.Data("Bills", 25), new PieChart.Data("Others", 25)));
        pieChart.setTitle("Spending by Category");
        pieChart.setPrefHeight(250);
        VBox pieCard = card(pieChart);

        // Line chart
        CategoryAxis xAxis = new CategoryAxis(); xAxis.setLabel("Month");
        NumberAxis   yAxis = new NumberAxis();   yAxis.setLabel("Amount");
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Monthly Overview");
        lineChart.setPrefHeight(250);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Expenses");
        series.getData().addAll(
                new XYChart.Data<>("Jan", 400), new XYChart.Data<>("Feb", 600),
                new XYChart.Data<>("Mar", 350), new XYChart.Data<>("Apr", 800));
        lineChart.getData().add(series);
        VBox lineCard = card(lineChart);

        // Grid
        GridPane grid = new GridPane();
        grid.setHgap(15); grid.setVgap(15); grid.setPadding(new Insets(20));
        grid.add(balCard,  0, 0);
        grid.add(pieCard,  1, 0);
        grid.add(lineCard, 0, 1, 2, 1);

        ColumnConstraints c1 = new ColumnConstraints(); c1.setPercentWidth(40);
        ColumnConstraints c2 = new ColumnConstraints(); c2.setPercentWidth(60);
        grid.getColumnConstraints().addAll(c1, c2);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #f4f6f8; -fx-background-color: #f4f6f8;");

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(scroll);

        return new Scene(root, 900, 620);
    }

    private VBox card(javafx.scene.Node content) {
        VBox box = new VBox(content);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        return box;
    }

    private Button sidebarBtn(String text) {
        Button b = new Button(text);
        b.setPrefWidth(150);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 8 12;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 8 12; -fx-background-radius: 6;"));
        b.setOnMouseExited(e  -> b.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 8 12;"));
        return b;
    }

    public Label                    getBalanceLabel()     { return balanceLabel; }
    public PieChart                 getPieChart()         { return pieChart; }
    public LineChart<String,Number> getLineChart()        { return lineChart; }
    public Button                   getNavTransactions()  { return navTransactions; }
    public Button                   getNavBudget()        { return navBudget; }
    public Button                   getNavGoals()         { return navGoals; }
    public Button                   getNavReports()       { return navReports; }
    public Button                   getNavLogout()        { return navLogout; }
    public Stage                    getStage()            { return stage; }
}

// ─────────────────────────────────────────────
//  TRANSACTION VIEW
// ─────────────────────────────────────────────

class TransactionView {

    private Stage                  stage;
    private TableView<Transaction> tableView;
    private TextField              filterField;
    private Button                 addBtn, deleteBtn, backBtn;

    public TransactionView(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        Label title = new Label("📋 Transactions");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#2c3e50"));

        filterField = new TextField();
        filterField.setPromptText("🔍 Search transactions...");
        filterField.setPrefWidth(250); filterField.setPrefHeight(35);
        styleField(filterField);

        addBtn    = actionBtn("➕ Add",    "#27ae60");
        deleteBtn = actionBtn("🗑 Delete", "#e74c3c");
        backBtn   = actionBtn("← Back",   "#95a5a6");

        HBox controls = new HBox(10, filterField, addBtn, deleteBtn, backBtn);
        controls.setAlignment(Pos.CENTER_LEFT);

        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        tableView.setStyle("-fx-background-color: white;");

        TableColumn<Transaction, Integer> idCol     = new TableColumn<>("ID");     idCol.setCellValueFactory(new PropertyValueFactory<>("transactionId")); idCol.setMaxWidth(60);
        TableColumn<Transaction, String>  typeCol   = new TableColumn<>("Type");   typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Transaction, String>  amountCol = new TableColumn<>("Amount"); amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Transaction, String>  dateCol   = new TableColumn<>("Date");   dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Transaction, String>  descCol   = new TableColumn<>("Description"); descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        tableView.getColumns().addAll(idCol, typeCol, amountCol, dateCol, descCol);

        VBox root = new VBox(15, new HBox(10, title), controls, tableView);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f4f6f8;");

        return new Scene(root, 900, 620);
    }

    private void styleField(Control f) {
        f.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 6; -fx-font-size: 13px;");
    }

    private Button actionBtn(String text, String color) {
        Button b = new Button(text);
        b.setPrefHeight(35);
        b.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 6 14;");
        return b;
    }

    public TableView<Transaction> getTableView()  { return tableView; }
    public TextField              getFilterField() { return filterField; }
    public Button                 getAddBtn()      { return addBtn; }
    public Button                 getDeleteBtn()   { return deleteBtn; }
    public Button                 getBackBtn()     { return backBtn; }
    public Stage                  getStage()       { return stage; }
}

// ─────────────────────────────────────────────
//  BUDGET VIEW
// ─────────────────────────────────────────────

class BudgetView {

    private Stage       stage;
    private TextField   limitField;
    private Label       statusLabel;
    private ProgressBar progressBar;
    private Button      saveBtn, backBtn;

    public BudgetView(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        Label title = new Label("📊 Budget Manager");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#2c3e50"));

        Label limitLbl = new Label("Monthly Budget Limit ($):");
        limitLbl.setFont(Font.font("Arial", 13));

        limitField = new TextField();
        limitField.setPromptText("e.g. 2000");
        limitField.setPrefHeight(38);
        styleField(limitField);

        saveBtn = actionBtn("💾 Save Budget", "#3498db");
        backBtn = actionBtn("← Back",        "#95a5a6");

        Label progressLbl = new Label("Spending Progress:");
        progressLbl.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400); progressBar.setPrefHeight(25);
        progressBar.setStyle("-fx-accent: #27ae60;");

        statusLabel = new Label("Status: ON TRACK ✅");
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        statusLabel.setTextFill(Color.web("#27ae60"));

        VBox card = new VBox(15, limitLbl, limitField,
                new HBox(10, saveBtn, backBtn), new Separator(),
                progressLbl, progressBar, statusLabel);
        card.setPadding(new Insets(30)); card.setMaxWidth(500);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 4);");

        VBox root = new VBox(20, title, card);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f4f6f8;");

        return new Scene(root, 700, 450);
    }

    private void styleField(Control f) {
        f.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 6; -fx-font-size: 13px;");
    }

    private Button actionBtn(String text, String color) {
        Button b = new Button(text);
        b.setPrefHeight(38);
        b.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 6 16;");
        return b;
    }

    public TextField   getLimitField()  { return limitField; }
    public Label       getStatusLabel() { return statusLabel; }
    public ProgressBar getProgressBar() { return progressBar; }
    public Button      getSaveBtn()     { return saveBtn; }
    public Button      getBackBtn()     { return backBtn; }
    public Stage       getStage()       { return stage; }
}

// ─────────────────────────────────────────────
//  GOAL VIEW
// ─────────────────────────────────────────────

class GoalView {

    private Stage       stage;
    private ProgressBar progressBar;
    private Label       statusLabel;
    private DatePicker  deadlinePicker;
    private TextField   nameField, targetField, contributionField;
    private Button      addGoalBtn, updateBtn, backBtn;

    public GoalView(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        Label title = new Label("🎯 Goals");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#2c3e50"));

        Label formTitle = new Label("Create New Goal");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        nameField = new TextField(); nameField.setPromptText("Goal name (e.g. Buy a car)"); styleField(nameField);
        targetField = new TextField(); targetField.setPromptText("Target amount ($)"); styleField(targetField);

        deadlinePicker = new DatePicker();
        deadlinePicker.setPromptText("Deadline"); deadlinePicker.setPrefHeight(38);

        addGoalBtn = actionBtn("➕ Add Goal", "#27ae60");
        backBtn    = actionBtn("← Back",     "#95a5a6");

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10);
        form.add(new Label("Name:"),     0, 0); form.add(nameField,       1, 0);
        form.add(new Label("Target:"),   0, 1); form.add(targetField,     1, 1);
        form.add(new Label("Deadline:"), 0, 2); form.add(deadlinePicker,  1, 2);
        form.add(new HBox(10, addGoalBtn, backBtn), 1, 3);

        Label progressTitle = new Label("Update Progress");
        progressTitle.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        contributionField = new TextField();
        contributionField.setPromptText("Contribution amount ($)"); styleField(contributionField);

        updateBtn = actionBtn("📈 Update", "#3498db");

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400); progressBar.setPrefHeight(22);
        progressBar.setStyle("-fx-accent: #3498db;");

        statusLabel = new Label("Progress: 0%");
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        statusLabel.setTextFill(Color.web("#3498db"));

        VBox card = new VBox(20, formTitle, form, new Separator(),
                progressTitle, contributionField, updateBtn, progressBar, statusLabel);
        card.setPadding(new Insets(25)); card.setMaxWidth(500);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 4);");

        VBox root = new VBox(20, title, card);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f4f6f8;");

        return new Scene(root, 700, 560);
    }

    private void styleField(Control f) {
        f.setPrefHeight(38);
        f.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 6; -fx-font-size: 13px;");
    }

    private Button actionBtn(String text, String color) {
        Button b = new Button(text);
        b.setPrefHeight(38);
        b.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 6 14;");
        return b;
    }

    public ProgressBar getProgressBar()       { return progressBar; }
    public Label       getStatusLabel()       { return statusLabel; }
    public DatePicker  getDeadlinePicker()    { return deadlinePicker; }
    public TextField   getNameField()         { return nameField; }
    public TextField   getTargetField()       { return targetField; }
    public TextField   getContributionField() { return contributionField; }
    public Button      getAddGoalBtn()        { return addGoalBtn; }
    public Button      getUpdateBtn()         { return updateBtn; }
    public Button      getBackBtn()           { return backBtn; }
    public Stage       getStage()             { return stage; }
}

// ─────────────────────────────────────────────
//  REPORT VIEW
// ─────────────────────────────────────────────

class ReportView {

    private Stage                  stage;
    private TableView<Transaction> summaryTable;
    private Label                  totalLabel;
    private Button                 generateBtn, exportBtn, backBtn;

    public ReportView(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        Label title = new Label("📄 Reports");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#2c3e50"));

        generateBtn = actionBtn("📊 Generate Report", "#3498db");
        exportBtn   = actionBtn("⬇ Export CSV",      "#27ae60");
        backBtn     = actionBtn("← Back",             "#95a5a6");

        summaryTable = new TableView<>();
        summaryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(summaryTable, Priority.ALWAYS);

        TableColumn<Transaction, Integer> idCol     = new TableColumn<>("ID");     idCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        TableColumn<Transaction, String>  typeCol   = new TableColumn<>("Type");   typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Transaction, String>  amountCol = new TableColumn<>("Amount"); amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Transaction, String>  dateCol   = new TableColumn<>("Date");   dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Transaction, String>  descCol   = new TableColumn<>("Description"); descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        summaryTable.getColumns().addAll(idCol, typeCol, amountCol, dateCol, descCol);

        totalLabel = new Label("Total Expense: $0.00");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        totalLabel.setTextFill(Color.web("#e74c3c"));

        VBox root = new VBox(15, title, new HBox(10, generateBtn, exportBtn, backBtn), summaryTable, totalLabel);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f4f6f8;");

        return new Scene(root, 900, 580);
    }

    private Button actionBtn(String text, String color) {
        Button b = new Button(text);
        b.setPrefHeight(38);
        b.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 6 14;");
        return b;
    }

    public TableView<Transaction> getSummaryTable() { return summaryTable; }
    public Label                  getTotalLabel()   { return totalLabel; }
    public Button                 getGenerateBtn()  { return generateBtn; }
    public Button                 getExportBtn()    { return exportBtn; }
    public Button                 getBackBtn()      { return backBtn; }
    public Stage                  getStage()        { return stage; }
}