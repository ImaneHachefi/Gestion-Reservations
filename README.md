# Gestion-Reservations

TP de persistance avec **JPA / Hibernate** et une base **H2 en mémoire** : modélisation d'un système de réservation de salles avec des utilisateurs, des salles, des réservations et des équipements.

## Objectifs

- Créer les entités `User`, `Salle`, `Reservation` et `Equipement`
- Modéliser des relations `@OneToMany` / `@ManyToOne` bidirectionnelles
- Modéliser une relation `@ManyToMany` avec table de jointure entre `Salle` et `Equipement`
- Utiliser les cascades (`ALL`, `PERSIST`, `MERGE`) et `orphanRemoval`
- Écrire une requête JPQL avec jointure
- Vérifier les tables et les données dans la console H2

## Technologies

| Outil | Version |
|-------|---------|
| Java | 17 |
| Maven | 3.x |
| Hibernate (hibernate-entitymanager) | 5.6.15.Final |
| H2 Database | 2.1.214 |

## Structure du projet

```
src/main/java/main/
├── model/
│   ├── User.java
│   ├── Salle.java
│   ├── Equipement.java
│   ├── Reservation.java
│   └── StatutReservation.java

└── App.java
src/main/resources/META-INF/persistence.xml
```

## Modèle de données

| Entité | Attributs principaux | Relations |
|--------|---------------------|-----------|
| `User` (table `utilisateur`) | `id`, `prenom`, `nom`, `email` (unique) | 1 — N `Reservation` |
| `Salle` | `id`, `nom` (unique), `capacite`, `batiment`, `etage` | 1 — N `Reservation`, N — N `Equipement` |
| `Equipement` | `id`, `libelle` (unique) | N — N `Salle` |
| `Reservation` | `id`, `dateDebut`, `dateFin`, `motif`, `statut` | N — 1 `User`, N — 1 `Salle` |

> La table de `User` s'appelle `utilisateur` car `USER` est un mot réservé dans H2 2.x.

### Choix de conception

- `Reservation` est le côté propriétaire des relations avec `User` et `Salle` (elle porte les clés étrangères).
- `Salle` est le côté propriétaire de la relation ManyToMany : la table de jointure `salle_equipement` contient `salle_id` et `equipement_id`.
- `CascadeType.REMOVE` n'est pas utilisé sur le ManyToMany pour ne pas supprimer des équipements partagés entre plusieurs salles.
- `orphanRemoval = true` supprime automatiquement les réservations retirées d'un utilisateur ou d'une salle.
- Le statut de la réservation est un `enum` stocké en texte (`EnumType.STRING`).
- Des méthodes utilitaires (`ajouterReservation`, `ajouterEquipement`) synchronisent les deux côtés des relations.
- La validation des dates (fin postérieure au début) est faite dans le constructeur de `Reservation`.

## Lancer le projet


```bash
git clone https://github.com/ImaneHachefi/Gestion-Reservations.git
cd Gestion-Reservations
mvn compile exec:java -Dexec.mainClass="main.App"
```

Le programme démarre la console H2, crée les données, puis attend une touche avant de s'arrêter.

## Accès à la console H2

- URL : http://localhost:8082
- JDBC URL : `jdbc:h2:mem:testdb`
- User : `sa`
- Mot de passe : *(vide)*

La base est en mémoire : elle n'existe que pendant l'exécution du programme.

## Exécution du programme

<img width="737" height="639" alt="image" src="https://github.com/user-attachments/assets/5304b98f-53d6-451e-807a-5aba4ee67910" />
<img width="820" height="719" alt="image" src="https://github.com/user-attachments/assets/9fe8c346-8a4c-400c-be68-0e4d1b3161b3" />
<img width="1279" height="836" alt="image" src="https://github.com/user-attachments/assets/dce6a0e5-4072-4fbf-9ec7-bf87e5bf9f6b" />


## Vérification de la base dans la console H2

### Connexion

<img width="804" height="471" alt="Capture d&#39;écran 2026-10-14 162249" src="https://github.com/user-attachments/assets/2242c25b-006d-45f5-916f-0819c6f6d7c9" />

### Tables générées

<img width="1808" height="1025" alt="Capture d&#39;écran 2026-10-14 162602" src="https://github.com/user-attachments/assets/11f0106a-8362-4944-b58c-da098a7dfa02" />


### Table `UTILISATEUR`

```sql
SELECT * FROM UTILISATEUR;
```
<img width="1376" height="561" alt="image" src="https://github.com/user-attachments/assets/11f5d17e-b0b5-4413-beea-f1af8595a028" />

### Table `SALLE`

```sql
SELECT * FROM SALLE;
```
<img width="1309" height="554" alt="image" src="https://github.com/user-attachments/assets/e1d7bdba-fdd5-4f24-af8d-35719e452fd8" />

### Table `EQUIPEMENT`

```sql
SELECT * FROM EQUIPEMENT;
```
<img width="1361" height="622" alt="image" src="https://github.com/user-attachments/assets/4c25fce6-3772-4d98-816e-580a2724f7df" />

### Table de jointure `SALLE_EQUIPEMENT`

```sql
SELECT * FROM SALLE_EQUIPEMENT;
```
<img width="1315" height="597" alt="image" src="https://github.com/user-attachments/assets/b81b392e-266c-4a38-abf6-2206bdeb1013" />

### Table `RESERVATION`

```sql
SELECT * FROM RESERVATION;
```
<img width="1325" height="611" alt="image" src="https://github.com/user-attachments/assets/b62a88f1-c49a-45c6-9c4d-8eb32fd612dd" />

### Salles et leurs équipements

```sql
SELECT s.nom AS salle, e.libelle AS equipement
FROM SALLE s
JOIN SALLE_EQUIPEMENT se ON se.salle_id = s.id
JOIN EQUIPEMENT e ON e.id = se.equipement_id;
```
<img width="1310" height="622" alt="image" src="https://github.com/user-attachments/assets/2156a83f-1d05-4bdf-9fe8-f379e7c8bd23" />

### Réservations avec utilisateur et salle

```sql
SELECT u.nom, s.nom AS salle, r.dateDebut, r.dateFin, r.statut
FROM RESERVATION r
JOIN UTILISATEUR u ON u.id = r.utilisateur_id
JOIN SALLE s ON s.id = r.salle_id;
```
<img width="1301" height="588" alt="image" src="https://github.com/user-attachments/assets/a9a0aabb-472c-4ef4-8383-3741af838905" />

## Requête JPQL

```java
em.createQuery(
    "SELECT DISTINCT s FROM Salle s JOIN s.equipements e WHERE e.libelle = :libelle", Salle.class)
  .setParameter("libelle", "Projecteur")
  .getResultList();
```
<img width="822" height="482" alt="image" src="https://github.com/user-attachments/assets/6d4f4646-0eaf-44f0-830b-127bfa93c520" />


