# Fiche technique — JobPilot Secure

## Présentation

JobPilot Secure est une application web full-stack permettant à un utilisateur de gérer ses candidatures, suivre leur statut, planifier des relances et visualiser un tableau de bord sécurisé.

Le projet a été conçu comme une application professionnelle orientée portfolio, avec une architecture claire, une API REST sécurisée, un frontend moderne et une documentation complète.

---

## Objectifs du projet

- Centraliser le suivi des candidatures
- Sécuriser l’accès aux données utilisateur
- Gérer les statuts de candidature
- Planifier et suivre les relances
- Fournir un tableau de bord synthétique
- Mettre en pratique une architecture Java / Spring Boot / React
- Ajouter des pratiques professionnelles : tests, logs, documentation, sécurité

---

## Stack technique

### Backend

- Java 21
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- PostgreSQL
- Flyway
- Maven
- JUnit
- Mockito
- Swagger / OpenAPI

### Frontend

- React
- TypeScript
- Vite
- Tailwind CSS
- Axios
- React Router
- Lucide React

### Base de données

- PostgreSQL
- Migrations Flyway
- Docker Compose pour l’environnement local

---

## Architecture globale

```text
Frontend React
     |
     | HTTP / JSON
     |
Backend Spring Boot
     |
     | JPA / Hibernate
     |
PostgreSQL
```

---

## Architecture backend

Le backend est organisé par domaines fonctionnels.

```text
backend/src/main/java/com/jobpilot/backend/
├── auth/
├── user/
├── jobapplication/
├── dashboard/
├── common/
└── config/
```

### Rôle des packages

#### auth

Gestion de l’inscription, de la connexion et de la génération du token JWT.

#### user

Gestion de l’utilisateur connecté et récupération du profil via `/api/users/me`.

#### jobapplication

Gestion complète des candidatures :

- création
- modification
- suppression
- consultation
- recherche
- filtres
- pagination
- tri
- favori
- statut
- relance

#### dashboard

Gestion des données synthétiques :

- statistiques
- résumé des actions
- relances du jour
- relances en retard
- relances à venir
- candidatures sans relance

#### common

Fonctionnalités transverses :

- gestion des erreurs
- logs
- exceptions
- réponses communes

#### config

Configuration de sécurité, JWT, OpenAPI et autres composants techniques.

---

## Architecture frontend

Le frontend est structuré par responsabilité.

```text
frontend/src/
├── api/
├── layouts/
├── pages/
├── routes/
├── types/
└── utils/
```

### Rôle des dossiers

#### api

Centralisation des appels HTTP vers le backend avec Axios.

#### layouts

Layout principal des pages connectées :

- sidebar
- header
- navigation
- bouton déconnexion

#### pages

Pages principales :

- connexion
- inscription
- dashboard
- liste des candidatures
- formulaire de candidature

#### routes

Protection des routes :

- routes publiques uniquement
- routes protégées par JWT

#### types

Types TypeScript utilisés par le frontend.

#### utils

Fonctions utilitaires, notamment la gestion du stockage du token JWT.

---

## Sécurité

Le projet applique plusieurs mécanismes de sécurité.

### Authentification

- Authentification par email et mot de passe
- Génération d’un token JWT après connexion
- Envoi du token dans le header HTTP `Authorization`
- Protection des endpoints privés côté backend
- Protection des routes privées côté frontend

### Isolation des données

Chaque candidature est liée à un utilisateur.

Un utilisateur ne peut accéder qu’à ses propres candidatures.

Les requêtes sensibles utilisent l’identifiant de l’utilisateur connecté pour éviter l’accès aux données d’un autre utilisateur.

### Validation métier

Le backend applique plusieurs règles métier :

- interdiction de modifier le statut d’une candidature terminée
- interdiction de modifier la relance d’une candidature terminée
- validation des dates de relance
- exclusion des candidatures terminées des relances actives
- limitation de la taille des pages
- tri limité à des champs autorisés

### Logs

Les logs API affichent :

- méthode HTTP
- chemin de la requête
- statut HTTP
- durée d’exécution
- identifiant de requête

Les logs n’exposent pas :

- token JWT
- mot de passe
- body sensible

---

## Fonctionnalités backend

### Authentification

```http
POST /api/auth/register
POST /api/auth/login
GET /api/users/me
```

### Candidatures

```http
GET /api/job-applications
POST /api/job-applications
GET /api/job-applications/{id}
PUT /api/job-applications/{id}
DELETE /api/job-applications/{id}
PATCH /api/job-applications/{id}/status
PATCH /api/job-applications/{id}/favorite
PATCH /api/job-applications/{id}/follow-up
```

