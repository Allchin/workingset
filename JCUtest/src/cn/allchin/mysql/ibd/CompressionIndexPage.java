package cn.allchin.mysql.ibd;

/**
 * 
 * ѹ������ҳ
 * 
 * @author renxing.zhang
 *
 */
public class CompressionIndexPage {
	byte[] fil_page_header; // ҳ��ͷ���ݲ���ѹ��
	byte[] index_field_infomation; // ����������Ϣ�������ظ��ǿ��Ը��ݴ����ݻָ���������Ϣ
	byte[] compressed_data; // ѹ�����ݣ�����heap no �������ѹ����
							// ��ѹ�����ݲ�����ϵͳ��trx_id,roll_ptr���ⲿ�洢ҳָ��
	byte[] modification_log; // ѹ��ҳ�޸���־
	byte[] free_space; // ���пռ�

	byte[] external_ptr;// �洢���ⲿ�洢ҳ���м�¼ָ�����飬ֻ���ھۼ�����Ҷ�ӽڵ㣬ÿ������Ԫ��ռ��20�ֽ�
	byte[] trx_id_Roll_ptr;// ֻ�����ھۼ�����Ҷ�ӽڵ㣬����Ԫ�غ���heap no һһ��Ӧ
	byte[] node_ptr;// ֻ������������Ҷ�ӽڵ㣬�洢�ڵ�ָ�����飬ÿ��Ԫ��ռ��4���ֽ�
	byte[] dense_page_directory;// �������֣���һ��������Ч��¼����¼���ڽ�ѹҳ�е�ƫ��λ�ã�n_owned��delete�����Ϣ,���ռ�ֵ˳��
	// �ڶ������ǿ��м�¼��ÿ��slotռ�������ַ�
}
