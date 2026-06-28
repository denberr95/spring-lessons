---
name: update-docker-images
description: Update all Docker/Podman image versions in this project — .env, compose files, and Containerfiles. Use this skill whenever the user asks to update images, bump docker versions, check for newer container images, or refresh the image pins. Also trigger when the user says things like "aggiorna le immagini", "controlla nuove versioni dei container", "bump images", or notices that images might be outdated.
---

# Update Docker Images

This project pins all image versions centrally in `collections/.env` using `IMAGE_*` variables, which are then referenced in compose files via `${IMAGE_*}`. Some Containerfiles also have hardcoded `FROM` versions that must stay in sync.

## Workflow

### 1. Discover current pins

Read `collections/.env` and extract all `IMAGE_*` variables with their current values. Then scan for any hardcoded image references in:
- `collections/wiremock/Containerfile` — `FROM wiremock/wiremock:<version>`
- `Containerfile` (root) — check the `FROM` base image (currently `bellsoft/liberica-openjre-alpine:25`)
- Any other `Dockerfile` or `Containerfile` found in the project

### 2. Resolve the latest stable tag for each image

For each image, fetch the latest stable tag from Docker Hub (or the relevant registry). Use the Docker Hub API:

```bash
curl -s "https://registry.hub.docker.com/v2/repositories/<owner>/<image>/tags/?page_size=50&ordering=-last_updated" \
  | python3 -c "import sys,json; tags=json.load(sys.stdin)['results']; [print(t['name']) for t in tags]"
```

For images on other registries (e.g. `quay.io/keycloak/keycloak`), use:
```bash
curl -s "https://quay.io/api/v1/repository/keycloak/keycloak/tag/?limit=20&onlyActiveTags=true" \
  | python3 -c "import sys,json; tags=json.load(sys.stdin)['tags']; [print(t['name']) for t in tags]"
```

**Tag selection rules — these matter to get right:**
- Skip `latest`, `main`, `master`, `edge`, `nightly`, `snapshot` — these are moving targets
- Skip release candidates: anything containing `rc`, `beta`, `alpha`, `dev`, `pre`
- Skip architecture-specific tags: `arm64`, `amd64`, `linux/amd64`, `sha256-*`
- Skip distroless/busybox variants unless that's what was already used (e.g. `*-distroless`, `*-busybox`)
- Skip tags that are just suffixes without a base version (e.g. `-nonroot`)
- Prefer the shortest stable semver tag (e.g. `17.5` over `17.5-alpine`, `26.6.4` over `26.6.4-0`)
- Some images use a `v` prefix in their tags — Prometheus is a known case (`v3.12.0`). Check what format the current pin uses and match it.

**Known quirks for this project's images:**
- `prom/prometheus`: tags use `v` prefix — e.g. `v3.12.0`
- `axllent/mailpit`: tags use `v` prefix — e.g. `v1.30.3`
- `quay.io/keycloak/keycloak`: hosted on quay.io, not Docker Hub
- `bellsoft/liberica-openjre-alpine`: tied to the Java version (`25`), only update patch versions within the same major

Always verify a tag actually exists before writing it — pull it or check the manifest:
```bash
podman pull <image>:<tag> 2>&1 | grep -E "error|Error|Writing manifest" | head -3
```

### 3. Apply updates

**`collections/.env`**: Update each `IMAGE_*` variable with the new tag. Preserve all other variables unchanged.

**Hardcoded Containerfiles**: If the version in a `FROM` line has changed, update it to match. For `collections/wiremock/Containerfile`, the `FROM wiremock/wiremock:<version>` must match `IMAGE_WIREMOCK` in `.env`.

Do not touch the root `Containerfile` unless the base JRE image itself has a newer patch version — this is tied to Java 25 and should only change if there's a newer `bellsoft/liberica-openjre-alpine:25` patch.

### 4. Report changes

Print a clear summary table:

```
Image                            Old           New           Status
────────────────────────────────────────────────────────────────────
postgres                         17.10         18.1          ✓ updated
grafana/loki                     3.7.3         3.7.3         — unchanged
prom/prometheus                  v3.12.0       v3.13.0       ✓ updated
quay.io/keycloak/keycloak        26.6          26.6.4        ✓ updated
...
```

If a tag could not be resolved (API error, no matching tag), mark it as `⚠ skipped` and explain why — don't silently leave it unchanged.

### 5. Optional: validate the compose starts

If the user asks to verify, or if there were significant version jumps, run:
```bash
podman compose --project spring-lessons --env-file ./collections/.env \
  --file ./collections/compose-env.yaml up --force-recreate --remove-orphans --detach
```

Then wait for the health checks on `springdb`, `kafka`, and `keycloak` to resolve:
```bash
until podman inspect spring-lessons_<service>_1 --format '{{.State.Health.Status}}' \
  2>/dev/null | grep -qE "healthy|unhealthy"; do sleep 3; done
```

Report the final health status of all containers.

## What not to change

- Never change `POSTGRES_USER`, `POSTGRES_PASSWORD`, `POSTGRES_DB`, `TZ`, or other non-image variables in `.env`
- Never change application config in `compose-app.yaml` environment variables
- Never modify `grafana.ini`, prometheus config, or other tool-specific config files
- Don't update the Java version in `Containerfile` — that's a separate decision tied to the Spring Boot runtime
