package menon.cs5050.assignment4;

public class SmartPolynomialClient {

	public static void main(String[] args) throws Exception {
		
		SmartPolynomialClient client = new SmartPolynomialClient();
		client.doFFT();
	}
	
	private void doFFT() throws Exception {
		
		double[] coeffs1 = {0, 1, 2, 3};
		double[] coeffs2 = {10, 11, 12, 13};
		
		SmartPolynomial sp1 = new SmartPolynomial(coeffs1);
		SmartPolynomial sp2 = new SmartPolynomial(coeffs2);
		
		System.out.println("Answer is " + sp1.multiply(sp2).toString());
	}
}
