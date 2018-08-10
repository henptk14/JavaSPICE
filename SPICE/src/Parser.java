import java.io.*;
import java.util.*;
import java.text.*;

public class Parser {
	public static DecimalFormat df = new DecimalFormat(".####");	// formatting number in print.
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner s = fileinput();
		
		ArrayList<String> nodes = new ArrayList<String>();	// nodes holds an array of node names
		ArrayList<String> V_source = new ArrayList<String>();	// V_source holds an array of voltage source names 
		HashSet<CircuitElement> elements = new HashSet<CircuitElement>();	// elements holds an array of all elements in the netlist file
		ArrayList<String> Dependent_source = new ArrayList<String>();
		
		fill_data(s, V_source, nodes, Dependent_source, elements);
		ArrayList<CircuitElement> ele = new ArrayList<CircuitElement>(elements);
		System.out.println("Node array: " + nodes);
		System.out.println("Voltage Source array: " + V_source);
		System.out.println("Dependent source array: " + Dependent_source);
		System.out.println("Element array: " + ele + "\n");
		
		// initialize the size of matrix A 
		// which is the number of nodes + the number of voltage sources
		int size = nodes.size() + V_source.size() + Dependent_source.size(); 
		double[][] matrixA = new double[size][size];
		
		// initialize the size of matrix b 
		// which is the number of nodes + the number of voltage sources
		double[] vecB = new double[size];
		
		Stamper(matrixA, vecB, V_source, nodes, Dependent_source, ele);
		
		// These are just for printing out matrix A and vector B
		System.out.println("Matrix A: ");
		for(int i = 0; i < vecB.length; i++) {
			for(int j = 0; j < vecB.length; j++) {
				System.out.print(matrixA[i][j] + ",  ");
				//System.out.printf("%.04f  ", matrixA[i][j]);
			}
			System.out.println();
		}
		System.out.println("Vector B: ");
		for(int i = 0; i < vecB.length; i++) {
			System.out.println(df.format(vecB[i]));
		}
		
		double[][] L = new double[vecB.length][vecB.length];
		double[][] U = new double[vecB.length][vecB.length];
		LU_Decomposer(matrixA, vecB, L, U);
		
		//Printing out L and U matrix
		System.out.println("\nLower Matrix: ");
		for(int i = 0; i < vecB.length; i++) {
			for(int j = 0; j < vecB.length; j++) {
				System.out.printf("%.04f  ", L[i][j]);
			}
			System.out.println();
		}
		System.out.println("\nUpper Matrix: ");
		for(int i = 0; i < vecB.length; i++) {
			for(int j = 0; j < vecB.length; j++) {
				System.out.printf("%.04f  ", U[i][j]);
			}
			System.out.println();
		}
		
		double[] node_voltage = LU_Solver(L, U, vecB);
		
