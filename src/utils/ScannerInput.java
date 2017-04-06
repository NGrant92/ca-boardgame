package utils;
import java.util.Scanner;


public class ScannerInput{
	
	public static int validNextInt(String prompt){
		Scanner input =new Scanner (System.in);
		do{
			try{System.out.print(prompt);
			return input.nextInt();
			}
			catch (Exception e){
				input.nextLine();//swallows the buffer contents
				System.err.println("\tEnter a number please ");
			}
		}while (true);
	
	}
	public static String retrieveText (String prompt){
		Scanner input =new Scanner (System.in);
		do{
			System.out.print(prompt);
			return input.nextLine();
			
			
	
			
		}while (true);
		}
	
}



		
	

			
		
	
			
	
	
	







