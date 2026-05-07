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

    static final String BG         = "#1A2030";   // dark navy background
    static final String CARD       = "#242E42";   // slightly lighter card
    static final String PRIMARY    = "#5B8FCC";   // bright blue for buttons
    static final String PRIMARY_H  = "#4A78B5";   // hover state
    static final String ACCENT     = "#E07A5F";   // orange accent
    static final String TEXT_DARK  = "#E8EBF0";   // light text on dark bg
    static final String TEXT_MUTED = "#9099A8";   // muted text
    static final String BORDER     = "#3A4556";   // subtle border
    static final String SUCCESS    = "#3DBE7A";   // green
    static final String WARNING    = "#F0A500";   // amber
    static final String DANGER     = "#E05C5C";   // red
    static final String SIDEBAR_BG = "#111827";   // deepest dark for sidebar
    static final String FONT       = "Segoe UI";

    static void field(Control f) {
        f.setStyle("-fx-background-color: #2E3A50; "
                + "-fx-border-color: " + BORDER + "; "
                + "-fx-border-radius: 6; -fx-background-radius: 6; "
                + "-fx-padding: 9 12; -fx-font-size: 13px; "
                + "-fx-font-family: '" + FONT + "'; "
                + "-fx-text-fill: " + TEXT_DARK + "; "
                + "-fx-prompt-text-fill: " + TEXT_MUTED + ";");
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

    // FIX 1: new helper — always dark text, for labels on card/BG backgrounds
    static Label plainLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font(FONT, 13));
        l.setTextFill(Color.web(TEXT_DARK));
        return l;
    }

    static VBox card(javafx.scene.Node... children) {
        VBox box = new VBox(14);
        box.getChildren().addAll(children);
        box.setPadding(new Insets(22));
        box.setStyle("-fx-background-color: " + CARD + "; -fx-background-radius: 12; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 18, 0, 0, 4); "
                + "-fx-border-color: #2A3650; -fx-border-radius: 12; -fx-border-width: 1;");
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
        // ── Branding ──────────────────────────────────────────────────
        Label ftIcon = new Label("₿");
        ftIcon.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 28));
        ftIcon.setTextFill(Color.web(Styles.PRIMARY));
        ftIcon.setStyle("-fx-background-color: rgba(91,143,204,0.15); "
                + "-fx-background-radius: 10; -fx-padding: 6 14;");

        Label appTitle = new Label("Finance Tracker");
        appTitle.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 20));
        appTitle.setTextFill(Color.web(Styles.PRIMARY));

        VBox brandBox = new VBox(8, ftIcon, appTitle);
        brandBox.setAlignment(Pos.CENTER);

        Label title    = Styles.heading("Welcome back");
        Label subtitle = Styles.muted("Sign in to continue to your account");

        Label emailLbl = Styles.plainLabel("Email address");
        emailLbl.setFont(Font.font(Styles.FONT, FontWeight.SEMI_BOLD, 12));

        emailField = new TextField();
        emailField.setPromptText("you@example.com");
        emailField.setPrefHeight(42);
        emailField.setPrefWidth(320);
        Styles.field(emailField);

        Label passLbl = Styles.plainLabel("Password");
        passLbl.setFont(Font.font(Styles.FONT, FontWeight.SEMI_BOLD, 12));

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(42);
        passwordField.setPrefWidth(320);
        Styles.field(passwordField);

        loginBtn = Styles.primaryBtn("Sign In");
        loginBtn.setPrefWidth(320);

        registerLink = Styles.linkBtn("Create an account");
        Label noAcct = Styles.muted("Don't have an account?");

        HBox regRow = new HBox(6, noAcct, registerLink);
        regRow.setAlignment(Pos.CENTER);

        VBox form = new VBox(10,
                emailLbl, emailField,
                passLbl, passwordField,
                new Region() {{ setPrefHeight(4); }},
                loginBtn,
                new Region() {{ setPrefHeight(2); }},
                regRow);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setMaxWidth(320);

        VBox card = Styles.card(title, subtitle, form);
        card.setMaxWidth(420);
        card.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(24, brandBox, card);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: " + Styles.BG + ";");

        Scene scene = new Scene(root, 520, 540);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return scene;
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

    private Stage            stage;
    private TextField        nameField;
    private TextField        emailField;
    private PasswordField    passwordField;
    private PasswordField    confirmField;
    private ComboBox<String> currencyBox;
    private Button           registerBtn;
    private Button           backToLoginLink;

    public RegistrationView(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        Label appTitle = new Label("Finance Tracker");
        appTitle.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 22));
        appTitle.setTextFill(Color.web(Styles.PRIMARY));

        Label title    = Styles.heading("Create account");
        Label subtitle = Styles.muted("Fill in your details to get started");

        Label nameLbl = fieldLabel("Full name");
        nameField = new TextField();
        nameField.setPromptText("Ahmed Mohamed");
        nameField.setPrefHeight(42); nameField.setPrefWidth(320);
        Styles.field(nameField);

        Label emailLbl = fieldLabel("Email address");
        emailField = new TextField();
        emailField.setPromptText("you@example.com");
        emailField.setPrefHeight(42); emailField.setPrefWidth(320);
        Styles.field(emailField);

        Label passLbl = fieldLabel("Password");
        passwordField = new PasswordField();
        passwordField.setPromptText("Minimum 6 characters");
        passwordField.setPrefHeight(42); passwordField.setPrefWidth(320);
        Styles.field(passwordField);

        Label confirmLbl = fieldLabel("Confirm password");
        confirmField = new PasswordField();
        confirmField.setPromptText("Repeat your password");
        confirmField.setPrefHeight(42); confirmField.setPrefWidth(320);
        Styles.field(confirmField);

        Label currencyLbl = fieldLabel("Currency");
        currencyBox = new ComboBox<>(FXCollections.observableArrayList("USD", "EUR", "GBP", "EGP", "SAR", "AED"));
        currencyBox.setValue("USD");
        currencyBox.setPrefHeight(42); currencyBox.setPrefWidth(320);
        currencyBox.setStyle("-fx-font-family: '" + Styles.FONT + "'; -fx-font-size: 13px;");

        registerBtn = Styles.primaryBtn("Create Account");
        registerBtn.setPrefWidth(320);

        backToLoginLink = Styles.linkBtn("Sign in instead");
        Label alreadyLbl = Styles.muted("Already have an account?");
        HBox loginRow = new HBox(6, alreadyLbl, backToLoginLink);
        loginRow.setAlignment(Pos.CENTER);

        VBox form = new VBox(8,
                nameLbl, nameField,
                emailLbl, emailField,
                passLbl, passwordField,
                confirmLbl, confirmField,
                currencyLbl, currencyBox,
                new Region() {{ setPrefHeight(4); }},
                registerBtn,
                new Region() {{ setPrefHeight(2); }},
                loginRow);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setMaxWidth(320);

        VBox card = Styles.card(title, subtitle, form);
        card.setMaxWidth(420);

        ScrollPane scroll = new ScrollPane(card);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox root = new VBox(24, appTitle, scroll);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: " + Styles.BG + ";");

        Scene scene = new Scene(root, 520, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return scene;
    }

    private Label fieldLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font(Styles.FONT, FontWeight.SEMI_BOLD, 12));
        l.setTextFill(Color.web(Styles.TEXT_DARK));
        return l;
    }

    public TextField        getNameField()       { return nameField; }
    public TextField        getEmailField()      { return emailField; }
    public PasswordField    getPasswordField()   { return passwordField; }
    public PasswordField    getConfirmField()    { return confirmField; }
    public ComboBox<String> getCurrencyBox()     { return currencyBox; }
    public Button           getRegisterBtn()     { return registerBtn; }
    public Button           getBackToLoginLink() { return backToLoginLink; }
    public Stage            getStage()           { return stage; }
}

