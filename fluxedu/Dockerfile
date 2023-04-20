# pobierz obraz jenkinsa
FROM jenkins/jenkins:lts
# zainstaluj dodatkowe narzędzia
USER root
RUN apt-get update && apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg2 \
    software-properties-common \
    && curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add - \
    && add-apt-repository \
       "deb [arch=amd64] https://download.docker.com/linux/debian \
       $(lsb_release -cs) \
       stable" \
    && apt-get update && apt-get install -y docker-ce \
    && apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

#zainstaluj plugin blue ocean docker workflow przy pomocy jenkins-plugin-cli --> fail
#RUN /usr/local/bin/install-plugins.sh blueocean:1.3.2 docker-workflow:1.14

# install blueocean plugin --> fail
# RUN /usr/local/bin/install-plugins.sh blueocean:1.3.2

RUN jenkins-plugin-cli --plugins "blueocean docker-workflow"
