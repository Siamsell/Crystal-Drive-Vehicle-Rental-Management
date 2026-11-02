# Crystal Drive — Vehicle Rental Management

A **Java Swing** desktop application for managing a vehicle rental agency, backed by a **MySQL** database.

## Features

- **Authentication** with email and password, with role-based access (administrator, manager, client) and account creation.
- **Vehicle management**: add, edit, delete and browse vehicles.
- **Client management**.
- **Reservations and payments**: create, track and manage bookings.
- **Vehicle returns**.
- **Reporting**: dashboard and statistics.

## Tech Stack

| | |
|---|---|
| Language | Java 17 |
| UI | Swing (AWT) |
| Database | MySQL (JDBC) |
| Build tool | Maven |

## Getting Started

### Prerequisites
- JDK 17 or later
- MySQL server running locally
- Maven (or an IDE with Maven support, such as IntelliJ IDEA)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/<your-username>/crystaldrive.git
   cd crystaldrive
   ```

2. **Create the database** named `location_vehicules` with the tables `utilisateur`, `client`, `vehicule`, `reservation`, `retour` and `paiement`.

3. **Check the connection settings**. By default the app connects to `localhost:3306` with user `root` and an empty password. Adjust them in `DatabaseConnection.java` if your setup differs.

4. **Build the project**
   ```bash
   mvn clean compile
   ```

5. **Run the app** by launching the class `dashboar.GestationLitterateursGUI` (the login page) from your IDE.

## Project Structure

```
crystaldrive/
├── pom.xml
└── src/main/
    ├── java/dashboar/      # Swing screens, DAOs and models
    └── resources/images/   # Logos, backgrounds and icons
```