// ─────────────────────────────────────────────
//  DASHBOARD VIEW
// ─────────────────────────────────────────────

class DashboardView {

    private Stage                      stage;
    private Label                      balanceLabel;
    private PieChart                   pieChart;
    private LineChart<String, Number>  lineChart;
    // NEW: navProfile opens the Profile / Settings screen
    private Button navTransactions, navBudget, navGoals, navReports, navProfile, navLogout;
    // FIX: recentTable displays the last 5 transactions (getRecentTransactions spec requirement)
    private TableView<Transaction>     recentTable;

    public DashboardView(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        // ── Sidebar branding ─────────────────────────────────────────────
        Label ftIcon = new Label("₿");   // decorative icon
        ftIcon.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 22));
        ftIcon.setTextFill(Color.web(Styles.PRIMARY));
        ftIcon.setStyle("-fx-background-color: rgba(91,143,204,0.18); "
                + "-fx-background-radius: 8; -fx-padding: 4 10;");

        Label appName = new Label("Finance Tracker");
        appName.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 15));
        appName.setTextFill(Color.WHITE);

        HBox brand = new HBox(10, ftIcon, appName);
        brand.setAlignment(Pos.CENTER_LEFT);
        brand.setPadding(new Insets(0, 0, 8, 0));

        navTransactions  = sidebarBtn("📊  Transactions");
        navBudget        = sidebarBtn("💰  Budget");
        navGoals         = sidebarBtn("🎯  Goals");
        navReports       = sidebarBtn("📄  Reports");
        navProfile       = sidebarBtn("👤  Profile & Settings");
        navLogout        = sidebarBtn("⎋   Logout");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox sidebar = new VBox(4, brand, new Separator(),
                navTransactions, navBudget, navGoals, navReports,
                navProfile, spacer, navLogout);
        sidebar.setPadding(new Insets(22, 16, 22, 16));
        sidebar.setPrefWidth(210);
        sidebar.setStyle("-fx-background-color: " + Styles.SIDEBAR_BG + "; "
                + "-fx-border-color: #1E2A3A; -fx-border-width: 0 1 0 0;");

        balanceLabel = new Label("0.00");
        balanceLabel.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 42));
        balanceLabel.setTextFill(Color.web(Styles.SUCCESS));

        Label balTitle = Styles.plainLabel("Current Balance");
        balTitle.setFont(Font.font(Styles.FONT, FontWeight.SEMI_BOLD, 12));
        balTitle.setTextFill(Color.web(Styles.TEXT_MUTED));

        Label balCurrencyHint = Styles.muted("Net income − expenses");

        // Accent bar at top of balance card
        Region accentBar = new Region();
        accentBar.setPrefHeight(4);
        accentBar.setStyle("-fx-background-color: " + Styles.SUCCESS + "; "
                + "-fx-background-radius: 4 4 0 0;");

        VBox balInner = new VBox(6, balTitle, balanceLabel, balCurrencyHint);
        balInner.setPadding(new Insets(16, 18, 18, 18));

        VBox balCard = new VBox(accentBar, balInner);
        balCard.setStyle("-fx-background-color: " + Styles.CARD + "; -fx-background-radius: 10; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 12, 0, 0, 3);");

        pieChart = new PieChart(FXCollections.observableArrayList());
        pieChart.setTitle("Spending by Category");
        pieChart.setPrefHeight(270);
        pieChart.setLegendVisible(true);
        pieChart.setAnimated(false);
        pieChart.setStyle("-fx-background-color: transparent;");
        Label pieTitle = Styles.plainLabel("Spending by Category");
        pieTitle.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 13));
        pieTitle.setTextFill(Color.web(Styles.TEXT_MUTED));
        VBox pieCard = Styles.card(pieTitle, pieChart);

        CategoryAxis xAxis = new CategoryAxis(); xAxis.setLabel("Month");
        NumberAxis   yAxis = new NumberAxis();   yAxis.setLabel("Amount ($)");
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Monthly Expense Overview");
        lineChart.setPrefHeight(230);
        lineChart.setAnimated(false);
        lineChart.setStyle("-fx-background-color: transparent;");
        VBox lineCard = Styles.card(lineChart);

        recentTable = new TableView<>();
        recentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        recentTable.setPrefHeight(185);
        recentTable.setStyle("-fx-background-color: " + Styles.CARD + ";");
        Label noRecent = new Label("No recent transactions");
        noRecent.setStyle("-fx-text-fill: " + Styles.TEXT_MUTED + ";");
        recentTable.setPlaceholder(noRecent);

        TableColumn<Transaction, String> rtDateCol = new TableColumn<>("Date");
        rtDateCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(cd.getValue().getDate().toString()));

        TableColumn<Transaction, String> rtTypeCol = new TableColumn<>("Type");
        rtTypeCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(cd.getValue().getType()));

        TableColumn<Transaction, String> rtAmtCol = new TableColumn<>("Amount");
        rtAmtCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(cd.getValue().getAmount().toPlainString()));

        TableColumn<Transaction, String> rtDescCol = new TableColumn<>("Description");
        rtDescCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(cd.getValue().getDescription()));

        recentTable.getColumns().addAll(rtDateCol, rtTypeCol, rtAmtCol, rtDescCol);

        Label recentTitle = Styles.plainLabel("Recent Transactions");
        recentTitle.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 14));
        recentTitle.setTextFill(Color.web(Styles.TEXT_DARK));
        Label recentSub = Styles.muted("Last 5 transactions");
        HBox recentHeader = new HBox(10, recentTitle);
        recentHeader.setAlignment(Pos.CENTER_LEFT);
        VBox recentCard = Styles.card(recentHeader, recentSub, recentTable);

        GridPane grid = new GridPane();
        grid.setHgap(18); grid.setVgap(18); grid.setPadding(new Insets(28));
        grid.add(balCard,    0, 0);
        grid.add(pieCard,    1, 0);
        grid.add(recentCard, 0, 1, 2, 1);
        grid.add(lineCard,   0, 2, 2, 1);

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

        Scene scene = new Scene(root, 980, 680);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return scene;
    }

    private Button sidebarBtn(String text) {
        Button b = new Button(text);
        b.setPrefWidth(182);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        String normal = "-fx-background-color: transparent; -fx-text-fill: #8A95A8; "
                + "-fx-font-size: 13px; -fx-font-family: '" + Styles.FONT + "'; "
                + "-fx-padding: 10 14; -fx-background-radius: 7; -fx-border-width: 0;";
        String hover  = "-fx-background-color: rgba(91,143,204,0.12); -fx-text-fill: #E8EBF0; "
                + "-fx-font-size: 13px; -fx-font-family: '" + Styles.FONT + "'; "
                + "-fx-padding: 10 14; -fx-background-radius: 7; "
                + "-fx-border-color: transparent transparent transparent " + Styles.PRIMARY + "; "
                + "-fx-border-width: 0 0 0 3;";
        b.setStyle(normal);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e  -> b.setStyle(normal));
        return b;
    }

    public Label                    getBalanceLabel()     { return balanceLabel; }
    public PieChart                 getPieChart()         { return pieChart; }
    public LineChart<String,Number> getLineChart()        { return lineChart; }
    public TableView<Transaction>   getRecentTable()      { return recentTable; }
    public Button                   getNavTransactions()  { return navTransactions; }
    public Button                   getNavBudget()        { return navBudget; }
    public Button                   getNavGoals()         { return navGoals; }
    public Button                   getNavReports()       { return navReports; }
    // NEW: expose navProfile so DashboardController can bind its action
    public Button                   getNavProfile()       { return navProfile; }
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
        Label title    = Styles.subheading("Transactions");
        Label subtitle = Styles.muted("Add, search, and manage all your financial transactions.");

        filterField = new TextField();
        filterField.setPromptText("🔍  Search by description, type, amount, date…");
        filterField.setPrefWidth(320); filterField.setPrefHeight(38);
        Styles.field(filterField);
        HBox.setHgrow(filterField, Priority.ALWAYS);

        addBtn    = Styles.primaryBtn("+ Add");
        deleteBtn = Styles.ghostBtn("Delete", Styles.DANGER);
        backBtn   = Styles.ghostBtn("← Back", Styles.TEXT_MUTED);

        HBox controls = new HBox(10, filterField, addBtn, deleteBtn, backBtn);
        controls.setAlignment(Pos.CENTER_LEFT);

        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        tableView.setStyle("-fx-background-color: " + Styles.CARD + "; -fx-border-color: " + Styles.BORDER + ";");

        // ROOT-CAUSE FIX: PropertyValueFactory uses reflection + JavaFX property conventions.
        // When the getter returns a non-String, non-primitive type (BigDecimal, LocalDate),
        // the generic type parameter on the column (e.g. TableColumn<Transaction, String>)
        // causes a silent ClassCastException inside JavaFX — the cell simply renders blank.
        // The ONLY reliable fix is to use explicit lambda cellValueFactories that return
        // a SimpleStringProperty wrapping the value's toString(). This works for every type.

        // Column: ID — int getter, wrap as String for consistent rendering
        TableColumn<Transaction, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(cd.getValue().getTransactionId())));
        idCol.setMaxWidth(60);

        // Column: Type — String getter, still use lambda for consistency
        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(
                        cd.getValue().getType()));

        // Column: Amount — BigDecimal getter (was BLANK with PropertyValueFactory<..,String>)
        // Fix: convert to plain string via toPlainString() inside the lambda
        TableColumn<Transaction, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(
                        cd.getValue().getAmount().toPlainString()));

        // Column: Date — LocalDate getter (was BLANK with PropertyValueFactory<...,String>)
        // Fix: convert to string via toString() inside the lambda
        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(
                        cd.getValue().getDate().toString()));

        // Column: Description — String getter
        TableColumn<Transaction, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(
                        cd.getValue().getDescription()));

        // Column: Category — nested object, read getName() safely with null guard
        TableColumn<Transaction, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(cd -> {
            Category cat = cd.getValue().getCategory();
            return new javafx.beans.property.SimpleStringProperty(
                    cat != null ? cat.getName() : "General");
        });

        tableView.getColumns().addAll(idCol, typeCol, amountCol, dateCol, descCol, catCol);

        VBox root = new VBox(14, title, subtitle, controls, tableView);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color: " + Styles.BG + ";");

        Scene scene = new Scene(root, 980, 680);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return scene;
    }

    public TableView<Transaction> getTableView()   { return tableView; }
    public TextField              getFilterField()  { return filterField; }
    public Button                 getAddBtn()       { return addBtn; }
    public Button                 getDeleteBtn()    { return deleteBtn; }
    public Button                 getBackBtn()      { return backBtn; }
    public Stage                  getStage()        { return stage; }
}

