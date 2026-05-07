# 💰 Finance Tracker

A desktop personal finance management application built with **Java** and **JavaFX**, featuring a dark-themed UI, multi-user authentication, and real-time budget monitoring.

---

## 📸 Overview

Finance Tracker helps users take control of their money by recording income and expenses, setting budgets, tracking savings goals, and visualising spending patterns — all from a clean, dark-themed desktop interface.

---

## ✨ Features

- **User Authentication** — Register and log in with email/password; each user's data is stored separately.
- **Dashboard** — At-a-glance balance (total income minus expenses), recent transactions table, and spending charts.
- **Transaction Management** — Add, edit, and delete income or expense transactions with categories and dates.
- **Budget Monitoring** — Set spending limits per period; receive automatic alerts when nearing or exceeding limits.
- **Savings Goals** — Create goals with target amounts and deadlines; track progress with a visual progress bar.
- **Reports** — View financial summaries by period and export transaction data to CSV.
- **Notifications** — In-app notification centre for budget warnings and goal completions.
- **Profile & Settings** — Update display name, email, preferred currency, and password at any time.
- **Data Persistence** — User accounts and transactions are saved to local JSON files between sessions.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17+ |
| UI Framework | JavaFX |
| Styling | JavaFX CSS (dark navy theme) |
| Persistence | Local JSON files (hand-rolled parser) |
| Build Tool | Maven / Gradle *(configure as needed)* |

---

## 🗂️ Project Structure

```
src/
└── com/example/software_project/
    ├── Launcher.java       # Entry point — bootstraps the JavaFX Application
    ├── Controllers.java    # All MVC controllers (Login, Registration, Dashboard,
    │                       #   Transactions, Budgets, Goals, Reports,
    │                       #   Notifications, Profile, Settings)
    ├── Models.java         # Domain models & persistence layer
    │                       #   (User, Transaction, Category, Budget,
    │                       #    Goal, Report, Notification, UserStore)
    └── Views.java          # JavaFX scene builders for every screen
resources/
└── styles.css              # Global dark-theme stylesheet
```

---

## 🚀 Getting Started

### Prerequisites

- Java **17** or higher
- JavaFX SDK **17+** (if not bundled with your JDK)
- Maven **3.8+** or Gradle **7+**

### Clone & Run

```bash
# 1. Clone the repository
git clone https://github.com/your-username/finance-tracker.git
cd finance-tracker

# 2. Build (Maven example)
mvn clean javafx:run

# 3. Or run the JAR directly after packaging
java -jar target/finance-tracker.jar
```

> **Note:** Make sure `javafx.controls` and `javafx.fxml` modules are on the module path if you are using a JDK that does not bundle JavaFX.

### First Launch

1. Click **Create an account** on the login screen.
2. Fill in your name, email, password, and preferred currency.
3. Log in and start tracking your finances.

---

## 💾 Data Storage

All data is persisted locally in the application's working directory:

| File | Contents |
|---|---|
| `users.json` | Registered user accounts |
| `transactions_<userId>.json` | Transactions per user |

No external database or internet connection is required.

---

## 🏗️ Architecture

The application follows the **MVC (Model-View-Controller)** pattern:

- **Models** (`Models.java`) — Plain Java classes representing domain entities (`User`, `Transaction`, `Budget`, `Goal`, etc.) plus `UserStore`, which acts as the data-access layer.
- **Views** (`Views.java`) — JavaFX scene builders that construct the UI programmatically for each screen.
- **Controllers** (`Controllers.java`) — Wire together models and views; handle user events, validation, and navigation between screens.

---

## 🔔 Budget Alert System

Budgets are evaluated automatically every time the dashboard loads:

- **On Track** — spending is below 80 % of the limit.
- **Near Limit** — spending is between 80 % and 100 % of the limit → yellow warning notification.
- **Exceeded** — spending has surpassed the limit → red alert notification.

Duplicate notifications for the same budget state are suppressed.

---

## 📊 Charts & Visualisation

| Chart | Description |
|---|---|
| Pie Chart | Expense breakdown by category |
| Line Chart | Income vs. expense trend over time |
| Progress Bar | Individual savings goal completion |

---

## 🤝 Contributing

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/your-feature`.
3. Commit your changes: `git commit -m "Add your feature"`.
4. Push to the branch: `git push origin feature/your-feature`.
5. Open a Pull Request.

Please keep controllers, views, and models in their respective files and follow the existing code style.

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.
