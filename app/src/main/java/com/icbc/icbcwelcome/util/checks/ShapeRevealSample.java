package com.icbc.icbcwelcome.util.checks;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.icbc.icbcwelcome.R;
import com.icbc.icbcwelcome.config.bgPrama;
import com.icbc.icbcwelcome.json.FestivalListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.animations.AnimationsSet;
import su.levenetc.android.textsurface.contants.TYPE;
import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
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
	private static Context context;
	private static FestivalListBean fontsBean;
	private static bgPrama ShapeBgPrama=new bgPrama(context);

	public ShapeRevealSample(Context context, String birthPeopleName,String birthMsg,FestivalListBean fontsBean) {
		this.context=context;
		this.birthMsg = birthMsg;
		this.birthPeopleName = birthPeopleName;
		this.fontsBean=fontsBean;
	}

	public static void setBirthMsg() {
		ShapeRevealSample.birthMsg = blessingSelect();
		Log.e("blessing", ShapeRevealSample.birthMsg );
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


	public static void play(TextSurface textSurface, AssetManager assetManager, String fontPath, String txtColor) {
		Log.e("fontPath",fontPath );
		int colSize = birthMsg.length()/ 4 + 1;
        final Typeface robotoBlack = Typeface.createFromAsset(assetManager, fontPath);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(robotoBlack);

		List<String> birthMsgList = getStrList(birthMsg, colSize);

//		Text textA = TextBuilder.create(addBlankForNum(birthPeopleName + "：",colSize - birthPeopleName.length())).setPaint(paint).setColor(Color.BLACK).setSize(70).setPosition(Align.SURFACE_CENTER).build();
//		Text textB = TextBuilder.create(birthMsgList.get(0)).setPaint(paint).setColor(Color.BLACK).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF , textA).build();
//		Text textC = TextBuilder.create(birthMsgList.get(1)).setPaint(paint).setColor(Color.BLACK).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF, textB).build();
//		Text textD = TextBuilder.create(birthMsgList.get(2)).setPaint(paint).setColor(Color.BLACK).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF , textC).build();
//		Text textE = TextBuilder.create(birthMsgList.get(3)).setPaint(paint).setColor(Color.BLACK).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF , textD).build();
		Text textA = TextBuilder.create(addBlankForNum(birthPeopleName + "：",colSize - birthPeopleName.length())).setPaint(paint).setColor(Color.parseColor(selectColor(txtColor))).setSize(70).setPosition(Align.SURFACE_CENTER).build();
		Text textB = TextBuilder.create(birthMsgList.get(0)).setPaint(paint).setColor(Color.parseColor(selectColor(txtColor))).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF , textA).build();
		Text textC = TextBuilder.create(birthMsgList.get(1)).setPaint(paint).setColor(Color.parseColor(selectColor(txtColor))).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF, textB).build();
		Text textD = TextBuilder.create(birthMsgList.get(2)).setPaint(paint).setColor(Color.parseColor(selectColor(txtColor))).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF , textC).build();
		Text textE = TextBuilder.create(birthMsgList.get(3)).setPaint(paint).setColor(Color.parseColor(selectColor(txtColor))).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF , textD).build();