// ─────────────────────────────────────────────
//  BUDGET VIEW
// ─────────────────────────────────────────────

class BudgetView {

    private Stage       stage;
    private TextField   limitField;
    private Label       statusLabel;
    private Label       spentLabel;   // FIX 3: shows "Spent X / Limit Y" with dark text
    private ProgressBar progressBar;
    private Button      saveBtn, backBtn;

    public BudgetView(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        Label title = Styles.subheading("Budget Manager");
        Label subtitle = Styles.muted("Set a monthly spending limit and track your progress.");

        Label limitLbl = Styles.plainLabel("Monthly Budget Limit");
        limitLbl.setFont(Font.font(Styles.FONT, FontWeight.SEMI_BOLD, 12));

        limitField = new TextField();
        limitField.setPromptText("e.g. 2000.00");
        limitField.setPrefHeight(42);
        Styles.field(limitField);

        saveBtn = Styles.primaryBtn("Save Budget");
        backBtn = Styles.ghostBtn("← Back", Styles.TEXT_MUTED);

        Label progressLbl = Styles.plainLabel("Spending Progress");
        progressLbl.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 13));

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(Double.MAX_VALUE);
        progressBar.setPrefHeight(14);
        progressBar.setStyle("-fx-accent: " + Styles.SUCCESS + ";");

        spentLabel = new Label("Spent: — / Limit: —");
        spentLabel.setFont(Font.font(Styles.FONT, 13));
        spentLabel.setTextFill(Color.web(Styles.TEXT_DARK));

        statusLabel = new Label("Status: —");
        statusLabel.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 14));
        statusLabel.setTextFill(Color.web(Styles.TEXT_DARK));

        VBox inputSection = new VBox(10, limitLbl, limitField, new HBox(10, saveBtn, backBtn));
        VBox progressSection = new VBox(10, progressLbl, progressBar, spentLabel, statusLabel);

        VBox card = Styles.card(subtitle, inputSection, new Separator(), progressSection);
        card.setMaxWidth(540);

        VBox root = new VBox(22, title, card);
        root.setPadding(new Insets(32));
        root.setStyle("-fx-background-color: " + Styles.BG + ";");

        Scene scene = new Scene(root, 780, 500);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return scene;
    }

    public TextField   getLimitField()  { return limitField; }
    public Label       getSpentLabel()  { return spentLabel; }
    public Label       getStatusLabel() { return statusLabel; }
    public ProgressBar getProgressBar() { return progressBar; }
    public Button      getSaveBtn()     { return saveBtn; }
    public Button      getBackBtn()     { return backBtn; }
    public Stage       getStage()       { return stage; }
}

