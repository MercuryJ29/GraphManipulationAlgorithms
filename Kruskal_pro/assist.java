package kruskal_pro;
import java.util.*;

/*
 * @author:Jiangzh
 * assist辅助工具类
 * 实现在改进的kruskal算法中所用到的方法
 * 
 * */

public class assist {
	public static Scanner scanner =new Scanner(System.in);
	public int[] visited;			//visited数组表示该顶点是否访问过：1访问过   /   0未访问过
	public int[] ancestor;			//ancestor[]表示记录当前结点的前驱结点
	public int vertexSum;			//顶点的总数
	public int edgeSum;				//边的总数
	public int flag=0;				//标志，初始为0，为1则说明有环
	public edge[] array;			//模拟优先队列的数组
	public int MIN=10000;			//当前最小生成树的权值和
	public static Stack<int [][]> stack=new Stack();	//stack栈用来保存可能最小的生成树，然后依次确定是否为最小生成树后考虑输出
	
	
	
	/*
	 * initAssist()函数
	 * 作用：初始化assist对象
	 * 输入：vertexSum表示顶点的总数，edgeSum表示边的总数
	 * 输出：void
	 * */
	public void initAssist(int vertexSum,int edgeSum) {
		this.vertexSum=vertexSum;
		this.edgeSum=edgeSum;
		array=new edge[edgeSum];
		ancestor=new int[vertexSum];
		visited=new int[vertexSum];
	}
	
	
	/*
	 * hasLoop()函数
	 * 作用：判断一个无向图中是否存在环，具体是借助DFS()
	 * 参数：graph表示所要处理的无向图
	 *  返回：void （表现为对flag的修改，flag默认为0，若有环则为1，无环则为0）
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
	 * 作用：构造优先队列数组
	 * 输入：表示Graph的二维数组
	 * 返回：void（优先队列数组edge[]）
	 * */
	public void generateArray(int[][] graph) {
		int index=0;//array数组的指针，最后index的值就是edgeSum-1
		//装入array数组
		for (int i=0;i<vertexSum;i++) {
			for (int j=i+1;j<vertexSum;j++) {
				if (graph[i][j]!=0) {
					edge e=new edge();
					e.endpoint1=i;
					e.endpoint2=j;
					e.value=graph[i][j];
					array[index]=e;
					index++;
				}
			}
		}
		//对array数组冒泡排序，并且初始化每条边的equalEdgeCount值
		for (int i=index-1;i>0;i--) {
			for (int j=0;j<i;j++) {
				if (array[j].value>array[j+1].value) {//前一条边的value值大于后一条边的value值，则交换它们
					edge e=new edge();
					e=array[j];
					array[j]=array[j+1];
					array[j+1]=e;
				}
			}
		}
		int temp=0;//当出现边权相等的边时，记录前一条边的equalEdgeCount值
		//对array数组当中有权值相等的边初始化它们的equalEdgeCount值
		for (int i=0;i<index-1;i++) {
			if (array[i].value==array[i+1].value) {//出现边权相等的边
				temp=array[i].equalEdgeCount;
				for (int j=0;j<temp+1;j++) {
					array[i+1-j].equalEdgeCount=temp+1;//从后往前将相等的边的equalEdgeCount值确定
				}
			}
		}
		/*
		for (int i=0;i<index;i++)
			System.out.println("第"+(i+1)+"条边：端点："+array[i].endpoint1+"  端点："+array[i].endpoint2
					+"   value:"+array[i].value+"   equalEdgeCount:"+array[i].equalEdgeCount);
		System.out.println();
		*/
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
			/*
			for (int i=0;i<vertexSum;i++) {
				for (int j=0;j<vertexSum;j++) {
					if (graph[i][j]!=0 && i<j) {
						System.out.println(i+" "+j);
					}
				}
			}
			*/
			if (edgeValueSum<=MIN) {
				MIN=edgeValueSum;
				//System.out.println("该生成树权值和为："+edgeValueSum);	
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
	 * 2021/4/13考虑改进，用stack保存边序号不是更好吗？之后改进算法的时候考虑一下！！！
	 * 2021/4/22其实实际上存储边，也只是在存储整个二维数组之后把它转化为了边序列而已
	 * */
	public void outputAllMST() {
		int number=1;
		System.out.println("最小生成树的权值和为："+MIN);
		while (!stack.isEmpty()) {
			if (stack.lastElement()[0][0]==MIN) {
				System.out.println("第"+number+"个最小生成树：（边集）");
				number++;
				for (int i=0;i<vertexSum;i++) {
					for (int j=i+1;j<vertexSum;j++) {
						if (stack.lastElement()[i][j]!=0) {
							System.out.println(i+" "+j);
						}
					}
				}
				System.out.println();
			}
			stack.pop();
		}
		
	}
	
}
