
public class CCCS extends CircuitElement{
	private String vname;
	
	public CCCS(String s) {
		super(s);
		vname = super.get_Sarray()[3];
	}
	
	public String get_vname() {
		return vname;
	}
	
	public void stamp(int n1, int n2, int ncp, int ncn, int k, IVS vs, double v, double[][] m, double[] b) {
		if(!vs.stamped)
		{
			if(ncp == -1)	// if the positive terminal of Vsource is connected to ground
			{
				m[ncn][k] -= 1;
				m[k][ncn] -= 1;
			}
			else if(ncn == -1)	// if the negative terminal of Vsource is connected to ground
			{
				m[ncp][k] += 1;
				m[k][ncp] += 1;
			}
			else
			{
				m[ncp][k] += 1;
				m[ncn][k] -= 1;
				m[k][ncp] += 1;
				m[k][ncn] -= 1;
			}
			b[k] = vs.value;
			vs.set_stamp(true);
		}
		
		// Below is for stamping CCCS
		if(n1 == -1) {
			m[n2][k] -= v;
		}
		else if(n2 == -1) {
			m[n1][k] += v;
		} 
		else {
			m[n1][k] += v;
			m[n2][k] -= v;
		}
	}
}
