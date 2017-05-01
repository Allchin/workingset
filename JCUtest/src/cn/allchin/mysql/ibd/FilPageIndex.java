package cn.allchin.mysql.ibd;

/**
 * ����ҳ
 * @author renxing.zhang
 *
 */
public class FilPageIndex {

}

/**
 * 38���ֽ�������ͷ��Ϣ��FIL_PAGE_DATA, or PAGE_HEADER��
 * 
 * @author renxing.zhang
 *
 */
class PageHeader {
	byte[] fil_page_space_or_chksum = new byte[4]; // mysql 4.0֮ǰ�洢space id
													// ,֮��İ汾���ڴ洢checksum
	byte[] fil_page_offset = new byte[4]; // ��ǰҳ��page no
	byte[] fil_page_prev = new byte[4];// ͨ������ά��btreeͬһ��level��˫������ָ�������ǰһ��page,û�еĻ���ֵΪfil_null
	byte[] fil_page_next = new byte[4];// ��¼������¸�page��page no
	byte[] fil_page_lsn = new byte[8];// ���һ���޸� page��lsn
	byte[] fil_page_type = new byte[2];// page ����
	/**
	 * ֻ����ϵͳ�ռ�ĵ�һ��page,��¼������shutdownʱ��ȫchecnkpoint���ĵ㣬�����û���ռ䣬����ֶ�ͨ���ǿ��еģ�����507�
	 * fil_page_compressed���͵�����Ҳ��������;��
	 */
	byte[] fil_page_file_flush_lsn = new byte[8];
	byte[] fil_page_space_id = new byte[4]; // �洢page���ڵ�page id
}

/**
 * ����FIL_PAGE_DATA֮�����������Ϣ���ⲿ����Ϣ������ҳ���еġ�
 * 
 * @author renxing.zhang
 *
 */
class IndexHeader {
	byte[] page_n_dir_slots = new byte[2];// page directory�е�slot����
	byte[] page_heap_top = new byte[2];// ָ��ǰpage���Ѿ�ʹ�õĿռ��ĩβƫ��λ��,��free space
										// �Ŀ�ʼλ��
	/** 
	 *   page �����м�¼�����������û���¼��ϵͳ��¼�Լ����ɾ���ļ�¼��ͬʱ
	 *   ����һ��bit����Ϊ1ʱ����ʾ���page������compact��ʽ�洢��
	 */
	byte[] page_n_heap = new byte[2];
	
	byte[] page_free = new byte[2]; // ָ����ɾ���ļ�¼����ĵ�һ����¼
	/**
	 *  ��ɾ���ļ�¼������ռ�õ��ܵ��ֽ���,���ڿ��Ի��յ�������Ƭ�ռ�
	 */
	byte[] page_garbage = new byte[2]; 

	byte[] page_last_insert = new byte[2]; // ָ�����һ�β���ļ�¼ƫ��������Ҫ�����Ż�˳��������
	byte[] page_direction = new byte[2]; // ����ֻ�ǵ�ǰ��¼�Ĳ���˳���Լ��Ƿ����ڽ���˳����룬ÿ�β���ʱ��page_last_insert��͵�ǰ��¼���бȽϣ���ȷ�ϲ��뷽�򣬸��ݴ˽��в����Ż�
	/**
	 * һ���������������¼������
	 */
	byte[] page_n_direction=new byte[2];

	/**
	 * page ����Ч��δ�����ɾ�����û���¼����
	 */
	byte[] page_n_recs=new byte[2]; 
	
	
	byte[] page_n_max_trx_id=new byte[8];//���һ���޸ĸ�page��¼������id,��Ҫ���ڸ����ж϶���������¼�Ŀɼ���
	byte[] page_level=new byte[2];//��page���ڵ�btree level ,���ڵ��level���Ҷ�ӽڵ��level Ϊ0
	byte[] page_index_id=new byte[8];//��page ����������id
	
}

/**
 * ���20���ֽ���������Ϣ������Btree��root Page�б����ã�����Page����δʹ�õġ�
 * 
 * @author renxing.zhang
 *
 */
class SegmentInfo {
	byte[] page_btr_seg_leaf = new byte[10]; // leaf segment ��inode page�е�λ��
	byte[] page_btr_seg_top = new byte[10];// non leaf segment ��inode page�е�λ��
}

/**
 * 10�ֽ� ���ǿ����ҵ���Ӧsegment��inode page�е�������������Բ�������segment��
 * 
 * @author renxing.zhang
 *
 */
class Inode {
	byte[] fseg_hdr_space = new byte[4];// ������segemnt��inode page ���ڵ�space id
	byte[] fseg_hdr_page_no = new byte[4];// ������segment �� inode page ��page no
	byte[] fseg_hdr_offset = new byte[2];// inode page�ڵ�ҳ��ƫ����

}

