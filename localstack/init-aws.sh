#!/bin/bash

echo "🚀 Initializing LocalStack AWS resources for GirLocal..."

# Create SNS Topic
echo "Creating SNS topic 'local-events'..."
awslocal sns create-topic \
    --name local-events \
    --region eu-central-1
echo "✅ SNS topic 'local-events' created."

# Create DynamoDB Table for User Sessions
echo "Creating DynamoDB table 'user_sessions_dev'..."
awslocal dynamodb create-table \
    --table-name user_sessions_dev \
    --attribute-definitions \
        AttributeName=PK,AttributeType=S \
        AttributeName=SK,AttributeType=S \
        AttributeName=userId,AttributeType=S \
    --key-schema \
        AttributeName=PK,KeyType=HASH \
        AttributeName=SK,KeyType=RANGE \
    --global-secondary-indexes \
        "IndexName=GSI_USER_ID,KeySchema=[{AttributeName=userId,KeyType=HASH}],Projection={ProjectionType=ALL}" \
    --billing-mode PAY_PER_REQUEST \
    --region eu-central-1

# Wait for table to be active before updating TTL
awslocal dynamodb wait table-exists \
    --table-name user_sessions_dev \
    --region eu-central-1

# Configure TTL on DynamoDB Table
echo "Configuring TTL for 'user_sessions_dev' table..."
awslocal dynamodb update-time-to-live \
    --table-name user_sessions_dev \
    --time-to-live-specification "Enabled=true,AttributeName=ttl" \
    --region eu-central-1

echo "✅ DynamoDB table 'user_sessions_dev' created and configured."

# Create SQS Queue
echo "Creating SQS queue 'initialize-profile-queue'..."
awslocal sqs create-queue \
    --queue-name initialize-profile-queue \
    --region eu-central-1
echo "✅ SQS queue 'initialize-profile-queue' created."

# Verify SES Email Identity
echo "Verifying SES email identity 'noreply@girlocal.com'..."
awslocal ses verify-email-identity \
    --email-address noreply@girlocal.com \
    --region eu-central-1
echo "✅ SES email identity 'noreply@girlocal.com' verified."

# Create API Gateway (HTTP Proxy to Spring Boot)
echo "Creating API Gateway 'girapi-gateway'..."
API_ID=$(awslocal apigateway create-rest-api --name 'girapi-gateway' --region eu-central-1 --query 'id' --output text)
ROOT_ID=$(awslocal apigateway get-resources --rest-api-id $API_ID --region eu-central-1 --query 'items[0].id' --output text)

# Create a catch-all proxy resource: /{proxy+}
RESOURCE_ID=$(awslocal apigateway create-resource \
    --rest-api-id $API_ID \
    --parent-id $ROOT_ID \
    --path-part '{proxy+}' \
    --region eu-central-1 \
    --query 'id' --output text)

# Create ANY method for the proxy
awslocal apigateway put-method \
    --rest-api-id $API_ID \
    --resource-id $RESOURCE_ID \
    --http-method ANY \
    --authorization-type "NONE" \
    --request-parameters "method.request.path.proxy=true" \
    --region eu-central-1

# Integrate API Gateway with Spring Boot (running on localhost:8080)
# Note: 'host.docker.internal' is used so LocalStack (Docker) can reach your local machine.
awslocal apigateway put-integration \
    --rest-api-id $API_ID \
    --resource-id $RESOURCE_ID \
    --http-method ANY \
    --type HTTP_PROXY \
    --integration-http-method ANY \
    --uri "http://host.docker.internal:8080/{proxy}" \
    --request-parameters "integration.request.path.proxy=method.request.path.proxy" \
    --region eu-central-1

# Deploy the API Gateway
awslocal apigateway create-deployment \
    --rest-api-id $API_ID \
    --stage-name dev \
    --region eu-central-1

echo "✅ API Gateway created."
echo "🔗 You can access your API via Gateway at: http://localhost:4566/restapis/$API_ID/dev/_user_request_/"

echo "🎉 LocalStack initialization complete."
