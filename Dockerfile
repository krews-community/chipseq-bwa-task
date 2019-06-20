FROM openjdk:8-jre-slim as base
RUN apt-get update && apt-get install -y \
    build-essential \
    wget \
    zlib1g-dev \
    python \
    git && \
    wget https://github.com/samtools/samtools/releases/download/1.9/samtools-1.9.tar.bz2 && \
    tar xvjf samtools-1.9.tar.bz2 && cd samtools-1.9 && \
    ./configure --without-curses --disable-lzma --disable-bz2 && \
    make && make install && cd .. && \
    rm -r samtools-1.9 && rm samtools-1.9.tar.bz2 && \
    git clone https://github.com/lh3/bwa.git && cd bwa && make && mv bwa /bin && cd .. && rm -rf bwa && \
    apt-get purge --auto-remove -y  git build-essential wget && \
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

FROM openjdk:8-jdk-alpine as build
COPY . /src
RUN ./gradlew clean shadowJar

FROM base
COPY --from=build /src/build/chipseq-bwa*.jar /app/chipseq.jar