package topological_dlx;
import java.util.*;
/*
 * @author:Jiangzh
 * 拓扑排序类
 * 借助舞蹈链结构进行拓扑排序
 * */

public class sort {
	public int n;					//结点数目
	public DancingLinks[] row;		//行结点数组
	public DancingLinks[] column;	//列节点数组
	public Stack stack;				//栈（用来回溯和保存序列信息）
	public int pre;					//刚刚出栈的结点编号
	public int sign;				//出栈标志元素（只有在出现出栈后的第一次检索需要用到pre，因此使用sign来确定每次出栈后的第一次检索
									//每次出栈时将其置为1，在一次入栈后就将其置为0
	public long startTime;			//开始时间
	public long endTime;			//结束时间
	public long time;				//用时
	
	/*
	 * Init()函数
	 *作用： 初始化n、row数组、column数组、pre
	 * */
	public void Init() {
		Scanner scanner=new Scanner(System.in);
		System.out.println("输入结点的数目");
		this.n=scanner.nextInt();
		this.row=new DancingLinks[n];
		this.column=new DancingLinks[n];
		for (int i=0;i<n;i++) {
			row[i]=new DancingLinks();
			row[i].row_value=i;
			column[i]=new DancingLinks();
			column[i].column_value=0;
		}
		this.stack=new Stack();
		this.sign=0;
	}
	
	/*
	 *inputDLX()函数
	 * 作用：先将图的信息输入到一个二维数组中，再根据这个二维数组来构造符合算法的图结构
	 * */
	public void inputDLX() {
		Init();
		Scanner scanner=new Scanner(System.in);
		int[][] map=new int[n][n];			//大小为n*n的二维数组
		System.out.println("输入图的信息：（用邻接矩阵的方式）");
		
		for (int i=0;i<n;i++) {//输入二维数组
			for (int j=0;j<n;j++) {
				map[i][j]=scanner.nextInt();
			}
		}
		
		for (int i=0;i<n;i++) {//初始化column数组的count值
			int sum=0;
			for (int j=0;j<n;j++)
				if (map[j][i]!=0)	sum++;
			column[i].column_value=sum;
		}
		
		//利用这个0-1二维数组来初始化舞蹈链结构的十字矩阵
		for (int i=0;i<n;i++) {
			for (int j=0;j<n;j++) {
				if (map[i][j]!=0) {
					DancingLinks temp_dlx=new DancingLinks();//temp_dlx用来构造当前结点
					DancingLinks column_next=new DancingLinks();//column_next用来找当前元素的up应该指向的结点
					DancingLinks row_next=new DancingLinks();//row_next用来找当前元素的left应该指向的结点
					//找up结点
					column_next=column[j];
					while (column_next.down!=null)
						column_next=column_next.down;
					//找left结点
					row_next=row[i];
					while (row_next.right!=null)
						row_next=row_next.right;
					
					temp_dlx.Init(i, j, column_next, null, row_next, null);
					column_next.down=temp_dlx;
					row_next.right=temp_dlx;
				}
			}
		}
		
		
	}
	
	/*
	 * checkColunmVertex()函数
	 * 作用：寻找无入度的结点函数
	 * 	返回列结点数组中第一个列count值为0的列
	 * 	返回-1则说明图中已无入度为0的结点
	 * */
	public int checkColumnVertex(int pre) {
		int i;
		if (sign==0)
			for (i=0;i<n;i++) {
				if (column[i].column_value==0) {
					return i;
				}
			}
		else
			for(i=pre+1;i<n;i++) {
				if (column[i].column_value==0) {
					return i;
				}
			}
		return -1;
	}
	
	/*
	 * hideRow()函数
	 * 作用：隐藏相应行函数
	 * 	根据row_number隐去对应的行，即修改该行结点的up、down的对应元素的down、up值   
	 * 	将该编号入栈   
	 * 	修改该行结点对应的列结点的column_value值即column_value--
	 * 	将该行的行标对应的列节点的column_value值设置为-1，表示已经将该行隐藏
	 * */
	public void hideRow(int row_number) {
		if (row[row_number].right!=null) {
			DancingLinks temp=row[row_number].right;
			while (temp!=null) {
				temp.up.down=temp.down;			//将temp结点的up指向的结点的dwon修改为指向temp结点的down指向的结点
				if (temp.down!=null) {
					temp.down.up=temp.up;			//将temp结点的down指向的结点的up修改为指向temp结点的up指向的结点
				}
				column[temp.column_value].column_value--;			//将对应列结点的column_value--
				temp=temp.right;			//temp向右移动一个结点
			}
		}
		column[row_number].column_value=-1;			//将对应行标的列结点的column_value值修改为-1，表示已遍历
		stack.push(row_number);			//将该元素入栈
		sign=0;			//将sign置为0，从而从0开始寻找入度为0的结点
	}
	
