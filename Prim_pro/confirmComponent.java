package prim_pro;
import java.util.*;
/*
 *  @author:Jiangzh
 * 分量确定类
 * 确定原图中各个分量（基于prim算法），从而确定以分量为结点的新图的信息
 * */
public class confirmConponent {
	public static Scanner scanner = new Scanner(System.in);
	
	//原图信息
	public int[] visited; 			// visited数组表示该顶点是否访问过：1访问过 / 0未访问过
	public edge[] array; 			// array数组存储边的信息（按权值从小到大存储）
	public int[][] graph;			//graph数组存储原图的信息
	public int vertexSum; 			// 结点的总数
	public int edgeSum; 			// 边的总数
	
	//划分过程所用的参数
	public int partCount;			//图划分分量的数目
	public int flag=0;				//表示上一次find()后，这次是接着上一次的连通分量，还是新开始一个连通分量（用于find()函数）
	public int vertexCount=0;		//已访问顶点数目（用于find()函数）
	
	//合并和确定分量过程所用参数
	public int[][] preGraph;		//用作确定分量时所用的由visited等于1的边所构成的图
	public int[] ancestor;			//用于DFS()中确定祖宗结点
	
	//简化后的新图的参数
	public int newVertexSum;		//新图中顶点的总数
	public int newEdgeSum;			//新图中边的总数
	public edge[] newArray;			//新图中边信息
	public int[][] newGraph;		//新图
	
	
	
