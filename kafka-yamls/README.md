# Kafka Setup in Kubernetes

This directory contains YAML configuration files to set up a Kafka system in a Kubernetes environment. The configuration includes Kubernetes Service and Deployment definitions for deploying Kafka brokers.

It's been tested with a minikube deployment in a local computer and not with a full Cloud Deployment.

## Files

- `kafka-namespace.yaml`: Defines a specific namespace for Kafka. (Alternatively you can run `kubectl create namespace <add-namespace-here>`)
- `zookeeper-deployment.yaml`: Defines a Kubernetes Deployment and Service for Zookeeper.
- `kafka-broker-deployment.yaml`: Defines a Kubernetes Deployment and Service for the Kafka brokers.
- `schema-resgistry.yaml`: Defines a Service and Deployment for a schema registry listening on port 8081.

## Usage

To deploy the Kafka system in your Kubernetes cluster, apply the YAML configuration files using `kubectl`:

```bash
kubectl apply -f kafka-namespace.yaml
kubectl apply -f zookeeper-deployment.yaml
kubectl apply -f kafka-broker-deployment.yaml
```

This will create the necessary Kubernetes resources to run Kafka brokers in your cluster.

## Hosts File Configuration

For this Kafka setup to fully work, it's required to add an entry to the `/etc/hosts` file on your local machine:

```bash
sudo vim /etc/hosts
```

Adding the following line:

```bash
127.0.0.1    kafka-broker
```

This entry ensures that the hostname `kafka-broker` resolves to `127.0.0.1` on your local machine, allowing clients to connect to the Kafka brokers deployed in the Kubernetes cluster.

Make sure to edit the `/etc/hosts` file with appropriate permissions (e.g., using `sudo` on Unix-based systems) and replace `127.0.0.1` with the IP address of the machine where the Kafka brokers are running if necessary.

