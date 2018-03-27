package org.daisy.streamline.api.config;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides a locking mechanism using a {@link FileLock} that can be used to negotiate access
 * across JVMs. For example, whoever owns the lock on a predetermined file has exclusive access
 * to a set of resources until the lock is released. 
 * 
 * @author Joel Håkansson
 */
class Lock {
	private static final Logger logger = Logger.getLogger(Lock.class.getCanonicalName());
	private final File file;
	private LockDetails details = null;
	
	private static class LockDetails {
		private final FileLock lock;
		private final FileChannel fileChannel;
		
		private LockDetails(FileLock lock, FileChannel fileChannel) {
			this.lock = lock;
			this.fileChannel = fileChannel;
		}
		
		static Optional<LockDetails> lock(File file) throws IOException {
			// Create file if it doesn't exist
			file.createNewFile();
			@SuppressWarnings("resource")
			FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
			FileLock lock = channel.tryLock();
			if (lock == null) {
				if (logger.isLoggable(Level.FINE)) {
					logger.fine("Failed to acquire lock..");
				} 
				channel.close();
				return Optional.empty();
			}
			return Optional.of(new LockDetails(lock, channel));
		}
		
		void release() throws IOException {
			if (lock != null) {
				lock.release();
			}
			fileChannel.close();			
		}

	}
	
	/**
	 * Creates a new lock using the supplied file. 
	 * @param file the file to use as a lock
	 */
	public Lock(File file) {
		File path = file.getAbsoluteFile().getParentFile();
		if (!path.isDirectory()) {
			throw new IllegalArgumentException("Not an existing directory: " + path);
		}
		this.file = file;
	}

	/**
	 * Acquire a lock on the file.
	 * @return returns true if a lock was acquired, false otherwise
	 * @throws IOException if an I/O error occurs
	 */
	public synchronized boolean acquireLock() throws IOException {
		if (file.exists() && !file.delete()) {
			// could not delete file, must be a lock on it...
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Could not acquire lock...");
			}
			return false;
		} else {
			// attempt to acquire lock
			Optional<LockDetails> ld = LockDetails.lock(file);

			if (ld.isPresent()) {
				details = ld.get();
				return true;
			} else {
				if (logger.isLoggable(Level.FINE)) {
					logger.fine("Failed to acquire lock..");
				}
				return false;
			}
		}
	}
	
	public synchronized void releaseLock() {
		try {
			if (details!=null) {
				details.release();
				details = null;
			}
			file.delete();
		} catch (IOException e) {
			logger.log(Level.WARNING, "Failed to release lock.", e);
		}
	}

}