		// Printing out node voltages and current through voltage sources
		System.out.println("\nNode Voltages and currents through Vsource: ");
		for(int i = 0; i < node_voltage.length; i++) {
			if(i < nodes.size()) {
				System.out.printf("V" + nodes.get(i) + " = %.4f V\n", node_voltage[i]);
			} else if(i >= nodes.size() && i < nodes.size() + V_source.size()){
				System.out.printf("Current through " + V_source.get(i - nodes.size()) + " = %.4f A\n", node_voltage[i]);
			} else {
				System.out.printf("Current through " + Dependent_source.get(i - nodes.size() + V_source.size() - 2) + " = %.4f A\n", node_voltage[i]);
			}
		}
		
	}
	
	/**
	 * This method asks user to input netlist file name.
	 * If the file name is invalid, it will asks the user to input again repeatedly
	 * until valid file is entered.
	 * 
	 * @return Scanner Type that is ready to access line by line
	 * @throws FileNotFoundException
	 */
	public static Scanner fileinput() throws FileNotFoundException {
		String file_name;
		Scanner console = new Scanner(System.in);
		System.out.println("Please input netlist file name. ");
		file_name = console.nextLine();
		File f;
		do {
			f = new File(file_name);
			if(!f.exists()) {
				System.out.println("The netlist file " + file_name + " does not exist. Please try again. ");
				file_name = console.nextLine();
			}
		} while(!f.exists());
		
		Scanner sc = new Scanner(f);
		console.close();
		return sc;
	}
	
	/**
	 * This method reads through the netlist file and store 
	 * voltage sources and nodes into ArrayList, which will be
	 * used for element stamping
	 * 
	 * @param sc, a scanner that is ready to read file line-by-line
	 * @param varr, an ArrayList to store names of voltage sources
	 * @param narr, an ArrayList to store names of nodes
	 */
	public static void fill_data(Scanner sc, ArrayList<String> varr, ArrayList<String> narr, ArrayList<String> ds, HashSet<CircuitElement> c ) {
		while(sc.hasNextLine())	// If there is a line
		{
			String line = sc.nextLine().toLowerCase();
			if(line.length() > 0)	// if the line not \n
			{
				char first_char = line.charAt(0);
				switch (first_char)
				{
					case 'v': 
						IVS vs = new IVS(line);
						varr.add(vs.name);
						if(!narr.contains(vs.n1)) {
							narr.add(vs.n1);
						}
						if(!narr.contains(vs.n2)) {
							narr.add(vs.n2);
						}
						c.add(vs);
						break;
						
					case 'r':
						Resistor r = new Resistor(line);
						if(!narr.contains(r.n1))
							narr.add(r.n1);
						if(!narr.contains(r.n2))
							narr.add(r.n2);
						c.add(r);
						break;
						
					case 'i':
						ICS is = new ICS(line);
						if(!narr.contains(is.n1))
							narr.add(is.n1);
						if(!narr.contains(is.n2))
							narr.add(is.n2);
						c.add(is);
						break;
						
					case 'g':
						VCCS vccs = new VCCS(line);
						if(!narr.contains(vccs.n1))
							narr.add(vccs.n1);
						if(!narr.contains(vccs.n2))
							narr.add(vccs.n2);
						c.add(vccs);
						break;
						
					case 'h':
						CCVS ccvs = new CCVS(line);
						ds.add(ccvs.name);
						if(!narr.contains(ccvs.n1))
							narr.add(ccvs.n1);
						if(!narr.contains(ccvs.n2))
							narr.add(ccvs.n2);
						c.add(ccvs);
						break;
						
					case 'f':
						CCCS cccs = new CCCS(line);
						if(!narr.contains(cccs.n1))
							narr.add(cccs.n1);
						if(!narr.contains(cccs.n2))
							narr.add(cccs.n2);
						c.add(cccs);
						break;
						
					case 'e':
						VCVS vcvs = new VCVS(line);
						ds.add(vcvs.name);
						if(!narr.contains(vcvs.n1))
							narr.add(vcvs.n1);
						if(!narr.contains(vcvs.n2))
							narr.add(vcvs.n2);
						c.add(vcvs);
						break;
						
					default:
						break;
				}
			}
		}
		// Below is for sorting node array
		if(narr.contains("0"))
			narr.remove("0");
		Collections.sort(narr);
	}
	
	public static void Stamper(double[][] A, double[] b, ArrayList<String> varr, ArrayList<String> narr, ArrayList<String> ds, ArrayList<CircuitElement> c) {
		int n1, n2, k, ncp, ncn, kc;
		for(CircuitElement e: c) 
		{
			if(e instanceof IVS)	// if the element is IVS
			{
				n1 = narr.indexOf(e.n1);
				n2 = narr.indexOf(e.n2);
				k = narr.size() + varr.indexOf(e.name);
				((IVS) e).stamp(n1, n2, k, e.value, A, b);
			}
			else if(e instanceof Resistor)
			{
				n1 = narr.indexOf(e.n1);
				n2 = narr.indexOf(e.n2);
				((Resistor) e).stamp(n1, n2, e.value, A);
			}
			else if(e instanceof ICS)
			{
				n1 = narr.indexOf(e.n1);
				n2 = narr.indexOf(e.n2);
				((ICS) e).stamp(n1, n2, e.value, b);
			}
			else if(e instanceof VCCS)
			{
				n1 = narr.indexOf(e.n1);
				n2 = narr.indexOf(e.n2);
				ncp = narr.indexOf(((VCCS) e).get_nc1());
				ncn = narr.indexOf(((VCCS) e).get_nc2());
				((VCCS) e).stamp(n1, n2, ncp, ncn, A, e.value);
			}
			else if(e instanceof CCVS)
			{
				int i = 0;
				boolean found = false;
				// The while loop below is for searching the IVS in c ArrayList
				while(!found && i < c.size())
				{
					int temp = ((CCVS) e).get_vname().compareTo(c.get(i).name);
					if(temp == 0)
						found = true;
					else
						i++;
				}
				
				if(!found) {
					System.out.println("The voltage source for Current Controlled Voltage Source " + e.name + " is missing!");
					System.exit(1);
				}
				else
				{
					n1 = narr.indexOf(e.n1);
					n2 = narr.indexOf(e.n2);
					ncp = narr.indexOf(c.get(i).n1);
					ncn = narr.indexOf(c.get(i).n2);
					kc = narr.size() + varr.indexOf(c.get(i).name);
					k = narr.size() + varr.size() + ds.indexOf(e.name);
					((CCVS) e).stamp(n1, n2, ncp, ncn, k, kc, (IVS)c.get(i), e.value, A, b);
				}
			}
			else if(e instanceof CCCS)
			{
				int i = 0;
				boolean found = false;
				// The while loop below is for searching the IVS in c ArrayList
				while(!found && i < c.size())
				{
					int temp = ((CCCS) e).get_vname().compareTo(c.get(i).name);
					if(temp == 0)
						found = true;
					else
						i++;
				}
				
				if(!found) {
					System.out.println("The voltage source for Current Controlled Current Source " + e.name + " is missing!");
					System.exit(1);
				}
				else
				{
					n1 = narr.indexOf(e.n1);
					n2 = narr.indexOf(e.n2);
					ncp = narr.indexOf(c.get(i).n1);
					ncn = narr.indexOf(c.get(i).n2);
					k = narr.size() + varr.indexOf(c.get(i).name);
					((CCCS) e).stamp(n1, n2, ncp, ncn, k, (IVS)c.get(i), e.value, A, b);
				}
			}
			else if(e instanceof VCVS)
			{
				n1 = narr.indexOf(e.n1);
				n2 = narr.indexOf(e.n2);
				ncp = narr.indexOf(((VCVS) e).get_nc1());
				ncn = narr.indexOf(((VCVS) e).get_nc2());
				k = narr.size() + varr.size() + ds.indexOf(e.name);
				((VCVS) e).stamp(n1, n2, ncp, ncn, k, e.value, A);
			}
		}
	}
	
	public static void LU_Decomposer(double[][] m, double[] b, double[][] l, double[][] u) {
		// this for-loop fill the upper matrix with 
		// elements from matrix A
		for(int i = 0; i < b.length; i++) {
			u[i] = m[i];
		}
		
		l[0][0] = 1;
		int count = 1;
		int size = b.length;
		while(count < size) {
			for(int i = count; i < size; i++) {	// this for loop is for rows
				if(u[count - 1][count - 1] == 0) { // to avoid denominator being 0 in x below
					double[] temp1 = u[count -1];
					u[count - 1] = u[count];
					u[count] = temp1;
					
					double[] temp2 = l[count -1];
					l[count -1] = l[count];
					l[count] = temp2;
					
					double temp3 = b[count - 1];
					b[count - 1] = b[count];
					b[count] = temp3;
				}
				double x = u[i][count - 1] / u[count - 1][count -1];	// in ith row, temp is equal to
				for(int j = count - 1; j < size; j++) {		// this for loop is for columns
					u[i][j] = u[i][j] - (x * u[count - 1][j]);
				}
				l[i][count] = 1;
				l[i][count - 1] = x;
			}
			count++;
		}
	}
	
	public static double[] LU_Solver(double[][] l, double[][] u, double[] b) {
		int size = b.length;
		double[] ans = new double[size];
		double[] z = new double[size];
		
		z[0] = b[0];
		for(int i = 0; i < size; i++) {
			z[i] = b[i];
			for(int j = 0; j < i; j++) {
				z[i] -= z[j] * l[i][j];
			}
		}
		
		ans[size - 1] = z[size - 1] / u[size - 1][size - 1];
		for(int i = size - 2; i >= 0; i--) {
			ans[i] = z[i];
			for(int j = size - 1; j > i; j--) {
				ans[i] -= ans[j] * u[i][j];
			}
			ans[i] /= u[i][i];
		}
		return ans;
	}
}


