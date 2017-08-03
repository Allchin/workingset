package cn.allchin.mysql.ibd;

import java.util.Arrays;
import java.util.BitSet;

/**
 * �����¼��ʽ
 * 
 * @author renxing.zhang
 *
 */
public class Ibd {
	byte[] col_offset_list = new byte[1000];// ����*1�ֽ� when col len <127 ;
	byte[] record_header = new byte[6];
	byte[] rowId = new byte[6];
	byte[] transactionId = new byte[6];
	byte[] rollPoint = new byte[7];
	byte[] cols = new byte[1000];

	public void read(byte[] src, int start) {
		// TODO
	}



}

/**
 * 48λ
 *
 */
class RecordHeader {

	byte info_bits;// 4 bit
	byte n_owned;// 4;//��¼����4 bit
	int heap_no;// 13 bit
	int number_of_field ;//= 10; // ���� 10 bit
	byte short_flag;// 1;
	short next_record;// 16 bit;

	/**
	 * ����Ϊ48 �������ǳ���Ϊ6������
	 * @param src
	 */
	public void read(byte[] src) {
		info_bits=Reader.read(src[0], 0, 3);
		n_owned=Reader.read(src[0], 4, 7);
		heap_no=Reader.read(Arrays.copyOf(src, 4), 8, 20);
		number_of_field=Reader.read(Arrays.copyOf(src, 4), 21, 30);
		short_flag=Reader.read(src[3], 7, 7);
		next_record=(short) (src[4]+src[5]); 
	}
}

/**
 * 4λ
 */
class InfoBits {
	byte unname;// 1;
	byte unname2;// 1;
	byte deleted_flag;// 1;
	byte min_rec_flag;// 1;

	/**
	 * @param src  ����λ��Ч
	 */
	public void read(byte  src){
		 unname=Reader.read(src, 4, 4);
		 unname2=Reader.read(src, 5,5);
		 deleted_flag=Reader.read(src, 6, 6);
		 min_rec_flag=Reader.read(src, 7, 7);
	}
}

