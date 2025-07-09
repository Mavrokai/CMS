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
