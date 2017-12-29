#!/bin/sh
kubectl delete --namespace aai-cassandra statefulset cassandra
kubectl delete --namespace aai-cassandra service cassandra
#kubectl delete --namespace aai-cassandra pv cassandra-data
kubectl delete namespace aai-cassandra
