package cn.allchin.mysql.ibdata;

/**
 * 实际存储undo记录的Page类型为FIL_PAGE_UNDO_LOG
 * @author renxing.zhang
 *
 */
public class FilPageUndoLog {
	
}

class UndoEssayHeader{
	byte[] trx_undo_page_hdr=new byte[38];//page 头
	byte[] trx_undo_page_type=new byte[2];//记录undo类型，是trx_undo_insert_还是trx_undo_update
	byte[] trx_undo_page_start=new byte[2];//事务所写入的最近的一个undo log 在page 中的偏移位置
	byte[] trx_undo_page_free=new byte[2];//指向当前undo page 中的可以用的空闲空间的起始偏移量
	byte[] trx_undo_page_node=new byte[12]; //链表节点，提交后的事务，其拥有的undo页会加到history list上
	
}
