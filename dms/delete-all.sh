#!/bin/sh
kubectl delete --namespace objectify pod dms-mysql
kubectl delete --namespace objectify pod dms-solr
kubectl delete --namespace objectify deployment dms-jetty
kubectl delete --namespace objectify service mysql
kubectl delete --namespace objectify service solr
kubectl delete --namespace objectify service dms
kubectl delete namespace objectify 

