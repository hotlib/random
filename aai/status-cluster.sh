#!/bin/sh
kubectl exec cassandra-0 --namespace aai-cassandra -- nodetool status
#kubectl logs --namespace aai-cassandra cassandra-0
