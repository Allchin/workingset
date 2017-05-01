package cn.allchin.mysql.ibd;

/**
 * 索引页
 * @author renxing.zhang
 *
 */
public class FilPageIndex {

}

/**
 * 38个字节来描述头信息（FIL_PAGE_DATA, or PAGE_HEADER）
 * 
 * @author renxing.zhang
 *
 */
class PageHeader {
	byte[] fil_page_space_or_chksum = new byte[4]; // mysql 4.0之前存储space id
													// ,之后的版本用于存储checksum
	byte[] fil_page_offset = new byte[4]; // 当前页的page no
	byte[] fil_page_prev = new byte[4];// 通常用于维护btree同一个level的双向链表，指向链表的前一个page,没有的话则值为fil_null
	byte[] fil_page_next = new byte[4];// 记录链表的下个page的page no
	byte[] fil_page_lsn = new byte[8];// 最近一次修该 page的lsn
	byte[] fil_page_type = new byte[2];// page 类型
	/**
	 * 只用于系统空间的第一个page,记录在正常shutdown时安全checnkpoint到的点，对于用户表空间，这个字段通常是空闲的，但在507里，
	 * fil_page_compressed类型的数据也则麟游用途。
	 */
	byte[] fil_page_file_flush_lsn = new byte[8];
	byte[] fil_page_space_id = new byte[4]; // 存储page所在的page id
}

/**
 * 紧随FIL_PAGE_DATA之后的是索引信息，这部分信息是索引页独有的。
 * 
 * @author renxing.zhang
 *
 */
class IndexHeader {
	byte[] page_n_dir_slots = new byte[2];// page directory中的slot个数
	byte[] page_heap_top = new byte[2];// 指向当前page内已经使用的空间的末尾偏移位置,即free space
										// 的开始位置
	/** 
	 *   page 内所有记录个数，包含用户记录，系统记录以及标记删除的记录，同时
	 *   当第一个bit设置为1时，表示这个page内是以compact格式存储的
	 */
	byte[] page_n_heap = new byte[2];
	
	byte[] page_free = new byte[2]; // 指向标记删除的记录链表的第一个记录
	/**
	 *  被删除的记录链表上占用的总的字节数,属于可以回收的垃圾碎片空间
	 */
	byte[] page_garbage = new byte[2]; 

	byte[] page_last_insert = new byte[2]; // 指向最近一次插入的记录偏移量，主要用于优化顺序插入操作
	byte[] page_direction = new byte[2]; // 用于只是当前记录的插入顺序以及是否正在进行顺序插入，每次插入时，page_last_insert会和当前记录进行比较，以确认插入方向，根据此进行插入优化
	/**
	 * 一个方向连续插入记录的数量
	 */
	byte[] page_n_direction=new byte[2];

	/**
	 * page 上有效的未被标记删除的用户记录个数
	 */
	byte[] page_n_recs=new byte[2]; 
	
	
	byte[] page_n_max_trx_id=new byte[8];//最近一次修改该page记录的事务id,主要用于辅助判断二级索引记录的可见性
	byte[] page_level=new byte[2];//该page所在的btree level ,根节点的level最大，叶子节点的level 为0
	byte[] page_index_id=new byte[8];//该page 归属的索引id
	
}

/**
 * 随后20个字节描述段信息，仅在Btree的root Page中被设置，其他Page都是未使用的。
 * 
 * @author renxing.zhang
 *
 */
class SegmentInfo {
	byte[] page_btr_seg_leaf = new byte[10]; // leaf segment 在inode page中的位置
	byte[] page_btr_seg_top = new byte[10];// non leaf segment 在inode page中的位置
}

/**
 * 10字节 我们可以找到对应segment在inode page中的描述项，进而可以操作整个segment。
 * 
 * @author renxing.zhang
 *
 */
class Inode {
	byte[] fseg_hdr_space = new byte[4];// 描述该segemnt的inode page 所在的space id
	byte[] fseg_hdr_page_no = new byte[4];// 描述该segment 的 inode page 的page no
	byte[] fseg_hdr_offset = new byte[2];// inode page内的页内偏移量

}

// 系统记录
/**
 * 两种格式的主要差异在于不同行存储模式下，单个记录的描述信息不同
 * 
 * @author renxing.zhang
 * 
 *         infimum记录的固定heap no为0，supremum记录的固定Heap no 为1
 * 
 *         page上最小的用户记录前节点总是指向infimum page上最大的记录后节点总是指向supremum记录。
 *
 */
class SystemRecord {
}

/**
 * infimum_supremum_redundant
 * 
 * @author renxing.zhang
 *
 */
class Redundant extends SystemRecord {
	byte[] rec_n_old_extra_bytes_1 = new byte[7]; // 固定值，见infimun_supremum_redundent的注释
	byte[] page_old_infimum = new byte[8]; // infimun\0
	byte[] rec_n_old_extra_byte_1 = new byte[7];// 固定值，见infomun_supremum_redundent的注释
	byte[] page_old_supremum = new byte[9]; // "supremum\0"
}

