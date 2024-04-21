package imd.ufrn;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        SocketStub stubClient = new SocketStub(message -> handleMessageRecieved(message));
        Thread stubThread = new Thread(stubClient);
        System.out.println("antes do run");
        stubThread.start();
        System.out.println("depois do run");
    }

    public static void handleMessageRecieved(String message) {
        System.out.println("message recieved: " + message);
    }
}
