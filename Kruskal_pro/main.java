package kruskal_pro;
import java.util.*;

/*
 * @author:Jiangzh
 * 执行
 * */

public class main {
		public static void main(String[] args) {
			int vertexSum;											//顶点数目
			int edgeSum=0;											//边数目
			assist ass=new assist();								//辅助对象
			Scanner scanner=new Scanner(System.in);
			
			long startTime;			//程序开始时间
			long endTime;			//程序结束时间
			long time;				//用时
			long startExecute;		//算法执行开始的时间（初始化结束后）
			long endExecute;		//算法执行结束的时间（输出结果前）
			
			//图的初始化
			System.out.print("输入顶点的数目：");
			vertexSum=scanner.nextInt();
			int[][] graph=new int[vertexSum][vertexSum];
			for (int i=0;i<vertexSum;i++)
				for (int j=0;j<vertexSum;j++) {
					graph[i][j]=scanner.nextInt();
					if (graph[i][j]!=0)	edgeSum++;
				}
			edgeSum=edgeSum/2;
			
			startTime=System.nanoTime();//程序开始执行时间
			
			ass.initAssist(vertexSum, edgeSum);
			
			startExecute=System.nanoTime();//算法执行开始时间
			
			//优先队列数组的初始化
			ass.generateArray(graph);
			
			//开始探测
			int[][] graph_temp=new int[vertexSum][vertexSum];//从一个初始为全0的二维数组开始
			for (int i=0;i<vertexSum;i++)
				for (int j=0;j<vertexSum;j++)
					graph_temp[i][j]=0;
			ass.detectEdge(graph_temp, 0, 0, 0);//初始探测：空二维数组、权值和为0，优先队列数组下表为0、生成树边数为0
			
			endExecute=System.nanoTime();//算法执行结束时间
			
			//输出所有最小生成树
			ass.outputAllMST();
			
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
