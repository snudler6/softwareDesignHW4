package ac.il.technion.twc.api.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The IDataManager interface describes an object that encapsulates a data
 * source, such as file.
 * 
 * @see the FileDataManager implementation
 */
public interface IDataManager {
    /**
     * Gets whether the data source exists, that is whether it was created or
     * initialized.
     * 
     * @return whether the data source exists
     */
    boolean exists();

    /**
     * Gets a new instance of InputStream associated with the data source
     * 
     * @return a new instance of InputStream associated with the data source
     * @throws IOException
     */
    InputStream getInputStreamInstance() throws IOException;

    /**
     * Gets a new instance of OutputStream associated with the data source
     * 
     * @return a new instance of OutputStream associated with the data source
     * @throws IOException
     */
    OutputStream getOutputStreamInstance() throws IOException;

    /**
     * Cleans the data in the data source
     */
    void cleanData();
}
