package topological_dlx;
import java.util.*;
/*
 * @author:Jiangzh
 * 栈类
 * 设置了一些在执行算法过程中所需要的一些功能
 * */

public class Stack {
		public int top = 0;			//栈顶指针，top指向栈顶元素的下一个位置
		public int[] vertex = new int[1000];			//存储结点
		public int sum=0;			//记录已经输出了多少个拓扑序列
		
		//初始化
		public void init() {
			for (int i=0;i<10;i++) {
				vertex[i]=-2;
			}
		}
		
		//返回栈顶元素
		public int getTop() {
			if (top>0)
				return this.vertex[top-1];
			else
				return -1;			//此时栈为空，返回-1
		}
		
		//入栈
		public void push(int num) {
			if (top==vertex.length) 
				System.out.println("栈溢出！");
			else {
				vertex[top] = num;
				top++;
			}
		}
		
		//出栈
		public void pop() {
			top--;
			if (top==-1) {
				System.out.println("@@@top此时为-1，遍历应结束");
				end();
			}
		}
		
		//输出栈中所有元素
		public void showNumbers() {
			sum++;
			System.out.print("第"+sum+"个拓扑序列为：");
			for (int i=0;i<top;i++) {
				System.out.print(vertex[i]+" ");
			}
			System.out.println();
		}
		
		//结束
		public void end() {
			System.out.println("拓扑序列寻找完毕，共找到"+sum+"个拓扑序列");
		}
}
