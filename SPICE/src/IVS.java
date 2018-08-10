
public class IVS extends CircuitElement{
	
	public IVS(String s)
	{
		super(s);
	}
	
	public void set_stamp(boolean b) {
		stamped = b;
	}
	
	public void stamp(int n1, int n2, int k, double v, double[][] A, double[] b)
	{
		if(!stamped) {
			if(n1 == -1)	// if the positive terminal of Vsource is connected to ground
			{
				A[n2][k] -= 1;
				A[k][n2] -= 1;
			}
			else if(n2 == -1)	// if the negative terminal of Vsource is connected to ground
			{
				A[n1][k] += 1;
				A[k][n1] += 1;
			}
			else
			{
				A[n1][k] += 1;
				A[n2][k] -= 1;
				A[k][n1] += 1;
				A[k][n2] -= 1;
			}
			
			// Below fill the RHS
			b[k] = v;
			stamped = true;
		}
	}
}
