#!/bin/sh
kubectl create secret generic nginx-proxy-tls --from-file=key.pem --from-file=dhparam.pem --from-file=cert.crt --namespace objectify
