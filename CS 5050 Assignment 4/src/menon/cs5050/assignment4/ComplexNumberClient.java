package menon.cs5050.assignment4;

public class ComplexNumberClient {

	public static void main(String[] args) {
		ComplexNumber[] roots = ComplexNumber.getRootsOfUnity(8);
		System.out.print("\n[");
		boolean firstTime = true;
		for (ComplexNumber root: roots) {
			if (firstTime) {
				firstTime = false;
			} else {
				System.out.print(", ");
			}
			System.out.print(root.toString());
		}
		System.out.print("]");
		
		ComplexNumber root = new ComplexNumber(0.7071067811865476, 0.7071067811865475);
		System.out.print("\nInverse is ");
		System.out.print(root.getInverseOfRootOfUnity().toString());
		
		ComplexNumber c1 = new ComplexNumber(2,3);
		ComplexNumber c2 = new ComplexNumber(4,-5);
		
		System.out.print("\nProduct of " + c1.toString() + " and " + c2.toString() + " is " + c1.multiply(c2).toString());
		System.out.print("\nSum of " + c1.toString() + " and " + c2.toString() + " is " + c1.add(c2).toString());
		System.out.print("\nDifference of " + c1.toString() + " and " + c2.toString() + " is " + c1.subtract(c2).toString());
	}
	
}
