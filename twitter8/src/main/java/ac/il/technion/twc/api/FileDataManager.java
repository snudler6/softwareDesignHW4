package ac.il.technion.twc.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ac.il.technion.twc.api.interfaces.IDataManager;

/**
 * The FileDataManager class provides an implementation of the interface
 * IDataManager for using a file as it's data source. The path of that file is
 * provided at the constructor.
 */
public class FileDataManager implements IDataManager
{
	File file;

	/**
	 * Constructor to create a new instance
	 * 
	 * @param filePath
	 *            the path that stores the date
	 */
	public FileDataManager(String filePath)
	{
		this.file = new File(filePath);
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.IDataManager#exists()
	 */
	public boolean exists()
	{
		return file.exists();
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.IDataManager#getInputStreamInstance()
	 */
	public InputStream getInputStreamInstance() throws IOException
	{
		return new FileInputStream(file);
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.IDataManager#getOutputStreamInstance()
	 */
	public OutputStream getOutputStreamInstance() throws IOException
	{
		if (!exists())
		{
			file.createNewFile();
		}
		return new FileOutputStream(file);
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.IDataManager#cleanData()
	 */
	public void cleanData()
	{
		this.file.delete();
	}
}
