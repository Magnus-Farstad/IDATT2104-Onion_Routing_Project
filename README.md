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



## Videre utvikling 🏗️

Videre utvikling vil bestå av å gjøre løsningen mer dynamisk ved at noder blir laget fortløpende.


## Eksterne avhengigheter

Kort beskrivelse av hver avhengighet. Hva er egentlig våre avhengigheter?


## Installasjon/Instruksjon 🗃️

For å kune kjøre programmet vårt kreves det:

### Steg 1 - Start Spring-boot server:

- Kjør ekstern spring-boot server ved å kjøre kommando:
```bash
mvn spring-boot:run 
```
- NB! Denne kommandoen kjøres i Spring-Boot REST API-prosjektet. Denne er linket til nederst i README-filen.
- Nå venter server på forespørsler fra noder

### Steg 2 - Kjør Client:

- Kjør Client og send med en IP- adresse til tilhørende Rest server 

- Nå er klientens offentlige nøkkel lagret i server slik at fremtidige aktive naboer kan hente ut denne

- Klienten vil nå vente på at noder kobler seg opp på server og deretter hente dem ut. Klienten vil vente i 20 sekunder før den henter ut nøklene. På denne måten kan man velge hvor mange noder som skal være med i systemet.



### Steg 3 - Kjør NodeMain:

- Kjør NodeMain og send med en IP- adresse til tilhørende Rest server og portnummer hvor videre kommunikasjon skal foregå.

- Nå er en node koblet opp til Rest server og hentet ut den offentlige asymetriske (RSA) nøkkelen til klienten slik at AES nøklene blir kryptert

- Man har også gitt klient tilgang på alle aktive noder sine kryptere nøkler, samt portnummere.


### Steg 4 - Handling:

- Nå gjenstår det bare å sende meldinger kryptert fra klient !


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



  
                                 