// ─────────────────────────────────────────────
//  GOAL VIEW
// FIX 2: Added ListView so all goals are stored and visible.
//        User selects a goal from the list to update its progress independently.
// ─────────────────────────────────────────────

class GoalView {

    private Stage            stage;
    private ProgressBar      progressBar;
    private Label            statusLabel;
    private DatePicker       deadlinePicker;
    private TextField        nameField, targetField, contributionField;
    private Button           addGoalBtn, updateBtn, backBtn;
    // FIX 2: ListView holds display strings; selection tells Controller which goal is active
    private ListView<String> goalListView;

    public GoalView(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        Label title = Styles.subheading("Goals");

        // ── Create New Goal ──
        Label formTitle = Styles.plainLabel("Create New Goal");
        formTitle.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 14));

        nameField = new TextField(); nameField.setPromptText("Goal name (e.g. Buy a car)"); Styles.field(nameField);
        targetField = new TextField(); targetField.setPromptText("Target amount ($)"); Styles.field(targetField);
        deadlinePicker = new DatePicker();
        deadlinePicker.setPromptText("Deadline"); deadlinePicker.setPrefHeight(42);

        addGoalBtn = Styles.ghostBtn("Add Goal", Styles.SUCCESS);
        backBtn    = Styles.ghostBtn("← Back",  Styles.TEXT_MUTED);

        GridPane form = new GridPane();
        form.setHgap(12); form.setVgap(10);
        form.add(fl("Name"),     0, 0); form.add(nameField,      1, 0);
        form.add(fl("Target"),   0, 1); form.add(targetField,    1, 1);
        form.add(fl("Deadline"), 0, 2); form.add(deadlinePicker, 1, 2);
        form.add(new HBox(10, addGoalBtn, backBtn), 1, 3);

        ColumnConstraints cc1 = new ColumnConstraints(); cc1.setMinWidth(80);
        ColumnConstraints cc2 = new ColumnConstraints(); cc2.setHgrow(Priority.ALWAYS);
        form.getColumnConstraints().addAll(cc1, cc2);

        // ── Goals list ──
        Label listTitle = Styles.plainLabel("Your Goals — select one to update");
        listTitle.setFont(Font.font(Styles.FONT, FontWeight.SEMI_BOLD, 13));

        goalListView = new ListView<>();
        goalListView.setPrefHeight(120);

        // ── Update progress ──
        Label progressTitle = Styles.plainLabel("Update Selected Goal");
        progressTitle.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 14));

        contributionField = new TextField();
        contributionField.setPromptText("Contribution amount ($)"); Styles.field(contributionField);
        updateBtn = Styles.ghostBtn("Update", Styles.PRIMARY);

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(420); progressBar.setPrefHeight(20);
        progressBar.setStyle("-fx-accent: " + Styles.PRIMARY + ";");

        // FIX 1: statusLabel starts with dark text; Controller sets colour per state
        statusLabel = new Label("Select a goal to see progress");
        statusLabel.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 13));
        statusLabel.setTextFill(Color.web(Styles.TEXT_DARK));

        VBox card = Styles.card(
                formTitle, form,
                new Separator(),
                listTitle, goalListView,
                new Separator(),
                progressTitle, contributionField, updateBtn, progressBar, statusLabel);
        card.setMaxWidth(560);

        VBox inner = new VBox(22, title, card);
        inner.setPadding(new Insets(28));
        inner.setStyle("-fx-background-color: " + Styles.BG + ";");

        ScrollPane scroll = new ScrollPane(inner);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + Styles.BG + "; -fx-background-color: " + Styles.BG + ";");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scroll, 780, 680);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return scene;
    }

    private Label fl(String text) {
        Label l = new Label(text);
        l.setFont(Font.font(Styles.FONT, 13));
        l.setTextFill(Color.web(Styles.TEXT_MUTED));
        return l;
    }

    public ProgressBar      getProgressBar()       { return progressBar; }
    public Label            getStatusLabel()       { return statusLabel; }
    public DatePicker       getDeadlinePicker()    { return deadlinePicker; }
    public TextField        getNameField()         { return nameField; }
    public TextField        getTargetField()       { return targetField; }
    public TextField        getContributionField() { return contributionField; }
    public Button           getAddGoalBtn()        { return addGoalBtn; }
    public Button           getUpdateBtn()         { return updateBtn; }
    public Button           getBackBtn()           { return backBtn; }
    public ListView<String> getGoalListView()      { return goalListView; }
    public Stage            getStage()             { return stage; }
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
        Label title    = Styles.subheading("Reports");
        Label subtitle = Styles.muted("Generate a spending report or export it as a CSV file.");

        generateBtn = Styles.primaryBtn("Generate Report");
        exportBtn   = Styles.ghostBtn("Export CSV",    Styles.SUCCESS);
        backBtn     = Styles.ghostBtn("← Back",         Styles.TEXT_MUTED);

        HBox toolbar = new HBox(10, generateBtn, exportBtn, backBtn);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        summaryTable = new TableView<>();
        summaryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(summaryTable, Priority.ALWAYS);
        summaryTable.setStyle("-fx-background-color: " + Styles.CARD + ";");

        // Same fix as TransactionView: use explicit lambda cellValueFactories
        // to avoid silent ClassCastException from PropertyValueFactory type mismatches.
        TableColumn<Transaction, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(cd.getValue().getTransactionId())));

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(
                        cd.getValue().getType()));

        TableColumn<Transaction, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(
                        cd.getValue().getAmount().toPlainString()));

        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(
                        cd.getValue().getDate().toString()));

        TableColumn<Transaction, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cd ->
                new javafx.beans.property.SimpleStringProperty(
                        cd.getValue().getDescription()));

        TableColumn<Transaction, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(cd -> {
            Category cat = cd.getValue().getCategory();
            return new javafx.beans.property.SimpleStringProperty(
                    cat != null ? cat.getName() : "General");
        });

        summaryTable.getColumns().addAll(idCol, typeCol, amountCol, dateCol, descCol, catCol);


        totalLabel = new Label("Total Expense: $0.00");
        totalLabel.setFont(Font.font(Styles.FONT, FontWeight.BOLD, 15));
        totalLabel.setTextFill(Color.web(Styles.DANGER));

        VBox root = new VBox(16, title, subtitle, toolbar, summaryTable, totalLabel);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color: " + Styles.BG + ";");

        Scene scene = new Scene(root, 980, 640);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return scene;
    }

    public TableView<Transaction> getSummaryTable() { return summaryTable; }
    public Label                  getTotalLabel()   { return totalLabel; }
    public Button                 getGenerateBtn()  { return generateBtn; }
    public Button                 getExportBtn()    { return exportBtn; }
    public Button                 getBackBtn()      { return backBtn; }
    public Stage                  getStage()        { return stage; }
}

