#  Crystal Drive — Gestion de location de véhicules

Application de bureau **Java Swing** pour gérer une agence de location de véhicules, connectée à une base **MySQL**.

##  Fonctionnalités

- **Authentification** par e-mail / mot de passe, avec redirection selon le rôle (administrateur, gestionnaire, client) et création de compte.
- **Gestion des véhicules** : ajout, modification, suppression, consultation.
- **Gestion des clients**.
- **Réservations et paiements** : création, suivi et gestion des réservations.
- **Retour des véhicules**.
- **Reporting** : tableau de bord et statistiques.

##  Technologies

| | |
|---|---|
| Langage | Java 17 |
| Interface | Swing (AWT) |
| Base de données | MySQL (JDBC) |
| Build | Maven |

##  Lancer le projet

1. **Créer la base de données** MySQL nommée `location_vehicules` (tables : `utilisateur`, `client`, `vehicule`, `reservation`, `retour`, `paiement`).
2. **Vérifier la connexion** : par défaut `localhost:3306`, utilisateur `root`, mot de passe vide (voir `DatabaseConnection.java`).
3. Ouvrir le dossier dans **IntelliJ IDEA** (ou tout IDE compatible Maven) et laisser Maven télécharger le pilote MySQL.
4. Lancer la classe **`dashboar.GestationLitterateursGUI`** (page de connexion).

##  Structure

```
/src/main/
├── java/dashboar/     # Interfaces Swing, DAO, modèles
└── resources/images/  # Logos, fonds et icônes
```


