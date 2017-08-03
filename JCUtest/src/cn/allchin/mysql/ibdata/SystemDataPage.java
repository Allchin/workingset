package cn.allchin.mysql.ibdata;

import cn.allchin.mysql.ibd.Fsp_hdr;
import cn.allchin.mysql.ibd.IbufBitmapPage;
import cn.allchin.mysql.ibd.InodePage;

/**
 * ϵͳ����ҳ �����зǶ���������ҳͳ��Ϊϵͳ����ҳ����Ҫ�洢��ibdata��
 * 
 * @author renxing.zhang
 *
 */
public class SystemDataPage {
	// ibdata��ǰ����page����ͨ���û���ռ�һ������������ά���͹����ļ�ҳ��
	Fsp_hdr fil_page_type_fsp_hdr;
	IbufBitmapPage fil_page_ibuf_bitmap;
	InodePage fil_page_inode;

	//
	EssayHeaderPage fil_page_type_sys;
	
	/**
	 * ��0�Żع���ͷҳ������ibdata�ĵ�7��page�С�
	 * fil_page_type_sys
	 * @author renxing.zhang
	 *
	 */
	FilPageTypeSys  fil_page_type_sys2;

}

/**
 * Ibdata�ĵ�4��page��Change Buffer��essay-header page������ΪFIL_PAGE_TYPE_SYS����Ҫ���ڶ�ibuf
 * btree��Page����
 * 
 * FIL_PAGE_TYPE_SYS
 * 
 * @author renxing.zhang
 *
 */
class EssayHeaderPage {

}

/**
 * ���ڴ洢change buffer�ĸ�page��change
 * bufferĿǰ�洢��Ibdata�У��䱾����Ҳ��һ��btree��rootҳΪ�̶�page��
 * Ҳ����Ibdata�ĵ�5��page��
 * FSP_IBUF_TREE_ROOT_PAGE_NO
 * @author renxing.zhang
 *
 */
class IbufRootPage {
	
}

/**
 * ��6��page
 * FSP_TRX_SYS_PAGE_NO/FSP_FIRST_RSEG_PAGE_NO
 * @author renxing.zhang
 *
 */

class FilPageTypeTrxRsys{
	
}
class FilPageTypeSys{
	byte[] trx_sys =new byte[38];//ÿ������ҳ���ᱣ�����ļ�ͷ�ֶ�
	//TODO 
	byte[] trx_rseg_max_size=new byte[4];//�ع�������ʹ�õ����page������ǰֵΪulint_max
	byte[] trx_rseg_history_size=new byte[4] ;//��history list�ϵ�undo page ������Щpage ��Ҫ��purge�߳̽�������ͻ���
	byte[] trx_rseg_history=new byte[16];//history list ��base node 
	byte[] trx_rseg_fseg_header=new byte[10]; //ָ��ǰ����ǰ�ع��ε�inode entry 
	byte[] trx_rseg_undo_slots=new byte[1024*4]; //undo slot ���飬��1024��slot ,ֵΪfil_null, ��ʾδ��ռ�ã����ż�¼ռ��slot�ĵ�һ��undo page 
	
}


 

class FspDictHdrPageNo{
	
	
}
 
class DictHdrPage{
	byte[] dict_hdr=new byte[38];//page ͷ
	byte[] dict_hdr_row_id=new byte[8];//��������Ƶ�row id ,����,���ڸ�δ���������ı���λ�����ص�������ֵ������
	byte[] dict_hdr_table_id=new byte[8]; //��ǰϵͳ������������id��ÿ����һ���±�,������һ��Ψһ��table id��Ȼ�����
	byte[] dict_hdr_index_id=new byte[8];//���ڷ�������id
	byte[] dict_hdr_max_space_id=new byte[4];//���ڷ���space id 
	byte[] dict_hdr_mix_id_low=new byte[4]; //
	byte[] dict_hdr_tables=new byte[4];//sys_tableϵͳ��ľۼ�����root page 
	
	byte[] dict_hdr_table_ids=new byte[4];//sys_table_ids������root page 
	byte[] dict_hdr_columns=new byte[4];//sys_columnsϵͳ��ľۼ����� root page 
	byte[] dict_hdr_indexes=new byte[4];//sys_indexesϵͳ��ľۼ�����root page 
	byte[] dict_hdr_fields=new byte[4];//sys_fieldsϵͳ��ľۼ�����root page 
	
	
}
 
