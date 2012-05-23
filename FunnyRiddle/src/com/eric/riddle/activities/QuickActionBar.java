package com.eric.riddle.activities;

import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Sodino E-mail:sodinoopen@hotmail.com
 * @version Time��2011-5-2 ����09:57:23
 */
public class QuickActionBar {
	// ���������ֶ�����ʽ��
	public static final int ANIM_GROW_FROM_LEFT = 1;
	public static final int ANIM_GROW_FROM_RIGHT = 2;
	public static final int ANIM_GROW_FROM_CENTER = 3;
	/** ����λ���Զ�ƥ�䶯���ĳ���λ�á� */
	public static final int ANIM_AUTO = 4;
	public static final Interpolator interpolator;
	/** ����Ӧ�������ʾ������������������Ϊ��������ͼ�ꡣ */
	private View anchor;
	private Vector<ActionItem> vecActions;
	/** ��ݲ����򸽼���PopupWindow�ϡ� */
	private PopupWindow popupWindow;
	/** ������ݲ����򲼾֡� */
	private View qaBarRoot;
	/** �ֻ���Ļ�Ŀ�ȡ� */
	private int phoneScreenWidth;
	/** �ֻ���Ļ�ĸ߶ȡ� */
	private int phoneScreenHeight;
	private int animType;
	/** ���������������� */
	private LayoutInflater inflater;
	/** Ĭ��PopupWindow���Լ���͸���Ļ�ɫ�ı��������ܻ��������񲻷���������������ñ����� */
	private Drawable popupWindowBackground;
	/** ��ݲ���������ϼ�ͷ�����ܲ���ʾ�� */
	private ImageView arrowUp;
	/** ��ݲ���������¼�ͷ�����ܲ���ʾ�� */
	private ImageView arrowDown;
	/** �Ƿ�����ActionsLayout�Զ�������ʽ���֡� */
	private boolean enableActionsLayout;
	private Animation actionsLayoutAnim;
	/** ������QuickActionBar��ListItem����š� */
	private int listItemIndex;
	private Context mcontext;
	private View smsBtn;
	private View shareBtn;

	static {
		interpolator = new CustomInterpolator();
	}

