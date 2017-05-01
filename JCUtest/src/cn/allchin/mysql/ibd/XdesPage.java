package cn.allchin.mysql.ibd;

/**
 * FIL_PAGE_TYPE_XDES
 * 
 * @author renxing.zhang
 *
 */
public class XdesPage {
	/**
	 * ���256��
	 */
	XdesEntry[] xdesEntry=new XdesEntry[256];
}

/**
 * 40�ֽ�,����64��page ,����һ��extend
 * @author renxing.zhang
 *
 */
class XdesEntry {
	byte[] xdes_id = new byte[8]; // �����extend������ĳ��segment�Ļ������¼��id
	byte[] xdes_flst_node = new byte[12]; // FLST_NODE_SIZE ά��extent �����˫��ָ��ڵ�

	byte[] xdes_state = new byte[4]; // ��extend��״̬��Ϣ������xdes_free,xdes_free_frag,xdes_full_frag,xdes_fseg
	byte[] xdes_bitmap = new byte[16]; // �ܹ�16*8 =128 ��bit
										// ,��2��bit��ʾextend�е�һ��page��һ��bit��ʾ��page�Ƿ��ǿ��е�(xdes_free_bit)
										// ,��һ������λ����δʹ��(xdes_clean_bit)

}

/**
 * extend��״̬��Ϣ
 * @author renxing.zhang
 *
 */
class XdesState{
	byte xdes_free=1; //������free������
	byte xdes_free_frag=2; //������free_frag������
	byte xdes_full_frag=3; //������full_frag������
	byte xdes_fseg=4; //��extend������idΪxdes_id��¼��ֵ��segment.
	 
}
