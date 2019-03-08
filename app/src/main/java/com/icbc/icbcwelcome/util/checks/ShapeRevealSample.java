package com.icbc.icbcwelcome.util.checks;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;

import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.animations.AnimationsSet;
import su.levenetc.android.textsurface.contants.TYPE;
import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.Alpha;
import su.levenetc.android.textsurface.animations.Circle;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Rotate3D;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.animations.Slide;
import su.levenetc.android.textsurface.animations.TransSurface;
import su.levenetc.android.textsurface.contants.Axis;
import su.levenetc.android.textsurface.contants.Direction;
import su.levenetc.android.textsurface.contants.Pivot;
import su.levenetc.android.textsurface.contants.Side;

/**
 * Created by Eugene Levenetc.
 */
public class ShapeRevealSample {

	private static String birthMsg;
	private static String birthPeopleName;
//	public ShapeRevealSample(String birthPeopleName,String birthMsg) {
//		this.birthMsg = birthMsg;
//		this.birthPeopleName = birthPeopleName;
//	}

	public static void setBirthMsg(String birthMsg) {
		ShapeRevealSample.birthMsg = birthMsg;
	}

	public static void setBirthPeopleName(String birthPeopleName) {
		ShapeRevealSample.birthPeopleName = birthPeopleName;
	}

	/**
	 * 把原始字符串分割成指定长度的字符串列表
	 *
	 * @param inputString
	 *            原始字符串
	 * @param length
	 *            指定长度
	 * @return
	 */
	public static List<String> getStrList(String inputString, int length) {
		int size = inputString.length() / length;
		if (inputString.length() % length != 0) {
			size += 1;
		}
		return getStrList(inputString, length, size);
	}

	/**
	 * 把原始字符串分割成指定长度的字符串列表
	 *
	 * @param inputString
	 *            原始字符串
	 * @param length
	 *            指定长度
	 * @param size
	 *            指定列表大小
	 * @return
	 */
	public static List<String> getStrList(String inputString, int length,
										  int size) {
		List<String> list = new ArrayList<String>();
		for (int index = 0; index < size; index++) {
			String childStr = substring(inputString, index * length,
					(index + 1) * length);
			list.add(childStr);
		}
		return list;
	}

	/**
	 * 分割字符串，如果开始位置大于字符串长度，返回空
	 *
	 * @param str
	 *            原始字符串
	 * @param f
	 *            开始位置
	 * @param t
	 *            结束位置
	 * @return
	 */
	public static String substring(String str, int f, int t) {
		if (f > str.length())
			return null;
		if (t > str.length()) {
			return str.substring(f, str.length());
		} else {
			return str.substring(f, t);
		}
	}

	public static String addBlankForNum(String str, int strLength) {
		int strLen = str.length();
		if (strLen < strLength) {
			while (strLen < strLength) {
				StringBuffer sb = new StringBuffer();
//				sb.append(" ").append(str);// 左补空格
				sb.append(str).append(" ");//右补0
				str = sb.toString();
				strLen = str.length();
			}
		}

		return str;
	}



	public static void play(TextSurface textSurface,AssetManager assetManager) {
		int colSize = birthMsg.length()/ 4 + 1;

        final Typeface robotoBlack = Typeface.createFromAsset(assetManager, "fonts/birthday.ttf");
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(robotoBlack);

		List<String> birthMsgList = getStrList(birthMsg, colSize);

//		Text textA = TextBuilder.create(addBlankForNum(birthPeopleName + "：",colSize - birthPeopleName.length())).setPaint(paint).setColor(Color.BLACK).setSize(70).setPosition(Align.SURFACE_CENTER).build();
//		Text textB = TextBuilder.create(birthMsgList.get(0)).setPaint(paint).setColor(Color.BLACK).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF , textA).build();
//		Text textC = TextBuilder.create(birthMsgList.get(1)).setPaint(paint).setColor(Color.BLACK).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF, textB).build();
//		Text textD = TextBuilder.create(birthMsgList.get(2)).setPaint(paint).setColor(Color.BLACK).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF , textC).build();
//		Text textE = TextBuilder.create(birthMsgList.get(3)).setPaint(paint).setColor(Color.BLACK).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF , textD).build();
		Text textA = TextBuilder.create(addBlankForNum(birthPeopleName + "：",colSize - birthPeopleName.length())).setPaint(paint).setColor(Color.WHITE).setSize(70).setPosition(Align.SURFACE_CENTER).build();
		Text textB = TextBuilder.create(birthMsgList.get(0)).setPaint(paint).setColor(Color.WHITE).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF , textA).build();
		Text textC = TextBuilder.create(birthMsgList.get(1)).setPaint(paint).setColor(Color.WHITE).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF, textB).build();
		Text textD = TextBuilder.create(birthMsgList.get(2)).setPaint(paint).setColor(Color.WHITE).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF , textC).build();
		Text textE = TextBuilder.create(birthMsgList.get(3)).setPaint(paint).setColor(Color.WHITE).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF , textD).build();

		final int flash = 1500;

		textSurface.play(TYPE.SEQUENTIAL,
				Rotate3D.showFromCenter(textA, 500, Direction.CLOCK, Axis.X),
				new AnimationsSet(TYPE.PARALLEL,
						ShapeReveal.create(textA, flash, SideCut.hide(Side.LEFT), false),
						new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(flash / 5), ShapeReveal.create(textA, flash, SideCut.show(Side.LEFT), false))
				),
				new AnimationsSet(TYPE.PARALLEL,
						Rotate3D.showFromSide(textB, 500, Pivot.TOP),
						new TransSurface(500, textB, Pivot.CENTER)
				),
				Delay.duration(500),
				new AnimationsSet(TYPE.PARALLEL,
						Slide.showFrom(Side.TOP, textC, 500),
						new TransSurface(1000, textC, Pivot.CENTER)
				),
				Delay.duration(500),
				new AnimationsSet(TYPE.PARALLEL,
						Slide.showFrom(Side.TOP, textD, 500),
						new TransSurface(1500, textD, Pivot.CENTER)
				),
				Delay.duration(500),
				new AnimationsSet(TYPE.PARALLEL,
						ShapeReveal.create(textE, 500, Circle.show(Side.CENTER, Direction.OUT), false),
						new TransSurface(2000, textE, Pivot.CENTER)
				),
				Delay.duration(500)

		);

	}

}
