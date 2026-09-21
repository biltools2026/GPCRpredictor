import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
public class bayesTrain {
	public static void main(String[] args) throws Exception {		
		String my_file = args[0];		
		BufferedReader br = null;
		String line = "";
		String[] wds;
		int count = 0;
		double[]   pos_mean_list  = null;
		double[]   neg_mean_list  = null;
		double[]   pos_var_list   = null;
		double[]   neg_var_list   = null;
		double[][] pos_matrix = null;
		double[][] neg_matrix = null;		
		int pos_count = 0;
		int neg_count = 0;
		br = new BufferedReader(new FileReader(my_file));	    
		while ((line = br.readLine()) != null){
			count++;
			if(count>1){
				wds = line.split("\\s+");
				if(wds[0].equals("+1")||(wds[0].equals("1"))){
					pos_count++;
				}else{
					neg_count++;
				}
			}
		}
		br.close();		
		count = 0;
		int index_1 = 0;
		int index_2 = 0;
		int vector_count = 0;
		br = new BufferedReader(new FileReader(my_file));	    
		while ((line = br.readLine()) != null){
				count++;				
				if(1==count){
					// read the title and initial the matrix
					wds = line.split("\\s+");				
					vector_count = wds.length;
					//System.out.println("vector_count=" + vector_count);
					pos_matrix = new double[pos_count+1][wds.length+1];
					neg_matrix = new double[neg_count+1][wds.length+1];
					for(int i=0;i<=pos_count;i++){
						for(int j=0;j<=wds.length;j++){
							pos_matrix[i][j] = -1;
						}
					}
					for(int i=0;i<=neg_count;i++){
						for(int j=0;j<=wds.length;j++){
							neg_matrix[i][j] = -1;
						}
					}
					pos_mean_list = new double[pos_count + 1];
					for(int i=0;i<=pos_count;i++){
						pos_mean_list[i] = 0;
					}
					neg_mean_list = new double[neg_count + 1];
					for(int i=0;i<=neg_count;i++){
						neg_mean_list[i] = 0;
					}
					pos_var_list = new double[pos_count + 1];
					for(int i=0;i<=pos_count;i++){
						pos_var_list[i] = 0;
					}
					neg_var_list = new double[neg_count + 1];
					for(int i=0;i<=neg_count;i++){
						neg_var_list[i] = 0;
					}
				}				
				if(count>1){			    	
			    	wds = line.split("\\s+");			    
			    	if(wds[0].equals("+1")||(wds[0].equals("1"))){
			    		index_1++;
			    		for(int i=1;i<wds.length;i++){
			    			//pos_matrix[index_1][i] = Double.valueOf(wds[i]);
			    			pos_matrix[index_1][i] = Double.valueOf(wds[i]);
			    		}
			    	}else{
			    		index_2++;
			    		for(int i=1;i<wds.length;i++){
			    			//neg_matrix[index_2][i] = Double.valueOf(wds[i]);
			    			neg_matrix[index_2][i] = Double.valueOf(wds[i]);
			    		}
			    	}
				}
		}
		br.close();		
//		System.out.println(index_1 + " " + index_2);	
//		System.out.println("m11=" + pos_matrix[1][1]);
//		System.out.println("m21=" + pos_matrix[2][1]);
//		System.out.println("m31=" + pos_matrix[3][1]);
//		System.out.println("m41=" + pos_matrix[4][1]);
//		System.out.println("pos_c=" + pos_count);//
//		System.out.println("n_m11=" + neg_matrix[1][1]);
//		System.out.println("n_m21=" + neg_matrix[2][1]);
//		System.out.println("n_m31=" + neg_matrix[3][1]);
//		System.out.println("n_m41=" + neg_matrix[4][1]);
//		System.out.println("n_c=" + neg_count);				
		int i,j;
		for(j=1;j<vector_count;j++){
			// mean calculation
			for(i=1;i<=pos_count;i++){
				pos_mean_list[j]+=pos_matrix[i][j];
			}
			for(i=1;i<=neg_count;i++){
				neg_mean_list[j]+=neg_matrix[i][j];
			}	
			pos_mean_list[j]/=pos_count;
			neg_mean_list[j]/=neg_count;
			// variable calculation
			for(i=1;i<=pos_count;i++){
				pos_var_list[j] = pos_var_list[j] + ((pos_matrix[i][j]-pos_mean_list[j])*(pos_matrix[i][j]-pos_mean_list[j]));
			}
			pos_var_list[j] =(pos_var_list[j]/(pos_count-1));			
			for(i=1;i<=neg_count;i++){
				neg_var_list[j]  = neg_var_list[j] + ((neg_matrix[i][j]-neg_mean_list[j])*(neg_matrix[i][j]-neg_mean_list[j]));
			}
			neg_var_list[j] =(neg_var_list[j]/(neg_count-1));						
//			System.out.println(j + " pos_v1_mean="+ pos_mean_list[j]);
//			System.out.println(j + " pos_v1_var ="+ pos_var_list[j]);
//			System.out.println(j + " neg_v1_mean="+ neg_mean_list[j]);
//			System.out.println(j + " neg_v1_var ="+ neg_var_list[j]);			
		}								
		// real prediction		
		double[] real_data = new double[]{6,130,8};
		double pos_possible=1,neg_possible=1;
		double evident = 0;		
		pos_possible*=0.5; // P(male)
		neg_possible*=0.5; // P(female)		
		FileWriter fw = new FileWriter("bayes_model");
		for(j=1;j<vector_count;j++){
			//System.out.println("feature " + j +" pos mean=" + pos_mean_list[j] + " var=" +pos_var_list[j]);
			//System.out.println("feature " + j +" neg mean=" + neg_mean_list[j] + " var=" +neg_var_list[j]);			
			fw.append("+1 feature " + j +" " + pos_mean_list[j] + " " +pos_var_list[j] + "\n");
			fw.append("-1 feature " + j +" " + neg_mean_list[j] + " " +neg_var_list[j] + "\n");	
//			System.out.println("nnnd= " + pos_mean_list[j] + " " + pos_var_list[j] + " " + real_data[j-1]);			
			//System.out.println("QQQQ= " + neg_mean_list[j] + " " + neg_var_list[j] + " " + real_data[j-1]);
			//pos_possible = pos_possible*fun(pos_mean_list[j],pos_var_list[j],real_data[j-1]);
			//neg_possible = neg_possible*fun(neg_mean_list[j],neg_var_list[j],real_data[j-1]);
			//System.out.println("RRRRRRRRRRRR=" + neg_possible);
			//evident = pos_possible + neg_possible;
			//System.out.println("train check " + evident + " " + pos_possible + " " + neg_possible);
		}
		fw.flush();
		fw.close();	
//		System.out.println("ccc +1 possibility=" + pos_possible/evident);
//		System.out.println("ccc -1 possibility=" + neg_possible/evident);		
	}
		
	public static double fun(double mean,double var,double value){
		double f1 = -(value-mean)*(value-mean)/2.0/var;
		double f = Math.pow(Math.E,f1)*1.0/Math.sqrt(2.0*Math.PI*var)*1.0;
		return f;
	}	
}