#!/bin/sh
kubectl create secret generic mysql-pass --from-literal=user_password=XXXX --from-literal=root_password=XXXX --namespace objectify
