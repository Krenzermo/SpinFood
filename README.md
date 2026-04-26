# SpinFood

University group project (Summer 2024). A JavaFX desktop application that pairs participants into dinner groups based on their dietary preferences and home locations.

---

## What it does

Given a list of participants with dietary restrictions (vegan, vegetarian, meat-eater) and GPS coordinates, the app forms compatible pairs and organizes them into dinner groups. Each group gets a designated host. Participants who couldn't be matched are collected separately as successors.

The matching logic uses a greedy algorithm that weighs dietary compatibility against geographic distance to produce reasonable groupings without brute-forcing all combinations.

---

## Tech stack

- Java 17
- JavaFX 22 (FXML-based GUI)
- Maven
- JUnit 5

---

## Setup

Requires Java 17+ and Maven 3.6+.

```bash
git clone https://github.com/Krenzermo/SpinFood.git
cd SpinFood
mvn clean javafx:run
```

```bash
# Tests
mvn test
```

---

## Project structure

```
src/
  main/java/        # application logic & algorithm
  main/resources/   # FXML layouts
  test/java/        # unit tests
Dokumentation/      # full project documentation (German)
pom.xml
```

---

## Authors

- Finn Brecher
- Daniel Hinkelmann
- Moritz Ole Krenzer
- Davide Piacenza
