package topological_dlx;
import java.util.*;
/*
 * @author:Jiangzh
 * 舞蹈链结点类
 * 由这些舞蹈链结点组成图结构
 * */

public class DancingLinks {			//舞蹈链结构
		public int row_value;			//行值
		public int column_value;		//列值
	
		public DancingLinks up;
		public DancingLinks down;
		public DancingLinks left;
		public DancingLinks right;
		
		/*
		 * Init()函数
		 * 作用：初始化一个舞蹈链结点
		 * */
		public void Init(int row_value,int column_value,DancingLinks up,DancingLinks down,DancingLinks left,DancingLinks right) {			//初始化单个舞蹈链结点
			this.row_value=row_value;
			this.column_value=column_value;
			this.up=up;
			this.down=down;
			this.left=left;
			this.right=right;
		}
}
