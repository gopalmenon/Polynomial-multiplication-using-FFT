package menon.cs5050.assignment4;

public class ComplexNumber {
	
	private double real;
	private double imaginary;
	
	/**
	 * Constructor
	 * @param real
	 * @param imaginary
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	/**
	 * @param complexToAdd
	 * @return a complex number that is the result of adding this and another complex number
	 */
	public ComplexNumber add(ComplexNumber complexToAdd) {
		
		return new ComplexNumber(this.real + complexToAdd.real, this.imaginary + complexToAdd.imaginary);
		
	}

	
	/**
	 * @param multiplier
	 * @return a new complex number that is the result of multiplying this and another complex number
	 * using the Karatsuba multiplication algorithm
	 */
	public ComplexNumber multiply(ComplexNumber multiplier) {
		
		double realsProduct = this.real * multiplier.real;
		double imaginariesProduct = this.imaginary * multiplier.imaginary;
		double sumProduct = (this.real + this.imaginary) * (multiplier.real + multiplier.imaginary);
			
		return new ComplexNumber(realsProduct - imaginariesProduct, sumProduct - (realsProduct + imaginariesProduct));
		
	}
	
	/**
	 * @return the inverse of this complex number when it is a root of unity
	 */
	public ComplexNumber getInverseOfRootOfUnity() {
		
		return new ComplexNumber(this.real, -this.imaginary);
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * return a string representation of the complex number
	 */
	public String toString() {
		
		StringBuffer returnValue = new StringBuffer();
		
		returnValue.append(this.real);
		returnValue.append(" + ");
		returnValue.append(this.imaginary);
		returnValue.append("j");
		
		return returnValue.toString();
		
	}
	
	/**
	 * @param exponent
	 * @return the roots of unity as an array of complex numbers
	 */
	public static ComplexNumber[] getRootsOfUnity(int exponent) {
		
		ComplexNumber[] returnValue = new ComplexNumber[exponent];
		
		//Find the angle for the first root of unity
		double complexNumberAngle = 2* Math.PI / exponent;
		
		//Find the first and last roots of unity
		returnValue[0] = new ComplexNumber(Math.cos(complexNumberAngle), Math.sin(complexNumberAngle));
		
		//Find the rest by multiplying the previous one with the first one
		for (int index = 1; index < exponent; ++index) {
			returnValue[index] = new ComplexNumber(returnValue[index - 1].real, returnValue[index - 1].imaginary).multiply(returnValue[0]);
		}
		
		return returnValue;
		
	}
}
