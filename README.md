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
kubectl port-forward postgres-deployment-777779fd7b-nqkc7 5432:5432
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
)
```

### Kafka

Deploy `Kafka` to `minikube` as follows: 

```shell
kubectl apply -f k8s/kafka/kafka-strimzi.yaml
kubectl apply -f k8s/kakfa/kafka.yaml
```

## Running

```shell
sbt runAll
```
