package cn.allchin.game.s3;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StoneRefine {

	public static void main(String[] args) {
		// ʣ���ֵ = ���ڵ��ܼ�ֵ-���ѵĳɱ� + �ϳɵ�ʯͷ���� * ÿ���ļ�ֵ

		BigDecimal refineL5With4 = calcRefundBenifit(51, 2,4);
		BigDecimal refineL5With3 = calcRefundBenifit(51, 2,3);

	}

 

	/**
	 * @param stones ���ϸ���
	 * @param level ���ϼ���
	 * @param groupStones ÿ�����ļ���
	 * @return
	 */
	private static BigDecimal calcRefundBenifit(int stones, int level, int groupStones) {
		BigDecimal result = null;
		Rate rate = getRate(groupStones);
		int groupCost=getRefindCost(level, groupStones);
		//

		BigDecimal originalValue = BigDecimal.valueOf(stones).multiply(BigDecimal.valueOf(stonePrice(level)));
		System.out.println("ǿ��ǰ|ԭ����|" + stones + "|�ȼ�|" + level +"|ÿ��|"+groupStones + "|ԭʼ��ֵ|" + originalValue);
		BigDecimal cost = BigDecimal.valueOf(groupCost).multiply(BigDecimal.valueOf(stones));
		System.out.println("ǿ������|" + cost);

		BigDecimal stone5Count = BigDecimal.valueOf(stones).multiply(BigDecimal.valueOf(rate.rate))
				.divide(BigDecimal.valueOf(groupStones), 0, RoundingMode.DOWN);

		System.out.println("ǿ�������ȼ�|" + (level + 1) + "��ʯͷ����|" + stone5Count);

		BigDecimal stone6Count = BigDecimal.valueOf(stones).multiply(BigDecimal.valueOf(rate.rate2))
				.divide(BigDecimal.valueOf(groupStones), 0, RoundingMode.DOWN);

		System.out.println("ǿ�������ȼ�|" + (level + 2) + "��ʯͷ����|" + stone6Count);
		//
		result = originalValue.subtract(cost).add(stone5Count.multiply(BigDecimal.valueOf(stonePrice(level + 1))))
				.add(stone6Count.multiply(BigDecimal.valueOf(stonePrice(level + 2))));
		System.out.println("ǿ����|ʯͷ��ֵ|" + result);
		return result;
	}

 

  
	public static class Rate {
		double rate = 0.00;
		double rate2 = 0.00;

	}

	public static Rate getRate(int groupStones) {
		Rate result = new Rate();
		switch (groupStones) {
		case 3:

			result.rate = 0.7777;
			result.rate2 = 0.00;
			return result;

		case 4:

			result.rate = 0.9436;
			result.rate2 = 0.0564;
			return result;

		case 5:
			// TODO
			break;
		}

		return null;
	}

	public static int getRefindCost(int level, int count) {
		int singleCost = 0;
		//��һ�����ϼ���3.7
		switch (level) {
		case 1:
			singleCost = 300;
			break;
		case 2:
			singleCost = 1110;
			break;
		case 3:
			singleCost=4107;
			break;
		case 4:
			singleCost=15195;
			break;
		case 5:singleCost=56225;
			break;
		case 6:
			singleCost=208031;
			break;
		case 7:
			singleCost=769717;
			break;
		case 8:
			singleCost=2847956;
			break;
		}
		return singleCost*count;
	}

	/**
	 * 1�� =10000
	 * �г��۸�
	 * 
	 * @param level
	 * @return
	 */
	public static int stonePrice(int level) {
		switch (level) {
		case 1:
			return 8300;
		case 2:
			return 53194;
		case 3:
			return 128300;
		case 4:
			return 429900;
		case 5:
			return 1797900;
		case 6:
			return 5497700;
		case 7:
			return 16811115;
		case 8:
			return 800000000;
		}
		return 0;
	}
}
