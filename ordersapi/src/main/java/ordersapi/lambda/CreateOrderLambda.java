package ordersapi.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ordersapi.model.Order;
;
public class CreateOrderLambda {
    public APIGatewayProxyResponseEvent createOrder(APIGatewayProxyResponseEvent request) throws JsonMappingException, JsonProcessingException{
    	System.out.println("Inside crate order*********************");
        ObjectMapper objectMapper = new ObjectMapper();
        Order order = objectMapper.readValue(request.getBody(), Order.class);
        DynamoDB dynamoDb =  new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
        Table table = dynamoDb.getTable(System.getenv("ORDERS_TABLE"));
        Item item = new Item().withPrimaryKey("id",order.getId()).
        		withString("itemName", order.getItemName()).
        		withInt("quantity", order.getQuantity());
        table.putItem(item);
		return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Order ID: "+order.getItemName());

    }
}
