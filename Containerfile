ARG IMAGE_JRE
FROM bellsoft/liberica-openjre-alpine:${IMAGE_JRE} AS runtime

# Install utilities, update binaries, create user, set permissions and clean up
RUN apk add --no-cache openssl bash && \
    apk upgrade --no-cache && \
    addgroup -g 1000 appgroup && \
    adduser -u 1000 -G appgroup -s /bin/sh -D appuser && \
    rm -rf /var/cache/apk/* /tmp/* /var/tmp/*

# Set working directory
WORKDIR /app

RUN chown appuser:appgroup /app

# Copy application and entrypoint
COPY --chown=appuser:appgroup bin/app.jar app.jar
COPY entrypoint.sh /usr/local/bin/entrypoint.sh

# Set entrypoint permissions
RUN chmod +x /usr/local/bin/entrypoint.sh

# Use non-root user
USER appuser

# Set application start entrypoint
ENTRYPOINT ["entrypoint.sh"]
