#!/bin/bash

# Faz com que o script pare imediatamente se algum comando falhar
set -e

# --- VARIÁVEIS DE CONFIGURAÇÃO ---
RESOURCE_GROUP="patio-api"
ACR_NAME="acrpatioapi31315"
IMAGE_NAME="patio-api:v1"

# --- COMANDO PARA BUILD E PUSH DA IMAGEM ---
echo "=> Fazendo build da imagem Docker e enviando para o ACR: $ACR_NAME..."
az acr build \
    --registry $ACR_NAME \
    --resource-group $RESOURCE_GROUP \
    --image $IMAGE_NAME .

echo ""
echo "=> Verificando se a imagem foi enviada com sucesso..."

# Comando de verificação
az acr repository list --name $ACR_NAME -o table

echo ""
echo "SUCESSO! A imagem foi construída e enviada para o ACR."