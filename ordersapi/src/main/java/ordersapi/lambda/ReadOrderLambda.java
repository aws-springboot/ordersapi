package ordersapi.lambda;

import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ordersapi.model.Order;

public class ReadOrderLambda {

	public APIGatewayProxyResponseEvent readOrder(APIGatewayProxyResponseEvent request) throws JsonMappingException, JsonProcessingException{
		System.out.println("Inside read order*********************");
        ObjectMapper objectMapper = new ObjectMapper();
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        ScanResult scanResult = amazonDynamoDB.scan(new ScanRequest().withTableName(System.getenv("ORDERS_TABLE")));
        List<Order> orders = scanResult.getItems().stream().map(item->new Order(Integer.parseInt(item.get("id").getN()),item.get("itemName").getS(),Integer.parseInt(item.get("quantity").getN()))).collect(Collectors.toList());
        String body = objectMapper.writeValueAsString(orders);
		return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(body);

    }
}
