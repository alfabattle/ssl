# Description

Ssl example application.

# Setup local environment

1. Copy `.env.dist` file to `.env.` and optionally make configuration changes.
1. Run `docker-compose up -d`. Be patient it takes some time on first run.

# Useful commands

## Common

1. Building for production

        ./gradlew build

1. Update dependencies

        ./gradlew clean resolveAndLockAll --write-locks

## Certificates

1. keytool -genkey -keyalg RSA -keystore keystore.jks -keysize 2048   (sukapizda - keystore, blabla - key)
1. keytool -list -keystore keystore.jks
1. openssl s_client -connect apiws.alfabank.ru:443 -showcerts
1. openssl s_client -showcerts -connect apiws.alfabank.ru:443 </dev/null 2>/dev/null|openssl x509 -outform PEM >mycertfile.pem
1. keytool -delete -alias mykey -keystore keystore.jks
1. openssl pkcs8 -topk8 -inform PEM -outform PEM -in apidevelopers.key -out apidevelopers.pem -nocrypt
