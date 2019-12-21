package robutev3.core;

import java.io.IOException;

/**
 * @author Nearchos
 * Created: 07-Dec-19
 */
public interface CommunicationInterface {

    void connect() throws IOException;
    void disconnect() throws IOException;
    void write(final byte [] message) throws IOException;
    byte [] read() throws IOException;
}
