FROM eclipse-temurin:17-jdk

ENV DEBIAN_FRONTEND noninteractive

# Get the basic stuff
RUN apt-get update && \
    apt-get -y upgrade && \
    apt-get install -y \
    sudo git nano htop docker.io && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# (Optional) Add the ubuntu user to the docker group so that it can access the Docker socket
RUN sudo groupadd docker || true && sudo usermod -aG docker ubuntu

RUN echo "ubuntu ALL=(ALL) NOPASSWD:ALL" > /etc/sudoers.d/ubuntu && \
    chmod 0440 /etc/sudoers.d/ubuntu

RUN mkdir /workspace
RUN chown ubuntu:ubuntu /workspace

# Set as default user
USER ubuntu
#WORKDIR /home/ubuntu
WORKDIR /workspace

ENV DEBIAN_FRONTEND teletype

CMD ["/bin/bash"]