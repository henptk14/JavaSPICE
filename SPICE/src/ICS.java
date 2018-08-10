
public class ICS extends CircuitElement	{
	public ICS(String s) {
		super(s);
	}
	
	public void stamp(int n1, int n2, double v, double[] vecB) {
		if(n1 == -1) {	// if arrow tail(+node) is connected to ground
			vecB[n2] += v;
		}
		else if(n2 == -1) {	// if arrow head(-node) is connected to ground
			vecB[n1] -= v;
		}
		else {
			vecB[n1] -= v;
			vecB[n2] += v;
		}
		stamped = true;
	}
}
