#!/bin/bash

# Faz com que o script pare imediatamente se algum comando falhar
set -e

# --- VARIÁVEIS DE CONFIGURAÇÃO ---
RESOURCE_GROUP="patio-api"
ACR_NAME="acrpatioapi31315"
IMAGE_NAME="patio-api:v2" # <- A TAG FOI ATUALIZADA AQUI

# --- COMANDO PARA BUILD E PUSH DA IMAGEM ---
echo "=> Construindo e enviando a imagem v2: $IMAGE_NAME..."
az acr build \
    --registry $ACR_NAME \
    --resource-group $RESOURCE_GROUP \
    --image $IMAGE_NAME .

echo ""
echo "=> Verificando se a nova tag 'v2' foi enviada..."

# Comando de verificação para a nova tag
az acr repository show-tags --name $ACR_NAME --repository patio-api -o table

echo ""
echo "SUCESSO! A imagem v2 foi construída e enviada para o ACR."