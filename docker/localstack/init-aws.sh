#!/bin/bash
awslocal sqs create-queue --queue-name ordem-finalizada
awslocal sqs create-queue --queue-name ordem-cancelada
awslocal sqs list-queues

awslocal events create-event-bus --name status-pedido-bus --region us-east-1

awslocal events put-rule --name pedido-aprovado --event-bus-name status-pedido-bus --event-pattern '{"detail-type": ["APROVADO"]}'
awslocal events put-rule --name pedido-reprovado --event-bus-name status-pedido-bus --event-pattern '{"detail-type": ["REPROVADO"]}'

awslocal events put-targets --rule pedido-aprovado --event-bus-name status-pedido-bus --targets "Id=1,Arn=arn:aws:sqs:us-east-1:000000000000:ordem-finalizada"
awslocal events put-targets --rule pedido-reprovado --event-bus-name status-pedido-bus --targets "Id=1,Arn=arn:aws:sqs:us-east-1:000000000000:ordem-cancelada"

awslocal events list-targets-by-rule --event-bus-name status-pedido-bus --rule pedido-aprovado
awslocal events list-targets-by-rule --event-bus-name status-pedido-bus --rule pedido-reprovado
