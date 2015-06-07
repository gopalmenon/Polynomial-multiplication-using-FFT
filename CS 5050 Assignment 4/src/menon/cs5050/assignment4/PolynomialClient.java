package menon.cs5050.assignment4;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class PolynomialClient {
	
	private Random randomDoubleGenerator;
	private PrintWriter out;
	private boolean debugOn;
	private int[] polynomialSize;
	private double[] naiveRunTime;
	private double[] fourSubProblemsDivideAndConquerRunTime;
	private double[] karatsubaRunTime;
	
	public static final String OUTPUT_FILE = "output.txt";
	public static final String DEBUG_ON = "Y";
	public static final int START_POLYNOMIAL_ARRAY_SIZE_EXPONENT = 5;
	public static final int NUMBER_OF_ITERATIONS = 10;
	
	/**
	 * Constructor
	 * @param debugOn
	 */
	public PolynomialClient(boolean debugOn) {
		this.randomDoubleGenerator = new Random(2038905);
		try {
			this.out = new PrintWriter(new FileWriter(OUTPUT_FILE));
		} catch (IOException e) {
			System.err.println("IOException while opening file ");
			e.printStackTrace();
			System.exit(0);
		}
		this.debugOn = debugOn;
		
		this.polynomialSize = new int[NUMBER_OF_ITERATIONS];
		this.naiveRunTime = new double[NUMBER_OF_ITERATIONS];
		this.fourSubProblemsDivideAndConquerRunTime = new double[NUMBER_OF_ITERATIONS];
		this.karatsubaRunTime = new double[NUMBER_OF_ITERATIONS];
		
	}

	public static void main(String[] args) {
		
		boolean debugOn = false;
		if (args.length > 0 && args[0].length() > 0 && DEBUG_ON.equalsIgnoreCase(args[0])) {
			debugOn = true;
		}
		
		PolynomialClient polynomialClient = new PolynomialClient(debugOn);
		
		polynomialClient.collectMultiplicationStatistics();
		
	}
	
	/**
	 * Run polynomial multiplication for logarithmically increasing sizes and print run time statistics.
	 */
	private void collectMultiplicationStatistics() {
		
		double[] polynomialArray1 = null, polynomialArray2 = null;
		
		for (int polynomialArraySizeExponent = START_POLYNOMIAL_ARRAY_SIZE_EXPONENT, iterationCounter = 0; iterationCounter < NUMBER_OF_ITERATIONS; ++polynomialArraySizeExponent, ++iterationCounter) {
		
			polynomialArray1 = getPolynomialArray(polynomialArraySizeExponent);
			polynomialArray2 = getPolynomialArray(polynomialArraySizeExponent);
			this.polynomialSize[iterationCounter] = polynomialArray1.length;
			
			try {
				
				Polynomial multiplicand = new Polynomial(polynomialArray1);
				Polynomial multipliplier = new Polynomial(polynomialArray2);
				
				log("Muliplicand: " + multiplicand.toString(), true);
				log("Muliplier: " + multipliplier.toString(), true);
				
				long beforeMultiplication = System.currentTimeMillis();
				Polynomial product = multiplicand.naiveMultiply(multipliplier);
				long afterMultiplication = System.currentTimeMillis();
				this.naiveRunTime[iterationCounter] = (afterMultiplication - beforeMultiplication);
				
				log("The product with the naive algorithm is ", false);
				log(product.toString(), true);
				
				beforeMultiplication = System.currentTimeMillis();
				product = multiplicand.divideAndConquereMultiply(multipliplier);
				afterMultiplication = System.currentTimeMillis();
				this.fourSubProblemsDivideAndConquerRunTime[iterationCounter] = (afterMultiplication - beforeMultiplication);
				
				log("The product with the four-subproblem Divide and Conquer algorithm is ", false);
				log(product.toString(), true);
				
				beforeMultiplication = System.currentTimeMillis();
				product = multiplicand.karatsubaMultiply(multipliplier);
				afterMultiplication = System.currentTimeMillis();
				this.karatsubaRunTime[iterationCounter] = (afterMultiplication - beforeMultiplication);

				log("The product with the Karatsuba algorithm is ", false);
				log(product.toString(), true);
				
			} catch (Exception e) {
				System.err.println("Exception thrown when constructing Polynomial.");
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
		for (int size : this.polynomialSize) {
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
		this.out.print("\nNaive.Run.Time=c(");
		for (double runTime : this.naiveRunTime) {
			if (firstTime) {
				this.out.print(runTime);
				firstTime = false;
			} else {
				this.out.print(", ");
				this.out.print(runTime);
			}
		}
		this.out.print(")");
		
		firstTime = true;
		this.out.print("\nFour.Sub.Prob.Run.Time=c(");
		for (double runTime : this.fourSubProblemsDivideAndConquerRunTime) {
			if (firstTime) {
				this.out.print(runTime);
				firstTime = false;
			} else {
				this.out.print(", ");
				this.out.print(runTime);
			}
		}
		this.out.print(")");
		
		firstTime = true;
		this.out.print("\nKaratsuba.Run.Time=c(");
		for (double runTime : this.karatsubaRunTime) {
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
