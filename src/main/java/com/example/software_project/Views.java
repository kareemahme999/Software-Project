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
//  SHARED STYLE HELPERS
// ─────────────────────────────────────────────

class Styles {

    // Color palette — calm, professional
    static final String BG         = "#F7F8FA";
    static final String CARD       = "#FFFFFF";
    static final String PRIMARY    = "#3D5A80";   // deep navy-blue
    static final String PRIMARY_H  = "#2C4466";   // hover
    static final String ACCENT     = "#E07A5F";   // warm coral for accents
    static final String TEXT_DARK  = "#1A2030";
    static final String TEXT_MUTED = "#8D93A0";
    static final String BORDER     = "#E2E6EC";
    static final String SUCCESS    = "#4CAF7D";
    static final String WARNING    = "#F4A261";
    static final String DANGER     = "#E63946";
    static final String SIDEBAR_BG = "#1A2030";
    static final String FONT       = "Segoe UI";

    static void field(Control f) {
        f.setStyle("-fx-background-color: " + CARD + "; "
                + "-fx-border-color: " + BORDER + "; "
                + "-fx-border-radius: 6; -fx-background-radius: 6; "
                + "-fx-padding: 9 12; -fx-font-size: 13px; "
                + "-fx-font-family: '" + FONT + "'; "
                + "-fx-text-fill: " + TEXT_DARK + ";");
    }

    static Button primaryBtn(String text) {
        Button b = new Button(text);
        b.setPrefHeight(42);
        b.setStyle("-fx-background-color: " + PRIMARY + "; -fx-text-fill: white; "
                + "-fx-font-size: 13px; -fx-font-weight: bold; "
                + "-fx-font-family: '" + FONT + "'; "
                + "-fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 22;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: " + PRIMARY_H + "; -fx-text-fill: white; "
                + "-fx-font-size: 13px; -fx-font-weight: bold; "
                + "-fx-font-family: '" + FONT + "'; "
                + "-fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 22;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: " + PRIMARY + "; -fx-text-fill: white; "
                + "-fx-font-size: 13px; -fx-font-weight: bold; "
                + "-fx-font-family: '" + FONT + "'; "
                + "-fx-background-radius: 6; -fx-cursor: hand; -fx-padding: 0 22;"));
        return b;
    }

    static Button ghostBtn(String text, String color) {
        Button b = new Button(text);
        b.setPrefHeight(38);
        b.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; "
                + "-fx-font-size: 12px; -fx-font-family: '" + FONT + "'; "
                + "-fx-background-radius: 6; -fx-padding: 6 16;");
        return b;
    }

    static Button linkBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: " + PRIMARY + "; "
                + "-fx-font-size: 12px; -fx-font-family: '" + FONT + "'; "
                + "-fx-cursor: hand; -fx-padding: 0;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: transparent; -fx-text-fill: " + ACCENT + "; "
                + "-fx-font-size: 12px; -fx-font-family: '" + FONT + "'; "
                + "-fx-cursor: hand; -fx-padding: 0;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: transparent; -fx-text-fill: " + PRIMARY + "; "
                + "-fx-font-size: 12px; -fx-font-family: '" + FONT + "'; "
                + "-fx-cursor: hand; -fx-padding: 0;"));
        return b;
    }

    static Label heading(String text) {
        Label l = new Label(text);
        l.setFont(Font.font(FONT, FontWeight.BOLD, 26));
        l.setTextFill(Color.web(TEXT_DARK));
        return l;
    }

    static Label subheading(String text) {
        Label l = new Label(text);
        l.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        l.setTextFill(Color.web(TEXT_DARK));
        return l;
    }

    static Label muted(String text) {
        Label l = new Label(text);
        l.setFont(Font.font(FONT, 13));
        l.setTextFill(Color.web(TEXT_MUTED));
        return l;
    }

    static VBox card(javafx.scene.Node... children) {
        VBox box = new VBox(14);
        box.getChildren().addAll(children);
        box.setPadding(new Insets(22));
        box.setStyle("-fx-background-color: " + CARD + "; -fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 12, 0, 0, 3);");
        return box;
    }
}

