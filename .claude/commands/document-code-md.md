# Document Code MD

Analizza il codice sorgente del progetto e mantieni aggiornata la documentazione nei file Markdown, riflettendo fedelmente lo stato attuale del codice.

## File da aggiornare

- Individua tutti i file `.md` nella root del progetto e nella cartella `docs/`
- **Escludi** qualsiasi file sotto `.claude/`
- **`docs/CLAUDE_AGENTS.md` è incluso**: va aggiornato quando vengono aggiunti, modificati o rimossi comandi in `.claude/commands/`

## Procedura

### 1. Scoperta del progetto

Esplora la struttura del progetto per capire cosa c'è da documentare:

- Leggi i file di build (`pom.xml`, `build.gradle`, `package.json`, ecc.) per identificare tecnologie, dipendenze e versioni
- Esplora le directory principali del codice sorgente
- Individua file di configurazione rilevanti (properties, yaml, env, docker-compose, ecc.)
- Rileva eventuali script, Dockerfile/Containerfile, pipeline CI/CD
- Elenca i file presenti in `.claude/commands/` per rilevare comandi aggiunti, modificati o rimossi

### 2. Lettura della documentazione esistente

Leggi tutti i file `.md` da aggiornare e comprendi la struttura, lo stile e i contenuti attuali di ciascuno.

### 3. Confronto codice ↔ documentazione

Per ogni file `.md`, confronta il contenuto documentato con lo stato attuale del codice e identifica:

- **Obsoleto:** informazioni non più corrispondenti al codice (versioni, endpoint, configurazioni, nomi cambiati)
- **Mancante:** funzionalità o componenti presenti nel codice ma non documentati
- **Errato:** dati parzialmente imprecisi o fuorvianti
- **Da rimuovere:** riferimenti a componenti o funzionalità eliminate dal codice

Per `docs/CLAUDE_AGENTS.md` in particolare:

- Confronta i file in `.claude/commands/` con i comandi elencati nel file
- Aggiungi la documentazione per ogni comando presente ma non descritto
- Rimuovi la documentazione di comandi che non esistono più
- Aggiorna la descrizione dei comandi il cui file `.md` è stato modificato

### 4. Aggiornamento

Applica le modifiche necessarie rispettando queste regole:

- Mantieni struttura, stile e tono già presenti nel file
- Modifica solo ciò che è effettivamente cambiato — non riscrivere sezioni corrette
- Preserva i link di navigazione esistenti tra i file `.md`
- Aggiorna diagrammi (Mermaid o ASCII) se le strutture che rappresentano sono cambiate
- Rimuovi intere sezioni solo se il contenuto non ha più ragione di esistere

### 5. Report finale

Al termine mostra un riepilogo in questo formato:

```text
## Documentation update report

### <nome-file.md>
- [aggiornato | rimosso | invariato] <descrizione sintetica della modifica>
```

Se un file non richiede modifiche, indicalo con `invariato — nessuna discrepanza rilevata`.

Non creare commit. Al termine suggerisci all'utente di eseguire `/commit-conventional`.