//		Text textB = TextBuilder.create(birthMsgList.get(0)).setPaint(paint).setColor(Color.parseColor(selectColor(txtColor))).setSize(40).setPosition(Align.BOTTOM_OF|Align.CENTER_OF , textA).build();


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

	//颜色选择
	public static String selectColor(String msg)
	{
		String colorSelected;
		switch (msg){
			case "white":
				colorSelected="#ffffff";
				break;
			case "red":
				colorSelected="#ff0033";
				break;
			case "purple":
				colorSelected="#990099";
				break;
			case "blue":
				colorSelected="#0066ff";
				break;
			case "lightyellow":
				colorSelected="#ffffcc";
				break;
			case "orange":
				colorSelected="#ff660";
				break;
			case "yellow":
				colorSelected="#ffff00";
				break;
			case "pink":
				colorSelected="#ffcccc";
				break;
			case "green":
				colorSelected="#33ff33";
				break;
			case "deepgreen":
				colorSelected="#336600";
				break;
			case "black":
				colorSelected="#000000";
				break;
			case "fuchsia":
				colorSelected="#ff00ff";
				break;
			case "Olive":
				colorSelected="#999900";
				break;
			case "navy":
				colorSelected="#330099";
				break;
			case "Maroon":
				colorSelected="#990000";
				break;
			default:
				colorSelected="#000000";
				break;
		}
		return colorSelected;
	}


	//生日祝福语选择
	public static String blessingSelect()
	{
		String blessingMsg;
		//随机抽选
		Random random=new Random();
		int num=random.nextInt(17);
		switch (num){
			case 1:
				blessingMsg="花朝月夕，如诗如画；在这个真正属于你的日子里，愿你拥抱未来，愿你的容颜像春天般绚烂。生日快乐！";
				break;
			case 2:
				blessingMsg="生日是谁，它快不快乐有什么要紧的。在这个世界上，我希望你是快乐的。都自由，都拥有。祝您生日快乐！";
				break;
			case 3:
				blessingMsg="今天是您的生日，愿所有的快乐、所有的幸福、所有的温馨、所有的好运围绕在您身边。祝您生日快乐！";
				break;
			case 4:
				blessingMsg="当我把神灯擦三下后，灯神问我想许什么愿?我说：我想你帮我保佑一个正在看短信的人，希望那人生日快乐，永远幸福。祝您生日快乐！";
				break;
			case 5:
				blessingMsg="在你生日来临之际，祝事业正当午，身体壮如虎，金钱不胜数，干活不辛苦，悠闲像老鼠，浪漫似乐谱，快乐莫你属！祝您生日快乐！";
				break;
			case 6:
				blessingMsg="主银，我是麻麻精心准备的生日祝福大礼包，我现在可牛气了呢，我手下统率着幸福一世、快乐一生、平安幸福到永远、健康百分百、讨人喜欢等好多祝福，麻麻说从此以后我就归你领导了，让我好好照顾你，祝你生日快乐！";
				break;
            case 7:
                blessingMsg="悬赏令：捉拿微笑，捉住一个奖你一生快乐，捉住十个奖你一世幸福，捉住一百个奖你永远顺利平安，捉住后回复“微笑在我这”即可。快快行动，先笑先得！鉴于今天是你的生日，这条短信我提前发给你了：生日快乐！";
                break;
            case 8:
                blessingMsg="听说今日好多人都爱上了你这个寿星老，如意爱上你的美，吉祥爱上你的俏，好运爱上了你的能说会道，鉴于你如此的美好，我这个朋友只能站在一旁哈哈大笑，祝贺你生日快乐！";
                break;
            case 9:
                blessingMsg="为提高您的幸福质量，确保您的快乐健康，保证您事业的一路辉煌，特送给你无敌生日祝福一份，可有效预防一切烦恼忧伤，隔绝一切烦恼入侵，并有诱惑幸福之功效。愿你生活路上幸福无限！生日快乐！";
                break;
            case 10:
                blessingMsg="用时间的链串上健康的珠戴在手腕，你就会把幸福抓在手里面。用平安的诗谱写生活的歌，你就会让快乐长在心里头。祝你生日快乐，天天快乐！";
                break;
            case 11:
                blessingMsg="这条短信很神奇，山也挡不住，水也能过去，风也吹不走，雨也淋不湿，快速跑进你手机里，悄悄钻进你心坎里，伴你事事称心如意，伴你岁岁有今朝：生日快乐！";
                break;
            case 12:
                blessingMsg="致亲爱的你：十分热情，九分大气，八分智慧，七分灵气，六分浪漫，五分可爱，四分慵懒，三两个爱你的人和一个你想要的生活！祝完美的你生日快乐！";
                break;
            case 13:
                blessingMsg="一筐平安，一份健康，一包幸福，一袋甜蜜，一桶温馨，一封爱心，一箩快乐，再送上我最美好的祝福，祝你永远开心快乐，健康幸福！生日快乐！";
                break;
            case 14:
                blessingMsg="每一年是诗句泼洒的画面，美丽的容颜，每一天是旋律书写的笑脸，开开心心的一天，祝你生日快乐，祝你分分秒秒都有平安幸福陪伴。祝您生日快乐！";
                break;
            case 15:
                blessingMsg="一生一世，时间和生命的精彩在轮回中相逢。这一刻，我虔诚的在佛前为你祈祷祝福：愿你的人生都像今天这般七彩斑斓，你的耳边永远充满甜蜜的喝彩，心里流淌着幸福的旖旎。";
                break;
            case 16:
                blessingMsg="心情只要快乐，风风雨雨，亦觉阳光明媚；心态只要平和，坎坎坷坷，亦觉幸福无限；怀着美好的信念，怀着幸福的憧憬，幸福就在你我的身边。生日来临之际，愿你读懂幸福，享受美好！祝你生日快乐!";
                break;
			case 17:
				blessingMsg="让我们为您祝福，让我们为您欢笑，因为今天是您的生日，我们把缀满幸福快乐和平安的祝福悄然奉送，祝您心想事成！幸福快乐！生日快乐！";
				break;
			default:
				blessingMsg="花朝月夕，如诗如画；在这个真正属于你的日子里，愿你拥抱未来，愿你的容颜像春天般绚烂。生日快乐！";
				break;
		}
		return blessingMsg;
	}

}





















