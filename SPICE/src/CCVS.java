
public class CCVS extends CircuitElement{
	private String vname;
	
	public CCVS(String s) {
		super(s);
		vname = super.get_Sarray()[3];
	}
	
	public String get_vname() {
		return vname;
	}
	
	public void stamp(int n1, int n2, int ncp, int ncn, int k, int c, IVS vs, double v, double[][] m, double[] b) {
		if(!vs.stamped) {	// if Vname is not stamped yet
			if(ncp == -1)	// if the positive terminal of Vsource is connected to ground
			{
				m[ncn][c] -= 1;
				m[c][ncn] -= 1;
			}
			else if(ncn == -1)	// if the negative terminal of Vsource is connected to ground
			{
				m[ncp][c] += 1;
				m[c][ncp] += 1;
			}
			else
			{
				m[ncp][c] += 1;
				m[ncn][c] -= 1;
				m[c][ncp] += 1;
				m[c][ncn] -= 1;
			}
			b[c] = vs.value;
			vs.set_stamp(true);
		}
		
		// Below is for stamping CCVS
		if(n1 == -1) {
			m[n2][k] -= 1;
			m[k][n2] -= 1;
			m[k][c] -= v;
		}
		else if(n2 == -1) {
			m[n1][k] += 1;
			m[k][n1] += 1;
			m[k][c] -= v;
		}
		else {
			m[n1][k] += 1;
			m[n2][k] -= 1;
			m[k][n1] += 1;
			m[k][n2] -= 1;
			m[k][c] -= v;
		}
		stamped = true;
	}
}
