#!/bin/bash

# Faz com que o script pare imediatamente se algum comando falhar
set -e

# --- 1. VARIÁVEIS DE CONFIGURAÇÃO ---
# O script espera que as variáveis DB_URL, DB_USER e DB_PASS
# já existam no ambiente do seu terminal.
if [ -z "$DB_PASS" ]; then
    echo "ERRO: As variáveis de ambiente do banco de dados (DB_URL, DB_USER, DB_PASS) não foram definidas."
    echo "Execute os comandos 'export' antes de rodar este script."
    exit 1
fi

RESOURCE_GROUP="patio-api"
ACR_NAME="acrpatioapi31315"
ACI_NAME="aci-patio-api" # <- O MESMO NOME DE ANTES PARA ATUALIZAR
IMAGE_NAME="patio-api:v2" # <- APONTANDO PARA A NOVA IMAGEM V2

# --- 2. COMANDO PARA ATUALIZAR O AZURE CONTAINER INSTANCE (ACI) ---
echo "=> Atualizando a instância de contêiner '$ACI_NAME' para usar a imagem v2..."

az container create \
    --resource-group $RESOURCE_GROUP \
    --name $ACI_NAME \
    --image "$ACR_NAME.azurecr.io/$IMAGE_NAME" \
    --ports 8080 \
    --dns-name-label "aci-patio-api-$(date +%s)" \
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
echo "SUCESSO! Sua API foi atualizada para a v2."
echo "Acesse através da URL: http://$PUBLIC_URL:8080/motos"
echo ""
echo "Para ver os logs, use o comando:"
echo "az container logs --resource-group $RESOURCE_GROUP --name $ACI_NAME"