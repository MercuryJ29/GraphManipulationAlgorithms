package prim_pro;
import java.util.*;
/*
 *   @author:Jiangzh
 *   使用改进的Kruskal算法处理类
 * 将图划分结束之后，采用改进的kruskal算法来完成接下来的所有最小生成树的生成（即确定可选边中哪些边纳入最小生成树）
 * */
public class useKruskalPro {
	public static Scanner scanner =new Scanner(System.in);
	public int[] visited;			//visited数组表示该顶点是否访问过：1访问过   /   2未访问过
	public int[] ancestor;			//ancestor[]表示记录当前结点的前驱结点
	public int vertexSum;			//顶点的总数
	public int edgeSum;				//边的总数
	public int flag=0;				//标志，初始为0，为1则说明有环
	public edge[] array;			//模拟优先队列的数组
	public int MIN=10000;			//当前最小生成树的权值和
	public static Stack<int [][]> stack=new Stack();	//stack栈用来保存可能最小的生成树，然后依次确定是否为最小生成树后考虑输出
	public int[][] refer;			//参考二维数组，输出时根据新图的边的两个端点确定该边对应在array[]中的下标
	
	
	
	/*
	 * init()函数
	 * 作用：完成该类的初始化
	 * */
	public void init(confirmConponent con) {
		//visited[]的初始化
		visited=new int[con.newVertexSum];
		
		//ancestor[]的初始化
		ancestor=new int[con.newVertexSum];
		
		vertexSum=con.newVertexSum;
		edgeSum=con.newEdgeSum;
		
		//array[]的初始化
		array=new edge[edgeSum];
		for (int i=0;i<edgeSum;i++)
			array[i]=con.newArray[i];
		
		//refer[][]的初始化
		refer=new int[con.newVertexSum][con.newVertexSum];
		for (int i=0;i<con.newEdgeSum;i++) {
			refer[con.newArray[i].endpoint1][con.newArray[i].endpoint2]=i;
			refer[con.newArray[i].endpoint2][con.newArray[i].endpoint1]=i;
		}
	}
	
	
	
	/*
	 * hasLoop()函数
	 * 作用：判断一个图中是否存在环
	 * 输入：要判断的graph[][]
	 * 输出：void（表现为对flag的修改）
	 * */
	public void hasLoop(int[][] graph) {
		for (int i=0;i<vertexSum;i++)
			ancestor[i]=-1;//将祖宗结点数组全部置为-1
		for (int i=0;i<vertexSum;i++) {
			visited[i]=0;//将访问数组全部置为0
		}
		for (int i=0;i<vertexSum;i++) {
			if (visited[i]==0)
				DFS(i,graph);//执行DFS
		}
	}
	
	
	
	/*
	 * DFS()函数
	 *作用：使用深度优先遍历来检测图中是否有环 
	 *参数：v表示当前结点，graph表示所要处理的无向图
	 *返回：void （基于深度优先遍历，根据是否访问了已访问过的且不是祖宗结点的结点，来判断图中是否有环）
	 * */
	public void DFS(int v,int[][] graph) {
		visited[v]=1;//表示该结点已被访问
		for (int i=0;i<vertexSum;i++) {
			if (i!=v && i!=ancestor[v] && graph[v][i]!=0) {//该结点与当前v结点相连且不是前驱结点也不是当前结点
				if (visited[i]==0) {//该结点未访问过
					ancestor[i]=v;
					DFS(i,graph);
				}else {
					flag=1;//存在环，将flag修改为1
				}
			}
		}
	}
	
	
	
	/*
	* generateArray()函数
	 * 作用：构造优先队列数组（在之前newEdge[] array的基础之上，确定这些newEdge的equalEdgeCount的值）
	 * 输入：void（先前的array[]）
	 * 返回：void（优先队列数组erray[]）
	 * */
	public void generateArray() {
		int temp=0;//当出现边权相等的边时，记录前一条边的equalEdgeCount值
		//对array数组当中有权值相等的边初始化它们的equalEdgeCount值
		for (int i=0;i<edgeSum-1;i++) {
			if (array[i].value==array[i+1].value) {//出现权值相等的边
				temp=array[i].equalEdgeCount;
				for (int j=0;j<temp+1;j++) {
					array[i+1-j].equalEdgeCount=temp+1;//从后往前将相等的边equalEdgeCount
				}
			}
		}
	}
	
	
	
