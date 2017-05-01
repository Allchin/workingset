package cn.allchin.mysql.ibd;

/**
 * page 3 ���ڹ��������ļ��е�segement��ÿ������ռ��2��segment���ֱ����ڹ���Ҷ�ӽڵ�ͷ�Ҷ�ӽڵ㡣
 * ÿ��inodeҳ���Դ洢FSP_SEG_INODES_PER_PAGE��Ĭ��Ϊ85������¼��
 * 
 * @author renxing.zhang
 *
 */
public class InodePage {
	int fseg_inode_page_node;// 12 inodeҳ������ڵ㣬��¼ǰ��inode page
								// ��λ�ã�baseNdoe��¼��ͷpage
								// ��fsp_seg_inodes_full����fsp_seg_inodes_free�ֶ�
	InodeEntry[] inodeEntry = new InodeEntry[85]; //
}

/**
 * 192bits inode��¼
 * 
 * @author renxing.zhang
 *
 */
class InodeEntry {
	byte fseg_id;// 8 bit ��inode������segment id ,��ֵΪ0��ʾ��slotδ��ʹ��
	byte fseg_not_full_n_used; // fseg_not_full ������δ��ʹ�õ�page ����

	/**
	 * 16 bit ,��ȫû�б�ʹ�ò��������segment ��extent ����
	 */
	short fseg_free;//
	/**
	 * 16 bit ������һ��page //
	 * �������ǰsegment��extent����ȫ������ʱ��ת�Ƶ�fseg_full���棬ȫ���ͷ�ʱ����黹����ǰ��ռ�fsp_free����
	 */
	short fseg_not_full;//
	/**
	 *  16 �������ǰsegment ��page ��ȫ��ʹ�����extent����
	 */
	short fseg_full; //
	byte fseg_magic_n; // 4 magic number
	byte[] fseg_frag_arr = new byte[32];// 4bit
										// ���ڸ�segment�Ķ���page�������ȴ�ȥȫ�ַ��������page��������32��������ʱ������ÿ�η���ʱ������һ��������extent,����xdes_page�н���segment
										// id ����Ϊ��ǰֵ

}