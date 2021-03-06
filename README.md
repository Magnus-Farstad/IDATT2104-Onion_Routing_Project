# Onion Routing 🧅
![Java CI with Maven](https://github.com/Magnus-Farstad/IDATT2104-Onion_Routing_Project/actions/workflows/maven.yml/badge.svg)


 Dette er et frivillig prosjekt i IDATT2104- Nettverksprogrammering for å forbedre karakteren i faget 😏
 
 Lenke til siste workflow: [CI/CD](https://github.com/Magnus-Farstad/IDATT2104-Onion_Routing_Project/actions)

## Oppgave

Oppgaven besto i å utvikle The Onion Router. 


## Løsning 📝

**generell info:**
- Utviklingspråk: Java

- Løsningen består av to prosjekter: **IDATT2104-Onion_Routing_Project** og **OnionRouterREST**.

- **IDATT2104-Onion_Routing_Project** inneholder klient, klasser for kryptering og selve nodene.

- **OnionRouterREST** er en REST server som mottar portnummere, adresser og nøkler, for så å dele disse ut til klient og noder. 
(Link til dette prosjektet finnes nederst i README-filen).

- For kryptering brukes både RSA og AES. RSA for kryptering av AES nøkler, og AES for sending av lag-kryptert melding. 

- Klienten kan sende en melding som går gjennom x antall noder, vise til en API server og motta et svar i form av en string.


**Detaljer:**

:one: Klient sender public key til server.

![ClientServer](https://user-images.githubusercontent.com/91839835/159998190-876df2a6-5d57-4ceb-b692-ab5f29570db1.jpg)


:two: Noder kobler seg på server og henter ut den offentlige nøkkelen til klienten. Brukes så til å kryptere AES nøkkelen til påkoblede noder.

![Bilde 24 03 2022 klokken 20 23 (1)](https://user-images.githubusercontent.com/91839835/159998612-c936e193-3ed4-455b-bc5e-a168c6629dfb.jpg)


:three: Nodene sender tilbake portnummer, adresse og krypterte AES nøkler til server 

![Bilde 25 03 2022 klokken 15 21](https://user-images.githubusercontent.com/91839835/160139091-17f29fee-6039-40f2-b1f6-d709ed3967de.jpg)

:four: Klient henter ut nodene fra server og bruker dette til å lage en rute av noder, samt kryptere en melding lag for lag med AES nøkkelen fra hver av nodene. Deretter blir melding sendt til de ulike nodene hvor de blir dekryptert lag for lag ved ankomst. Når meldingen ankommer siste node gjøres et api kall til den gitte adressen fra klienten.

![Bilde 24 03 2022 klokken 20 23](https://user-images.githubusercontent.com/91839835/159998647-b62e589f-767e-4653-a049-3780b524a5f1.jpg)

![Bilde 24 03 2022 klokken 20 24](https://user-images.githubusercontent.com/91839835/159998678-b71acf52-e792-4982-88c5-76366a14ea99.jpg)

:five: Respons fra server vil nå bli sendt tilbake til klient ved at den blir kryptert et lag for hver node den ankommer, og ved ankomst hos klient vil alle lagene i meldingen bli dekryptert med AES nøklene til alle nodene den har tilgjengelig.
![Bilde 25 03 2022 klokken 15 22](https://user-images.githubusercontent.com/91839835/160139963-71ccad6c-6558-41af-b3ef-0b7b74164199.jpg)


## Funksjonalitet

- Kryptering ved bruk av både AES (symmetrisk) og RSA (asymmetrisk)
    * AES-nøkler krypteres med RSA public key
    * Meldinger krypteres med AES-nøkler
- Kryptert meldingsutveksling mellom klient og noder.
    * Kryptert melding med AES
    * Meldingsutveksling med så mange noder man ønsker.
    * Meldingen blir kryptert i lag, slik at diverse noder kun kan dekryptere det som er ment for dem.
- Kryptert meldingsutveksling fra klient, gjennom noder til et API
    * Dette er et eksempel-API, samme prosjekt som serveren, for å illustrere at det er mulig å kommunisere kryptert til et API
- Kryptert meldingsutveksling fra API til klient
    * Meldingen API-et vil sende blir kryptert i flere lag med AES hos nodene, og ender opp hos klient
- Keep-alive connection
    * Kan holde forbindelsen så lenge man måtte ønske



## Videre utvikling 🏗️

Videre utvikling vil bestå av å gjøre løsningen mer dynamisk ved at noder blir laget fortløpende.


## Eksterne avhengigheter


### Maven
- Maven er brukt som rammeverk i dette prosjektet. Prosjektet importerte Maven underveis, blant annet for å gjøre CI/CD og Releases enklere.

### Spring-Boot
- I server-prosjektet er Spring-Boot blitt benyttet. Dette er en server med endepunkter for å motta og sende nøkler, portnummer og adresser.

### JUnit
- JUnit er blitt brukt for å teste kritisk funksjonalitet, som kryptering, dekryptering og generering av nøkler.


## Installasjon

For å kjøre programmet, må de kjørbare filene lastes ned.
Trykk på linkene for å laste ned.

- [Klient](https://github.com/Magnus-Farstad/IDATT2104-Onion_Routing_Project/releases/download/v1.0.0/Onion-Routing-Client.jar)
- [Node](https://github.com/Magnus-Farstad/IDATT2104-Onion_Routing_Project/releases/download/v1.0.0/Onion-Routing-Node.jar)
- [Server](https://github.com/Magnus-Farstad/IDATT2104-Onion_Routing_Project/releases/download/v1.0.0/Onion-Routing-Server.jar)

## Installasjon/Instruksjon 🗃️

Les gjennom alle stegene før bruk!

For å kune kjøre programmet vårt kreves det:

### Steg 1 - Start Spring-boot server:

- Naviger til mappen der filene ble lagret etter nedlastingen.
- Kjør ekstern spring-boot server ved å kjøre kommando:
```
java -jar Onion-Routing-Server.jar
```
- Eventuelt kan man navigere til selve prosjekt-mappen og kjøre kommandoen:

```bash
mvn spring-boot:run 
```
- NB! Denne kommandoen kjøres i Spring-Boot REST API-prosjektet. Denne er linket til nederst i README-filen.
- Nå venter server på forespørsler fra noder

### Steg 2 - Kjør client.Client:

- Kjør client.Client og send med en Public Key til tilhørende Rest server.
- For å gjøre dette, naviger til filkatalogen der klient-filen ble lagret. Bruk så kommandoen:
```
java -jar Onion-Routing-Client.jar
```
NB! Det er viktig at serveren kjører først, da klienten automatisk kobler seg til serveren.

- Nå er klientens offentlige nøkkel lagret i server slik at fremtidige aktive naboer kan hente ut denne

- Klienten vil nå vente på at noder kobler seg opp på server og deretter hente dem ut. 
NB! Klienten vil vente i 30 sekunder før den henter ut nøklene. På denne måten kan man velge hvor mange noder som skal være med i systemet. Det kan derfor være en idé å ha kommandoene klare for å kjøre nodene. Rekker man ikke å kjøre en node vil ikke denne gjøre noe.



### Steg 3 - Kjør NodeMain:

- Kjør Node, spesifiser adresse til serveren og hvilket portnummer du ønsker at denne noden skal ha.
- For å kjøre, naviger til filkatalogen der server-filen ble lagret. Bruk så kommandoen:
```
java -jar Onion-Routing-Node.jar
```
Om serveren kjører på samme maskin kan 'localhost' brukes som adresse. Hvis serveren kjører på en annen adresse, må din maskin være på samme subnett som serveren. Da må serveren sin lokale IPv4 sendes som adresse.

- Nå er en node koblet opp til Rest server og hentet ut den offentlige asymetriske (RSA) nøkkelen til klienten slik at AES nøklene kan krypteres med denne.
- Deretter vil noden sende den krypterte AES-nøkkelen tilbake til serverern, som så vil sende denne tilbake til klienten. Hos klienten vil nøkkelen bli dekryptert.

- Man har også gitt klient tilgang på alle aktive noder sine kryptere nøkler, samt portnummere.
- For å kjøre flere noder, bruk flere terminalvinduer og skriv inn kommandoen over for hver node man ønkser at skal være med.


### Steg 4 - Handling:

- Nå gjenstår det bare å sende meldinger kryptert fra klient !
NB! Bare engelske bokstaver er støttet foreløpig (æ, ø og å vil ikke fungere som man vil)


### Ved feilmelding

- Ved eventuelle feilmeldinger kan det være nødvendig å kjøre:
```
mvn clean install
```


## Tester

Vi har tatt i bruk JUnit tester i backend for sikre at kritisk funksjonalitet fungerer. Vi har ikke fokusert på testing i Rest API server, likevel er det mest essensielle testet.

For å kjøre tester kan denne kommandoen brukes:
```
mvn clean test
```
Dette kan gjøres i både Spring-Boot REST API-serveren og i Onion_Routing_Project

## API 📡

Besøk vår Rest server [Node Rest API](https://github.com/mariusklemp/OnionRouterREST) !


## Dokumentasjon

Lenke til dokumentasjon: [JavaDoc](https://magnus-farstad.github.io/IDATT2104-Onion_Routing_Project/)
  
                                 

