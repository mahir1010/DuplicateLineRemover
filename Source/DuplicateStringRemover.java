package openEndedProblems.removeDuplicateString.algorithm;

import java.util.ArrayList;

public class DuplicateStringRemover {

	public DuplicateStringRemover(ArrayList<StringHash> list,byte c) {
		System.out.println("Working");
		this.list = list;
		check=c;
		this.sortedList = (new StringHashSort(list.toArray())).getSortedList();
		System.out.println(sortedList.length);
		removeDuplicateString();
		System.out.println("Completed");
	}

/************************************************************
 ************************************************************
	Uses a sorted array of strings to compare and find 
	String generating same Hash Code
 ************************************************************
 ***********************************************************/
	private void removeDuplicateString() {
		short hash1 = ((StringHash)sortedList[0]).getHash1();
		short hash2 = ((StringHash)sortedList[0]).getHash2();
		int l=sortedList.length;
		byte perc=0;
		for (int i = 1; i < l; i++) {
			if(check==1 && ((100*i/l)-1==perc)){
				perc++;
				System.out.println("File Proccessed="+perc+"%");
			}
			if (((StringHash)sortedList[i]).getHash1() == hash1) {
				if (((StringHash)sortedList[i]).getHash2() == hash2) {
					list.remove(((StringHash)sortedList[i]));
					sortedList[i]=null;
				} else {
					hash2 = ((StringHash)sortedList[i]).getHash2();
				}
			} else {

				hash1 = ((StringHash)sortedList[i]).getHash1();
				hash2 = ((StringHash)sortedList[i]).getHash2();
			}
		}
	}

	private Object[] sortedList;
	private ArrayList<StringHash> list;
	private byte check;
}

/************************************************************
 ************************************************************
	Uses Merge Sort to sort the Array according to Hash1 
	of Objects of type StringHash
 ************************************************************
 ***********************************************************/
class StringHashSort {

	StringHashSort(Object[] inplist) {
		list = inplist;
		l = list.length;
		sortHash(0, l - 1);
	}



	private void sortHash(int lb, int ub) {
		if (lb == ub) {
			return;
		}
		int m = (ub + lb + 1) / 2;
		sortHash(lb, m - 1);
		sortHash(m, ub);
		mergeHash(lb, m, ub);

	}
	private void mergeHash(int lb, int m, int ub) {
		int i = lb, j = m, k = 0;
		Object[] result = new Object[ub - lb + 1];
		while (i <= m - 1 && j <= ub) {
			if (((StringHash)list[i]).getHash1() <= ((StringHash)list[j]).getHash1()) {
				result[k] = list[i];
				i++;
				k++;
			} else {
				result[k] = list[j];
				j++;
				k++;
			}
		}
		if (i > m - 1) {
			for (int l = j; l <= ub; l++) {
				result[k] = list[l];
				k++;
			}
		} else {
			for (int l = i; l <= m - 1; l++) {
				result[k] = list[l];
				k++;
			}
		}
		for (int l = 0; l < result.length; l++) {
			list[l + lb] = result[l];
		}
	}

	public Object[] getSortedList() {
		return list;
	}
	private Object[] list;
	private int l;
}