package prim_pro;
import java.util.*;
/*
 *  @author:Jiangzh
 * 边类
 * 算法中所要使用的边
 * */
public class edge {
	public int endpoint1;			//当前边端点1
	public int endpoint2;			//当前边端点2
	public int value;				//边权值
	public int visited;				//该边的划分情况
	public int preEndpoint1;		//对应的先前的边的端点1
	public int preEndpoint2;		//对应的先前的边的端点2
	public int sameCount;			//同类边的数量（用于输出时考虑两个分量之间的多条等权值边）
	public int equalEdgeCount;		//相等权值边的数量（使用kruskal_pro得到生成树时的全图中各顶点的等权值边）
	public edge next;			//下一个同类边
	
	
	
	/*
	 * initNewEdge()函数
	 * 作用：赋予默认值
	 * */
	public void initNewEdge() {
		endpoint1=-1;
		endpoint2=-1;
		value=-1;
		visited=-1;
		preEndpoint1=-1;
		preEndpoint2=-1;
		sameCount=-1;
		equalEdgeCount=-1;
		next=null;
	}
	
	
	
	/*
	 * getNewEdge()函数
	 * 作用：根据参数赋值一条边对象
	 * */
	public void getNewEdge(int endpoint1,int endpoint2,
			int value,int visited,int preEndpoint1,int preEndpoint2,
			int sameCount,int equalEdgeCount,edge next) {
		this.endpoint1=endpoint1;
		this.endpoint2=endpoint2;
		this.value=value;
		this.visited=visited;
		this.preEndpoint1=preEndpoint1;
		this.preEndpoint2=preEndpoint2;
		this.sameCount=sameCount;
		this.equalEdgeCount=equalEdgeCount;
		this.next=next;
	}
}
