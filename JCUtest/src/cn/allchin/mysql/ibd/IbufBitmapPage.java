package cn.allchin.mysql.ibd;

/**
 * ʹ��4��bit������ÿ��page��change buffer��Ϣ
 * 
 * @author renxing.zhang
 *
 */
public class IbufBitmapPage {
	byte ibuf_bitmap_free; // 2 bits ,ʹ������bit������page �Ŀ��пռ䷶Χ:0 ,1(512bytes) ,2
							// (1024 bytes) ,3 (2048 bytes)
	byte ibuf_bitmap_buffered;// 1 bit ,�Ƿ���ibuf��������
	
	byte ibuf_bitmap_ibuf;//1 bit ��page �����Ƿ���ibuf btree
}
