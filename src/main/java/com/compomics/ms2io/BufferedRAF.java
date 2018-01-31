package com.compomics.ms2io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

/**
 *
 * @author Genet
 */
public class BufferedRAF extends RandomAccessFile{
    
    	private final int bufferSize;
        private static final int defaultSize = 4096;
	private byte buffer[];
	private int buf_end = 0;
	private int buf_pos = 0;
	private long real_pos = 0L;

	/**
	 * Creates a BufferedRandomAccessFile
	 * @param file the file object
	 * @param mode the access mode
	 * @throws IOException
	 */
	public BufferedRAF(File file, String mode) throws IOException {
		this(file, mode, defaultSize);
                
	}

	/**
	 * Creates a BufferedRandomAccessFile
	 * @param file the file object
	 * @param mode the access mode
	 * @param bufferSize the size of the read buffer
	 * @throws IOException
	 */
	public BufferedRAF(File file, String mode, int bufferSize)
			throws IOException {
		super(file, mode);
		invalidate();
		this.bufferSize = bufferSize;
		this.buffer = new byte[bufferSize];
	}

	/**
	 * Creates a BufferedRandomAccessFile.
	 * @param filename the path to the file
	 * @param mode the access mode
	 * @throws IOException
	 */
	public BufferedRAF(String filename, String mode)
			throws IOException {
		this(filename, mode, defaultSize);
	}

	/**
	 * Creates a BufferedRandomAccessFile.
	 * @param filename the path to the file
	 * @param mode size the size of the read buffer
	 * @throws IOException
	 */
	public BufferedRAF(String filename, String mode, int bufsize)
			throws IOException {
		super(filename, mode);
		invalidate();
		this.bufferSize = bufsize;
		this.buffer = new byte[bufsize];
	}

	/**
	 * Returns the current offset in this file.
	 * @return the offset from the beginning of the file, in bytes,
         * at which the next read or write occurs.
	 */
	@Override
	public long getFilePointer() throws IOException {
		return (real_pos - buf_end) + buf_pos;
	}

	/**
	 *  Reads the next line of text from this file using the default character
	 * set.
	 *this method reads bytes from the buffer.
	 * A line of text is terminated by a carriage-return character ('\r'), a
	 * newline character ('\n'), a carriage-return character immediately
	 * followed by a newline character, or the end of the file. Line-terminating
	 * characters are discarded and are not included as part of the string
	 * returned.
	 * @return the next line of text from this file, or null if end of file is
	 *  encountered before even one byte is read.
	 * @throws IOException
	 */
	public final String getNextLine() throws IOException {
		return getNextLine(Charset.defaultCharset());
	}

	/**
	 * Reads the next line of text from this file using the specified character
	 * set.
	 * this method reads bytes from the buffer.
	 *A line of text is terminated by a carriage-return character ('\r'), a
	 * newline character ('\n'), a carriage-return character immediately
	 * followed by a newline character, or the end of the file. Line-terminating
	 * characters are discarded and are not included as part of the string
	 * returned.
	 * @param charset the character set to use when reading the line
	 * @return the next line of text from this file, or null if end of file is
	 * encountered before even one byte is read.
	 * @throws IOException
	 */
	public final String getNextLine(Charset charset) throws IOException {
		String str = null;

		// Fill the buffer
		if (buf_end - buf_pos <= 0) {
			if (fillBuffer() < 0) {
				return null;
			}
		}

		// Find line terminator from buffer
		int lineEnd = -1;
		for (int i = buf_pos; i < buf_end; i++) {
			if (buffer[i] == '\n') {
				lineEnd = i;
				break;
			}
		}

		// Line terminator not found from buffer
		if (lineEnd < 0) {
			StringBuilder sb = new StringBuilder(256);

			int c;
			while (((c = read()) != -1) && (c != '\n')) {
				if ((char) c != '\r') {
					sb.append((char) c);
				}
			}
			if ((c == -1) && (sb.length() == 0)) {
				return null;
			}

			return sb.toString();
		}

		if (lineEnd > 0 && buffer[lineEnd - 1] == '\r') {
			str = new String(buffer, buf_pos, lineEnd - buf_pos - 1,
					charset);
		} else {
			str = new String(buffer, buf_pos, lineEnd - buf_pos, charset);
		}

		buf_pos = lineEnd + 1;
		return str;
	}

	/**
	 * Reads a byte of data from this file.
	 * The read is performed on the buffer.
	 *The byte is returned as an integer in the range 0 to 255
         *This method blocks if no input is yet available
	 *@return the next byte of data, or -1 if the end of the file has been
	 *reached.
	 * @throws IOException
	 */
	@Override
	public final int read() throws IOException {
		if (buf_pos >= buf_end) {
			if (fillBuffer() < 0) {
				return -1;
			}
		}

		if (buf_end == 0) {
			return -1;
		} else {
			return buffer[buf_pos++];
		}
	}

	/**
	 *  Reads up to len bytes of data from this file into an array
	 * of bytes. This method blocks until at least one byte of input is
	 * available.
	 * The read is performed on the buffer.
	 * @param b the buffer into which the data is read.
	 * @param off the start offset in array b at which the data is written.
	 * @param len the maximum number of bytes read.
	 * @throws IOException
	 */
	@Override
	public int read(byte b[], int off, int len) throws IOException {
		int leftover = buf_end - buf_pos;
		if (len <= leftover) {
			System.arraycopy(buffer, buf_pos, b, off, len);
			buf_pos += len;
			return len;
		}

		for (int i = 0; i < len; i++) {
			int c = this.read();
			if (c != -1)
				b[off + i] = (byte) c;
			else {
				return i;
			}
		}

		return len;
	}

	/**
	 * Sets the file-pointer offset, measured from the beginning of this file,
	 * at which the next read or write occurs. The offset may be set beyond the
	 * end of the file. Setting the offset beyond the end of the file does not
	 * change the file length. The file length will change only by writing after
	 * the offset has been set beyond the end of the file.
	 * The offset is calculated relative to the buffer.	 * 
	 * @param pos the offset position, measured in bytes from the beginning of
	 * the file, at which to set the file pointer.
	 * @throws IOException
	 */
	@Override
	public void seek(long pos) throws IOException {
		int n = (int) (real_pos - pos);
		if (n >= 0 && n <= buf_end) {
			buf_pos = buf_end - n;
		} else {
			super.seek(pos);
			invalidate();
		}
	}

	private int fillBuffer() throws IOException {
		int n = super.read(buffer, 0, bufferSize);
		if (n >= 0) {
			real_pos += n;
			buf_end = n;
			buf_pos = 0;
		}

		return n;
	}

	private void invalidate() throws IOException {
		buf_end = 0;
		buf_pos = 0;
		real_pos = super.getFilePointer();
	}
    
    
}
