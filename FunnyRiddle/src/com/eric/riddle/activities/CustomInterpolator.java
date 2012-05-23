package com.eric.riddle.activities;

import android.util.Log;
import android.view.animation.Interpolator;

/**
 * @author Sodino E-mail:sodinoopen@hotmail.com
 * @version Time��2011-5-3 ����08:02:01
 */
public class CustomInterpolator implements Interpolator {

	/**
	 * @param input
	 *            A value between 0 and 1.0 indicating our current point in the
	 *            animation where 0 represents the start and 1.0 represents the
	 *            end
	 * @return Returns The interpolation value. This value can be more than 1.0
	 *         for Interpolators which overshoot their targets, or less than 0
	 *         for Interpolators that undershoot their targets.
	 */
	public float getInterpolation(float input) {
		Log.d("ANDROID_LAB", "input=" + input);
		// �趨�����ļ��ٶȱ仯ֵ��������Ч����ʹ��actionsLayout����Ŀ����ת�����ٷ���������
		// ��ֵ���㹫ʽ: 1.2-((x*1.55f)-1.1)^2
		// ��������ͼ�Ļ����ɹ۲������ִ�й�����Խ��Ŀ�����ٷ�������ϸ���̡�
		// x :0 <= v <= 1.0
		// (x*1.55f) :0 <= v <= 1.55
		// ((x*1.55f)-1.1) :-1.1 <= v <= 0.45
		// ((x*1.55f)-1.1)^2 :0<= v <= 1.21
		// 1.2-((x*1.55f)-1.1)^2 :-0.1 <= v <= 1.2
		final float inner = (input * 1.55f) - 1.1f;
		// �������ֵΪ����1�Ļ������൱��û�ж���Ч����
		return 1.2f - inner * inner;
	}
}