package menon.cs5050.assignment4;

public class Polynomial {

	private double[] coefficients;
	
	public Polynomial(double[] coefficients) throws Exception {
		
		if (!isPowerOfTwo(coefficients.length)) {
			throw new Exception("Polynomial should have number of coefficients equal to integral power of 2.");
		}
		this.coefficients = coefficients;
	}
	
	/**
	 * Multiply using naive algorithm
	 * @param multipleWith
	 * @return a polynomial representing the product of this one and the parameter
	 * @throws Exception 
	 */
	public Polynomial naiveMultiply(Polynomial multiplier) throws Exception {
		
		if (this.coefficients.length != multiplier.length()) {
			throw new Exception("Can only multiply a polynomial with " + this.coefficients.length + " coefficients.");
		}
		
		double[] product = new double[2 * this.coefficients.length];
		
		//Multiply the two polynomials
		for (int multiplicandIndex = 0; multiplicandIndex < this.coefficients.length; ++multiplicandIndex) {
			for (int multiplierIndex = 0; multiplierIndex < multiplier.length(); ++multiplierIndex) {
				product[multiplicandIndex + multiplierIndex] += this.coefficients[multiplicandIndex] * multiplier.at(multiplierIndex);
			}
		}
		
		return new Polynomial(product);
	}
	
	/**
	 * Multiply using divide and conquer that generates 4 sub-problems
	 * @param multiplier
	 * @return
	 * @throws Exception
	 */
	public Polynomial divideAndConquereMultiply(Polynomial multiplier) throws Exception {
		
		if (this.coefficients.length != multiplier.length()) {
			throw new Exception("Can only multiply a polynomial with " + this.coefficients.length + " coefficients.");
		}
		
		return new Polynomial(divideAndConquereMultiply(this.coefficients, multiplier.coefficients));
		
	}
	
	/**
	 * @param multiplier
	 * @return array of doubles containing coefficients of the product with the multiplier
	 */
	private double[] divideAndConquereMultiply(double[] multiplicand, double[] multiplier) throws Exception {

		double[] product = new double[2 * multiplicand.length];

		//Handle the base case where the polynomial has only one coefficient
		if (multiplicand.length == 1) {
			product[0] = multiplicand[0] * multiplier[0];
			return product;
		}
		
		int halfArraySize = multiplicand.length / 2;

		//Declare arrays to hold halved factors
		double[] multiplicandLow = new double[halfArraySize];
		double[] multiplicandHigh = new double[halfArraySize];
		double[] multipliplierLow = new double[halfArraySize];
		double[] multiplierHigh = new double[halfArraySize];

		//Fill in the low and high arrays
		for (int halfSizeIndex = 0; halfSizeIndex < halfArraySize; ++halfSizeIndex) {
			
			multiplicandLow[halfSizeIndex] = multiplicand[halfSizeIndex];
			multiplicandHigh[halfSizeIndex] = multiplicand[halfSizeIndex + halfArraySize];
			
			multipliplierLow[halfSizeIndex] = multiplier[halfSizeIndex];
			multiplierHigh[halfSizeIndex] = multiplier[halfSizeIndex + halfArraySize];
			
		}
		
		//Recursively call method on smaller arrays and construct the low and high parts of the product
		double[] productLow = divideAndConquereMultiply(multiplicandLow, multipliplierLow);//good
		double[] productHigh = divideAndConquereMultiply(multiplicandHigh, multiplierHigh);//good
		double[] multiplicandLowMultiplierHigh = divideAndConquereMultiply(multiplicandLow, multiplierHigh);
		double[] multiplicandHighMultiplierLow = divideAndConquereMultiply(multiplicandHigh, multipliplierLow);
				
		//Construct the middle portion of the product
		double[] productMiddle = new double[multiplicand.length];
		for (int halfSizeIndex = 0; halfSizeIndex < multiplicand.length; ++halfSizeIndex) {
			productMiddle[halfSizeIndex] = multiplicandLowMultiplierHigh[halfSizeIndex] + multiplicandHighMultiplierLow[halfSizeIndex];
		}
		
		//Assemble the product from the low, middle and high parts.
		for (int halfSizeIndex = 0, middleOffset = multiplicand.length / 2; halfSizeIndex < multiplicand.length; ++halfSizeIndex) {
			product[halfSizeIndex] += productLow[halfSizeIndex];
			product[halfSizeIndex + multiplicand.length] += productHigh[halfSizeIndex];
			product[halfSizeIndex + middleOffset] += productMiddle[halfSizeIndex];
		}
		
		return product;
		
	}
	