	/*
	 * detect()函数
	 * 作用：逐步探测边，将边加入到生成树当中
	 * 输入：preGraph表示传递参数的生成树，edgeValueSum表示当前生成树的权值和，index表示优先队列数组的指针位置，edgeCount表示当前生成树的边的数目
	 * */
	public void detectEdge(int[][] preGraph,int edgeValueSum,int index,int edgeCount) {
		//处理引用传递
		int[][] graph=new int[vertexSum][vertexSum];
		for (int i=0;i<vertexSum;i++)
			for (int j=0;j<vertexSum;j++)
				graph[i][j]=preGraph[i][j];
		
		//递归终止判断，共3个if语句
		if (edgeValueSum>MIN) {//如果当前的生成树的权值和已经大于当前已经得到的最小的生成树的权值和，结束递归
			return;
		}
		
		if (edgeCount==(vertexSum-1)) {//当前生成树的边的数目达到顶点数目减1，输出这条生成树，结束递归
			graph[0][0]=edgeValueSum;//把该生成树的权值和赋给graph[0][0]
			stack.push(graph);
			if (edgeValueSum<=MIN) {
				MIN=edgeValueSum;
			}
			return;
		}
		
		if (index == edgeSum) {//如果当前的优先队列数组指针已经到达边界外并且不能输出，结束递归
			return;
		}
		
		//考虑当前边纳入生成树后是否会成环
		//先尝试将其纳入生成树
		graph[array[index].endpoint1][array[index].endpoint2]=array[index].value;
		graph[array[index].endpoint2][array[index].endpoint1]=array[index].value;
		//判断此时图中是否有环
		flag=0;//先重置flag，使其等于0
		hasLoop(graph);//执行判断
		if (flag==1) {//flag值变为1，说明此时图中有环
			//跳过当前边，将图还原到上一状态
			graph[array[index].endpoint1][array[index].endpoint2]=0;
			graph[array[index].endpoint2][array[index].endpoint1]=0;
			detectEdge(graph,edgeValueSum,index+1,edgeCount);//跳过当前边，探测下一条边
		}else {//说明此时图中无环
			if (array[index].equalEdgeCount!=1) {//如果是面对连续相等权值边，则要考虑不纳入生成树的情况
				//跳过当前边，将图还原到上一状态
				graph[array[index].endpoint1][array[index].endpoint2]=0;
				graph[array[index].endpoint2][array[index].endpoint1]=0;
				detectEdge(graph,edgeValueSum,index+1,edgeCount);//跳过当前边，继续探测下一条边
			}
			//面对不连续相等的边和连续相等的边的纳入生成树的情况，更新edgeVlaueSum当前生成树权值和与edgeCount当前生成树边的数目
			edgeValueSum+=array[index].value;
			edgeCount++;
			graph[array[index].endpoint1][array[index].endpoint2]=array[index].value;
			graph[array[index].endpoint2][array[index].endpoint1]=array[index].value;
			detectEdge(graph,edgeValueSum,index+1,edgeCount);//纳入当前边到生成树后，继续探测下一条边
		}
	}
	
	
	
	/*
	 * outputAllMST()函数
	 * 作用：将stack中所有权值和为MIN的graph按边输出
	 * */
	public void outputAllMST() {
		int number=1;
		System.out.println("第二部分生成树的权值和为："+MIN);
		while (!stack.isEmpty()) {
			if (stack.lastElement()[0][0]==MIN) {
				System.out.println("第"+number+"类最小生成树的第二部分边：（边集）");
				number++;
				for (int i=0;i<vertexSum;i++) {
					for (int j=i+1;j<vertexSum;j++) {
						if (stack.lastElement()[i][j]!=0) {
							System.out.print(array[refer[i][j]].preEndpoint1+" "+array[refer[i][j]].preEndpoint2+" value="+array[refer[i][j]].value);
							edge nextEdge=array[refer[i][j]].next;
							if (nextEdge!=null)
								System.out.print(" 存在等效可替换边：");
							while (nextEdge!=null) {
								System.out.print(nextEdge.preEndpoint1+" "+nextEdge.preEndpoint2+"   ");
								nextEdge=nextEdge.next;
							}
							System.out.println();
						}
					}
				}
				System.out.println();
			}
			stack.pop();
		}
		
	}
}