// ─────────────────────────────────────────────
//  NOTIFICATION VIEW
//  Displays unread/read notifications for the current user.
//  Controller populates ListView via requestNotifications().
//  "Mark as Read" button triggers the markAsRead() flow.
// ─────────────────────────────────────────────

class NotificationView {

    private Stage           stage;
    private ListView<String> listView;
    private Button          markReadBtn, markAllBtn, backBtn;

    public NotificationView(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        Label title    = Styles.subheading("Notifications");
        Label subtitle = Styles.muted("Budget alerts and system messages appear here.");

        listView = new ListView<>();
        listView.setPrefHeight(380);
        Label emptyLbl = new Label("No new notifications");
        emptyLbl.setStyle("-fx-text-fill: " + Styles.TEXT_MUTED + ";");
        listView.setPlaceholder(emptyLbl);
        listView.setStyle("-fx-background-color: " + Styles.CARD + ";");
        VBox.setVgrow(listView, Priority.ALWAYS);

        markReadBtn = Styles.primaryBtn("Mark Selected as Read");
        markAllBtn  = Styles.ghostBtn("Mark All as Read", Styles.SUCCESS);
        backBtn     = Styles.ghostBtn("← Back",           Styles.TEXT_MUTED);

        HBox buttons = new HBox(10, markReadBtn, markAllBtn, backBtn);
        buttons.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        VBox root = new VBox(16, title, subtitle, listView, buttons);
        root.setPadding(new javafx.geometry.Insets(28));
        root.setStyle("-fx-background-color: " + Styles.BG + ";");

        Scene scene = new Scene(root, 780, 560);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return scene;
    }

