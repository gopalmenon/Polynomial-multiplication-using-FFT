package menon.cs5050.assignment4;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class SmartPolynomialClient {
	
	private Random randomDoubleGenerator;
	private PrintWriter out;
	private boolean debugOn;
	private int[] smartPolynomialSize;
	private double[] smartPolynomialRunTime;
	
	public static final String OUTPUT_FILE = "output.txt";
	public static final String DEBUG_ON = "Y";
	public static final int START_POLYNOMIAL_ARRAY_SIZE_EXPONENT = 5;
	public static final int NUMBER_OF_ITERATIONS = 13;

	/**
	 * Constructor
	 * @param debugOn
	 */
	public SmartPolynomialClient(boolean debugOn) {
		this.randomDoubleGenerator = new Random(2038905);
		try {
			this.out = new PrintWriter(new FileWriter(OUTPUT_FILE));
		} catch (IOException e) {
			System.err.println("IOException while opening file ");
			e.printStackTrace();
			System.exit(0);
		}
		this.debugOn = debugOn;
		
		this.smartPolynomialSize = new int[NUMBER_OF_ITERATIONS];
		this.smartPolynomialRunTime = new double[NUMBER_OF_ITERATIONS];
		
	}
	

	public static void main(String[] args) {
		
		boolean debugOn = false;
		if (args.length > 0 && args[0].length() > 0 && DEBUG_ON.equalsIgnoreCase(args[0])) {
			debugOn = true;
		}
		
		SmartPolynomialClient smartPolynomialClient = new SmartPolynomialClient(debugOn);
		
		smartPolynomialClient.collectMultiplicationStatistics();
		
	}
	
	/**
	 * Run polynomial multiplication for logarithmically increasing sizes and print run time statistics.
	 */
	private void collectMultiplicationStatistics() {
		
		double[] polynomialArray1 = null, polynomialArray2 = null;
		
		for (int polynomialArraySizeExponent = START_POLYNOMIAL_ARRAY_SIZE_EXPONENT, iterationCounter = 0; iterationCounter < NUMBER_OF_ITERATIONS; ++polynomialArraySizeExponent, ++iterationCounter) {
		
			polynomialArray1 = getPolynomialArray(polynomialArraySizeExponent);
			polynomialArray2 = getPolynomialArray(polynomialArraySizeExponent);
			this.smartPolynomialSize[iterationCounter] = polynomialArray1.length;
			
			try {
				
				SmartPolynomial multiplicand = new SmartPolynomial(polynomialArray1);
				SmartPolynomial multipliplier = new SmartPolynomial(polynomialArray2);
				
				log("Muliplicand: " + multiplicand.toString(), true);
				log("Muliplier: " + multipliplier.toString(), true);
				
				long beforeMultiplication = System.currentTimeMillis();
				SmartPolynomial product = multiplicand.multiply(multipliplier);
				long afterMultiplication = System.currentTimeMillis();
				this.smartPolynomialRunTime[iterationCounter] = (afterMultiplication - beforeMultiplication);
				
				log("The product with the FFT algorithm is ", false);
				log(product.toString(), true);
				
			} catch (Exception e) {
				System.err.println("Exception thrown when constructing Smart Polynomial.");
				e.printStackTrace();
			}
		}
		
		printRunTimeStatistics();
		
		this.out.close();

	}
	
	/**
	 * @param polynomialArraySizeExponent
	 * @return an array of doubles with random values
	 */
	private double[] getPolynomialArray(int polynomialArraySizeExponent) {
		
		int polynomialArraySize = Double.valueOf(Math.pow(2, polynomialArraySizeExponent)).intValue();
		double[] returnValue = new double[polynomialArraySize];
		
		for (int index = 0; index < polynomialArraySize; ++ index) {
			returnValue[index] = getNextRandomDouble();
		}
		
		return returnValue;
		
	}
	
	/**
	 * @return a random double between -1.0 and +1.0
	 */
	private double getNextRandomDouble() {
		
		return randomDoubleGenerator.nextDouble() * 2 - 1;

	}
	
	/**
	 * Write log into output file if debug flag is set on
	 * @param logText
	 * @param newLine
	 */
	private void log(String logText, boolean newLine) {
		
		if (this.debugOn) {
			if (newLine) {
				this.out.println(logText);
			} else {
				this.out.print(logText);
			}
		}
	}
	
	/**
	 * Print run time statistics in R script format for ease of graphing
	 */
	private void printRunTimeStatistics() {
		
		boolean firstTime = true;
		this.out.print("\nPolynomial.Sizes=c(");
		for (int size : this.smartPolynomialSize) {
			if (firstTime) {
				this.out.print(size);
				firstTime = false;
			} else {
				this.out.print(", ");
				this.out.print(size);
			}
		}
		this.out.print(")");
		
		firstTime = true;
		this.out.print("\nSmart.Run.Time=c(");
		for (double runTime : this.smartPolynomialRunTime) {
			if (firstTime) {
				this.out.print(runTime);
				firstTime = false;
			} else {
				this.out.print(", ");
				this.out.print(runTime);
			}
		}
		this.out.print(")");

	}
	
}
