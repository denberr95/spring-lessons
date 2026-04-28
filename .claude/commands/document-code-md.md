# Document Code MD

Analizza il codice sorgente del progetto e mantieni aggiornata la documentazione nei file Markdown, riflettendo fedelmente lo stato attuale del codice.

## File da aggiornare

- Individua tutti i file `.md` nella root del progetto e nella cartella `docs/`
- **Escludi** qualsiasi file sotto `.claude/`
- I seguenti file Claude-specifici in `docs/` sono inclusi e vanno mantenuti separati:

| File | Fonte di verità |
| --- | --- |
| `docs/CLAUDE_COMMANDS.md` | `.claude/commands/` — comandi slash custom e skills (plugin + globali rilevanti) |
| `docs/CLAUDE_HOOKS.md` | hooks in `.claude/settings.json` e `~/.claude/settings.json` |
| `docs/CLAUDE_AGENTS.md` | tipi di subagent usati/configurati nel progetto |

Se uno di questi file non esiste, crealo con la struttura base coerente con gli altri.

## Procedura

### 1. Scoperta del progetto

Esplora la struttura del progetto per capire cosa c'è da documentare:

- Leggi i file di build (`pom.xml`, `build.gradle`, `package.json`, ecc.) per identificare tecnologie, dipendenze e versioni
- Esplora le directory principali del codice sorgente
- Individua file di configurazione rilevanti (properties, yaml, env, docker-compose, ecc.)
- Rileva eventuali script, Dockerfile/Containerfile, pipeline CI/CD
- Elenca i file presenti in `.claude/commands/` per rilevare comandi aggiunti, modificati o rimossi
- Leggi `.claude/settings.json` (progetto) e `~/.claude/settings.json` (globale) per hooks e permissions
- Identifica i tipi di subagent lanciati nel progetto (Explore, Plan, general-purpose, ecc.)

### 2. Lettura della documentazione esistente

Leggi tutti i file `.md` da aggiornare e comprendi la struttura, lo stile e i contenuti attuali di ciascuno.

### 3. Confronto codice ↔ documentazione

Per ogni file `.md`, confronta il contenuto documentato con lo stato attuale del codice e identifica:

- **Obsoleto:** informazioni non più corrispondenti al codice (versioni, endpoint, configurazioni, nomi cambiati)
- **Mancante:** funzionalità o componenti presenti nel codice ma non documentati
- **Errato:** dati parzialmente imprecisi o fuorvianti
- **Da rimuovere:** riferimenti a componenti o funzionalità eliminate dal codice

Per i file Claude-specifici in particolare:

**`docs/CLAUDE_COMMANDS.md`**

- Confronta i file in `.claude/commands/` con i comandi elencati
- Aggiungi la documentazione per ogni comando presente ma non descritto
- Rimuovi la documentazione di comandi che non esistono più
- Aggiorna la descrizione dei comandi il cui file `.md` è stato modificato
- Aggiorna la sezione Platform Skills: elenca plugin installati (leggi `~/.claude/plugins/installed_plugins.json`) e skills globali rilevanti per il progetto

**`docs/CLAUDE_HOOKS.md`**

- Leggi `.claude/settings.json` per hooks a livello progetto
- Leggi `~/.claude/settings.json` per hooks globali attivi in questo progetto
- Se non ci sono hooks configurati, documenta la struttura attesa e lascia le sezioni vuote

**`docs/CLAUDE_AGENTS.md`**

- Documenta i tipi di subagent (Explore, Plan, general-purpose, ecc.) e quando usarli in questo progetto
- Aggiorna se vengono aggiunti nuovi pattern di utilizzo degli agent

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
- [aggiornato | creato | rimosso | invariato] <descrizione sintetica della modifica>
```

Se un file non richiede modifiche, indicalo con `invariato — nessuna discrepanza rilevata`.

Non creare commit. Al termine suggerisci all'utente di eseguire `/commit-conventional`.