    public ListView<String> getListView()     { return listView; }
    public Button           getMarkReadBtn()  { return markReadBtn; }
    public Button           getMarkAllBtn()   { return markAllBtn; }
    public Button           getBackBtn()      { return backBtn; }
    public Stage            getStage()        { return stage; }
}

// ─────────────────────────────────────────────
//  PROFILE VIEW
//  Sequence diagram: UI → requestUserData() → displayProfile(userData)
//  Read-only display of the current user's profile information.
//  "Edit Settings" button navigates to SettingsView.
// ─────────────────────────────────────────────

class ProfileView {

    private Stage  stage;
    private Label  nameValueLabel;
    private Label  emailValueLabel;
    private Label  currencyValueLabel;
    private Button editSettingsBtn;
    private Button backBtn;

    public ProfileView(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        Label pageTitle = Styles.subheading("My Profile");

        // ── Profile info rows ──────────────────────────────────────────
        Label nameLbl     = fieldLabel("Full Name");
        nameValueLabel    = valueLabel("—");

        Label emailLbl    = fieldLabel("Email Address");
        emailValueLabel   = valueLabel("—");

        Label currencyLbl = fieldLabel("Currency");
        currencyValueLabel= valueLabel("—");

        GridPane grid = new GridPane();
        grid.setHgap(24); grid.setVgap(16);
        grid.add(nameLbl,          0, 0); grid.add(nameValueLabel,     1, 0);
        grid.add(emailLbl,         0, 1); grid.add(emailValueLabel,    1, 1);
        grid.add(currencyLbl,      0, 2); grid.add(currencyValueLabel, 1, 2);

        ColumnConstraints cc1 = new ColumnConstraints(); cc1.setMinWidth(130);
        ColumnConstraints cc2 = new ColumnConstraints(); cc2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(cc1, cc2);

        // ── Buttons ────────────────────────────────────────────────────
        editSettingsBtn = Styles.primaryBtn("Edit Settings");
        backBtn         = Styles.ghostBtn("Back to Dashboard", Styles.TEXT_MUTED);
        HBox btnRow     = new HBox(12, editSettingsBtn, backBtn);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = Styles.card(grid, new Separator(), btnRow);
        card.setMaxWidth(520);

        VBox root = new VBox(24, pageTitle, card);
        root.setPadding(new Insets(36));
        root.setStyle("-fx-background-color: " + Styles.BG + ";");

        Scene scene = new Scene(root, 680, 400);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return scene;
    }

