package cn.allchin.mysql.ibdata;

/**
 * ʵ�ʴ洢undo��¼��Page����ΪFIL_PAGE_UNDO_LOG
 * @author renxing.zhang
 *
 */
public class FilPageUndoLog {
	
}

class UndoEssayHeader{
	byte[] trx_undo_page_hdr=new byte[38];//page ͷ
	byte[] trx_undo_page_type=new byte[2];//��¼undo���ͣ���trx_undo_insert_����trx_undo_update
	byte[] trx_undo_page_start=new byte[2];//������д��������һ��undo log ��page �е�ƫ��λ��
	byte[] trx_undo_page_free=new byte[2];//ָ��ǰundo page �еĿ����õĿ��пռ����ʼƫ����
	byte[] trx_undo_page_node=new byte[12]; //����ڵ㣬�ύ���������ӵ�е�undoҳ��ӵ�history list��
	
}
