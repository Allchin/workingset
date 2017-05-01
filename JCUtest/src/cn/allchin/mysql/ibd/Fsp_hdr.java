package cn.allchin.mysql.ibd;

/**
 * �����ļ��ĵ�һ��Page����Ϊ
 * FIL_PAGE_TYPE_FSP_HDR
 * 
 * http://chuansong.me/n/2711290
 * @author renxing.zhang
 *
 */
public class Fsp_hdr {
	 
	byte[] fsp_space_id=new byte[4]; //���ļ���Ӧ��space id 
	byte[] fsp_not_used=new byte[4]; //�����ֽڣ�δʹ��
	byte[] fsp_size=new byte[4];   //��ǰ��ռ��ܵ�page����,��չ�ļ�ʱ��Ҫ���¸�ֵ
	byte[] fsp_free_limit=new byte[4]; //��ǰ��δ��ʼ������СpageNo.�Ӹ�page����Ķ���δ���뵽��ռ��free list��
	
	byte[] fsp_space_flags=new byte[4]; //��ǰ��ռ��flag��Ϣ
	byte[] fsp_space_n_used=new byte[4];  //fsp_free_frag�������Ѿ���ʹ�õ�page �������ڿ��ټ���������Ͽ��ÿ���page��
	byte[] fsp_free=new byte[16]; //��һ��extends �����е�page ��δ��ʹ��ʱ���ŵ��������ϣ������������ķ���
	byte[] fsp_free_frag=new byte[18]; //free_frag�����base Node ��ͨ��������extend�е�page ���ܹ����ڲ�ͬ��segment,����segment frag array page �ķ���
	
	byte[] fsp_full_frag = new byte[16];//extent �����е�page ����ʹ�õ�ʱ����ŵ����������ϣ�����page �Ӹ�extent �ͷ�ʱ�����ƻص�free_frag����
	byte[] fsp_seg_id = new byte[8]; //��ǰ�ļ������segment id +1 ���ڶ˷���ʱ��seg id ������
	byte[] fsp_seg_inodes_full = new byte[16]; //�Ѿ�����ȫ������idnode page ����
	byte[] fsp_seg_inodes_free = new byte[16]; //���ٴ���һ�����е�inode  entory ��indoe page ���ŵ���������
	
	
}

/**
 * ��ǰ��ռ��flag��Ϣ
 * @author renxing.zhang
 *
 */
class Fsp_space_flags{
	byte fsp_flags_pos_zip_ssize;  //ѹ��ҳ��block size ,���Ϊ0��ʾ��ѹ��  
	byte fsp_flags_pos_atomic_blobs;//ʹ�õ���compressed ����dynamic���и�ʽ
	byte fsp_flags_pos_page_ssize; //page size 
	byte fsp_flags_pos_data_dir; // ����ñ�ռ���ʾ���ƶ���data_dir �����ø�flag
	byte fsp_flags_pos_shared; //�Ƿ��ǹ���ı�ռ䡣��5.7 �����General Tablespace,����һ����ռ��д��������
	byte fsp_flags_pos_temporary;//�Ƿ�����ʱ��ռ� 
	byte fsp_flags_pos_encryption;//�Ƿ��Ǽ��ܱ�ռ� 
	byte fsp_flags_pos_unused; //δʹ�õ�λ
	
	
	
} 
