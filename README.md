# Maitre T

Outil de gestion de restaurant dans le but d'organiser la grande activité d'ouverture, l'Hoppening. 

## Guide de démarrage
### Pour démarrer l'application

Pour démarrer l'application sur le port 8181
```bash
mvn clean install
mvn exec:java -pl application
```

#### Démarrer l'application avec Docker

```bash
docker build -t application-glo4002 .
docker run -p 8080:8080 -p 8181:8181 application-glo4002
```

## Principales fonctionnalités
### Créer une réservation
Il est possible de créer une réservation en effectuant un POST à l'endpoint ``/reservations``.
Le format de la requête doit suivre la structure suivante:

```json
{
  "vendorCode": "TEAM"::string,
  "dinnerDate": "2150-07-21T15:23:20.142Z"::string,
  "from": {
    "country": {
        "code": "CA"::string,
        "fullname": "CANADA"::string,
        "currency": "CAD"::string,
    },
    "reservationDate": "2150-05-21T15:23:20.142Z"::string
  },
  "tables": [
    {
         "customers": [
            { "name": "John", "restrictions": [] },
            { "name": "Jane", "restrictions": ["vegetarian"] }
         ]
      },
      {
         "customers": [
            { "name": "Roger", "restrictions": ["allergies"] },
            { "name": "Rogette", "restrictions": ["vegetarian", "illness"] }
         ]
      },
      ...
  ]
}
```

L'ID de la réservation créé se trouvera dans le header ``Location`` de la réponse

#### Contraintes
- Les dates de réservation ``reservationDate`` ne peuvent être effectuées qu'entre le 1er janvier 2150 et 16 juillet 2150 inclusivement
- La date de dinner ``dinnerDate`` doivent être entre le 20 juillet 2150 et le 30 juillet 2150 inclusivement
- En raison du COVID, le nombre maximal de personne par table ne doit pas dépasser 4 personnes et une réservation ne doit pas dépasser 6 personnes au total.
- Actuellement, les restrictions alimentaires permises sont
    - vegetarian
    - vegan
    - allergies
    - illness
- Il doit y avoir au moins une table dans une requête
- Chaque table doit avoir au moins un client
- Les dates doivent suivre le format `yyyy-MM-dd'T'HH:mm:ss.SSS'Z'`
    
### Obtenir les informations sur une réservation
Pour obtenir une réservation, faire une requête GET sur le endpoint ``/reservations/<reservationId>``, où `reservationId` est l'ID de la réservation désirée.
Les réponses ont la forme suivante:
```json
{
    "reservationPrice": 0.00::float,
    "dinnerDate": "2150-07-21T15:23:20.142Z"::string,
    "customers": [
          { "name": "Luce", "restrictions": ["allergies"] }, ...
    ]
}
```

## Contributors
- Laurent Aubin
- Vincent Breault
- Jean-Christophe Drouin
- Toma Gagné
- Benjamin Girard
- Vincent Lambert
- Rémi Lévesque
- Philippe Vincent
- Yu Xuan Zhao
