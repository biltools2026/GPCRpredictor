import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class bayesTest {
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub	
		String test = args[1];
		String model = args[0];
		String line = "";
		ArrayList<Double> pos_mean_list = new ArrayList<Double>();
		ArrayList<Double> pos_var_list = new ArrayList<Double>();
		ArrayList<Double> neg_mean_list = new ArrayList<Double>();
		ArrayList<Double> neg_var_list = new ArrayList<Double>();
		BufferedReader br = new BufferedReader(new FileReader(model));
		int count = 1;
		int len = -1;
		pos_mean_list.clear();
		pos_var_list.clear();
		neg_mean_list.clear();
		neg_var_list.clear();
		while ((line = br.readLine()) != null){
			String[] wds = line.split("\\s+");
			len = wds.length;
			len--;
			if(count%2==1){
				pos_var_list.add(Double.valueOf(wds[len]));
				pos_mean_list.add(Double.valueOf(wds[len-1]));
			}else{
				neg_var_list.add(Double.valueOf(wds[len]));
				neg_mean_list.add(Double.valueOf(wds[len-1]));
			}
			count++;
		}
		br.close();
		
//		for(int i=0;i<pos_mean_list.size();i++){
//			System.out.println(pos_mean_list.get(i) + " " + pos_var_list.get(i));
//			System.out.println(neg_mean_list.get(i) + " " + neg_var_list.get(i));
//		}
		
//		System.exit(1);
//		real prediction					
		count = 1;
		br = new BufferedReader(new FileReader(test));
		br.readLine();
		while ((line = br.readLine()) != null){
//			line = "+1 6 130 8";
			double pos_possible=1,neg_possible=1;
			double evident = 0;		
			pos_possible=0.5; // P(male)
			neg_possible=0.5; // P(female)	
			String[] wds = line.split("\\s+");
			for(int i=1;i<wds.length;i++){				
				pos_possible = pos_possible*fun(pos_mean_list.get(i-1),pos_var_list.get(i-1),Double.valueOf(wds[i]).doubleValue());				
				//System.out.println("QQQQ= " + neg_mean_list.get(i-1) + " " + neg_var_list.get(i-1) + " " + Double.valueOf(wds[i]).doubleValue());
				neg_possible = neg_possible*fun(neg_mean_list.get(i-1),neg_var_list.get(i-1)*1.0,Double.valueOf(wds[i]).doubleValue()*1.0);
				//System.out.println("RRRRRRRRRRRR=" + neg_possible);
				evident = pos_possible + neg_possible;
			}
			//System.out.println("test check " + evident + " " + pos_possible + " " + neg_possible);
			System.out.print("Instance " + count + " [+1 possibility]=" + pos_possible/evident);
			System.out.println(" [-1 possibility]=" + neg_possible/evident);
			count++;
			//System.exit(1);
		}
		br.close();		
		
	}
	
	public static double fun(double mean,double var,double value){
//		System.out.println("mean="+mean + " var=" + var + " value=" + value);
		double f1 = -(value-mean)*(value-mean)/2.0/var;
		double f = Math.pow(Math.E,f1)*1.0/Math.sqrt(2.0*Math.PI*var)*1.0;
		return f;
	}
	
}
