# Setup kuberentes in Docker (KinD)

Tested on Debian 12 running Gnome

## Prerequisites:
* Install docker: https://docs.docker.com/engine/install/debian/
* Install helm: https://helm.sh/docs/intro/install/
* Install kubectl: https://kubernetes.io/docs/tasks/tools/install-kubectl-linux/


## Install Kind

Up to date instructions here: https://kind.sigs.k8s.io/docs/user/quick-start/

ARM64: 
```
[ $(uname -m) = aarch64 ] && curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.24.0/kind-linux-arm64
```

AMD64:
```
[ $(uname -m) = x86_64 ] && curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.24.0/kind-linux-amd64
```

Set execution flag for kind binary: 

```
chmod +x ./kind
```

Copy to PATH:
```
sudo mv ./kind /usr/local/bin/kind
```

#Create a cluster
```
kind create cluster
```

#Start a docker container on the "kind" network to serve "public IPs" (not really public, but public for Kubernetes)
```
docker run --rm --network kind -v /var/run/docker.sock:/var/run/docker.sock petrivaadin/cloud-provider-kind:0.4.0
```

Alternatively, you can clone: https://github.com/kubernetes-sigs/cloud-provider-kind and build the image yourself

# Install Control Center
```
helm install control-center oci://docker.io/vaadin/control-center -n control-center --create-namespace --set serviceAccount.clusterAdmin=true --set service.type=LoadBalancer --set service.port=8000 --wait
```

## Follow the containers using: 
```
watch -n 1 kubectl get pods -A
```

once the control-center container is up and running, note the name and port forward port 8000 to 8080 like this: 
```
kubectl port-forward control-center-689978d4c8-kx52x 8000:8080 -n control-center
```

open http://localhost:8000 and you should see the control-center installation wizard. 

Follow the control-center installation tutorial, using `keycloak.local` as the keycloak hostname (or modify later in the hosts file), once the installation starts. Follow the containers and espeically their external IP's using: 
```
watch -n 1 kubectl get svc -A 
```

And once the `ingress-nginx-controller` gets a pulbic ip, then update: 
```
sudo nano /etc/hosts
```

with the public ip for the `ingress-nginx-controller`, for instance: 

* 172.18.0.5 keycloak.local
* 172.18.0.5 ccapp.local
* 172.18.0.5 preview.ccapp.local

If you update the hosts file before clicking on "Go to Dashboard" the transition from the wizard to keycloak should work. If not, update the hosts file and navigate to http://localhost:8000/startup 

Once logged in, control-center should be ready to use, refer to minikube instructions https://hub.docker.com/repository/docker/petrivaadin/control-center-testing/general for additional configs etc.. 

Refer to Kind documentation for how to deploy local docker images: [
](https://kind.sigs.k8s.io/docs/user/quick-start/#loading-an-image-into-your-cluster)

For instance how to move a image from the host to Kinds registry: 
```
kind load docker-image my-custom-image:unique-tag
```
