# control-center-testing

- Tested with a Debian 12 VM running Gnome Desktop

## Prerequisites: 
- Install minikube: https://minikube.sigs.k8s.io/docs/start/
- Install helm: https://helm.sh/docs/intro/install/
- Install kubectl: https://kubernetes.io/docs/tasks/tools/install-kubectl-linux/

## Setup instructions:

1. Start minikube 
```
   minikube start
```
   see below for handy script to delete and recreate with set amount of ram and cpus... Control Center needs aobut 4G of ram with a small Vaadin app deployed.  

3. turn on minikube tunnel (needs sudo and takes a few seconds)
```
minikube tunnel
```
5. install Control Center (check up to date script from: https://vaadin.com/docs/latest/control-center/getting-started
```
helm install control-center oci://docker.io/vaadin/control-center -n control-center --create-namespace --set serviceAccount.clusterAdmin=true --set service.type=LoadBalancer --set service.port=8000 --wait
```
or a specifcic version of ControlCenter (1.0.0 for instance):
```
helm install control-center oci://docker.io/vaadin/control-center --version 1.0.0 -n control-center --create-namespace --set serviceAccount.clusterAdmin=true --set service.type=LoadBalancer --set service.port=8000 --wait
```

6. portforward localhost:8000 to the CC node port 8080 in order to follow tutorial:  
You can find the pod name with: 

```
kubectl get pods --all-namespaces
``` 

And then port forward like this: 

```
kubectl port-forward control-center-689978d4c8-kx52x 8000:8080 -n control-center
```

7. navigate to http://localhost:8000 and follow tutorial to get the password from logs etc. Use *keycloak.local* as keycloak name, remember the email address and password for KC realm (you'll need it later) (If the installation fails just hit retry... it'll succeed eventually...)

8. Find the external ip for your `ingress-nginx-controller` for instance with:

```
kubectl get svc -A
```

it should look something like this: 

```
deb@deb-desk:~$ kubectl get svc -A
NAMESPACE        NAME                                 TYPE           CLUSTER-IP       EXTERNAL-IP     PORT(S)                      AGE
cert-manager     cert-manager                         ClusterIP      10.109.130.95    <none>          9402/TCP                     32d
cert-manager     cert-manager-webhook                 ClusterIP      10.109.114.7     <none>          443/TCP                      32d
cnpg-system      cnpg-webhook-service                 ClusterIP      10.99.170.32     <none>          443/TCP                      32d
control-center   ccapp                                ClusterIP      10.97.203.67     <none>          8080/TCP                     30d
control-center   control-center                       LoadBalancer   10.101.43.73     10.101.43.73    8000:31428/TCP               32d
control-center   control-center-keycloak-discovery    ClusterIP      None             <none>          7800/TCP                     32d
control-center   control-center-keycloak-service      ClusterIP      10.110.207.226   <none>          8180/TCP,8543/TCP,9000/TCP   32d
control-center   control-center-postgres-r            ClusterIP      10.100.177.161   <none>          5432/TCP                     32d
control-center   control-center-postgres-ro           ClusterIP      10.110.43.38     <none>          5432/TCP                     32d
control-center   control-center-postgres-rw           ClusterIP      10.109.204.17    <none>          5432/TCP                     32d
control-center   keycloak-operator                    ClusterIP      10.99.193.140    <none>          80/TCP                       32d
default          kubernetes                           ClusterIP      10.96.0.1        <none>          443/TCP                      32d
ingress-nginx    ingress-nginx-controller             LoadBalancer   10.99.205.130    10.99.205.130   80:31270/TCP,443:31459/TCP   32d
ingress-nginx    ingress-nginx-controller-admission   ClusterIP      10.97.189.25     <none>          443/TCP                      32d
kube-system      kube-dns                             ClusterIP      10.96.0.10       <none>          53/UDP,53/TCP,9153/TCP       32d
```

where the line you're looking for is the ingregess with ports 80 and 443, so in the example above it would be this (if there is no "External IP" check your `minikube tunnel` command is working) : 

```
ingress-nginx    ingress-nginx-controller             LoadBalancer   10.99.205.130    10.99.205.130   80:31270/TCP,443:31459/TCP   32d
```

in theory, the control center UI should be under: 

```
control-center   control-center                       LoadBalancer   10.101.43.73     10.101.43.73    8000:31428/TCP               32d
```

9. update `sudo nano /etc/hosts` entries to match, for instance: 

	* 10.107.151.82   keycloak.local
	* 10.107.151.82   ccapp.local
	* 10.107.151.82   preview.ccapp.local

12. on http://localhost:8000 you'll have ControlCenter UI where you can deploy apps.. 
13. on https://ccapp.local you'll eventually have an app (hopefully)

## Use a preeixting testing app
1. Navigate to the Control Center deployment UI "Applications -> Discovery" press "Deploy"
2. Give the application a suitable name for instance `ccapp`
3. In the "Image" field enter: 
```
petrivaadin/control-center-testing:latest
```
4. Set "Hostname" to `ccapp.local` or what ever you had set in your `/etc/hosts`
5. Leve other options off (i.e. no secure connections, identity management, localisations etc. the app is very minimal and intended to test that the env. is working, not demonstrate features)
6. Hit deploy... The image should download and be available under `http://ccapp.local
7. You can monitor the image deployment with: 
```
watch -n 1 kubectl get pods -n -control-cetner
```

### If things don't go as planned
1. Remove the app crom the CC UI if it is listed there...
2. Remove kubernetes configs for it:
```
kubectl delete deployment ccapp -n control-center & kubectl delete service ccapp -n control-center & kubectl delete ingress ccapp -n control-center
```
3. Retry.. 

## Deploy your own app: 

Create an app:

1. go to http://start.vaadin.com , start a new project and add a few screens
2. download the project, choose the appropriate java version (17/21) based on what is installed on your host
3. Select `docker runtime` to get the Dockerfile configs ready for you... 
4. compile the project (we need production as docker image doesn't have your proKey, hoswapping not really a thing etc...)

```
./mvnw clean package -Pproduction 
```

5. while it's downloading the internet, navigate to http://vaadin.com/docs and find the control center dependency that you'll need: https://vaadin.com/docs/latest/control-center/application-discovery

7. Modify your pom.xml and add the dependency... 

8. if you're doing this before actual release change the version of `control-center-start` to `1.0-SNAPSHOT` as that seems to be published somewhere
9. *Running locally* On the cluster this is somehow injectec, but if you want to run it locally from now on, in your `Application.java` class, add a new bean for the ControlCenterI18nProperties: 
    
```
    @Bean
    public ControlCenterI18NProperties createControlCenterI18NProperties() {
        return new ControlCenterI18NProperties();
    }  
```

9. Recompile again with 
```
./mvnw clean package -Pproduction
```

10. Assuming things went well let's make the docker image:
11. build the docker container with (notice the space and . after 1.0.0 this is important is it is the root context for the docker container buid, i.e. where your `Dockerfile` should be)
```
docker buildx build -t ccapp:1.0.0 .
```  
12. Let's push the image to minikube with: 
```
minikube image load ccapp:1.0.0
```
13. once done, go to http://localhost:8000 and deploy a new ccapp, set the "image" value to `ccapp:1.0.0` or what ever version you just uploaded. 
14: Monitor the deployment with 
```
watch -n 1 kubectl get pods --all-namespaces
````
if the ccapp pod keeps restarting, you can try: 
```
kubectl logs <POD_NAME_FROM_GET_PODS> -n control-center
```
15. naviagate to http://ccapp.local 
16. if it doesn't work: here are a few steps: 
* restart minikube and minikube tunnel and the 8000-port forward thing.. 
* If you get an error about cookies not being enabled: try in incognito if you've already tried a few times as the ingress cookies can sometimes do wierd things saying cookies, if it works, delete the cookies (espeically ingress) from your normal browser side...
* double check that your `/etc/hosts` points to the `External IP` of the `ingress-nginx-controller`

# Creating your own iamges and pushing them to Docker Hub
Pushing to Dockerhub: 

Tested on a Debian 12 VM 

1. Make a Dockerhub account
2. Enable 2fa.. 
3. Create a personal access token with write access
4. Create a docker hub repository, you can have 1 private on the free plan
* a private repo requires you to login or use a token to access it.. 
* public is public, but simpler to pull from.. 
* you can flip flop the repo from private to public to private... 

#### In your docker environment: 
1. Login to docker hub

``` 
docker login -u <username>
```

2. provide your token

3. Build your project (the . is important)
```
docker buildx build -t <username>/<repo>:<version> .
```

4. Push to upstream:
```
docker push <username>/<repo>:<version>
```

## Cross arch images
To make images for other environments than you are on (say you're on an ARM Macbook but your docker/kubernetes environment is x86): Dockers guide: https://docs.docker.com/build/building/multi-platform/

### Quick and dirty: 

1. For command line linux: 
```
sudo nano /etc/docker/daemon.json
```

Add there: 
```
{
  "features": {
    "containerd-snapshotter": true
  }
}
```

2. Restart docker to apply new configs
``` 
sudo systemctl restart docker
```

3. Some docker magic... 
```
docker buildx create --name container-builder --driver docker-container  --use --bootstrap
```

4. Build your cross arch containers

```
docker build --platform linux/amd64,linux/arm64 -t petrivaadin/control-center-testing:1.0.1 .
``` 

8. Push your docker image upstream

```
docker push <username>/<repo>:<version>
``` 

9. Use it like you would any other docker image... 

# Memo/Helpers

Misc stuff...

#### Reset and retry
handy script for resetting Minikube: 
- make a file `restartControlCenter.sh` with the following content to reset your minikube and restart/reinstall CC: 
```
#!/bin/sh

minikube stop && minikube delete --all && minikube start --memory 4096 --cpus 6 #--listen-address=0.0.0.0 #--ports 443:443,8000:8000

#minikube tunnel &

helm install control-center oci://docker.io/vaadin/control-center     -n control-center --create-namespace     --set serviceAccount.clusterAdmin=true     --set service.type=LoadBalancer --set service.port=8000     --wait
```

#### Port forward a port: 
```
kubectl port-forward control-center-689978d4c8-kx52x 8000:8080 -n control-center
```

#### MEMO - build and package - make container - upload container
```
./mvnw clean package -Pproduction && docker buildx build -t ccapp:1.0.0 . && minikube image load ccapp:1.0.0
```


#### MEMO - if CC looses the app but it's still deployed, check and remove it with (ccapp is the name you gave it in CC, also what the name starts with in kubectl get pods -n control-center)
#Don't care remove all: 
```
kubectl delete deployment ccapp -n control-center & kubectl delete service ccapp -n control-center & kubectl delete ingress ccapp -n control-center & kubectl delete secret ccapp -n control-center
```

#Check: 
```
kubectl get deployment -n control-center
```
```
kubectl get svc -n control-center
```
```
kubectl get ingress -n control-center
```
```
kubectl get secrets  -n control-center
```

#Remove
```
kubectl delete deployment ccapp -n control-center
```
```
kubectl delete service ccapp -n control-center
```
```
kubectl delete ingress ccapp -n control-center
```
```
kubectl delete secret ccapp -n control-center
```
after this you probably need to log out of CC and log back in to fix the "Select application" dropdown
