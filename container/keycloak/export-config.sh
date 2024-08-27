#!/bin/zsh

KC_DIR=/opt/keycloak
KC_VOL_DIR=$(pwd)/container/keycloak
KC_DB_USERNAME=admin
KC_DB_PWD=adminpwd
KC_DB_URL=jdbc:postgresql://keycloak-db:5432/keycloak
KC_REALM_NAME_EXPORT=realm-export.json
KC_REALM_NAME=realm.json

podman exec auth-server $KC_DIR/bin/kc.sh export --file $KC_DIR/data/import/$KC_REALM_NAME_EXPORT --db-url=$KC_DB_URL --db-username=$KC_DB_USERNAME --db-password=$KC_DB_PWD --verbose || true

if [[ -f "$KC_VOL_DIR/config/$KC_REALM_NAME_EXPORT" ]]; then
    echo "Keycloak Realm backup generated"
    rm -rf $KC_VOL_DIR/config/$KC_REALM_NAME
    mv $KC_VOL_DIR/config/$KC_REALM_NAME_EXPORT $KC_VOL_DIR/config/$KC_REALM_NAME
else
    echo "Keycloak Realm backup not generated"
fi
