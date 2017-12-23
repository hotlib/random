#!/bin/sh
kubectl create -f objectify-namespace.yaml
kubectl create -f mysql-pod.yaml
kubectl create -f solr-pod.yaml 
kubectl create -f mysql-service.yaml 
kubectl create -f solr-service.yaml 
sleep 10s
kubectl create -f jetty-deployment.yaml
kubectl create -f jetty-service.yaml 
