package cn.allchin.mysql.ibd;

/**
 * FIL_PAGE_TYPE_XDES
 * 
 * @author renxing.zhang
 *
 */
public class XdesPage {
	/**
	 * 最多256个
	 */
	XdesEntry[] xdesEntry=new XdesEntry[256];
}

/**
 * 40字节,描述64个page ,就是一个extend
 * @author renxing.zhang
 *
 */
class XdesEntry {
	byte[] xdes_id = new byte[8]; // 如果该extend归属于某个segment的话，则记录其id
	byte[] xdes_flst_node = new byte[12]; // FLST_NODE_SIZE 维持extent 链表的双向指针节点

	byte[] xdes_state = new byte[4]; // 该extend的状态信息，包括xdes_free,xdes_free_frag,xdes_full_frag,xdes_fseg
	byte[] xdes_bitmap = new byte[16]; // 总共16*8 =128 个bit
										// ,用2个bit表示extend中的一个page，一个bit表示该page是否是空闲的(xdes_free_bit)
										// ,另一个保留位，尚未使用(xdes_clean_bit)

}

/**
 * extend的状态信息
 * @author renxing.zhang
 *
 */
class XdesState{
	byte xdes_free=1; //存在于free链表上
	byte xdes_free_frag=2; //存在于free_frag链表上
	byte xdes_full_frag=3; //存在于full_frag链表上
	byte xdes_fseg=4; //该extend归属于id为xdes_id记录的值的segment.
	 
}
