#!/bin/sh
kubectl create secret generic mysql-pass --from-literal=user_password=XXXXXX --from-literal=root_password=XXXXX --namespace objectify
