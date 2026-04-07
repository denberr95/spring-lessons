# Update Maven Dependencies

Analizza le dipendenze e i plugin Maven del progetto e produce un report degli aggiornamenti disponibili a versioni stable.

## Procedura

### 1. Lettura del progetto

- Leggi `pom.xml` per identificare:
  - Parent BOM (es. `spring-boot-starter-parent`) e sua versione
  - Sezione `<properties>` con le versioni dichiarate esplicitamente
  - Sezione `<dependencyManagement>` con BOM importati
  - Sezione `<dependencies>` con tutte le dipendenze dirette
  - Sezione `<build><plugins>` con tutti i plugin e le loro versioni
- Se esiste `settings.xml` nella root del progetto, usalo nelle invocazioni Maven con `--settings settings.xml`
- Se non esiste, ometti il flag `--settings`

### 2. Esecuzione della scansione

Esegui i seguenti comandi Maven in sequenza. Se un comando fallisce, segnalalo e prosegui con il successivo.

**Aggiornamenti dipendenze:**

```bash
mvn versions:display-dependency-updates --file pom.xml [--settings settings.xml] -DprocessDependencyManagement=true -DprocessDependencies=true 2>&1
```

**Aggiornamenti plugin:**

```bash
mvn versions:display-plugin-updates --file pom.xml [--settings settings.xml] 2>&1
```

**Aggiornamenti properties:**

```bash
mvn versions:display-property-updates --file pom.xml [--settings settings.xml] 2>&1
```

### 3. Analisi dell'output

Filtra i risultati mantenendo **solo gli aggiornamenti a versioni stable**. Escludi:

- Versioni alpha, beta, milestone, release candidate (`-alpha`, `-beta`, `-M*`, `-RC*`, `-SNAPSHOT`, `-EA`)
- Versioni identiche a quella corrente
- Dipendenze gestite dal parent BOM senza versione esplicita nel `pom.xml` (a meno che non ci sia un aggiornamento del BOM stesso disponibile)

### 4. Report

Presenta il risultato organizzato per categoria:

```text
## Maven Dependency Update Report

### Parent BOM
| Artifact | Current | Latest Stable | Notes |
|---|---|---|---|
| spring-boot-starter-parent | x.y.z | x.y.z | ... |

### Properties (versioni esplicite)
| Property | Artifact | Current | Latest Stable |
|---|---|---|---|
| ... | ... | ... | ... |

### Dependencies
| GroupId:ArtifactId | Current | Latest Stable |
|---|---|---|
| ... | ... | ... |

### Plugins
| GroupId:ArtifactId | Current | Latest Stable |
|---|---|---|
| ... | ... | ... |

### Nessun aggiornamento disponibile
- Lista delle dipendenze/plugin già alla versione più recente stable
```

Se non ci sono aggiornamenti disponibili per una categoria, indicalo con `— nessun aggiornamento disponibile`.

### 5. Indicazioni

Dopo il report, fornisci indicazioni su:

- Aggiornamenti ad **alto impatto** (parent BOM, dipendenze core Spring) che potrebbero richiedere verifiche di compatibilità
- Aggiornamenti **sicuri** (librerie indipendenti, plugin di build) applicabili con rischio basso
- Eventuali **dipendenze obsolete o deprecate** da considerare per sostituzione

Non applicare alcuna modifica al `pom.xml`. Il report è solo informativo.
Al termine, se ci sono aggiornamenti da applicare, suggerisci all'utente di modificare il `pom.xml` e usare `/commit-conventional` con scope `setup`.
