package edu.iastate.cs228.hw5;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *  
 * @author Mariya Karasseva mariyak
 *
 */

/**
 * 
 * The Transactions class simulates video transactions at a video store.
 *
 */
public class Transactions {

	/**
	 * The main method generates a simulation of rental and return activities.
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws AllCopiesRentedOutException
	 * @throws FilmNotInInventoryException
	 * @throws IllegalArgumentException
	 */
	public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException,
			FilmNotInInventoryException, AllCopiesRentedOutException {

		// 1. Construct a VideoStore object.
		// 2. Simulate transactions as in the example given in Section 4 of the
		// the project description.
		
		VideoStore vs = new VideoStore("videoList1.txt");

		Scanner sc = new Scanner(System.in);
		System.out.println("Transactions at a Video Store");
		System.out.println("keys: 1 (rent)      2 (bulk rent)");
		System.out.println("      3 (return)    4 (bulk return)");
		System.out.println("      5 (summary)   6 (exit)");
		System.out.println();
		System.out.print("Transaction: ");

		String key = sc.next();
		while (true) {
			if (key.equals("1")) {
				System.out.print("Film to rent: ");
				sc.nextLine();
				String film = sc.nextLine();
				vs.videoRent(vs.parseFilmName(film), vs.parseNumCopies(film));

			} else if (key.equals("2")) {
				System.out.print("Video file (rent): ");
				String file = sc.next();
				vs.bulkRent(file);

			} else if (key.equals("3")) {
				System.out.print("Film to return: ");
				sc.nextLine();
				String film = sc.nextLine();
				vs.videoReturn(vs.parseFilmName(film), vs.parseNumCopies(film));

			} else if (key.equals("4")) {
				System.out.print("Video file (return): ");
				String file = sc.next();
				vs.bulkReturn(file);
			} else if (key.equals("5")) {
				System.out.println(vs.transactionsSummary());
			} else {
				break;
			}
			System.out.println();
			System.out.print("Transaction: ");
			key = sc.next();
		}
		sc.close();
	}
}
