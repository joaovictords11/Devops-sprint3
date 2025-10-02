#!/bin/bash

RESOURCE_GROUP="patio-api"

ACR_NAME="acrpatioapi$RANDOM"

LOCATION="brazilsouth"

IMAGE_NAME="patio-api:v1"

echo "=> Usando o grupo de recursos existente: $RESOURCE_GROUP"

echo "=> Criando Azure Container Registry (ACR)..."
az acr create --resource-group $RESOURCE_GROUP --name $ACR_NAME --sku Basic

echo "=> Fazendo build da imagem Docker e enviando para o ACR..."
az acr build \
    --registry $ACR_NAME \
    --resource-group $RESOURCE_GROUP \
    --image $IMAGE_NAME .

echo ""
echo "SUCESSO! A imagem '$IMAGE_NAME' foi construída e enviada para o ACR '$ACR_NAME'."
echo "Para ver sua imagem, acesse o Portal do Azure -> $ACR_NAME -> Repositórios."