#!/usr/bin/env python3
"""
Fetches the latest stable tag for each IMAGE_* variable in collections/.env.
Usage: python3 fetch_latest_tags.py [path/to/.env]
"""

import sys
import re
import json
import urllib.request

ENV_FILE = sys.argv[1] if len(sys.argv) > 1 else "collections/.env"

# Map: IMAGE_VAR -> (registry, repo, use_v_prefix)
IMAGE_MAP = {
    "IMAGE_POSTGRES":   ("dockerhub", "library/postgres",              False),
    "IMAGE_JAEGER":     ("dockerhub", "jaegertracing/jaeger",          False),
    "IMAGE_LOKI":       ("dockerhub", "grafana/loki",                  False),
    "IMAGE_PROMETHEUS": ("dockerhub", "prom/prometheus",               True),
    "IMAGE_GRAFANA":    ("dockerhub", "grafana/grafana-enterprise",    False),
    "IMAGE_OTELCOL":    ("dockerhub", "otel/opentelemetry-collector",  False),
    "IMAGE_KAFKA":      ("dockerhub", "apache/kafka",                  False),
    "IMAGE_KEYCLOAK":   ("quay",      "keycloak/keycloak",             False),
    "IMAGE_WIREMOCK":   ("dockerhub", "wiremock/wiremock",             False),
    "IMAGE_MAILPIT":    ("dockerhub", "axllent/mailpit",               True),
}

# Patterns that mark a tag as NOT stable
SKIP_RE = re.compile(
    r'(rc\d*|beta|alpha|dev|pre[-.]|nightly|snapshot|edge|latest|main|master|'
    r'distroless|busybox|nonroot|rootless|arm|amd64|arm64|386|ppc|s390|windows|ubi|'
    r'alpine|slim|buster|bullseye|jammy|focal|bionic|oracle|bookworm|noble)',
    re.IGNORECASE
)

# A stable tag is purely digits and dots (optionally v-prefixed)
SEMVER_RE = re.compile(r'^v?(\d+)(\.\d+)*$')

def read_env(path):
    pins = {}
    with open(path) as f:
        for line in f:
            line = line.strip()
            if line.startswith("IMAGE_") and "=" in line:
                k, v = line.split("=", 1)
                pins[k.strip()] = v.strip()
    return pins

def fetch_tags_dockerhub(repo, pages=6):
    """Fetch tags ordered by last_updated (newest first). More pages = better coverage."""
    tags = []
    url = f"https://registry.hub.docker.com/v2/repositories/{repo}/tags/?page_size=100&ordering=-last_updated"
    for _ in range(pages):
        if not url:
            break
        try:
            with urllib.request.urlopen(url, timeout=10) as r:
                data = json.loads(r.read())
            tags.extend(t["name"] for t in data.get("results", []))
            url = data.get("next")
        except Exception:
            break
    return tags

def fetch_tags_quay(repo, pages=3):
    tags = []
    page = 1
    for _ in range(pages):
        url = f"https://quay.io/api/v1/repository/{repo}/tag/?limit=100&page={page}&onlyActiveTags=true"
        try:
            with urllib.request.urlopen(url, timeout=10) as r:
                data = json.loads(r.read())
            batch = data.get("tags", [])
            if not batch:
                break
            tags.extend(t["name"] for t in batch)
            if not data.get("has_additional"):
                break
            page += 1
        except Exception:
            break
    return tags

def semver_tuple(tag):
    """Numeric tuple for semver comparison — purely numeric parts after stripping v prefix."""
    clean = tag.lstrip("v")
    parts = clean.split(".")
    try:
        return tuple(int(p) for p in parts)
    except ValueError:
        return (0,)

def best_stable_tag(tags, use_v_prefix):
    stable = []
    for t in tags:
        if SKIP_RE.search(t):
            continue
        if not SEMVER_RE.match(t):
            continue
        has_v = t.startswith("v")
        if use_v_prefix != has_v:
            continue
        stable.append(t)

    if not stable:
        return None

    stable.sort(key=semver_tuple, reverse=True)
    return stable[0]

def main():
    pins = read_env(ENV_FILE)
    results = []

    for var, (registry, repo, use_v) in IMAGE_MAP.items():
        current = pins.get(var, "?")

        if registry == "dockerhub":
            tags = fetch_tags_dockerhub(repo)
        else:
            tags = fetch_tags_quay(repo)

        latest = best_stable_tag(tags, use_v)

        if not latest:
            status = "skipped"
        elif semver_tuple(latest) > semver_tuple(current):
            status = "updated"
        elif semver_tuple(latest) == semver_tuple(current):
            status = "unchanged"
        else:
            # latest found is older than current pin — keep current
            latest = current
            status = "unchanged"

        results.append((var, current, latest or "?", status))

    print(f"{'Variable':<25} {'Current':<20} {'Latest':<20} Status")
    print("-" * 80)
    for var, cur, lat, status in results:
        icon = {"updated": "✓", "unchanged": "—", "skipped": "⚠"}.get(status, "?")
        print(f"{var:<25} {cur:<20} {lat:<20} {icon} {status}")

    print("\n--- UPDATES ---")
    for var, cur, lat, status in results:
        if status == "updated":
            print(f"UPDATE:{var}={lat}")

if __name__ == "__main__":
    main()
