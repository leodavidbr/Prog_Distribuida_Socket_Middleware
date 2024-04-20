package imd.ufrn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import imd.ufrn.interfaces.BaseCommunicationWithServerController;
import imd.ufrn.model.MessageRecieved;

// message for subscription should be of the format: "subscribeService;serviceName;host;port"
// ex:  subscribeService;catsService;127.0.0.1;9999 
// message for get service should be of the format: "getService;serviceName"
// ex: getService;catsService
public class nameServiceController {
    BaseCommunicationWithServerController communicationWithServerController;
    // Map of serviceName to host;port
    Map<String, String> namesSubscribed = new HashMap<>();
    String subscribeServiceCode = "subscribeService";
    String getServiceCode = "getService";

    public nameServiceController() {
        communicationWithServerController = new SocketCommunicationController(
                messageRecieved -> handleMessageRecieved(messageRecieved));
    }

    private void handleMessageRecieved(MessageRecieved messageRecieved) {
        List<String> messageTokens = Arrays.asList(messageRecieved.getMessage().split(";"));
        String command = messageTokens.get(0);
        List<String> argumentsMessage = messageTokens.subList(1, messageTokens.size());

        if (command == subscribeServiceCode) {
            handleSubscribeMessage(argumentsMessage, messageRecieved.getClientIdx());
        } else if (command == getServiceCode) {
            handleGetMessage(argumentsMessage, messageRecieved.getClientIdx());
        } else {
            System.out.println(
                    "Type of request not recognized: " + command + ". in message: " + messageRecieved);
        }

    }

    private void handleSubscribeMessage(List<String> subscribeMessageArguments, int clientIdx) {
        String serviceName = subscribeMessageArguments.get(0);
        String informationToStore = subscribeMessageArguments.get(1) + ";" + subscribeMessageArguments.get(2);

        namesSubscribed.put(serviceName, informationToStore);
    }

    private void handleGetMessage(List<String> getMessageArguments, int clientIdx) {
        String serviceName = getMessageArguments.get(0);

        String response = namesSubscribed.get(serviceName);
        communicationWithServerController.sendMessage(response, clientIdx);
    }
}
