package cn.allchin.mysql.ibd;

/**
 * 使用4个bit来描述每个page的change buffer信息
 * 
 * @author renxing.zhang
 *
 */
public class IbufBitmapPage {
	byte ibuf_bitmap_free; // 2 bits ,使用两个bit来描述page 的空闲空间范围:0 ,1(512bytes) ,2
							// (1024 bytes) ,3 (2048 bytes)
	byte ibuf_bitmap_buffered;// 1 bit ,是否有ibuf操作缓存
	
	byte ibuf_bitmap_ibuf;//1 bit 该page 本身是否是ibuf btree
}
