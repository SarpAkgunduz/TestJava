package TestiniumBeymen;
import kong.unirest.core.JsonNode;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import kong.unirest.core.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestAPI {
    public static void main(String[] args) {
        String createBoardUrl = "https://api.trello.com/1/boards";
        
        String apiKey = "0783e4baa5d36d207b12ab91ece94da3";
        String token = "ATTAc1ee6895ec466136fce1afedbcf82c72b563123bcbb9f76b544c139097c1c67921AB62C0";
        String boardName = "My Test Board";
        
        // Send a POST request to create the board
        HttpResponse<JsonNode> response = Unirest.post(createBoardUrl)
                .queryString("key", apiKey)
                .queryString("token", token)
                .queryString("name", boardName)
                .asJson();
        
        // Checking the response
        if (response.isSuccess()) {
            System.out.println("Board created successfully!");
            String boardID = response.getBody().getObject().getString("id");
            System.out.println("Board ID: " + boardID);
            
            List<String> cardIDs = new ArrayList<>();
            
            // First we need to create a list to create cards so we create it here 
            //also we are taking it's id from the return statement inside the method
            String listID = createList(boardID, apiKey, token, "List Test");
            // Here we create two cards
            for (int i = 0; i < 2; i++) {
                String cardName = "MyCard" + i;
                String cardID = createCard(listID, cardName, apiKey, token);
                cardIDs.add(cardID);
            }
            
            // Here we update the description of a random card
            Random random = new Random();
            int index = random.nextInt(cardIDs.size());
            String randomCardID = cardIDs.get(index);
            updateCardDescription(randomCardID, apiKey, token);
            
            // Here we delete the created cards
            for (String cardID : cardIDs) {
                deleteCard(cardID, apiKey, token);
            }
            
            // Here we delete the board
            deleteBoard(boardID, apiKey, token);
        } else {
            System.err.println("Failed to create board. Error: " + response.getStatusText());
        }
    }
    
    private static String createCard(String boardID, String cardName, String apiKey, String token) {
        HttpResponse<JsonNode> cardResponse = Unirest.post("https://api.trello.com/1/cards")
                .header("Accept", "application/json")
                .queryString("idList", boardID)
                .queryString("key", apiKey)
                .queryString("token", token)
                .queryString("name", cardName)
                .asJson();
        
        if (cardResponse.isSuccess()) {
            System.out.println("Card created successfully: " + cardName);
            return cardResponse.getBody().getObject().getString("id");
        } else {
            System.err.println("Failed to create card: " + cardName + ". Error: " + cardResponse.getStatusText());
            return null;
        }
    }
    
    private static String createList(String boardID, String apiKey, String token, String listName){
    	HttpResponse<String> response = Unirest.post("https://api.trello.com/1/lists")
    			  .queryString("name", listName)
    			  .queryString("idBoard", boardID)
    			  .queryString("key", apiKey)
    			  .queryString("token", token)
    			  .asString();

    	JSONObject jsonResponse = new JSONObject(response.getBody());
        return jsonResponse.getString("id");
    }
    
    private static void updateCardDescription(String cardID, String apiKey, String token) {
        HttpResponse<JsonNode> response = Unirest.put("https://api.trello.com/1/cards/{id}")
                .header("Accept", "application/json")
                .routeParam("id", cardID)
                .queryString("key", apiKey)
                .queryString("token", token)
                .queryString("desc", "This is the new description")
                .asJson();
        
        if (response.isSuccess()) {
            System.out.println("Card description updated successfully!");
        } else {
            System.err.println("Failed to update card description. Error: " + response.getStatusText());
        }
    }
    
    private static void deleteCard(String cardID, String apiKey, String token) {
        HttpResponse<JsonNode> response = Unirest.delete("https://api.trello.com/1/cards/{id}")
                .routeParam("id", cardID)
                .queryString("key", apiKey)
                .queryString("token", token)
                .asJson();
        
        if (response.isSuccess()) {
            System.out.println("Card deleted successfully!");
        } else {
            System.err.println("Failed to delete card. Error: " + response.getStatusText());
        }
    }
    
    private static void deleteBoard(String boardID, String apiKey, String token) {
        HttpResponse<JsonNode> response = Unirest.delete("https://api.trello.com/1/boards/{id}")
                .routeParam("id", boardID)
                .queryString("key", apiKey)
                .queryString("token", token)
                .asJson();
        
        if (response.isSuccess()) {
            System.out.println("Board deleted successfully!");
        } else {
            System.err.println("Failed to delete board. Error: " + response.getStatusText());
        }
    }
}
