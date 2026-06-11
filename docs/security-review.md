# Revue sécurité — JobPilot Secure

## Objectif

Ce document présente une revue sécurité du projet JobPilot Secure.

L’objectif est de vérifier que l’application applique les bonnes pratiques principales de sécurité pour une application web full-stack avec API REST, authentification JWT, frontend React et base de données PostgreSQL.

---

## 1. Authentification

### Points vérifiés

- L’utilisateur doit se connecter avec email et mot de passe.
- Un token JWT est généré après connexion.
- Les routes privées du backend nécessitent un token JWT valide.
- Les routes privées du frontend sont protégées.
- Le token est automatiquement envoyé dans le header `Authorization`.
- L’utilisateur est redirigé vers `/login` si le token est absent ou invalide.

### Résultat

Validé.

---

## 2. Autorisation et isolation des données

### Points vérifiés

- Chaque candidature appartient à un utilisateur.
- Les requêtes backend utilisent l’utilisateur connecté.
- Un utilisateur ne peut consulter que ses propres candidatures.
- Les endpoints de modification utilisent l’identifiant de l’utilisateur connecté.
- Les endpoints de suppression utilisent l’identifiant de l’utilisateur connecté.

### Résultat

Validé.

---

## 3. Protection des routes frontend

### Points vérifiés

- `/dashboard` est inaccessible sans token.
- `/applications` est inaccessible sans token.
- `/applications/new` est inaccessible sans token.
- `/applications/:id/edit` est inaccessible sans token.
- `/login` et `/register` sont redirigées vers `/dashboard` si l’utilisateur est déjà connecté.
- La déconnexion supprime le token et les données utilisateur du navigateur.

### Résultat

Validé.

---

## 4. Validation des règles métier

### Points vérifiés

- Une candidature terminée ne peut plus changer de statut.
- Une candidature terminée ne peut plus avoir sa relance modifiée.
- Une date de relance ne peut pas être avant la date de création.
- Les candidatures `REJECTED` et `WITHDRAWN` sont exclues des relances actives.
- Les statuts sont contrôlés par un enum.
- Les champs de tri sont limités à une liste autorisée.

### Résultat

Validé.

---

## 5. Sécurité de la pagination et du tri

### Points vérifiés

- La taille de page est limitée côté backend.
- Les champs de tri sont contrôlés côté backend.
- La direction du tri est normalisée.
- Les paramètres de recherche, filtre, pagination et tri ne donnent pas un accès aux données d’un autre utilisateur.

### Résultat

Validé.

---

## 6. Gestion des erreurs

### Points vérifiés

- Les erreurs sont retournées dans un format lisible.
- Les erreurs métier retournent un statut HTTP adapté.
- Les candidatures inexistantes retournent une erreur contrôlée.
- Les erreurs ne doivent pas exposer de stack trace au frontend.
- Les messages d’erreur restent compréhensibles pour le client.

### Résultat

Validé.

---

## 7. Logs

### Points vérifiés

- Chaque requête API est loggée.
- Les logs contiennent :
  - méthode HTTP
  - chemin
  - statut HTTP
  - durée
  - identifiant de requête

- Les logs n’affichent pas :
  - mot de passe
  - token JWT
  - body sensible
  - données confidentielles inutiles

### Résultat

Validé.

---

## 8. Documentation API

### Points vérifiés

- Swagger / OpenAPI est disponible.
- Les routes API sont documentées automatiquement.
- Les routes protégées peuvent être testées avec un Bearer Token.
- La documentation facilite les tests et la présentation du projet.

### Résultat

Validé.

---

## 9. Tests

### Points vérifiés

- Des tests unitaires existent pour le dashboard.
- Des tests unitaires existent pour les relances.
- Les tests vérifient les règles métier principales.
- Le backend compile avec `mvn clean test`.
- Le frontend compile avec `npm run build`.

### Résultat

Validé.

---

## 10. Points à améliorer plus tard

Ces points ne bloquent pas le projet portfolio, mais peuvent être ajoutés dans une version future.

### Sécurité backend

- Ajouter un système de refresh token.
- Ajouter une expiration plus courte du token JWT.
- Ajouter un mécanisme de révocation de token.
- Ajouter une limitation du nombre de tentatives de connexion.
- Ajouter une validation plus stricte des URLs.
- Ajouter une politique CORS spécifique pour la production.
- Ajouter des tests d’intégration avec Spring Security.

### Sécurité frontend

- Améliorer la gestion de l’expiration du token.
- Ajouter un message utilisateur plus clair quand la session expire.
- Ajouter une page 404.
- Ajouter une page erreur globale.
- Éviter de stocker trop d’informations utilisateur dans `localStorage`.

### Déploiement

- Utiliser des variables d’environnement pour les secrets.
- Ne jamais commiter les secrets.
- Configurer HTTPS en production.
- Configurer les logs de production.
- Ajouter une pipeline CI/CD complète.
- Ajouter une analyse statique de sécurité.

---

## Conclusion

La première version de JobPilot Secure applique les principales bonnes pratiques attendues pour un projet portfolio full-stack :

- authentification JWT
- protection backend
- protection frontend
- isolation des données utilisateur
- validation métier
- pagination et tri sécurisés
- logs propres
- documentation Swagger
- tests unitaires
- architecture claire

Le projet est prêt à être présenté sur GitHub, LinkedIn et dans un CV pour démontrer des compétences en Java, Spring Boot, React, TypeScript, sécurité web et développement full-stack.