// ─────────────────────────────────────────────
//  LOGIN VIEW
// ─────────────────────────────────────────────

class LoginView {

    private Stage         stage;
    private TextField     emailField;
    private PasswordField passwordField;
    private Button        loginBtn;
    private Button        registerLink;

    public LoginView(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        // Left decorative panel
        VBox leftPanel = new VBox();
        leftPanel.setPrefWidth(380);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPadding(new Insets(50));
        leftPanel.setStyle("-fx-background-color: " + Styles.SIDEBAR_BG + ";");

        Label brand = new Label("Finance\nTracker");
        brand.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 38));
        brand.setTextFill(Color.WHITE);
        brand.setLineSpacing(4);

        Label tagline = Styles.muted("Take control of your money.");
        tagline.setTextFill(Color.web("#8D93A0"));
        tagline.setFont(Font.font(Styles.FONT, 15));

        // Decorative horizontal rule
        Region rule = new Region();
        rule.setPrefHeight(2);
        rule.setPrefWidth(60);
        rule.setStyle("-fx-background-color: " + Styles.ACCENT + "; -fx-background-radius: 2;");

        VBox leftContent = new VBox(18, brand, rule, tagline);
        leftContent.setAlignment(Pos.CENTER_LEFT);
        leftPanel.getChildren().add(leftContent);

        // Right form panel
        Label title    = Styles.heading("Welcome back");
        Label subtitle = Styles.muted("Sign in to your account");

        Label emailLbl = new Label("Email address");
        emailLbl.setFont(Font.font(Styles.FONT, FontWeight.SEMI_BOLD, 12));
        emailLbl.setTextFill(Color.web(Styles.TEXT_DARK));

        emailField = new TextField();
        emailField.setPromptText("you@example.com");
        emailField.setPrefHeight(42);
        emailField.setPrefWidth(340);
        Styles.field(emailField);

        Label passLbl = new Label("Password");
        passLbl.setFont(Font.font(Styles.FONT, FontWeight.SEMI_BOLD, 12));
        passLbl.setTextFill(Color.web(Styles.TEXT_DARK));

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(42);
        passwordField.setPrefWidth(340);
        Styles.field(passwordField);

        loginBtn = Styles.primaryBtn("Sign In");
        loginBtn.setPrefWidth(340);

        registerLink = Styles.linkBtn("Create an account");
        Label noAcct = Styles.muted("Don't have an account?");

        HBox regRow = new HBox(6, noAcct, registerLink);
        regRow.setAlignment(Pos.CENTER);

        VBox form = new VBox(8,
                emailLbl, emailField,
                passLbl, passwordField,
                new Region() {{ setPrefHeight(6); }},
                loginBtn,
                new Region() {{ setPrefHeight(4); }},
                regRow);
        form.setAlignment(Pos.CENTER_LEFT);

        VBox rightPanel = new VBox(28, title, subtitle, form);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(60, 70, 60, 70));
        rightPanel.setStyle("-fx-background-color: " + Styles.BG + ";");
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        HBox root = new HBox(leftPanel, rightPanel);
        return new Scene(root, 780, 520);
    }

    public TextField     getEmailField()    { return emailField; }
    public PasswordField getPasswordField() { return passwordField; }
    public Button        getLoginBtn()      { return loginBtn; }
    public Button        getRegisterLink()  { return registerLink; }
    public Stage         getStage()         { return stage; }
}

// ─────────────────────────────────────────────
//  REGISTRATION VIEW
// ─────────────────────────────────────────────

class RegistrationView {

    private Stage         stage;
    private TextField     nameField;
    private TextField     emailField;
    private PasswordField passwordField;
    private PasswordField confirmField;
    private ComboBox<String> currencyBox;
    private Button        registerBtn;
    private Button        backToLoginLink;

