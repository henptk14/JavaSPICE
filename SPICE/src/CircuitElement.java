
public class CircuitElement {
	private String[] s_arr;
	protected String name;
	protected String n1, n2;
	protected double value;
	protected boolean stamped = false;
	
	public CircuitElement(String s)
	{
		// Split the array around spaces.
		s_arr = s.split("\\s+");
		name = s_arr[0];
		n1 = s_arr[1];
		n2 = s_arr[2];
		value = unitConversion(s_arr[s_arr.length - 1]);
	}
	
	public String[] get_Sarray()
	{
		return s_arr;
	}
	
	public boolean getStampStatus() {
		return stamped;
	}
	
	public double unitConversion(String s)
	{
		//double v = Double.valueOf(s.substring(0, s.length() - 1));
		char unit = s.charAt(s.length() - 1);
		double v;
		switch(unit)
		{
			case 'k':	// kilo
				v = Double.valueOf(s.substring(0, s.length() - 1)) * 1000;
				break;
				
			case 'M':	// Mega
				v = Double.valueOf(s.substring(0, s.length() - 1)) * 1E6;
				break;
				
			case 'G':	// Giga
				v = Double.valueOf(s.substring(0, s.length() - 1)) * 1E9;
				break;
				
			case 'T':	// Tera
				v = Double.valueOf(s.substring(0, s.length() - 1)) * 1E12;
				break;
				
			case 'P':	// Peta
				v = Double.valueOf(s.substring(0, s.length() - 1)) * 1E15;
				break;
				
			case 'm':	// milli
				v = Double.valueOf(s.substring(0, s.length() - 1)) * 1E-3;
				break;
				
			case 'u':	// micro
				v = Double.valueOf(s.substring(0, s.length() - 1)) * 1E-6;
				break;
				
			case 'n':	// nano
				v = Double.valueOf(s.substring(0, s.length() - 1)) * 1E-9;
				break;
				
			case 'p':	// pico
				v = Double.valueOf(s.substring(0, s.length() - 1)) * 1E-12;
				break;
				
			case 'f':	// femto
				v = Double.valueOf(s.substring(0, s.length() - 1)) * 1E-15;
				break;
				
			default:
				v = Double.valueOf(s);
				break;
		}
		return v;
	}
}
