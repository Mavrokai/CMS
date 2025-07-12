# Projet My_events

## Client

### Commandes

Dans le dossier client:

- npm install
- npm run dev

## Server

### installer un JDK

Dans un terminal quelconque:  
sudo apt install openjdk-21-jdk

### Commandes

Dans le dossier server:

- ./gradlew build
- ./gradlew bootRun

### .env

Installation de direnv :

- `sudo apt install direnv`
- Crée un fichier `.env` dans le dossier `client`, `server` ou à la racine (cela dépend du dossier qui utilisera les variables du `.env`)
- Crée un fichier `.envrc` dans le même dossier que le `.env`
- À l'intérieur du `.envrc` :
  ```
  dotenv
  ```
- Ouvre un terminal dans le dossier en question puis tape :
  ```
  direnv allow
  ```
- Relance le terminal puis fais :

  ```
  echo $OPENDATASOFT_BASEURL
  echo $SPRING_SECURITY_USER_NAME

  ```

- Si les variables s'affichent, tout est bon

- Si rien ne s'affiche faite ceci :

  ```
  export OPENDATASOFT_BASEURL="URL_DE_L'API" && export SPRING_SECURITY_USER_NAME="USERNAME" && export SPRING_SECURITY_USER_PASSWORD="PASSWORD" && echo "Variables définies :" && echo "OPENDATASOFT_BASEURL=$OPENDATASOFT_BASEURL" && echo "SPRING_SECURITY_USER_NAME=$SPRING_SECURITY_USER_NAME"

  ```

  changer bien les nom de varible selon vos désir

## Base de données H2

### Accès à la console H2

Une fois le serveur démarré avec `./gradlew bootRun`, accédez à la console H2 via :

- **URL** : `http://localhost:8080/h2-console`
- **JDBC URL** : `jdbc:h2:mem:testdb`
- **Username** : `sa`
- **Password** : (laissez vide)

La console permet de visualiser et modifier directement les données en base.
(la bdd se reset à chaque reboot du serveur)