    public RegistrationView(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        // Left panel
        VBox leftPanel = new VBox();
        leftPanel.setPrefWidth(380);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPadding(new Insets(50));
        leftPanel.setStyle("-fx-background-color: " + Styles.SIDEBAR_BG + ";");

        Label brand = new Label("Finance\nTracker");
        brand.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 38));
        brand.setTextFill(Color.WHITE);
        brand.setLineSpacing(4);

        Region rule = new Region();
        rule.setPrefHeight(2); rule.setPrefWidth(60);
        rule.setStyle("-fx-background-color: " + Styles.ACCENT + "; -fx-background-radius: 2;");

        Label tagline = Styles.muted("Your journey starts here.");
        tagline.setTextFill(Color.web("#8D93A0"));
        tagline.setFont(Font.font(Styles.FONT, 15));

        leftPanel.getChildren().add(new VBox(18, brand, rule, tagline));

        // Right form
        Label title    = Styles.heading("Create account");
        Label subtitle = Styles.muted("Fill in your details to get started");

        // Name
        Label nameLbl = fieldLabel("Full name");
        nameField = new TextField();
        nameField.setPromptText("Ahmed Mohamed");
        nameField.setPrefHeight(42); nameField.setPrefWidth(340);
        Styles.field(nameField);

        // Email
        Label emailLbl = fieldLabel("Email address");
        emailField = new TextField();
        emailField.setPromptText("you@example.com");
        emailField.setPrefHeight(42); emailField.setPrefWidth(340);
        Styles.field(emailField);

        // Password
        Label passLbl = fieldLabel("Password");
        passwordField = new PasswordField();
        passwordField.setPromptText("Minimum 6 characters");
        passwordField.setPrefHeight(42); passwordField.setPrefWidth(340);
        Styles.field(passwordField);

        // Confirm
        Label confirmLbl = fieldLabel("Confirm password");
        confirmField = new PasswordField();
        confirmField.setPromptText("Repeat your password");
        confirmField.setPrefHeight(42); confirmField.setPrefWidth(340);
        Styles.field(confirmField);

        // Currency
        Label currencyLbl = fieldLabel("Currency");
        currencyBox = new ComboBox<>(FXCollections.observableArrayList("USD", "EUR", "GBP", "EGP", "SAR", "AED"));
        currencyBox.setValue("USD");
        currencyBox.setPrefHeight(42); currencyBox.setPrefWidth(340);
        currencyBox.setStyle("-fx-font-family: '" + Styles.FONT + "'; -fx-font-size: 13px;");

        registerBtn = Styles.primaryBtn("Create Account");
        registerBtn.setPrefWidth(340);

        backToLoginLink = Styles.linkBtn("Sign in instead");
        Label alreadyLbl = Styles.muted("Already have an account?");
        HBox loginRow = new HBox(6, alreadyLbl, backToLoginLink);
        loginRow.setAlignment(Pos.CENTER);

        VBox form = new VBox(7,
                nameLbl, nameField,
                emailLbl, emailField,
                passLbl, passwordField,
                confirmLbl, confirmField,
                currencyLbl, currencyBox,
                new Region() {{ setPrefHeight(6); }},
                registerBtn,
                new Region() {{ setPrefHeight(4); }},
                loginRow);
        form.setAlignment(Pos.CENTER_LEFT);

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox rightPanel = new VBox(22, title, subtitle, scroll);
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setPadding(new Insets(50, 70, 50, 70));
        rightPanel.setStyle("-fx-background-color: " + Styles.BG + ";");
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        HBox root = new HBox(leftPanel, rightPanel);
        return new Scene(root, 780, 580);
    }

    private Label fieldLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font(Styles.FONT, FontWeight.SEMI_BOLD, 12));
        l.setTextFill(Color.web(Styles.TEXT_DARK));
        return l;
    }

    public TextField     getNameField()        { return nameField; }
    public TextField     getEmailField()       { return emailField; }
    public PasswordField getPasswordField()    { return passwordField; }
    public PasswordField getConfirmField()     { return confirmField; }
    public ComboBox<String> getCurrencyBox()   { return currencyBox; }
    public Button        getRegisterBtn()      { return registerBtn; }
    public Button        getBackToLoginLink()  { return backToLoginLink; }
    public Stage         getStage()            { return stage; }
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
        Label appName = new Label("Finance Tracker");
        appName.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 16));
        appName.setTextFill(Color.WHITE);

        navTransactions = sidebarBtn("Transactions");
        navBudget       = sidebarBtn("Budget");
        navGoals        = sidebarBtn("Goals");
        navReports      = sidebarBtn("Reports");
        navLogout       = sidebarBtn("Logout");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox sidebar = new VBox(10, appName, new Separator(),
                navTransactions, navBudget, navGoals, navReports, spacer, navLogout);
        sidebar.setPadding(new Insets(22));
        sidebar.setPrefWidth(195);
        sidebar.setStyle("-fx-background-color: " + Styles.SIDEBAR_BG + ";");

        // Balance card
        balanceLabel = new Label("$0.00");
        balanceLabel.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 38));
        balanceLabel.setTextFill(Color.web(Styles.SUCCESS));

        Label balTitle = Styles.muted("Current Balance");
        VBox balCard = Styles.card(balTitle, balanceLabel);

        // Pie chart
        pieChart = new PieChart(FXCollections.observableArrayList(
                new PieChart.Data("Food", 30), new PieChart.Data("Transport", 20),
                new PieChart.Data("Bills", 25), new PieChart.Data("Others", 25)));
        pieChart.setTitle("Spending by Category");
        pieChart.setPrefHeight(260);
        pieChart.setLegendVisible(true);
        VBox pieCard = Styles.card(pieChart);

        // Line chart
        CategoryAxis xAxis = new CategoryAxis(); xAxis.setLabel("Month");
        NumberAxis   yAxis = new NumberAxis();   yAxis.setLabel("Amount");
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Monthly Overview");
        lineChart.setPrefHeight(260);
        lineChart.setStyle("-fx-background-color: transparent;");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Expenses");
        series.getData().addAll(
                new XYChart.Data<>("Jan", 400), new XYChart.Data<>("Feb", 600),
                new XYChart.Data<>("Mar", 350), new XYChart.Data<>("Apr", 800));
        lineChart.getData().add(series);
        VBox lineCard = Styles.card(lineChart);

        // Grid
        GridPane grid = new GridPane();
        grid.setHgap(16); grid.setVgap(16); grid.setPadding(new Insets(24));
        grid.add(balCard,  0, 0);
        grid.add(pieCard,  1, 0);
        grid.add(lineCard, 0, 1, 2, 1);

        ColumnConstraints c1 = new ColumnConstraints(); c1.setPercentWidth(38);
        ColumnConstraints c2 = new ColumnConstraints(); c2.setPercentWidth(62);
        grid.getColumnConstraints().addAll(c1, c2);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + Styles.BG + "; -fx-background-color: " + Styles.BG + ";");

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(scroll);
        root.setStyle("-fx-background-color: " + Styles.BG + ";");

        return new Scene(root, 980, 680);
    }

    private Button sidebarBtn(String text) {
        Button b = new Button(text);
        b.setPrefWidth(160);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: #B0B8C8; "
                + "-fx-font-size: 13px; -fx-font-family: '" + Styles.FONT + "'; "
                + "-fx-padding: 9 14; -fx-background-radius: 6;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: rgba(255,255,255,0.08); "
                + "-fx-text-fill: white; -fx-font-size: 13px; "
                + "-fx-font-family: '" + Styles.FONT + "'; "
                + "-fx-padding: 9 14; -fx-background-radius: 6;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: transparent; "
                + "-fx-text-fill: #B0B8C8; -fx-font-size: 13px; "
                + "-fx-font-family: '" + Styles.FONT + "'; "
                + "-fx-padding: 9 14; -fx-background-radius: 6;"));
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
        Label title = Styles.subheading("Transactions");

        filterField = new TextField();
        filterField.setPromptText("Search transactions...");
        filterField.setPrefWidth(260); filterField.setPrefHeight(36);
        Styles.field(filterField);

        addBtn    = Styles.ghostBtn("Add",    Styles.SUCCESS);
        deleteBtn = Styles.ghostBtn("Delete", Styles.DANGER);
        backBtn   = Styles.ghostBtn("Back",   Styles.TEXT_MUTED);

        HBox controls = new HBox(10, filterField, addBtn, deleteBtn, backBtn);
        controls.setAlignment(Pos.CENTER_LEFT);

        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        tableView.setStyle("-fx-background-color: white; -fx-border-color: " + Styles.BORDER + ";");

        TableColumn<Transaction, Integer> idCol     = new TableColumn<>("ID");     idCol.setCellValueFactory(new PropertyValueFactory<>("transactionId")); idCol.setMaxWidth(60);
        TableColumn<Transaction, String>  typeCol   = new TableColumn<>("Type");   typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Transaction, String>  amountCol = new TableColumn<>("Amount"); amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Transaction, String>  dateCol   = new TableColumn<>("Date");   dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Transaction, String>  descCol   = new TableColumn<>("Description"); descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        tableView.getColumns().addAll(idCol, typeCol, amountCol, dateCol, descCol);

        VBox root = new VBox(16, title, controls, tableView);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color: " + Styles.BG + ";");

        return new Scene(root, 980, 680);
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
        Label title = Styles.subheading("Budget Manager");

        Label limitLbl = new Label("Monthly Budget Limit");
        limitLbl.setFont(Font.font(Styles.FONT, FontWeight.SEMI_BOLD, 12));
        limitLbl.setTextFill(Color.web(Styles.TEXT_DARK));

        limitField = new TextField();
        limitField.setPromptText("e.g. 2000");
        limitField.setPrefHeight(42);
        Styles.field(limitField);

        saveBtn = Styles.primaryBtn("Save Budget");
        backBtn = Styles.ghostBtn("Back", Styles.TEXT_MUTED);

        Label progressLbl = new Label("Spending Progress");
        progressLbl.setFont(Font.font(Styles.FONT, FontWeight.SEMI_BOLD, 13));
        progressLbl.setTextFill(Color.web(Styles.TEXT_DARK));

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(440); progressBar.setPrefHeight(22);
        progressBar.setStyle("-fx-accent: " + Styles.SUCCESS + ";");

        statusLabel = new Label("Status: ON TRACK");
        statusLabel.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 14));
        statusLabel.setTextFill(Color.web(Styles.SUCCESS));

        VBox card = Styles.card(
                limitLbl, limitField,
                new HBox(10, saveBtn, backBtn),
                new Separator(),
                progressLbl, progressBar, statusLabel);
        card.setMaxWidth(520);

        VBox root = new VBox(22, title, card);
        root.setPadding(new Insets(32));
        root.setStyle("-fx-background-color: " + Styles.BG + ";");

        return new Scene(root, 780, 480);
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
        Label title = Styles.subheading("Goals");

        Label formTitle = new Label("Create New Goal");
        formTitle.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 14));
        formTitle.setTextFill(Color.web(Styles.TEXT_DARK));

        nameField = new TextField(); nameField.setPromptText("Goal name (e.g. Buy a car)"); Styles.field(nameField);
        targetField = new TextField(); targetField.setPromptText("Target amount ($)"); Styles.field(targetField);

        deadlinePicker = new DatePicker();
        deadlinePicker.setPromptText("Deadline"); deadlinePicker.setPrefHeight(42);

        addGoalBtn = Styles.ghostBtn("Add Goal", Styles.SUCCESS);
        backBtn    = Styles.ghostBtn("Back",     Styles.TEXT_MUTED);

        GridPane form = new GridPane();
        form.setHgap(12); form.setVgap(10);
        form.add(fl("Name"),     0, 0); form.add(nameField,      1, 0);
        form.add(fl("Target"),   0, 1); form.add(targetField,    1, 1);
        form.add(fl("Deadline"), 0, 2); form.add(deadlinePicker, 1, 2);
        form.add(new HBox(10, addGoalBtn, backBtn), 1, 3);

        ColumnConstraints cc1 = new ColumnConstraints(); cc1.setMinWidth(80);
        ColumnConstraints cc2 = new ColumnConstraints(); cc2.setHgrow(Priority.ALWAYS);
        form.getColumnConstraints().addAll(cc1, cc2);

        Label progressTitle = new Label("Update Progress");
        progressTitle.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 14));
        progressTitle.setTextFill(Color.web(Styles.TEXT_DARK));

        contributionField = new TextField();
        contributionField.setPromptText("Contribution amount ($)"); Styles.field(contributionField);

        updateBtn = Styles.ghostBtn("Update", Styles.PRIMARY);

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(420); progressBar.setPrefHeight(20);
        progressBar.setStyle("-fx-accent: " + Styles.PRIMARY + ";");

        statusLabel = new Label("Progress: 0%");
        statusLabel.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 13));
        statusLabel.setTextFill(Color.web(Styles.PRIMARY));

        VBox card = Styles.card(formTitle, form, new Separator(),
                progressTitle, contributionField, updateBtn, progressBar, statusLabel);
        card.setMaxWidth(540);

        VBox root = new VBox(22, title, card);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color: " + Styles.BG + ";");

        return new Scene(root, 780, 590);
    }

    private Label fl(String text) {
        Label l = new Label(text);
        l.setFont(Font.font(Styles.FONT, 13));
        l.setTextFill(Color.web(Styles.TEXT_MUTED));
        return l;
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
        Label title = Styles.subheading("Reports");

        generateBtn = Styles.ghostBtn("Generate Report", Styles.PRIMARY);
        exportBtn   = Styles.ghostBtn("Export CSV",      Styles.SUCCESS);
        backBtn     = Styles.ghostBtn("Back",             Styles.TEXT_MUTED);

        summaryTable = new TableView<>();
        summaryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(summaryTable, Priority.ALWAYS);
        summaryTable.setStyle("-fx-background-color: white;");

        TableColumn<Transaction, Integer> idCol     = new TableColumn<>("ID");     idCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        TableColumn<Transaction, String>  typeCol   = new TableColumn<>("Type");   typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Transaction, String>  amountCol = new TableColumn<>("Amount"); amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Transaction, String>  dateCol   = new TableColumn<>("Date");   dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Transaction, String>  descCol   = new TableColumn<>("Description"); descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        summaryTable.getColumns().addAll(idCol, typeCol, amountCol, dateCol, descCol);

        totalLabel = new Label("Total Expense: $0.00");
        totalLabel.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 15));
        totalLabel.setTextFill(Color.web(Styles.DANGER));

        VBox root = new VBox(16, title, new HBox(10, generateBtn, exportBtn, backBtn), summaryTable, totalLabel);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color: " + Styles.BG + ";");

        return new Scene(root, 980, 640);
    }

    public TableView<Transaction> getSummaryTable() { return summaryTable; }
    public Label                  getTotalLabel()   { return totalLabel; }
    public Button                 getGenerateBtn()  { return generateBtn; }
    public Button                 getExportBtn()    { return exportBtn; }
    public Button                 getBackBtn()      { return backBtn; }
    public Stage                  getStage()        { return stage; }
}