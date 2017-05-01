package cn.allchin.mysql.ibd;

/**
 * 
 * 压缩索引页
 * 
 * @author renxing.zhang
 *
 */
public class CompressionIndexPage {
	byte[] fil_page_header; // 页面头数据不做压缩
	byte[] index_field_infomation; // 索引的列信息，在彭桂回复是可以根据此数据恢复出索引信息
	byte[] compressed_data; // 压缩数据，按照heap no 排序进入压缩流
							// ，压缩数据不包含系统列trx_id,roll_ptr或外部存储页指针
	byte[] modification_log; // 压缩页修改日志
	byte[] free_space; // 空闲空间

	byte[] external_ptr;// 存储在外部存储页的列记录指针数组，只存在聚集索引叶子节点，每个数组元素占用20字节
	byte[] trx_id_Roll_ptr;// 只存在于聚集索引叶子节点，数组元素和其heap no 一一对应
	byte[] node_ptr;// 只存在于索引非叶子节点，存储节点指针数组，每个元素占用4个字节
	byte[] dense_page_directory;// 分两部分，第一部分是有效记录，记录其在解压页中的偏移位置，n_owned和delete标记信息,按照键值顺序；
	// 第二部分是空闲记录，每个slot占用两个字符
}
