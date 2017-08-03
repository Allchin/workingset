package cn.allchin.mysql.ibd;

/**
 * page 3 用于管理数据文件中的segement，每个索引占用2个segment，分别用于管理叶子节点和非叶子节点。
 * 每个inode页可以存储FSP_SEG_INODES_PER_PAGE（默认为85）个记录。
 * 
 * @author renxing.zhang
 *
 */
public class InodePage {
	int fseg_inode_page_node;// 12 inode页的链表节点，记录前后inode page
								// 的位置，baseNdoe记录在头page
								// 的fsp_seg_inodes_full或者fsp_seg_inodes_free字段
	InodeEntry[] inodeEntry = new InodeEntry[85]; //
}

/**
 * 192bits inode记录
 * 
 * @author renxing.zhang
 *
 */
class InodeEntry {
	byte fseg_id;// 8 bit 该inode归属的segment id ,若值为0表示该slot未被使用
	byte fseg_not_full_n_used; // fseg_not_full 链表上未被使用的page 数量

	/**
	 * 16 bit ,完全没有被使用并分配给该segment 的extent 链表
	 */
	short fseg_free;//
	/**
	 * 16 bit 至少有一个page //
	 * 分配给当前segment的extent链表，全部用完时，转移到fseg_full上面，全部释放时，则归还给当前表空间fsp_free链表
	 */
	short fseg_not_full;//
	/**
	 *  16 分配给当前segment 且page 完全你使用完的extent链表
	 */
	short fseg_full; //
	byte fseg_magic_n; // 4 magic number
	byte[] fseg_frag_arr = new byte[32];// 4bit
										// 属于该segment的独立page，总是先从去全局分配独立的page，当填满32个数组项时，就在每次分配时都分配一个完整的extent,并在xdes_page中将其segment
										// id 设置为当前值

}