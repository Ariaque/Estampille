package istic.projet.estampille.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Subclass of {@link ObjectOutputStream} that allow to append
 * several Serializable object in one file.
 */
public class AppendableObjectOutputStream extends ObjectOutputStream {

    private final boolean append;
    private final boolean initialized;
    private final DataOutputStream dout;

    /**
     *
     * @param out
     * @param append true if we want to append an object after another, false otherwise.
     * @throws IOException
     */
    public AppendableObjectOutputStream(OutputStream out, boolean append) throws IOException {
        super(out);
        this.append = append;
        this.initialized = true;
        this.dout = new DataOutputStream(out);
        this.writeStreamHeader();
    }

    /**
     * If append is false, we must write an header.
     * @throws IOException
     */
    @Override
    protected void writeStreamHeader() throws IOException {
        if (!this.initialized || this.append) return;
        if (dout != null) {
            dout.writeShort(STREAM_MAGIC);
            dout.writeShort(STREAM_VERSION);
        }
    }

}