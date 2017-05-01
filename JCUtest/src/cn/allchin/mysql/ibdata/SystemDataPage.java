package cn.allchin.mysql.ibdata;

import cn.allchin.mysql.ibd.Fsp_hdr;
import cn.allchin.mysql.ibd.IbufBitmapPage;
import cn.allchin.mysql.ibd.InodePage;

/**
 * 系统数据页 将所有非独立的数据页统称为系统数据页，主要存储在ibdata中
 * 
 * @author renxing.zhang
 *
 */
public class SystemDataPage {
	// ibdata的前三个page和普通的用户表空间一样，都是用于维护和管理文件页。
	Fsp_hdr fil_page_type_fsp_hdr;
	IbufBitmapPage fil_page_ibuf_bitmap;
	InodePage fil_page_inode;

	//
	EssayHeaderPage fil_page_type_sys;
	
	/**
	 * 第0号回滚段头页总是在ibdata的第7个page中。
	 * fil_page_type_sys
	 * @author renxing.zhang
	 *
	 */
	FilPageTypeSys  fil_page_type_sys2;

}

/**
 * Ibdata的第4个page是Change Buffer的essay-header page，类型为FIL_PAGE_TYPE_SYS，主要用于对ibuf
 * btree的Page管理。
 * 
 * FIL_PAGE_TYPE_SYS
 * 
 * @author renxing.zhang
 *
 */
class EssayHeaderPage {

}

/**
 * 用于存储change buffer的根page，change
 * buffer目前存储于Ibdata中，其本质上也是一颗btree，root页为固定page，
 * 也就是Ibdata的第5个page。
 * FSP_IBUF_TREE_ROOT_PAGE_NO
 * @author renxing.zhang
 *
 */
class IbufRootPage {
	
}

/**
 * 第6个page
 * FSP_TRX_SYS_PAGE_NO/FSP_FIRST_RSEG_PAGE_NO
 * @author renxing.zhang
 *
 */

class FilPageTypeTrxRsys{
	
}
class FilPageTypeSys{
	byte[] trx_sys =new byte[38];//每个数据页都会保留的文件头字段
	//TODO 
	byte[] trx_rseg_max_size=new byte[4];//回滚段允许使用的最大page数，当前值为ulint_max
	byte[] trx_rseg_history_size=new byte[4] ;//在history list上的undo page 数，这些page 需要由purge线程进行清理和回收
	byte[] trx_rseg_history=new byte[16];//history list 的base node 
	byte[] trx_rseg_fseg_header=new byte[10]; //指向当前管理当前回滚段的inode entry 
	byte[] trx_rseg_undo_slots=new byte[1024*4]; //undo slot 数组，用1024个slot ,值为fil_null, 表示未被占用，飞着记录占用slot的第一个undo page 
	
}


 

class FspDictHdrPageNo{
	
	
}
 
class DictHdrPage{
	byte[] dict_hdr=new byte[38];//page 头
	byte[] dict_hdr_row_id=new byte[8];//最近被复制的row id ,递增,用于给未定义主键的表，座位其隐藏的主键键值来构建
	byte[] dict_hdr_table_id=new byte[8]; //当前系统分配的最大事务id，每创建一个新表,都赋予一个唯一的table id，然后递增
	byte[] dict_hdr_index_id=new byte[8];//用于分配索引id
	byte[] dict_hdr_max_space_id=new byte[4];//用于分配space id 
	byte[] dict_hdr_mix_id_low=new byte[4]; //
	byte[] dict_hdr_tables=new byte[4];//sys_table系统表的聚集索引root page 
	
	byte[] dict_hdr_table_ids=new byte[4];//sys_table_ids索引的root page 
	byte[] dict_hdr_columns=new byte[4];//sys_columns系统表的聚集索引 root page 
	byte[] dict_hdr_indexes=new byte[4];//sys_indexes系统表的聚集索引root page 
	byte[] dict_hdr_fields=new byte[4];//sys_fields系统表的聚集索引root page 
	
	
}
 
