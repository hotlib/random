#!/bin/sh
kubectl create -f aai-namespace.yaml
kubectl create -f cassandra-service.yaml
#kubectl create -f cassandra-volumes.yaml
kubectl create -f cassandra-statefulset.yaml