    // ── Helpers ───────────────────────────────────────────────────────
    private Label fieldLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font(Styles.FONT, FontWeight.SEMI_BOLD, 13));
        l.setTextFill(Color.web(Styles.TEXT_MUTED));
        return l;
    }

    private Label valueLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font(Styles.FONT, 14));
        l.setTextFill(Color.web(Styles.TEXT_DARK));
        return l;
    }

    // ── Getters used by ProfileController ────────────────────────────
    public Label  getNameValueLabel()     { return nameValueLabel; }
    public Label  getEmailValueLabel()    { return emailValueLabel; }
    public Label  getCurrencyValueLabel() { return currencyValueLabel; }
    public Button getEditSettingsBtn()    { return editSettingsBtn; }
    public Button getBackBtn()            { return backBtn; }
    public Stage  getStage()              { return stage; }
}

// ─────────────────────────────────────────────
//  SETTINGS VIEW
//  Sequence diagram: displayOptions() → user edits → updateProfile(data)
//  Editable form for name, email, currency, and optional password change.
// ─────────────────────────────────────────────

class SettingsView {

    private Stage            stage;
    private TextField        nameField;
    private TextField        emailField;
    private ComboBox<String> currencyBox;
    private PasswordField    newPasswordField;
    private PasswordField    confirmPasswordField;
    private Button           saveBtn;
    private Button           cancelBtn;

