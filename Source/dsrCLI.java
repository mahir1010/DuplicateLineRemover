package openEndedProblems.removeDuplicateString.cli;

import java.nio.MappedByteBuffer;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.io.File;
import java.util.ArrayList;
import openEndedProblems.removeDuplicateString.algorithm.*;

/************************************************************
 ************************************************************
	Command Line Interface for less CPU consumption 
	Run Script as follow:
		./[nix/win]runCLI <ABSOLUTE FILE PATH>
 ************************************************************
 ***********************************************************/

public class dsrCLI {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Invalid Execution");
			System.out.println("run ./runCLI <ABSOLUTE_FILE_PATH> ");
			return;
		}
		File sourceFile = new File(args[0]);
		if (sourceFile == null) {
			System.out.println("File Doesnt Exist at " + args[0]);
			return;
		}
		ArrayList<StringHash> list = new ArrayList<StringHash>();
		new FileReader(sourceFile, list);
		DuplicateStringRemover dsr=new DuplicateStringRemover(list,(byte)1);
		for(StringHash s:list){
			System.out.println(s.getString());
		}
	}
}