	/**
	 * Multiply using Karatsuba algorithm
	 * @param multipleWith
	 * @return a polynomial representing the product of this one and the parameter
	 * @throws Exception 
	 */
	public Polynomial karatsubaMultiply(Polynomial multiplier) throws Exception {
		
		if (this.coefficients.length != multiplier.length()) {
			throw new Exception("Can only multiply a polynomial with " + this.coefficients.length + " coefficients.");
		}
		
		return new Polynomial(karatsubaMultiplyRecursive(this.coefficients, multiplier.coefficients));
		
	}
	
	/**
	 * @param multiplier
	 * @return array of doubles containing coefficients of the product with the multiplier
	 */
	private double[] karatsubaMultiplyRecursive(double[] multiplicand, double[] multiplier) {

		double[] product = new double[2 * multiplicand.length];

		//Handle the base case where the polynomial has only one coefficient
		if (multiplicand.length == 1) {
			product[0] = multiplicand[0] * multiplier[0];
			return product;
		}
		
		int halfArraySize = multiplicand.length / 2;

		//Declare arrays to hold halved factors
		double[] multiplicandLow = new double[halfArraySize];
		double[] multiplicandHigh = new double[halfArraySize];
		double[] multipliplierLow = new double[halfArraySize];
		double[] multipliierHigh = new double[halfArraySize];

		double[] multiplicandLowHigh = new double[halfArraySize];
		double[] multipliplierLowHigh = new double[halfArraySize];

		//Fill in the low and high arrays
		for (int halfSizeIndex = 0; halfSizeIndex < halfArraySize; ++halfSizeIndex) {
			
			multiplicandLow[halfSizeIndex] = multiplicand[halfSizeIndex];
			multiplicandHigh[halfSizeIndex] = multiplicand[halfSizeIndex + halfArraySize];
			multiplicandLowHigh[halfSizeIndex] = multiplicandLow[halfSizeIndex] + multiplicandHigh[halfSizeIndex];
			
			multipliplierLow[halfSizeIndex] = multiplier[halfSizeIndex];
			multipliierHigh[halfSizeIndex] = multiplier[halfSizeIndex + halfArraySize];
			multipliplierLowHigh[halfSizeIndex] = multipliplierLow[halfSizeIndex] + multipliierHigh[halfSizeIndex];
			
		}
		
		//Recursively call method on smaller arrays and construct the low and high parts of the product
		double[] productLow = karatsubaMultiplyRecursive(multiplicandLow, multipliplierLow);
		double[] productHigh = karatsubaMultiplyRecursive(multiplicandHigh, multipliierHigh);
		
		double[] productLowHigh = karatsubaMultiplyRecursive(multiplicandLowHigh, multipliplierLowHigh);
		
		//Construct the middle portion of the product
		double[] productMiddle = new double[multiplicand.length];
		for (int halfSizeIndex = 0; halfSizeIndex < multiplicand.length; ++halfSizeIndex) {
			productMiddle[halfSizeIndex] = productLowHigh[halfSizeIndex] - productLow[halfSizeIndex] - productHigh[halfSizeIndex];
		}
		
		//Assemble the product from the low, middle and high parts. Start with the low and high parts of the product.
		for (int halfSizeIndex = 0, middleOffset = multiplicand.length / 2; halfSizeIndex < multiplicand.length; ++halfSizeIndex) {
			product[halfSizeIndex] += productLow[halfSizeIndex];
			product[halfSizeIndex + multiplicand.length] += productHigh[halfSizeIndex];
			product[halfSizeIndex + middleOffset] += productMiddle[halfSizeIndex];
		}
		
		return product;
		
	}
	
	public double at(int index) throws Exception {
		
		if (index >= this.coefficients.length) {
			throw new Exception("Index " + index + " is outside the coefficients for " + this.toString() + ".");
		}
		
		return this.coefficients[index];
	}
	
	/**
	 * @return the number of coefficients
	 */
	public int length() {
		return this.coefficients.length;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * Returns a String representation of the Polynomial
	 */
	public String toString() {
		
		StringBuffer returnValue = new StringBuffer();
		boolean firstTime = true;
		
		returnValue.append("\n[");		
		for (double coefficient : this.coefficients) {
			if (firstTime) {
				firstTime = false;
			} else {
				returnValue.append(", ");
			}
			returnValue.append(coefficient);
		}	
		returnValue.append("]\n");

		return returnValue.toString();
	}
	
	/**
	 * @param Number of coefficients
	 * @return true if input is an integral power of 2
	 */
	private boolean isPowerOfTwo(int numberOfCoefficients) {
		
		double numberOfCoefficientsLg = Math.log10(numberOfCoefficients)/Math.log10(2.0);
		int numberOfCoefficientsLgInt = Double.valueOf(numberOfCoefficientsLg).intValue();
		
		if (numberOfCoefficientsLgInt == numberOfCoefficientsLg) {
			return true;
		} else {
			return false;
		}

	}
}