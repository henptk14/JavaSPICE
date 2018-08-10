
public class VCCS extends CircuitElement {
	private String nc1;
	private String nc2;
	
	public VCCS(String s){
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
	
	public void stamp(int n1, int n2, int ncp, int ncn, double[][] m, double v) {
		if(n1 == -1 && ncp == -1) {
			m[n2][ncn] += v;
		}
		else if(n1 == -1 && ncn == -1) {
			m[n2][ncp] -= v;
		}
		else if(n2 == -1 && ncp == -1) {
			m[n1][ncn] -= v;
		}
		else if(n2 == -1 && ncn == -1) {
			m[n1][ncp] += v;
		}
		else if(n1 == -1) {
			m[n2][ncp] -= v;
			m[n2][ncn] += v;
		}
		else if(n2 == -1) {
			m[n1][ncp] += v;
			m[n1][ncn] -= v;
		}
		else if(ncp == -1) {
			m[n1][ncn] -= v;
			m[n2][ncn] += v;
		}
		else if(ncn == -1) {
			m[n1][ncp] += v;
			m[n2][ncp] -= v;
		}
		else {
			m[n1][ncp] += v;
			m[n1][ncn] -= v;
			m[n2][ncp] -= v;
			m[n2][ncn] += v;
		}
		stamped = true;
		//System.out.println("n1: " + n1 + " n2: " + n2 + " ncp: " + " ncn: " + ncn);
	}
}
