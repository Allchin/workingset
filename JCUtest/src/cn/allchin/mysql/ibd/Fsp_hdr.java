package cn.allchin.mysql.ibd;

/**
 * 数据文件的第一个Page类型为
 * FIL_PAGE_TYPE_FSP_HDR
 * 
 * http://chuansong.me/n/2711290
 * @author renxing.zhang
 *
 */
public class Fsp_hdr {
	 
	byte[] fsp_space_id=new byte[4]; //改文件对应的space id 
	byte[] fsp_not_used=new byte[4]; //保留字节，未使用
	byte[] fsp_size=new byte[4];   //当前表空间总的page个数,扩展文件时需要更新该值
	byte[] fsp_free_limit=new byte[4]; //当前尚未初始化的最小pageNo.从该page往后的都尚未加入到表空间的free list上
	
	byte[] fsp_space_flags=new byte[4]; //当前表空间的flag信息
	byte[] fsp_space_n_used=new byte[4];  //fsp_free_frag链表上已经被使用的page 数，用于快速计算该链表上可用空闲page表
	byte[] fsp_free=new byte[16]; //当一个extends 中所有的page 都未被使用时，放到该链表上，可以用于随后的分配
	byte[] fsp_free_frag=new byte[18]; //free_frag链表的base Node ，通常这样的extend中的page 可能归属于不同的segment,用于segment frag array page 的分配
	
	byte[] fsp_full_frag = new byte[16];//extent 中所有的page 都被使用掉时，会放到改立案表上，当有page 从该extent 释放时，则移回到free_frag链表
	byte[] fsp_seg_id = new byte[8]; //当前文件中最大segment id +1 用于端分配时的seg id 计数器
	byte[] fsp_seg_inodes_full = new byte[16]; //已经被完全用满的idnode page 链表
	byte[] fsp_seg_inodes_free = new byte[16]; //至少存在一个空闲的inode  entory 的indoe page 被放到该链表上
	
	
}

/**
 * 当前表空间的flag信息
 * @author renxing.zhang
 *
 */
class Fsp_space_flags{
	byte fsp_flags_pos_zip_ssize;  //压缩页的block size ,如果为0表示非压缩  
	byte fsp_flags_pos_atomic_blobs;//使用的是compressed 或者dynamic的行格式
	byte fsp_flags_pos_page_ssize; //page size 
	byte fsp_flags_pos_data_dir; // 如果该表空间显示的制定了data_dir 则设置该flag
	byte fsp_flags_pos_shared; //是否是共享的表空间。如5.7 引入的General Tablespace,可在一个表空间中创建多个表
	byte fsp_flags_pos_temporary;//是否是临时表空间 
	byte fsp_flags_pos_encryption;//是否是加密表空间 
	byte fsp_flags_pos_unused; //未使用的位
	
	
	
} 