    public SettingsView(Stage stage) { this.stage = stage; }

    public Scene getScene() {
        Label pageTitle = Styles.subheading("Settings");
        Label subtitle  = Styles.muted("Update your profile information below.");

        // ── Name ──────────────────────────────────────────────────────
        Label nameLbl = fieldLabel("Full Name");
        nameField = new TextField();
        nameField.setPromptText("Your full name");
        nameField.setPrefHeight(42);
        Styles.field(nameField);

        // ── Email ─────────────────────────────────────────────────────
        Label emailLbl = fieldLabel("Email Address");
        emailField = new TextField();
        emailField.setPromptText("you@example.com");
        emailField.setPrefHeight(42);
        Styles.field(emailField);

        // ── Currency ──────────────────────────────────────────────────
        Label currencyLbl = fieldLabel("Currency");
        currencyBox = new ComboBox<>(FXCollections.observableArrayList(
                "USD", "EUR", "GBP", "EGP", "SAR", "AED"));
        currencyBox.setPrefHeight(42);
        currencyBox.setPrefWidth(320);
        currencyBox.setStyle("-fx-font-family: '" + Styles.FONT + "'; -fx-font-size: 13px;");

        // ── Password (optional) ───────────────────────────────────────
        Label passwordSection = Styles.plainLabel("Change Password (leave blank to keep current)");
        passwordSection.setFont(Font.font(Styles.FONT, FontWeight.SEMI_BOLD, 12));
        passwordSection.setTextFill(Color.web(Styles.TEXT_MUTED));

        Label newPassLbl = fieldLabel("New Password");
        newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Minimum 6 characters");
        newPasswordField.setPrefHeight(42);
        Styles.field(newPasswordField);

        Label confirmPassLbl = fieldLabel("Confirm New Password");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Repeat new password");
        confirmPasswordField.setPrefHeight(42);
        Styles.field(confirmPasswordField);

        // ── Buttons ───────────────────────────────────────────────────
        saveBtn   = Styles.primaryBtn("Save Changes");
        cancelBtn = Styles.ghostBtn("Cancel", Styles.TEXT_MUTED);

        HBox btnRow = new HBox(12, saveBtn, cancelBtn);
        btnRow.setAlignment(Pos.CENTER_LEFT);
        btnRow.setPadding(new Insets(6, 0, 0, 0));

        // ── Layout ────────────────────────────────────────────────────
        VBox form = new VBox(10,
                nameLbl,         nameField,
                emailLbl,        emailField,
                currencyLbl,     currencyBox,
                new Separator(),
                passwordSection,
                newPassLbl,      newPasswordField,
                confirmPassLbl,  confirmPasswordField,
                new Region() {{ setPrefHeight(4); }},
                btnRow);
        form.setMaxWidth(380);

        VBox card = Styles.card(subtitle, form);
        card.setMaxWidth(460);

        VBox root = new VBox(24, pageTitle, card);
        root.setPadding(new Insets(36));
        root.setStyle("-fx-background-color: " + Styles.BG + ";");

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: " + Styles.BG + "; -fx-background-color: " + Styles.BG + ";");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scroll, 680, 640);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return scene;
    }

    private Label fieldLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font(Styles.FONT, FontWeight.SEMI_BOLD, 12));
        l.setTextFill(Color.web(Styles.TEXT_DARK));
        return l;
    }

    // ── Getters used by SettingsController ───────────────────────────
    public TextField        getNameField()            { return nameField; }
    public TextField        getEmailField()            { return emailField; }
    public ComboBox<String> getCurrencyBox()           { return currencyBox; }
    public PasswordField    getNewPasswordField()      { return newPasswordField; }
    public PasswordField    getConfirmPasswordField()  { return confirmPasswordField; }
    public Button           getSaveBtn()               { return saveBtn; }
    public Button           getCancelBtn()             { return cancelBtn; }
    public Stage            getStage()                 { return stage; }
}