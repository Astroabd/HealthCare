# HealthCare: Démarrage local avec Docker

HealthCare est un projet pédagogique en Java et Spring Boot. Il permet de gérer des patients, leurs notes médicales et de calculer leur niveau de risque de diabète à partir des informations enregistrées.

L'application utilise trois microservices :

- **Patient** : gère les patients dans **MySQL**.
- **Notes** : stocke les notes médicales dans **MongoDB**.
- **Assessment** : récupère les données des deux autres services et calcule le risque.

Elle utilise aussi **Eureka** pour découvrir les services, **Config Server** pour leur configuration et une **API Gateway** pour accéder aux API.



## 1. Prérequis

Sur Windows, il faut **Docker Desktop** (conteneurs Linux), **Docker Compose v2**, **Git** et **PowerShell**. Java 17 et Maven sont utiles pour lancer les tests hors Docker ; Postman sert à vérifier les API.

```powershell
docker version
docker compose version
java -version
mvn -version
```

**Attention aux versions :** dans les fichiers examinés pour le README d'origine, la Gateway utilise Spring Boot `4.1.0` et Spring Cloud `2025.1.2`, tandis que les autres modules utilisent Spring Boot `3.5.x` et Spring Cloud `2025.0.3`. Il faut vérifier la compatibilité de chaque module et corriger les versions nécessaires **avant** de considérer le déploiement comme fonctionnel. Ne pas désactiver le contrôle de compatibilité pour masquer une erreur.

## 2. Préparer les fichiers

Récupérer le projet :

```powershell
git clone https://github.com/Astroabd/HealthCare.git
cd HealthCare
```

À la racine du projet, ajouter les fichiers d'exemple fournis avec ce README :

```text
compose.yaml
.env.example
deploy/
  mongo-init.js
  config/
    application.yml
    discovery.yml
    patient.yml
    notes.yml
    assessment.yml
    gateway.yml
```

Copier `.env.example` vers `.env`, puis remplacer **tous** les secrets d'exemple par des valeurs différentes. Ajouter `.env` à `.gitignore` pour ne pas publier les identifiants. Les variables d'environnement servent ici au déploiement local, pas à la gestion de secrets en production.

```powershell
Copy-Item .env.example .env
```

La configuration proposée utilise le mode `native` de Config Server : il lit les fichiers dans `deploy/config`, montés en lecture seule. C'est une solution locale différente du dépôt Git de configuration utilisé dans la version examinée. Dans Docker, les services communiquent par leurs noms (`mysql`, `mongo`, `discovery`, etc.), pas par `localhost`.

## 3. Construire et démarrer

Une fois les versions et les fichiers vérifiés, lancer les commandes depuis la racine du projet :

```powershell
docker compose config --quiet
docker compose build
docker compose up -d mysql mongo configserver
```

Vérifier que MySQL, MongoDB et Config Server sont disponibles, puis démarrer les autres services :

```powershell
docker compose up -d discovery patient notes assessment gateway
docker compose ps
docker compose logs --tail 100
```

Le lancement des conteneurs ne garantit pas à lui seul que l'application fonctionne. Vérifier leurs logs et l'enregistrement des services dans Eureka.

| Adresse locale | Utilité |
| --- | --- |
| `http://localhost:8761` | Tableau de bord Eureka |
| `http://localhost:8888/patient/docker` | Configuration du service Patient (ne pas partager d'informations sensibles) |
| `http://localhost:8080` | Accès aux API via la Gateway |

Patient (`8090`), Notes (`8091`) et Assessment (`8092`) utilisent les ports internes prévus dans la configuration. MySQL (`3306`) et MongoDB (`27017`) ne sont pas exposés directement sur Windows.

## 4. Tester avec Postman

Définir `baseUrl = http://localhost:8080` dans Postman, puis vérifier ce parcours avec des **données fictives** :

1. Créer un patient avec `POST /patients`.
2. Récupérer l'identifiant retourné, puis consulter `GET /patients/{patientId}` : **HTTP 200** attendu.
3. Ajouter une note avec `POST /notes`, en utilisant cet identifiant.
4. Consulter `GET /notes/patient/{patientId}`.
5. Consulter le risque avec `GET /assessment/patient/{patientId}`.
6. Essayer un identifiant inexistant : **HTTP 404** attendu si la gestion d'erreurs prévue est bien en place.
7. Redémarrer les conteneurs et vérifier que les données sont toujours présentes.

Pour le scénario prévu dans le document initial (patient de plus de 30 ans et six termes déclencheurs distincts), le risque attendu est **« In Danger »**. C'est une valeur à **vérifier pendant les tests**, pas un résultat déjà obtenu.

Consulter aussi les réponses JSON, les codes HTTP et les logs pour comprendre les éventuelles erreurs. Le projet est pédagogique et ne doit pas servir à établir un diagnostic médical réel.

## 5. Tests automatisés

Les Dockerfiles du projet consulté utilisent `-DskipTests` : une image construite avec succès ne signifie donc **pas** que les tests passent.

Lorsque le profil Maven `test` et ses dépendances sont correctement configurés, exécuter séparément :

```powershell
mvn -f patient/pom.xml clean verify -Dspring.profiles.active=test
mvn -f notes/pom.xml clean verify -Dspring.profiles.active=test
mvn -f assessment/pom.xml clean verify -Dspring.profiles.active=test
```

Ne pas présenter les tests ou les rapports JaCoCo comme réussis tant que leurs résultats n'ont pas été vérifiés.

## 6. Arrêter ou relancer

```powershell
docker compose down         # Arrête les conteneurs et conserve les volumes
docker compose up -d        # Relance les services
docker compose ps -a        # Affiche leur état
docker compose logs --tail 200  # Affiche les logs
```

**Ne pas ajouter `-v` à `docker compose down`** pour un arrêt normal : cela supprime aussi les volumes nommés contenant les données.

En cas de problème, vérifier d'abord les logs, les ports `8080`, `8761` et `8888`, les identifiants des bases, les paramètres du Config Server et la présence des services dans Eureka. Un `503` de la Gateway peut venir d'un service indisponible ou non découvert ; un `404` peut venir d'une route incorrecte ou d'une ressource absente.

## 7. Limites et références

Cette configuration est prévue **uniquement pour des tests en local**. Elle ne constitue pas une solution prête pour la production ou l'hébergement de données médicales réelles : la sécurité, les sauvegardes, la supervision et les obligations réglementaires restent à traiter.

Avant de présenter le déploiement comme terminé, conserver une preuve des commandes exécutées, des tests réalisés et des résultats obtenus (date et commit Git compris).

Références : [dépôt HealthCare](https://github.com/Astroabd/HealthCare), [versions Spring Cloud](https://github.com/spring-cloud/spring-cloud-release/wiki/Supported-Versions), [Docker Compose](https://docs.docker.com/compose/) et [Spring Cloud Config](https://docs.spring.io/spring-cloud-config/reference/server/environment-repository/file-system-backend.html).
