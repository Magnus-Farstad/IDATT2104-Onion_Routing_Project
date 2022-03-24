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

Videre utvikling vil bestå av å gjøre løsningen mer dynamisk ved at noder blir laget fortløpende...


## Eksterne avhengigheter

Kort beskrivelse av hver avhengighet. Hva er egentlig våre avhengigheter?


## Installasjon/Instruksjon

For å kune kjøre programmet vårt kreves det:

### Steg 1 - Start Spring-boot server:

- Kjør ekstern spring-boot server ved å kjøre kommando:
```bash
mvn spring-boot:run 
```
- Nå venter server på forespørsler fra noder

### Steg 2 - Kjør Client:

- Kjør Client og send med en IP- adresse til tilhørende Rest server 

- Nå er klientens offentlige nøkkel lagret i server slik at fremtidige aktive naboer kan hente ut denne

- Klienten vil nå vente på at noder kobler seg opp på server og deretter hente dem ut


### Steg 3 - Kjør NodeMain:

- Kjør NodeMain og send med en IP- adresse til tilhørende Rest server og portnummer hvor videre kommunikasjon skal foregå.

- Nå er en node koblet opp til Rest server og hentet ut den offentlige asymetriske (RSA) nøkkelen til klienten slik at AES nøklene blir kryptert

- Man har også gitt klient tilgang på alle aktive noder sine kryptere nøkler, samt portnumere


### Steg 4 - Handling:

- Nå gjenstår det bare å sende meldinger kryptert !


## Tester

Vi har tatt i bruk JUnit tester i backend for å gjøre kritisk funksjonalitet stabil. Vi har ikke fokusert på testing i Rest API server.


## API 

Besøk vår Rest server [Node Rest API](https://github.com/mariusklemp/OnionRouterREST) !



  
                                 