	/**
	 * @param anchor
	 *            ��ݲ�����Ҫָʾ����ͼ�����
	 * @param idx
	 *            ������QuickActionBar��ListItem����š�
	 */
	public QuickActionBar(View anchor, int idx) {
		if (vecActions == null) {
			vecActions = new Vector<ActionItem>();
		}
		this.anchor = anchor;
		TextView tv_content=(TextView) anchor.findViewById(R.id.riddle_content);
		final String content=tv_content.getText().toString().trim();
		this.listItemIndex = idx;
		mcontext = this.anchor.getContext();
		this.popupWindow = new PopupWindow(mcontext);
		// ����ʾ��ݲ�����ʱ������û�����˿���ĵط������Ҫ��ʧ
		popupWindow.setTouchInterceptor(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					QuickActionBar.this.popupWindow.dismiss();
					// ���¼��Ѵ����귵��true��
					return true;
				}
				return false;
			}
		});
		WindowManager windowManager = (WindowManager) mcontext
				.getSystemService(Context.WINDOW_SERVICE);
		phoneScreenWidth = windowManager.getDefaultDisplay().getWidth();
		phoneScreenHeight = windowManager.getDefaultDisplay().getHeight();
		// ���QuickActionBar����
		inflater = (LayoutInflater) mcontext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		qaBarRoot = (ViewGroup) inflater.inflate(R.layout.qa_bar, null);
		arrowUp = (ImageView) qaBarRoot.findViewById(R.id.qa_arrow_up);
		arrowDown = (ImageView) qaBarRoot.findViewById(R.id.qa_arrow_down);
		smsBtn = qaBarRoot.findViewById(R.id.sms);
		smsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri smsToUri = Uri.parse("smsto:");// ��ϵ�˵�ַ

				Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO,smsToUri);

				mIntent.putExtra("sms_body", content+mcontext.getString(R.string.guess));// ���ŵ�����

				mcontext.startActivity(mIntent);
			}
		});
		shareBtn = qaBarRoot.findViewById(R.id.share);
		shareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				//�����ı���ʽ
				emailIntent.setType("text/plain");
				//���öԷ��ʼ���ַ
				//emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "");
				//���ñ�������
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"��Ȥ����");
				//�����ʼ��ı�����
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,content+mcontext.getString(R.string.guess));
				mcontext.startActivity(Intent.createChooser(emailIntent,"ѡ�����ƽ̨��"));
			}
		});
		animType = ANIM_AUTO;

		actionsLayoutAnim = AnimationUtils.loadAnimation(anchor.getContext(),
				R.anim.anim_actionslayout);
		actionsLayoutAnim.setInterpolator(interpolator);
	}

	/** ��������QuickActionBar�ĳ��ֶ���Ч���� */
	public void setAnimType(int type) {
		this.animType = type;
	}

	public void setEnableActionsLayoutAnim(boolean bool) {
		enableActionsLayout = bool;
	}

	public void addActionItem(ActionItem actionWeb) {
		vecActions.add(actionWeb);
	}

	public void show() {
		// ȷ��popupWindow����ʾλ��
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		Rect anchorRect = new Rect(location[0], location[1], location[0]
				+ anchor.getWidth(), location[1] + anchor.getHeight());
		// Log.d("ANDROID_LAB", "anchorRect:" + anchorRect.toString());
		qaBarRoot.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		qaBarRoot.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int rootWidth = qaBarRoot.getMeasuredWidth();
		int rootHeight = qaBarRoot.getMeasuredHeight();
		// ��ʶ��ݲ������ͷ��ǵ�X����
		int xPos = (phoneScreenWidth - rootWidth) >> 1;
		// ��ʶ��ݲ������ͷ��ǵ�Y����
		int yPos = anchorRect.bottom;
		// �ж�ָʾ��ͷ��λ�ã����趨��ݲ����������λ��ֵ
		int diff = -10;
		boolean arrowOnTop = true;
		if (anchorRect.bottom + diff + rootHeight > phoneScreenHeight) {
			arrowOnTop = false;
			yPos = anchorRect.top - rootHeight;
			diff = -diff;
		}
		// ��ʾ/����ָ���ļ�ͷ
		int arrowId = arrowOnTop ? R.id.qa_arrow_up : R.id.qa_arrow_down;
		int marginLeft = anchorRect.centerX()
				- (arrowUp.getMeasuredWidth() >> 1);
		handleArrow(arrowId, marginLeft);
		// ���ActionItem�������ӵ�actionsLayout��
		// ����ActionItemͷβ�ı߿��м�ĸ���ActionItem��
		LinearLayout actionsLayout = (LinearLayout) qaBarRoot
				.findViewById(R.id.actionsLayout);
		appendActionsItemUI(actionsLayout, vecActions);

		// ����PopupWindow
		// ����PopupWindow�ĳ��ֶ�������ʧ����
		setAnimationStyle(phoneScreenWidth, anchorRect.centerX(), arrowOnTop);
		// ����Բ���������Ĭ�ϱ����Ļ�������ʾ��ȥ�����û�еĻ���������һ��ȫ��͸����BitmapDrawable���ԭ�еĲ����Ϸ���Ĭ�ϱ�����
		if (popupWindowBackground == null) {
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
		} else {
			popupWindow.setBackgroundDrawable(popupWindowBackground);
		}
		popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		// �Կ�����������Ӧ�����ڵ����������ʱ�ÿ���ʧ
		popupWindow.setOutsideTouchable(true);
		// ����ʾ������PopupWindow����
		popupWindow.setContentView(qaBarRoot);
		popupWindow.showAtLocation(this.anchor, Gravity.NO_GRAVITY, xPos, yPos
				+ diff);

		if (enableActionsLayout) {
			actionsLayout.startAnimation(actionsLayoutAnim);
		}
	}

	/** ���ActionItem�������ӵ�actionsLayout�� */
	private void appendActionsItemUI(ViewGroup actionsLayout,
			Vector<ActionItem> vec) {
		if (vec == null || vec.size() == 0) {
			return;
		}

		View view = null;
		int idx = 1;
		int size = vec.size();
		for (int i = 0; i < size; i++, idx++) {
			view = getActionItemUI(vecActions.get(i));
			// ����QuickActionBar��ΪTag���������¼�ʱ����QuickActionBar
			view.setTag(this);
			actionsLayout.addView(view, idx);
		}
	}

	private View getActionItemUI(ActionItem item) {
		LinearLayout actionItemLayout = (LinearLayout) inflater.inflate(
				R.layout.action_item, null);
		ImageView icon = (ImageView) actionItemLayout
				.findViewById(R.id.qa_actionItem_icon);
		TextView txtName = (TextView) actionItemLayout
				.findViewById(R.id.qa_actionItem_name);
		Drawable drawable = item.getIcon();
		if (drawable == null) {
			icon.setVisibility(View.GONE);
		} else {
			icon.setImageDrawable(drawable);
		}
		String name = item.getActionName();
		if (name == null) {
			txtName.setVisibility(View.GONE);
		} else {
			txtName.setText(name);
		}
		actionItemLayout.setOnClickListener(item.getOnClickListener());
		return actionItemLayout;
	}

	/**
	 * ��ʾ/������Ӧ�ļ�ͷ��������ͷ��ǵ�λ������Ӧ��X���ꡣ
	 * 
	 * @param arrowShowId
	 *            ������ʾ����Ļ�ϵļ�ͷID��
	 * @param marginLeft
	 *            Ϊ����ͷ���������anchor���ĵ��Xֵ�������λ��ֵ��
	 */
	private void handleArrow(int arrowShowId, int marginLeft) {
		View showArrow = (arrowShowId == R.id.qa_arrow_up) ? arrowUp
				: arrowDown;
		View hideArrow = (arrowShowId == R.id.qa_arrow_up) ? arrowDown
				: arrowUp;
		showArrow.setVisibility(View.VISIBLE);
		hideArrow.setVisibility(View.INVISIBLE);
		// ���ü�ͷ��X������ʾ��λ�ã�ͨ��margin����λ����
		ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow
				.getLayoutParams();
		param.leftMargin = marginLeft;
	}

	/**
	 * ����PopupWindow��Styleѡ��PopupWindow����ʾ/���ض�����
	 * 
	 * @param screenWidth
	 *            �ֻ���Ļ�Ŀ�ȡ�
	 * @param coordinateX
	 *            ��Ӧͼ�����ĵ��X����ֵ��
	 * @param arrowOnTop
	 *            ��ʶָʾ��ͷ�Ƿ����ϡ�
	 */
	private void setAnimationStyle(int screenWidth, int coordinateX,
			boolean arrowOnTop) {
		int arrowPos = coordinateX - (arrowUp.getMeasuredWidth() >> 1);
		switch (animType) {
		case ANIM_GROW_FROM_LEFT:
			popupWindow
					.setAnimationStyle((arrowOnTop) ? R.style.QuickActionBar_PopDown_Left
							: R.style.QuickActionBar_PopUp_Left);
			break;
		case ANIM_GROW_FROM_RIGHT:
			popupWindow
					.setAnimationStyle((arrowOnTop) ? R.style.QuickActionBar_PopDown_Right
							: R.style.QuickActionBar_PopUp_Right);
			break;
		case ANIM_GROW_FROM_CENTER:
			popupWindow
					.setAnimationStyle((arrowOnTop) ? R.style.QuickActionBar_PopDown_Center
							: R.style.QuickActionBar_PopUp_Center);
			break;
		case ANIM_AUTO:
			if (arrowPos <= screenWidth / 4) {
				popupWindow
						.setAnimationStyle((arrowOnTop) ? R.style.QuickActionBar_PopDown_Left
								: R.style.QuickActionBar_PopUp_Left);
			} else if (arrowPos > screenWidth / 4
					&& arrowPos < 3 * (screenWidth / 4)) {
				popupWindow
						.setAnimationStyle((arrowOnTop) ? R.style.QuickActionBar_PopDown_Center
								: R.style.QuickActionBar_PopUp_Center);
			} else {
				popupWindow
						.setAnimationStyle((arrowOnTop) ? R.style.QuickActionBar_PopDown_Right
								: R.style.QuickActionBar_PopUp_Right);
			}
			break;
		}
	}

	// ���õ�������յ�����
	public void setAnswerText(String answer) {
		((TextView) qaBarRoot.findViewById(R.id.answer)).setText(answer);
		;
	}

	public void dismissQuickActionBar() {
		popupWindow.dismiss();
	}

	public int getListItemIndex() {
		return listItemIndex;
	}

}