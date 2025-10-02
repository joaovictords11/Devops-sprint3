#!/bin/bash

# Faz com que o script pare imediatamente se algum comando falhar
set -e

# --- 1. VARIÁVEIS DE CONFIGURAÇÃO ---
if [ -z "$DB_PASS" ]; then
    echo "ERRO: As variáveis de ambiente do banco de dados (DB_URL, DB_USER, DB_PASS) não foram definidas."
    echo "Execute os comandos 'export' antes de rodar este script."
    exit 1
fi

RESOURCE_GROUP="patio-api"
ACR_NAME="acrpatioapi31315"
ACI_NAME="aci-patio-api"
IMAGE_NAME="patio-api:v1"

# --- 2. COMANDO PARA CRIAR O AZURE CONTAINER INSTANCE (ACI) ---
echo "=> Criando a instância de contêiner '$ACI_NAME'..."
echo "=> Usando a imagem: $ACR_NAME.azurecr.io/$IMAGE_NAME"

az container create \
    --resource-group $RESOURCE_GROUP \
    --name $ACI_NAME \
    --image "$ACR_NAME.azurecr.io/$IMAGE_NAME" \
    --ports 8080 \
    --dns-name-label "$ACI_NAME-$(date +%s)" \
    --os-type Linux \
    --cpu 1 \
    --memory 1 \
    --registry-login-server "$ACR_NAME.azurecr.io" \
    --registry-username "$(az acr credential show --name $ACR_NAME --query "username" -o tsv)" \
    --registry-password "$(az acr credential show --name $ACR_NAME --query "passwords[0].value" -o tsv)" \
    --environment-variables \
        'SPRING_DATASOURCE_URL'="$DB_URL" \
        'SPRING_DATASOURCE_USERNAME'="$DB_USER" \
        'SPRING_DATASOURCE_PASSWORD'="$DB_PASS"

echo ""
echo "=> Verificando o status do deploy..."

# --- 3. COMANDOS DE VERIFICAÇÃO ---
sleep 15
PUBLIC_URL=$(az container show --resource-group $RESOURCE_GROUP --name $ACI_NAME --query "ipAddress.fqdn" -o tsv)

echo ""
echo "SUCESSO! Sua API foi implantada."
echo "Acesse através da URL: http://$PUBLIC_URL:8080/motos"
echo ""
echo "Para ver os logs, use o comando:"
echo "az container logs --resource-group $RESOURCE_GROUP --name $ACI_NAME"