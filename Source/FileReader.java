package openEndedProblems.removeDuplicateString.algorithm;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.io.File;
import java.io.RandomAccessFile;


public class FileReader {

/************************************************************
 ************************************************************
	Constructor for GUI version
 ************************************************************
 ***********************************************************/
	public	FileReader(File sourceFile, StringBuilder sb, ArrayList<StringHash> list) {
		this.sourceFile = sourceFile;
		this.sb = sb;
		this.list = list;
		read();
	}

/************************************************************
 ************************************************************
	Constructor for CLI version
 ************************************************************
 ***********************************************************/
	public FileReader(File sourceFile, ArrayList<StringHash> list) {
		this.sourceFile = sourceFile;
		this.list = list;
		sb = null;
		read();
	}

/************************************************************
 ************************************************************
	Uses MappedByteBuffer to read file with optimum speed
	This loads the whole file in the main memory or just parts
	of it in the form of pages to increase speed.
 ************************************************************
 ***********************************************************/

	private void read() {
		try {
			FileChannel channel = new RandomAccessFile(sourceFile, "r").getChannel();
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			char token;
			StringBuilder sbuffer = new StringBuilder();
			for (int i = 0; i < buffer.limit(); i++) {
				token = (char)(buffer.get() & 0xFF); 		//Converts int to char by extracting last 2 bytes
				if (token == '.' || token == '?' || token == '!') {
					list.add(new StringHash(sbuffer.toString()));
					if (sb != null) {
						sb.append(sbuffer.toString() + "\n");
					}
					sbuffer.delete(0, sbuffer.length());
					continue;
				}
				sbuffer.append(token);

			}
			if (!(sbuffer.length() == 0)) {
				if (sbuffer.charAt(sbuffer.length() - 1) == '\n') {
					sbuffer.delete(sbuffer.length() - 1, sbuffer.length());
				}
				if (sb != null) {
					sb.append(sbuffer.toString() + "\n");
				}
				list.add(new StringHash(sbuffer.toString()));
				sbuffer.delete(0, sbuffer.length());
			}
		}

		catch (Exception err) {
			System.err.println(err.getMessage());
		}
	}

	private StringBuilder sb;
	private StringBuilder sbuffer;
	private File sourceFile;
	private ArrayList<StringHash> list;
}