	/*
	 * backtrack()函数
	 * 作用：回溯函数
	 * 	完成出栈操作
	 * 	修改对应行结点的column_value值为0
	 * 	恢复对应行结点的up和down指向的结点的down和up指向
	 * */
	public void backtrack() {
		pre=stack.getTop();			//获取当前栈顶元素
		stack.pop();				//将其出栈
		sign=1;						//将sign置为1，从而从pre+1开始寻找入度为0的结点
		if (pre==-1) {
			endTime=System.nanoTime();//除去初始化过程程序结束时间
			time=endTime-startTime;
			System.out.println("除去初始化过程所花费的时间："+time/1000000+"ms"+"     精确表示为（"+time+"ns）");
			System.exit(0);//**********结束**********
		}
		if (row[pre].right!=null) {
			DancingLinks temp=row[pre].right;
			while (temp!=null) {
				temp.up.down=temp;			//将temp结点的up结点的down修改为指向temp
				if (temp.down!=null) {
					temp.down.up=temp;			//将temp结点的down结点的up修改为指向temp
				}
				column[temp.column_value].column_value++;			//将对应列结点的column_value值++
				temp=temp.right;
			}
		}
		column[pre].column_value=0;		//因为其刚出栈因而无结点指向它，将对应列结点的column_value值修改为0
	}
	
	/*
	 * execute()函数
	 * 作用：执行函数
	 * 	沿着树向下遍历，初始pre为-1，表示从0开始遍历
	 * 	获得now根据其值来选择操作，若为-1表示无法向下遍历了，考虑回溯（若遍历到叶节点则说明已找到一个拓扑序列）
	 * 	直至栈顶指针为-1表示整个图已经遍历完毕了
	 * */
	public void execute() {
		this.inputDLX();			//完成整个图的初始化
		startTime=System.nanoTime();//除去初始化过程程序开始时间
		pre=-1;						//从编号为0的结点开始遍历该层
		while(stack.top!=-1) {			//当top==-1时表示整个图已经遍历完毕，因为起始结点层已经遍历完了
			int now=checkColumnVertex(pre);			//获取当前目标元素
			if (now==-1) {			//说明此时已无度为0的结点，可能是已经找到一个拓扑序列，也可能是遍历未完成但是无法继续
				if (stack.top==n) 
					stack.showNumbers();	//输出拓扑序列
				backtrack();			//回溯（top--）
			}else {
				hideRow(now);			//隐藏相应的行结点（top++）
			}
		}
	}
	
	
	/*
	 * testConstruction()函数
	 * 作用：测试构造函数
	 * 	测试整个使用舞蹈链的十字链表结构是否正确
	 * */
	public void testConstruction() {
		System.out.println("**********测试构造**********");
		System.out.println("column[]信息：");
		for (int i=0;i<n;i++) {
			System.out.println("column[i].column_value:"+column[i].column_value);
		}
		System.out.println("每个结点的情况：");
		for (int i=0;i<n;i++) {
			DancingLinks temp=new DancingLinks();
			temp=row[i].right;
			while (temp!=null) {
				System.out.print("   行号："+temp.row_value+"   列号"+temp.column_value+"   其up指向元素是"+temp.up.column_value+"   其left指向元素是"+temp.left.row_value);
				if (temp.right!=null)
					System.out.print("   其right指向元素为"+temp.right.row_value+"   "+temp.right.column_value);
				else
					System.out.print("   其右边没有元素了");
				
				if (temp.down!=null)
					System.out.print("   其down指向元素为"+temp.down.row_value+"   "+temp.down.column_value);
				else
					System.out.print("   其下边没有元素了");
				temp=temp.right;
				System.out.println();
			}
			System.out.println();
		}
	}
}