	/*
	 * testCase()函数
	 * 作用：测试用例的生成
	 * */
	public void testCase() {
		System.out.println("请输入顶点的数目：");
		vertexSum=scanner.nextInt();//输入vertexSum
		graph=new int[vertexSum][vertexSum];
		for (int i=0;i<vertexSum;i++) {
			for (int j=i;j<vertexSum;j++) {
				graph[i][j]=0;
				graph[j][i]=0;
			}
		}
		Random random=new Random();
		for (int i=0;i<vertexSum;i++) {
			int out=random.nextInt(4)+1;//out取值为1~4
			for (int j=0;j<out;j++) {
				int target=random.nextInt(vertexSum);//目标顶点的编号取值范围为0~vertexSum-1
				if (target!=i) {
					int temp=random.nextInt(4)+1;//边权值取值范围为1~4
					graph[i][target]=temp;
					graph[target][i]=temp;
				}
			}
		}
		System.out.println("图：");
		for (int i=0;i<vertexSum;i++) {
			for (int j=0;j<vertexSum;j++) {
				System.out.print(graph[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	
	
	/*
	 * input()函数
	 * 作用：手动输入测试用例
	 * */
	public void input() {
		// 输入vertexSum
		System.out.print("输入顶点的数目：");
		vertexSum = scanner.nextInt();
		// 初始化graph[][]
		graph = new int[vertexSum][vertexSum];
		for (int i = 0; i < vertexSum; i++)
			for (int j = 0; j < vertexSum; j++) {
				graph[i][j] = scanner.nextInt();
			}
	}
	
	
	
	/*
	 * init()函数 
	 * 完成划分分量类的初始化（原图信息初始化）
	 */
	public void init() {
		// 初始化visited[]
		visited = new int[vertexSum];
		for (int i = 0; i < vertexSum; i++)
			visited[i] = 0;
		//获得edgeSum的值
		for (int i = 0; i < vertexSum; i++)
			for (int j = i+1; j < vertexSum; j++) 
				if (graph[i][j] != 0)
					edgeSum++;
		// 初始化array[]
		array = new edge[edgeSum];
		generateArray();
		// 初始化partCount
		partCount = 0;
		// 初始化preGraph[][]
		preGraph=new int[vertexSum][vertexSum];
		// 初始化ancestor[]
		ancestor=new int[vertexSum];
		// 初始化newVertexSum
		newVertexSum=0;
		// 初始化newEdgeSum
		newEdgeSum=0;
	}
	
	
	
	/*
	 * generateArray()函数 
	 * 作用：构造优先队列数组
	 */
	public void generateArray() {
		int index = 0;// array数组的指针
		// 装入array数组
		for (int i = 0; i < vertexSum; i++) {
			for (int j = i + 1; j < vertexSum; j++) {
				if (graph[i][j] != 0) {
					edge e = new edge();
					e.getNewEdge(i, j, graph[i][j], 0, -1, -1, -1, -1, null);//visited值初始为0
					array[index] = e;
					index++;
				}
			}
		}
		// 对array数组冒泡排序
		for (int i = index - 1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (array[j].value > array[j + 1].value) {// 前一条边的value值大于后一条边的value值，则交换它们
					edge e = new edge();
					e = array[j];
					array[j] = array[j + 1];
					array[j + 1] = e;
				}
			}
		}
	}
	
	
	
	/*
	 * find()函数
	 * 作用：借助prim算法逐步划分图为几个部分
	 * */
	public void find() {
		visited[0]=1;//从顶点0开始进行划分
		partCount++;
		vertexCount++;
		while (vertexCount<=vertexSum) {//当已经确定的结点的数目小于结点总数时，继续调用
			flag=0;
			findEdge();//调用寻找最近结点的方法
			if (flag==1) {//说明要重新开始一个新的连通分量的确定
				for (int i=0;i<vertexSum;i++) 
					if (visited[i]==0) {//找到一个未确定分量的结点
						visited[i]=partCount;
						break;
					}
				vertexCount++;
			}
		}
		/*
		System.out.println("刚划分完，还未合并时各顶点的visited值：");
		for (int i=0;i<vertexSum;i++) {
			System.out.println(i+"---visited="+visited[i]);
		}
		System.out.println("刚划分完，还未合并时各边的状态值：");
		for (int i=0;i<edgeSum;i++)
			System.out.println(array[i].endpoint1+" "+array[i].endpoint2+"  状态:"+array[i].visited);
			*/
	}
	
	
	
	/*
	 * findEdge()函数 
	 * 作用：顺序遍历array[]，找到离当前连通分量最近的一个或一些顶点
	 */
	public void findEdge() {
		int equalCount = 0;// 在有多个可以纳入生成树的顶点时，统计它们的数目
		int equalValue = 10000;// 在有多个可以纳入生成树的顶点时，统计它们到当前连通分量的边的权值
		int equalStart = 0;// 在有多个可以纳入生成树的顶点时，统计它们的起始连续边序号

		for (int i = 0; i < edgeSum; i++) {
			if (array[i].value > equalValue)// 并且当前边的权值要小于等于equalValue
				break;
			if ((array[i].visited == 0 || array[i].visited == 2)
					&& (visited[array[i].endpoint1] == partCount || visited[array[i].endpoint2] == partCount)
					&& (visited[array[i].endpoint1] != visited[array[i].endpoint2])) {
				// 该边未使用过或者被确定为第二阶段的可选边
				//并且边的两个端点中有且只有一个等于当前分量编号
				equalValue = array[i].value;
				equalCount++;
				if (equalCount == 1)
					equalStart = i;
			}
		}

		if (equalCount == 1) {// 如果只有一个顶点是纳入的选择，将该顶点的visited[]中的值改为partCount表明该顶点已经纳入当前连通分量
			flag=1;//先将flag置为1，表明要开始一个新分量的扩张，但如果对方结点未纳入任何分量，则将flag改为0，表明继续当前分量的扩张
			if (visited[array[equalStart].endpoint1] == 0) {
				visited[array[equalStart].endpoint1] = partCount;
				vertexCount++;
				flag=0;
			} else if (visited[array[equalStart].endpoint2] == 0) {
				visited[array[equalStart].endpoint2] = partCount;
				vertexCount++;
				flag=0;
			}
			array[equalStart].visited = 1;//扩张情况2、3都需将对应边状态更改为1
			if (flag==1)
				partCount++;
		} else {// 如果有多个顶点可以纳入（或者无顶点可纳入），那么就将这些顶点与当前连通分量相连的边的visited的值置为2，表明为第二阶段图的边
			flag = 1;// 表明要新开始一个连通分量
			for (int i = equalStart; i < edgeSum; i++) {
				if (array[i].value > equalValue)// 并且当前边的权值要小于等于equalValue
					break;
				if (equalCount==0)
					break;
				if (array[i].visited == 0
						&& (visited[array[i].endpoint1] == partCount || visited[array[i].endpoint2] == partCount)
						&& (visited[array[i].endpoint1] != visited[array[i].endpoint2])) {// 该边未使用过&&并且边的两个端点中有且只有一个等于当前分量编号
					array[i].visited = 2;
				}
			}
			partCount++;// 此时需要将partCount的值加1，因为这个连通分量同时出现了多个选择，说明这一部分已经划分结束了
		}
	}
	
	
	
	/*
	 * merge()函数
	 * 作用：在第一阶段的基础之上合并能够合并的分量（目的是构造出newVisited[]）
	 * */
	public void merge() {
		//使用第一阶段的固定边，初始化preGraph[][]
		for (int i=0;i<vertexSum;i++)
			for (int j=0;j<vertexSum;j++)
				preGraph[i][j]=0;
		for (int i=0;i<edgeSum;i++) {
			if (array[i].visited==1) {//用array[]中visited值为1的边来初始化这个图
				preGraph[array[i].endpoint1][array[i].endpoint2]=array[i].value;
				preGraph[array[i].endpoint2][array[i].endpoint1]=array[i].value;
			}
		}
		
		for (int i=0;i<vertexSum;i++) {//将visited[]和ancestor[]重新置为初始化状态
			visited[i]=-1;
			ancestor[i]=-1;
		}
		partCount=0;//将当前分量编号重新置为0，即从0开始为分量分配编号
		for (int i=0;i<vertexSum;i++) {
			if (visited[i]==-1) {//发现有未访问的顶点
				DFS(i,partCount);
				partCount++;
			}
		}
		/*
		System.out.println("分量合并后各顶点的visited值：");
		for (int i=0;i<vertexSum;i++)
			System.out.println(i+"---visited:"+visited[i]);
			*/
	}
	
	
	
	/*
	 * DFS()函数
	 * 作用：采用DFS来逐步访问图的各个顶点，完成的分量的合并（目的是构造出newVisited[]）
	 * */
	public void DFS(int index,int partCount) {
		visited[index]=partCount;
		for (int i=0;i<vertexSum;i++) {
			if (i!=index&&preGraph[index][i]!=0&&visited[i]==-1&&i!=ancestor[index]) {//该顶点不是当前结点，并且index结点与i结点之间存在固定边，并且i结点并未访问，并且该结点不是当前结点的父结点
				visited[i]=partCount;
				ancestor[i]=index;
				DFS(i,partCount);
			}
		}
	}
	
	
	
	/*
	 * generateNewGraph()函数
	 * 作用：把第一阶段得到的用于第二阶段的可选边以及newVisited[]用来构造newEdge组成的newArray[]和newGraph[][]
	 */
	public void generateNewGraph() {
		for (int i = 0; i < vertexSum; i++) {
			if (newVertexSum < visited[i])
				newVertexSum = visited[i];
		}
		newGraph = new int[newVertexSum + 1][newVertexSum + 1];// 新图的初始化
		newVertexSum++;// 新图中顶点的数目
		// 初始化newArray[]
		int count = 0;
		for (int i = 0; i < edgeSum; i++) {
			if (array[i].visited == 2) {
				count++;
			}
		}
		newArray = new edge[count];
		for (int i = 0; i < count; i++) {
			newArray[i] = new edge();
			newArray[i].initNewEdge();// 默认初始化
		}

		// 接下来遍历旧的array[]，确定Visited=2的边，逐步加入newArray[]，
		int indexNewArray = 0;// newArray[]的指针
		for (int i = 0; i < edgeSum; i++) {
			if (array[i].visited == 2) {// 找到第二阶段的可选边
				int judge = 0;
				edge tempEdge = new edge();// 将要插入的边对象
				tempEdge.getNewEdge(visited[array[i].endpoint1], visited[array[i].endpoint2], array[i].value, 2,
						array[i].endpoint1, array[i].endpoint2, 1, 1, null);
				for (int j = 0; j < indexNewArray; j++) {// 每次加入的时候检查是否已有同样新端点的边已加入，有的话则将该边纳入到同新端点边的next中
					if ((newArray[j].endpoint1 == tempEdge.endpoint1 && newArray[j].endpoint2 == tempEdge.endpoint2)
							|| (newArray[j].endpoint1 == tempEdge.endpoint2
									&& newArray[j].endpoint2 == tempEdge.endpoint1)) {
						edge temp = newArray[j];// 用来表示next的边对象
						while (temp.next != null) {
							temp = temp.next;
						}
						temp.next = tempEdge;
						newArray[j].sameCount++;
						judge = 1;
						break;
					}
				}
				if (judge == 0) {// 说明不是为某一个边的next
					newArray[indexNewArray] = tempEdge;
					indexNewArray++;
				}
			}
		}
		newEdgeSum = indexNewArray;// 新图中边的数目
		// 完成新图的初始化
		newGraph = new int[newVertexSum][newVertexSum];
		for (int i = 0; i < newVertexSum; i++)
			for (int j = 0; j < newVertexSum; j++)
				newGraph[i][j] = 0;
		/*
		for (int i = 0; i < newEdgeSum; i++) {
			System.out.println("新边："+newArray[i].endpoint1+" "+newArray[i].endpoint2+" value="+newArray[i].value);
			newGraph[newArray[i].endpoint1][newArray[i].endpoint2] = newArray[i].value;
			newGraph[newArray[i].endpoint2][newArray[i].endpoint1] = newArray[i].value;
		}
		System.out.println("新图的信息：");
		for (int i=0;i<newVertexSum;i++) {
			for (int j=0;j<newVertexSum;j++)
				System.out.print(newGraph[i][j]+" ");
			System.out.println();
		}
		*/
	}
}
