
public class Resistor extends CircuitElement{
	public Resistor(String s) {
		super(s);
	}
	
	public void stamp(int n1, int n2, double v, double[][] A) {
		if(n1 == -1)	// if node 1 is connected to ground
		{
			A[n2][n2] += (1/v);
		}
		else if(n2 == -1)	// if node 2 is connected to ground
		{
			A[n1][n1] += (1/v);
		} 
		else 
		{
			A[n1][n1] += (1/v);
			A[n1][n2] -= (1/v);
			A[n2][n1] -= (1/v);
			A[n2][n2] += (1/v);
		}
		stamped = true;
	}
}
