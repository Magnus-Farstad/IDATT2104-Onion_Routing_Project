# Onion Routing

 Dette er et frivillig prosjekt i IDATT2104- Nettverksprogrammering for å forbedre karakteren i faget. 

## Oppgave

Implementer onion routing enten som programvare eller programvarebibliotek i et valgfritt programmeringsspråk

## Løsning

- Programmet er utviklet i Java

- Består av en klient samt et nettverk av noder som er holdt styr på med en Rest server 

- Rest server sin oppgave er å fordele nøkler, portnummer og adresse til de ulike nodene slik at man får opprettet en vei av noder ved oppkobling

- For kryptering brukes både RSA og AES. RSA for sending av nøkler, og AES for sending av lag-kryptert melding. 

- Klienten kan sende inn en melding som viser til en API og motta et svar i form av en string.


Legg inn tegninger og mer forklaring


## Videre utvikling
Videre utvikling av systemet er å implementere muligheten for kommunikasjon tilbake til klient, etter melding og ip adresse har kommet frem til endepunktet.


## Eksterne avhengigheter

- Kort beskrivelse av hver avhengighet 


## Installasjon/Instruksjon

For å kune kjøre programmet vårt kreves det:
- blablabla
- blablabla
- blablabla


## Tester


### Server:
Mer spesifikt om hva man må gjøre med server

```bash
mvn spring-boot:run 
```

### Klient:
Mer spesifikt om hva man må gjøre med Klient

### Node:
Mer spesifikt om hva man må gjøre med Node




## Bidrag
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## API 

Her er vår Rest server som holder styr på aktive noder

Visit [Node Rest API](https://github.com/mariusklemp/OnionRouterREST) today!

  
                                 

