FROM maven:3.9-eclipse-temurin-17

# Install Google Chrome
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    --no-install-recommends \
 && wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
 && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" \
    > /etc/apt/sources.list.d/google-chrome.list \
 && apt-get update && apt-get install -y google-chrome-stable --no-install-recommends \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . .

# Pre-download dependencies so the test run is fast
RUN mvn dependency:go-offline -q

# CI=true triggers headless mode in BaseTest
ENV CI=true

CMD ["mvn", "test"]
