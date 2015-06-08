package menon.cs5050.assignment4;

public class SmartPolynomial {
	
	private ComplexNumber[] coefficients;
	private ComplexNumber[] rootsOfUnity;
	private ComplexNumber[] inverseRootsOfUnity;

	/**
	 * Constructor
	 * @param coefficients
	 * @throws Exception
	 */
	public SmartPolynomial(double[] coefficients) throws Exception {
		
		if (!isPowerOfTwo(coefficients.length)) {
			throw new Exception("Polynomial should have number of coefficients equal to integral power of 2.");
		}
		
		//Create a coefficients array that is twice the size of the parameter
		int numberOfCoefficients = coefficients.length;
		this.coefficients = new ComplexNumber[2 * numberOfCoefficients];
		
		//Fill the initial part with values from the parameter.
		for (int index = 0; index < numberOfCoefficients; ++index) {
			this.coefficients[index] = new ComplexNumber(coefficients[index], 0);
		}
		
		//Fill the rest with zeros
		for (int index = numberOfCoefficients; index < 2 * numberOfCoefficients; ++index) {
			this.coefficients[index] = new ComplexNumber(0, 0);
		}
		
		//Get the roots of unity
		rootsOfUnity = ComplexNumber.getRootsOfUnity(2 * numberOfCoefficients);
		
		//Get inverse roots of unity
		this.inverseRootsOfUnity = new ComplexNumber[2 * numberOfCoefficients];
		for (int index = 0; index < 2 * numberOfCoefficients; ++index) {
			this.inverseRootsOfUnity[index] = this.rootsOfUnity[index].getInverseOfRootOfUnity();
		}		
		
	}
	
	/**
	 * Constructor
	 * @param coefficients
	 * @throws Exception
	 */
	public SmartPolynomial(ComplexNumber[] coefficients) throws Exception {
		
		if (!isPowerOfTwo(coefficients.length)) {
			throw new Exception("Polynomial should have number of coefficients equal to integral power of 2.");
		}
		
		this.coefficients = coefficients;
		
	}
	
	/**
	 * Multiply two polynomials using Fast Fourier Transform
	 * @param multiplier
	 * @return a polynomial that is the product of this polynomial and the parameter
	 * @throws Exception 
	 */
	public SmartPolynomial multiply(SmartPolynomial multiplier) throws Exception {
		
		if (this.coefficients.length != multiplier.length()) {
			throw new Exception("Can only multiply a polynomial with " + this.coefficients.length + " coefficients.");
		}
		
		ComplexNumber[] fftThisPolynomial = fastFourierTransform(this, false);
		ComplexNumber[] fftMultiplier = fastFourierTransform(multiplier, false);
		
		ComplexNumber[] productFft = new ComplexNumber[multiplier.length()];
		for (int index = 0; index < multiplier.length(); ++index) {
			
			productFft[index] = fftThisPolynomial[index].multiply(fftMultiplier[index]);
			
		}
		
		ComplexNumber[] productCoefficients = fastFourierTransform(new SmartPolynomial(productFft), true);
		for (int index = 0; index < multiplier.length(); ++index) {
			
			productCoefficients[index] = new ComplexNumber(productCoefficients[index].getReal() / multiplier.length(), productCoefficients[index].getImaginary() / multiplier.length());
			
		}
		
		return new SmartPolynomial(productCoefficients);

	}
	
	private ComplexNumber[] fastFourierTransform(SmartPolynomial smartPolynomial, boolean useInverseRoots) throws Exception {
		
		ComplexNumber[] returnValue = new ComplexNumber[smartPolynomial.length()];
		
		if (smartPolynomial.length() == 1) {
			returnValue[0] = smartPolynomial.at(0);
			return returnValue;
		}
		
		//Fill in the odd and even parts
		int evenOddPartLength = smartPolynomial.length() / 2;
		ComplexNumber[] evenPart = new ComplexNumber[evenOddPartLength];
		ComplexNumber[] oddPart = new ComplexNumber[evenOddPartLength];
		
		for (int index = 0; index < evenOddPartLength; ++index) {
			
			evenPart[index] = smartPolynomial.at(2 * index);
			oddPart[index] = smartPolynomial.at(2 * index + 1);
						
		}
		
		//Find the FFT for each half of the array
		ComplexNumber[] evenSolution = fastFourierTransform(new SmartPolynomial(evenPart), useInverseRoots);
		ComplexNumber[] oddSolution = fastFourierTransform(new SmartPolynomial(oddPart), useInverseRoots);
		
		//Fill array with roots of unity corresponding to the length of the odd and even parts. The roots can be
		//obtained by computing a suitable offset and using it the roots of unity array for this polynomial.
		ComplexNumber[] rootsOfUnitySquared = new ComplexNumber[2 * evenOddPartLength];
		int offset = this.rootsOfUnity.length / (2 * evenOddPartLength);
		for (int offsetIndex = 0, index = 0; offsetIndex <  this.rootsOfUnity.length; offsetIndex += offset, ++index) {
			if (useInverseRoots) {
				rootsOfUnitySquared[index] = this.inverseRootsOfUnity[offsetIndex];
			} else {
				rootsOfUnitySquared[index] = this.rootsOfUnity[offsetIndex];
			}
		}
		
		//Combine the left and right solutions
		for (int index = 0; index < evenOddPartLength; ++index) {
			
			returnValue[index] = evenSolution[index].add(rootsOfUnitySquared[index].multiply(oddSolution[index]));
			returnValue[index + evenOddPartLength] = evenSolution[index].subtract(rootsOfUnitySquared[index].multiply(oddSolution[index]));
			
		}
		
		return returnValue;
		
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
		for (ComplexNumber coefficient : this.coefficients) {
			if (firstTime) {
				firstTime = false;
			} else {
				returnValue.append(", ");
			}
			returnValue.append(coefficient.toString());
		}	
		returnValue.append("]\n");

		return returnValue.toString();
	}
	
	/**
	 * @param index
	 * @return the element at the index
	 * @throws Exception
	 */
	private ComplexNumber at(int index) throws Exception {
		
		if (index >= this.coefficients.length) {
			throw new Exception("Index " + index + " is outside the coefficients for " + this.toString() + ".");
		}
		
		return this.coefficients[index];
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