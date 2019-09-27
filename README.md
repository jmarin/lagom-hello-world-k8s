# lagom-hello-world-k8s
Lagom Hello World deployment to Kubernetes

## Infrastructure Dependencies

### Postgres

Deploy `Postgres` to `minikube` as follows:

```shell
kubectl apply -f k8s/postgres/postgres-pv.yaml
kubectl apply -f k8s/postgres/postgres-pvc.yaml
kubectl apply -f k8s/postgres/postgres-secrets.yaml
kubectl apply -f k8s/postgres/postgres-deployment.yaml
kubectl apply -f k8s/postgres/postgres-service.yaml
```

Add the `Lagom` schema as follows:

- First, forward port from running `postgres` pod

```shell
kubectl port-forward postgres-deployment-777779fd7b-nqkc7 5432:5432 (change pod name as needed)
```

Connect to `postgres` in your localhost and create the Lagom schema:

```shell
psql -h localhost -U postgres
```

```sql
CREATE TABLE IF NOT EXISTS journal (
  ordering BIGSERIAL,
  persistence_id VARCHAR(255) NOT NULL,
  sequence_number BIGINT NOT NULL,
  deleted BOOLEAN DEFAULT FALSE,
  tags VARCHAR(255) DEFAULT NULL,
  message BYTEA NOT NULL,
  PRIMARY KEY(persistence_id, sequence_number)
);

CREATE UNIQUE INDEX journal_ordering_idx ON journal(ordering);

CREATE TABLE IF NOT EXISTS snapshot (
  persistence_id VARCHAR(255) NOT NULL,
  sequence_number BIGINT NOT NULL,
  created BIGINT NOT NULL,
  snapshot BYTEA NOT NULL,
  PRIMARY KEY(persistence_id, sequence_number)
);

CREATE TABLE read_side_offsets (
  read_side_id VARCHAR(255), tag VARCHAR(255),
  sequence_offset bigint, time_uuid_offset char(36),
  PRIMARY KEY (read_side_id, tag)
);
```

### Kafka

Deploy `Kafka` to `minikube` as follows: 

```shell
kubectl apply -f k8s/kafka/kafka_strimzi.yaml
kubectl apply -f k8s/kakfa/kafka.yaml
```

## Running

```shell
sbt runAll
```

## Deploying the application to Kubernetes

First, add the necessary permissions for the Lagom service to form a cluster:

```shell
kubectl apply -f k8s/hello-world/hello-world-rbac.yaml
```

For the application to run, we will need to create a secret for Play (it's a good idea to configure this, even though this example doesn't use it directly): 

```shell
kubectl create secret generic hello-world-service-secret --from-literal=secret="$(openssl rand -base64 48)"
```

And deploy the hello-world application: 

```shell
kubectl apply -f k8s/hello-world/hello-world-service.yaml
```