package gameConnectors;

import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.WriteCallback;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

/**
 * Created by mihanik
 * 21.05.15 10:15
 * Package: gameConnectors
 */
class MockEndpoint implements RemoteEndpoint {
    private String lastSent = "";

    public String getLastSent() {
        return lastSent;
    }

    @Override
    public void sendBytes(ByteBuffer byteBuffer) throws IOException {
        lastSent = byteBuffer.asCharBuffer().toString();
    }

    @Override
    public Future<Void> sendBytesByFuture(ByteBuffer byteBuffer) {
        lastSent = byteBuffer.asCharBuffer().toString();
        return null;
    }

    @Override
    public void sendBytes(ByteBuffer byteBuffer, WriteCallback writeCallback) {
        lastSent = byteBuffer.asCharBuffer().toString();
    }

    @Override
    public void sendPartialBytes(ByteBuffer byteBuffer, boolean b) throws IOException {
        lastSent = byteBuffer.asCharBuffer().toString();
    }

    @Override
    public void sendPartialString(String s, boolean b) throws IOException {
        lastSent = s;
    }

    @Override
    public void sendPing(ByteBuffer byteBuffer) throws IOException {
        lastSent = byteBuffer.asCharBuffer().toString();
    }

    @Override
    public void sendPong(ByteBuffer byteBuffer) throws IOException {
        lastSent = byteBuffer.asCharBuffer().toString();
    }

    @Override
    public void sendString(String s) throws IOException {
        lastSent = s;
    }

    @Override
    public Future<Void> sendStringByFuture(String s) {
        lastSent = s;
        return null;
    }

    @Override
    public void sendString(String s, WriteCallback writeCallback) {
        lastSent = s;
    }

    @Override
    public BatchMode getBatchMode() {
        return null;
    }

    @Override
    public void flush() throws IOException {
        lastSent = "";
    }
}