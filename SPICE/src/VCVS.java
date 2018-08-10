
public class VCVS extends CircuitElement{
	private String nc1;
	private String nc2;
	
	public VCVS(String s) {
		super(s);
		nc1 = super.get_Sarray()[3];
		nc2 = super.get_Sarray()[4];
	}
	
	public String get_nc1() {
		return nc1;
	}
	
	public String get_nc2() {
		return nc2;
	}
	
	public void stamp(int n1, int n2, int ncp, int ncn, int k, double v, double[][] m) {
		if(n1 == -1 && ncp == -1) {
			m[n2][k] -= 1;
			m[k][n2] -= 1;
			m[k][ncn] += v;
		}
		else if(n1 == -1 && ncn == -1) {
			m[n2][k] -= 1;
			m[k][n2] -= 1;
			m[k][ncp] -= v;
		}
		else if(n2 == -1 && ncp == -1) {
			m[n1][k] += 1;
			m[k][n1] += 1;
			m[k][ncn] += v;
		}
		else if(n2 == -1 && ncn == -1) {
			m[n1][k] += 1;
			m[k][n1] += 1;
			m[k][ncp] -= v;
		}
		else if(n1 == -1) {
			m[n2][k] -= 1;
			m[k][n2] -= 1;
			m[k][ncp] -= v;
			m[k][ncn] += v;
		}
		else if(n2 == -1) {
			m[n1][k] += 1;
			m[k][n1] += 1;
			m[k][ncp] -= v;
			m[k][ncn] += v;
		}
		else if(ncp == -1) {
			m[n1][k] += 1;
			m[n2][k] -= 1;
			m[k][n1] += 1;
			m[k][n2] -= 1;
			m[k][ncn] += v;
		}
		else if(ncn == -1) {
			m[n1][k] += 1;
			m[n2][k] -= 1;
			m[k][n1] += 1;
			m[k][n2] -= 1;
			m[k][ncp] -= v;
		}
		else {
			m[n1][k] += 1;
			m[n2][k] -= 1;
			m[k][n1] += 1;
			m[k][n2] -= 1;
			m[k][ncp] -= v;
			m[k][ncn] += v;
		}
		stamped = true;
	}
}
