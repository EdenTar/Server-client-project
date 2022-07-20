package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.impl.messages.LogoutMessage;
import bgu.spl.net.impl.messages.Message;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final BidiMessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, BidiMessagingProtocol<T> protocol, int connectionId, ConnectionsImpl connections) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        protocol.start(connectionId,connections);
        connections.connect(connectionId,this);
    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read;

            in = new BufferedInputStream(sock.getInputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {

                System.out.println("entering decode next byte");
                T nextMessage = encdec.decodeNextByte((byte) read);

                if (nextMessage != null && !nextMessage.equals("")) {
                    T message = ((LineMessageEncoderDecoder)encdec).convertToObject(((String) nextMessage).getBytes(StandardCharsets.UTF_8));
                    protocol.process(message);
                    if(message instanceof LogoutMessage)
                        ((BidiMessagingProtocolImpl)protocol).setLoggedOut();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

    @Override
    public void send(T msg) {

         if (msg != null) {
             try {
                 out = new BufferedOutputStream(sock.getOutputStream());
                 String message=msg.toString();
                 out.write(((LineMessageEncoderDecoder)encdec).encode(message));
                 out.flush();
             }
             catch (Exception e){
                 e.printStackTrace();
             }

         }

    }

}
