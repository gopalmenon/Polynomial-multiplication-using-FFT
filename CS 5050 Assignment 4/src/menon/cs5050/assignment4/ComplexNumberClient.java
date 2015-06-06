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
	}
	
}