/**
 * infimum_supremum_compact
 * 
 * @author renxing.zhang
 * 
 * 
 *
 */
class CompactPage extends SystemRecord {
	byte[] rec_n_new_extra_bytes = new byte[5]; // 固定值，见infimum_supremum_compact的注释
	byte[] page_new_infimum = new byte[8]; // infimum\0
	byte[] rec_n_new_extra_bytes2 = new byte[5]; // 固定值，见inimum_supremum_copact的注释
	byte[] page_new_supremum = new byte[8];// "supremum"这里不带0

}

/**
 * 用户记录
 * 
 * @author renxing.zhang 系统记录之后就是真正的用户记录了，heap no
 *         从2（PAGE_HEAP_NO_USER_LOW）开始算起。注意Heap no仅代表物理存储顺序，不代表键值顺序。
 * 
 *         本文的讨论中我们默认使用Compact格式 在文件rem/rem0rec.cc的头部注释描述了记录的物理结构。
 * 
 *         每个记录都存在rec essay-header，描述如下（参阅文件include/rem0rec.ic）
 */
class UserRecord {
	//rec essay-header 部分
	byte[] 变长列长数组;
	byte[] sql_null_flag;// 标示值为null的列的bitmap,每个位标示一个列，bitmap的长度取决于所以上可为null的列的个数,dict_index_t:n_nullable,这两个数组的解析可以参阅函数rec_init_offsets
	byte[] rec_n_new_extra_bytes=new byte[5];//5个字节,描述记录的额外信息
	
	/**
	 * 4 位,目前只使用了两个bit ,一个用于标示该记录是否被标记删除 rec_info_deleted_flag
	 * 另外一个bit rec_info_min_rec_flag 如果被设置，标示这个记录是当前level最左边的page 的第一个用户记录.
	 */
	byte rec_new_info_bits ;
	/**
	 * 4bit,当该值为非0 ，标示当前记录占用page directory里面一个slot,并和前一个slot之间存在这么多个记录
	 * 
	 */
	byte rec_new_n_owned;
	
	/**
	 * 13bit 该记录的heap no 
	 */
	int rec_new_heap_no;
	
	/**
	 * 3 bit 记录的类型，包括四种
	 * rec_status_ordinary 叶子节点记录;
	 * rec_status_node_ptr非叶子节点记录;
	 * rec_status_infimum infimum系统记录;
	 * rec_status_supremum supremum系统记录
	 * 
	 */
	byte rec_new_status;
	
	/**
	 * 指向按照简直排序的page内下一条记录数据起点,
	 * 这里存储的是和当前记录的相对位置偏移量
	 * 函数rec_set_next_offs_new 
	 */
	byte rec_next;
	
	
	//TODO 
	/**
	 * 在记录头信息之后的数据视具体情况有所不同：
	 * 对于聚集索引记录，数据包含了事务id，回滚段指针；
对于二级索引记录，数据包含了二级索引键值以及聚集索引键值。如果二级索引键和聚集索引有重合，则只保留一份重合的，例如pk (col1, col2)，sec key(col2, col3)，在二级索引记录中就只包含(col2, col3, col1);
对于非叶子节点页的记录，聚集索引上包含了其子节点的最小记录键值及对应的page no；二级索引上有所不同，除了二级索引键值外，还包含了聚集索引键值，再加上page no三部分构成。
	 * 
	 * 
	 * */ 
	
}

class FreeSpace{
	
	
}

/**
 * 为了加快页内的数据查找，会按照记录的顺序，每隔4~8个数量（PAGE_DIR_SLOT_MIN_N_OWNED ~ PAGE_DIR_SLOT_MAX_N_OWNED）的用户记录，就分配一个slot （每个slot占用2个字节，PAGE_DIR_SLOT_SIZE），存储记录的页内偏移量，可以理解为在页内构建的一个很小的索引(sparse index)来辅助二分查找。
 * 
 * Page Directory的slot分配是从Page末尾（倒数第八个字节开始）开始逆序分配的。在查询记录时。先根据page directory 确定记录所在的范围，然后在据此进行线性查询。
 * 
 * 
 * @author renxing.zhang
 *
 */
class PageDirectory{
	Slot[] slots=new Slot[999];
	
	/**
	 * 每个slot占用2个字节
	 * @author renxing.zhang
	 *
	 */
	class Slot{
		byte slot1;
		byte slot2;	 
	}
}


/**
 * 在每个文件页的末尾保留了8个字节（FIL_PAGE_DATA_END or FIL_PAGE_END_LSN_OLD_CHKSUM），
 * @author renxing.zhang
 * 
 *
 */
class FilTrailer{
	
	/**
	 * 这个值需要和page头部记录的checksum相匹配，否则认为page损坏(buf_page_is_corrupted)
	 */
	byte[] checksum=new byte[4];
	 
	
}