### Dashboard

```http
GET /api/dashboard/stats
GET /api/dashboard/action-summary
GET /api/dashboard/recent-applications
GET /api/dashboard/today-follow-ups
GET /api/dashboard/overdue-follow-ups
GET /api/dashboard/upcoming-follow-ups
GET /api/dashboard/applications-without-follow-up
```

---

## Fonctionnalités frontend

### Authentification

- Page de connexion
- Page d’inscription
- Sauvegarde du token JWT
- Redirection après connexion
- Déconnexion
- Redirection automatique si l’utilisateur n’est pas connecté

### Dashboard

- Affichage des statistiques
- Affichage du résumé d’actions
- Affichage des relances :
  - aujourd’hui
  - en retard
  - à venir

### Candidatures

- Liste des candidatures
- Recherche
- Filtre par statut
- Filtre favoris
- Tri
- Pagination
- Création d’une candidature
- Modification d’une candidature
- Actions rapides :
  - changement de statut
  - ajout / retrait favori
  - modification de la date de relance

---

## Modèle principal : JobApplication

Une candidature contient notamment :

- entreprise
- intitulé du poste
- localisation
- type de contrat
- statut
- source
- URL de l’offre
- notes
- date de candidature
- date de création
- date de modification
- date de relance
- favori
- utilisateur propriétaire

---

## Statuts possibles

```text
SAVED
APPLIED
INTERVIEW
OFFER
REJECTED
WITHDRAWN
```

### Signification

- `SAVED` : candidature sauvegardée mais pas encore envoyée
- `APPLIED` : candidature envoyée
- `INTERVIEW` : entretien en cours
- `OFFER` : offre reçue
- `REJECTED` : candidature refusée
- `WITHDRAWN` : candidature retirée

---

## Base de données

Le projet utilise PostgreSQL avec Flyway pour gérer les migrations.

Flyway permet de versionner les évolutions du schéma de base de données.

Exemples d’évolutions :

- création de la table users
- ajout des rôles utilisateur
- création de la table job_applications
- ajout des favoris
- ajout des dates de relance

---

## Tests

Le projet contient des tests unitaires backend.

### Tests dashboard

Les tests vérifient notamment :

- le calcul des statistiques
- le résumé des actions du dashboard

### Tests relances

Les tests vérifient notamment :

- ajout d’une date de relance
- suppression d’une date de relance
- blocage des relances pour les candidatures terminées
- refus d’une date de relance avant la création de la candidature

---

## Documentation API

La documentation Swagger / OpenAPI est disponible localement :

```text
http://localhost:8080/swagger-ui.html
```

Le JSON OpenAPI est disponible ici :

```text
http://localhost:8080/v3/api-docs
```

---

## Points forts du projet

- Application full-stack complète
- Backend sécurisé avec JWT
- Frontend moderne avec React et TypeScript
- Architecture claire par domaines
- Gestion avancée des candidatures
- Dashboard utile et orienté actions
- Documentation Swagger
- Tests unitaires
- Logs propres
- Pagination et tri sécurisés
- Gestion des relances avec règles métier

---

## Compétences démontrées

- Développement Java / Spring Boot
- Sécurité web avec Spring Security et JWT
- Création d’API REST
- Gestion de base de données PostgreSQL
- Migrations Flyway
- Développement React / TypeScript
- Intégration frontend/backend
- Gestion d’état côté frontend
- Appels API avec Axios
- Routing protégé avec React Router
- Tests unitaires avec JUnit et Mockito
- Documentation technique
- Bonnes pratiques de code
- Organisation d’un projet professionnel

---

## Utilisation dans un CV

Exemple de description courte :

JobPilot Secure — Application full-stack de suivi de candidatures avec Spring Boot, React, PostgreSQL et JWT. Développement d’une API REST sécurisée, gestion des candidatures, relances, dashboard, pagination, filtres, tests unitaires et documentation Swagger.

---

## Utilisation dans LinkedIn

Exemple de description :

J’ai développé JobPilot Secure, une application full-stack permettant de gérer ses candidatures, suivre les statuts, planifier les relances et visualiser un dashboard sécurisé.

Le projet utilise Spring Boot, Spring Security, JWT, PostgreSQL, Flyway, React, TypeScript, Tailwind CSS et Axios.

L’objectif était de construire un projet portfolio complet, proche d’un contexte professionnel, avec une attention particulière portée à la sécurité, à l’architecture, aux tests, à la documentation et à l’expérience utilisateur.
