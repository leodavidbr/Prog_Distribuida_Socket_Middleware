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
public class NameServiceController {
    BaseCommunicationWithServerController communicationWithServerController;
    // Map of serviceName to host;port
    Map<String, String> namesSubscribed = new HashMap<>();
    static final String subscribeServiceCode = "subscribeService";
    static final String getServiceCode = "getService";
    Thread communicationWithServerControllerThread;

    public NameServiceController() {
        communicationWithServerController = new SocketCommunicationController(
                messageRecieved -> handleMessageRecieved(messageRecieved));
        communicationWithServerControllerThread = new Thread(communicationWithServerController);
        communicationWithServerControllerThread.start();
    }

    private void handleMessageRecieved(MessageRecieved messageRecieved) {
        System.out.println("handleMessageRecieved: \"" + messageRecieved.getMessage() + "\"");

        List<String> messageTokens = Arrays.asList(messageRecieved.getMessage().split(";"));
        String command = messageTokens.get(0);
        System.out.println("message command is: \"" + command + "\"");

        List<String> argumentsMessage = messageTokens.subList(1, messageTokens.size());

        if (command.equals(subscribeServiceCode)) {
            handleSubscribeMessage(argumentsMessage, messageRecieved.getClientIdx());
        } else if (command.equals(getServiceCode)) {
            handleGetMessage(argumentsMessage, messageRecieved.getClientIdx());
        } else {
            System.out.println(
                    "Type of request not recognized: \"" + command + "\" in message: " + messageRecieved.getMessage());
        }

    }

    private void handleSubscribeMessage(List<String> subscribeMessageArguments, int clientIdx) {
        String serviceName = subscribeMessageArguments.get(0);
        String informationToStore = subscribeMessageArguments.get(1) + ";" + subscribeMessageArguments.get(2);

        namesSubscribed.put(serviceName, informationToStore);
        System.out.println("succesfuly subscribed service of name: \"" + serviceName + "\"");
    }

    private void handleGetMessage(List<String> getMessageArguments, int clientIdx) {
        System.out.println("[MAP start]");
        int i = 0;
        for (var name : namesSubscribed.entrySet()) {
            System.out.println("idx: " + i + " name: \"" + name.getKey() + "\" value: \"" + name.getValue() + "\"");
            ++i;
        }
        System.out.println("[MAP end]");
        String serviceName = getMessageArguments.get(0);

        String response = namesSubscribed.get(serviceName);
        System.out.println("Wanted service of name \"" + serviceName + "\"" + ". Got: " + response);
        System.out.println("will try to send response");
        communicationWithServerController.sendMessage(response, clientIdx);
        System.out.println("finished sending response");
    }
}
