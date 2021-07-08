package prim_pro;
import java.util.*;
/*
 *   @author:Jiangzh
 *   执行算法
 * */
public class execute {
	public static void main(String[] args) {
		Scanner scanner=new Scanner(System.in);
		long startTime;			//程序开始时间
		long endTime;			//程序结束时间
		long time;				//用时
		long startExecute;		//算法执行开始的时间（初始化结束后）
		long endExecute;		//算法执行结束的时间（输出结果前）
		int flag;
		
		System.out.println("选择随机生成测试数据还是手动输入数据：（1为随机生成，2为手动输入）");
		flag=scanner.nextInt();
		
		confirmConponent con=new confirmConponent();
		if (flag==1) {
			con.testCase();	//随机生成数据
		}else if(flag==2) {
			con.input();//手动输入数据
		}else {
			return;
		}
		
		startTime=System.nanoTime();//程序开始执行时间
		
		con.init();
		
		startExecute=System.nanoTime();//算法执行开始时间
		
		con.generateArray();
		con.find();
		con.merge();
		con.generateNewGraph();
		
		useKruskalPro ukp=new useKruskalPro();
		
		if (con.newVertexSum==1) {
			
			endExecute=System.nanoTime();//算法执行结束时间
			
			System.out.println("最小生成树仅有一个：");
			int sum=0;//权值和
			for (int i=0;i<con.edgeSum;i++) {
				if (con.array[i].visited==1) {
					System.out.println(con.array[i].endpoint1+" "+con.array[i].endpoint2);
					sum+=con.array[i].value;
				}
			}
			System.out.println("权值和为："+sum);
		}else {
		ukp.init(con);
		ukp.generateArray();
		//开始探测
		int[][] graph_temp=new int[ukp.vertexSum][ukp.vertexSum];//从一个初始为全0的二维数组开始
		for (int i=0;i<ukp.vertexSum;i++)
			for (int j=0;j<ukp.vertexSum;j++)
				graph_temp[i][j]=0;
		ukp.detectEdge(graph_temp, 0, 0, 0);
		
		endExecute=System.nanoTime();//算法执行结束时间
		
		//输出所有最小生成树
		System.out.println("第一部分固定边：");
		int sum1=0;//第一部分固定边权值和
		for (int i=0;i<con.edgeSum;i++) {
			if (con.array[i].visited==1) {
				sum1+=con.array[i].value;
			}
		}
		System.out.println("第一部分生成树的权值和为："+sum1);
		for (int i=0;i<con.edgeSum;i++) {
			if (con.array[i].visited==1) {
				System.out.println(con.array[i].endpoint1+" "+con.array[i].endpoint2+" value="+con.array[i].value);
			}
		}
		System.out.println("第二部分可选边：");
		ukp.outputAllMST();
		}
		
		endTime=System.nanoTime();//程序执行结束时间
		
		//程序和算法时间情况
		time=endTime-startTime;
		System.out.println("程序总用时："+time/1000000+"ms"+"   精确表示为："+time+"ns");
		time=startExecute-startTime;
		System.out.println("初始化用时："+time/1000000+"ms"+"   精确表示为："+time+"ns");
		time=endExecute-startExecute;
		System.out.println("算法执行用时："+time/1000000+"ms"+"   精确表示为："+time+"ns");
		time=endTime-endExecute;
		System.out.println("输出用时："+time/1000000+"ms"+"   精确表示为："+time+"ns");
	}
}
