# HealthCare: Installation et démarrage local

HealthCare est une application pédagogique développée avec Java et Spring Boot. Elle permet de gérer les patients, leurs notes médicales et d'évaluer leur risque de diabète.

L'application est organisée en trois microservices :

- **Patient** : gère les informations des patients dans **MySQL**.
- **Notes** : enregistre les notes médicales dans **MongoDB**.
- **Assessment** : récupère les informations des deux services pour calculer le niveau de risque.

**Eureka** assure la découverte des services, **Config Server** centralise leur configuration et **API Gateway** permet d'accéder aux API.

## 1. Prérequis

Pour lancer le projet sur Windows, utiliser :

- Docker Desktop avec Docker Compose v2 ;
- Git et PowerShell ;
- Java 17 et Maven pour exécuter les tests en dehors des conteneurs ;
- Postman pour tester les API.

```powershell
docker version
docker compose version
java -version
mvn -version
```

## 2. Préparer le projet

Cloner le dépôt :

```powershell
git clone https://github.com/Astroabd/HealthCare.git
cd HealthCare
```

Les fichiers de déploiement sont organisés comme ceci à la racine du projet :

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

Créer le fichier `.env` à partir de `.env.example`, puis renseigner les mots de passe locaux :

```powershell
Copy-Item .env.example .env
```

Le fichier `.env` doit être exclu de Git pour éviter de publier les identifiants. Le Config Server utilise des fichiers de configuration dans `deploy/config`, montés en lecture seule. À l'intérieur de Docker, les services communiquent à l'aide de leurs noms (`mysql`, `mongo`, `discovery`, etc.).

## 3. Construire et lancer les services

Depuis la racine du projet :

```powershell
docker compose config --quiet
docker compose build
docker compose up -d mysql mongo configserver
```

Démarrer ensuite les microservices et la Gateway :

```powershell
docker compose up -d discovery patient notes assessment gateway
docker compose ps
docker compose logs --tail 100
```

Adresses utiles :

| Adresse | Utilisation |
| --- | --- |
| `http://localhost:8761` | Interface Eureka |
| `http://localhost:8888/patient/docker` | Configuration du service Patient |
| `http://localhost:8080` | Accès aux API via la Gateway |

Les ports internes sont `8090` pour Patient, `8091` pour Notes et `8092` pour Assessment. Les bases MySQL (`3306`) et MongoDB (`27017`) sont accessibles aux services Docker sans être exposées sur Windows.

## 4. Tester les API avec Postman

Dans Postman, créer une variable `baseUrl` avec la valeur `http://localhost:8080`, puis effectuer ce parcours avec des données fictives :

1. Créer un patient avec `POST /patients`.
2. Récupérer son identifiant et consulter `GET /patients/{patientId}` (réponse attendue : **200 OK**).
3. Ajouter une note avec `POST /notes` en utilisant cet identifiant.
4. Consulter ses notes avec `GET /notes/patient/{patientId}`.
5. Consulter son niveau de risque avec `GET /assessment/patient/{patientId}`.
6. Essayer un identifiant de patient inexistant (réponse attendue : **404 Not Found**).
7. Redémarrer les conteneurs et consulter de nouveau le patient et ses notes pour contrôler la conservation des données.

Exemple de scénario métier : pour un patient de plus de 30 ans avec six termes déclencheurs distincts, le risque attendu est **In Danger**.

## 5. Lancer les tests

Les tests Maven se lancent séparément de la construction des images Docker :

```powershell
mvn -f patient/pom.xml clean verify -Dspring.profiles.active=test
mvn -f notes/pom.xml clean verify -Dspring.profiles.active=test
mvn -f assessment/pom.xml clean verify -Dspring.profiles.active=test
```

Les rapports de tests et de couverture JaCoCo, lorsqu'ils sont générés par Maven, sont disponibles dans les répertoires `target` des modules concernés.

## 6. Arrêter, redémarrer et consulter les logs

```powershell
docker compose down                 # Arrête les conteneurs et conserve les volumes
docker compose up -d                # Redémarre les services
docker compose ps -a                # Affiche l'état des conteneurs
docker compose logs --tail 200      # Affiche les derniers logs
```

Pour un arrêt normal, ne pas utiliser `docker compose down -v` : cette option supprime aussi les volumes et leurs données.

En cas de problème, consulter les logs, les paramètres de connexion aux bases, la configuration des ports et l'enregistrement des services dans Eureka. Une réponse **503** de la Gateway peut signaler un service indisponible ; une réponse **404** peut indiquer une route incorrecte ou une ressource inexistante.

**Ce projet utilise des données de démonstration fictives et n'est pas destiné au diagnostic médical.**

## Documentation

- [Dépôt HealthCare](https://github.com/Astroabd/HealthCare)
- [Docker Compose](https://docs.docker.com/compose/)
- [Spring Cloud Config](https://docs.spring.io/spring-cloud-config/reference/server/environment-repository/file-system-backend.html)
