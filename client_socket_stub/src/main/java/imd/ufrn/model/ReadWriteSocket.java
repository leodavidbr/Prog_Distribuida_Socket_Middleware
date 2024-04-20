package imd.ufrn.model;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ReadWriteSocket {
    private Socket socket;
    private BufferedReader readStream;
    private PrintWriter writeStream;

    public ReadWriteSocket(Socket socket) {
        this.socket = socket;
    }

    public ReadWriteSocket(Socket socket, BufferedReader readStream, PrintWriter writeStream) {
        this.socket = socket;
        this.readStream = readStream;
        this.writeStream = writeStream;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public BufferedReader getReadStream() {
        return readStream;
    }

    public void setReadStream(BufferedReader readStream) {
        this.readStream = readStream;
    }

    public PrintWriter getWriteStream() {
        return writeStream;
    }

    public void setWriteStream(PrintWriter writeStream) {
        this.writeStream = writeStream;
    }
}