// ϵͳ��¼
/**
 * ���ָ�ʽ����Ҫ�������ڲ�ͬ�д洢ģʽ�£�������¼��������Ϣ��ͬ
 * 
 * @author renxing.zhang
 * 
 *         infimum��¼�Ĺ̶�heap noΪ0��supremum��¼�Ĺ̶�Heap no Ϊ1
 * 
 *         page����С���û���¼ǰ�ڵ�����ָ��infimum page�����ļ�¼��ڵ�����ָ��supremum��¼��
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
	byte[] rec_n_old_extra_bytes_1 = new byte[7]; // �̶�ֵ����infimun_supremum_redundent��ע��
	byte[] page_old_infimum = new byte[8]; // infimun\0
	byte[] rec_n_old_extra_byte_1 = new byte[7];// �̶�ֵ����infomun_supremum_redundent��ע��
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
	byte[] rec_n_new_extra_bytes = new byte[5]; // �̶�ֵ����infimum_supremum_compact��ע��
	byte[] page_new_infimum = new byte[8]; // infimum\0
	byte[] rec_n_new_extra_bytes2 = new byte[5]; // �̶�ֵ����inimum_supremum_copact��ע��
	byte[] page_new_supremum = new byte[8];// "supremum"���ﲻ��0

}

/**
 * �û���¼
 * 
 * @author renxing.zhang ϵͳ��¼֮������������û���¼�ˣ�heap no
 *         ��2��PAGE_HEAP_NO_USER_LOW����ʼ����ע��Heap no����������洢˳�򣬲������ֵ˳��
 * 
 *         ���ĵ�����������Ĭ��ʹ��Compact��ʽ ���ļ�rem/rem0rec.cc��ͷ��ע�������˼�¼������ṹ��
 * 
 *         ÿ����¼������rec essay-header���������£������ļ�include/rem0rec.ic��
 */
class UserRecord {
	//rec essay-header ����
	byte[] �䳤�г�����;
	byte[] sql_null_flag;// ��ʾֵΪnull���е�bitmap,ÿ��λ��ʾһ���У�bitmap�ĳ���ȡ���������Ͽ�Ϊnull���еĸ���,dict_index_t:n_nullable,����������Ľ������Բ��ĺ���rec_init_offsets
	byte[] rec_n_new_extra_bytes=new byte[5];//5���ֽ�,������¼�Ķ�����Ϣ
	
	/**
	 * 4 λ,Ŀǰֻʹ��������bit ,һ�����ڱ�ʾ�ü�¼�Ƿ񱻱��ɾ�� rec_info_deleted_flag
	 * ����һ��bit rec_info_min_rec_flag ��������ã���ʾ�����¼�ǵ�ǰlevel����ߵ�page �ĵ�һ���û���¼.
	 */
	byte rec_new_info_bits ;
	/**
	 * 4bit,����ֵΪ��0 ����ʾ��ǰ��¼ռ��page directory����һ��slot,����ǰһ��slot֮�������ô�����¼
	 * 
	 */
	byte rec_new_n_owned;
	
	/**
	 * 13bit �ü�¼��heap no 
	 */
	int rec_new_heap_no;
	
	/**
	 * 3 bit ��¼�����ͣ���������
	 * rec_status_ordinary Ҷ�ӽڵ��¼;
	 * rec_status_node_ptr��Ҷ�ӽڵ��¼;
	 * rec_status_infimum infimumϵͳ��¼;
	 * rec_status_supremum supremumϵͳ��¼
	 * 
	 */
	byte rec_new_status;
	
	/**
	 * ָ���ռ�ֱ�����page����һ����¼�������,
	 * ����洢���Ǻ͵�ǰ��¼�����λ��ƫ����
	 * ����rec_set_next_offs_new 
	 */
	byte rec_next;
	
	
	//TODO 
	/**
	 * �ڼ�¼ͷ��Ϣ֮��������Ӿ������������ͬ��
	 * ���ھۼ�������¼�����ݰ���������id���ع���ָ�룻
���ڶ���������¼�����ݰ����˶���������ֵ�Լ��ۼ�������ֵ����������������;ۼ��������غϣ���ֻ����һ���غϵģ�����pk (col1, col2)��sec key(col2, col3)���ڶ���������¼�о�ֻ����(col2, col3, col1);
���ڷ�Ҷ�ӽڵ�ҳ�ļ�¼���ۼ������ϰ��������ӽڵ����С��¼��ֵ����Ӧ��page no������������������ͬ�����˶���������ֵ�⣬�������˾ۼ�������ֵ���ټ���page no�����ֹ��ɡ�
	 * 
	 * 
	 * */ 
	
}

class FreeSpace{
	
	
}

/**
 * Ϊ�˼ӿ�ҳ�ڵ����ݲ��ң��ᰴ�ռ�¼��˳��ÿ��4~8��������PAGE_DIR_SLOT_MIN_N_OWNED ~ PAGE_DIR_SLOT_MAX_N_OWNED�����û���¼���ͷ���һ��slot ��ÿ��slotռ��2���ֽڣ�PAGE_DIR_SLOT_SIZE�����洢��¼��ҳ��ƫ�������������Ϊ��ҳ�ڹ�����һ����С������(sparse index)���������ֲ��ҡ�
 * 
 * Page Directory��slot�����Ǵ�Pageĩβ�������ڰ˸��ֽڿ�ʼ����ʼ�������ġ��ڲ�ѯ��¼ʱ���ȸ���page directory ȷ����¼���ڵķ�Χ��Ȼ���ھݴ˽������Բ�ѯ��
 * 
 * 
 * @author renxing.zhang
 *
 */
class PageDirectory{
	Slot[] slots=new Slot[999];
	
	/**
	 * ÿ��slotռ��2���ֽ�
	 * @author renxing.zhang
	 *
	 */
	class Slot{
		byte slot1;
		byte slot2;	 
	}
}


/**
 * ��ÿ���ļ�ҳ��ĩβ������8���ֽڣ�FIL_PAGE_DATA_END or FIL_PAGE_END_LSN_OLD_CHKSUM����
 * @author renxing.zhang
 * 
 *
 */
class FilTrailer{
	
	/**
	 * ���ֵ��Ҫ��pageͷ����¼��checksum��ƥ�䣬������Ϊpage��(buf_page_is_corrupted)
	 */
	byte[] checksum=new byte[4];
	 
	
}
