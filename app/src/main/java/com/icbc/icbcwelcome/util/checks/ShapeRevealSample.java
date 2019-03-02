package com.icbc.icbcwelcome.util.checks;

import android.graphics.Color;

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
	public static void play(TextSurface textSurface) {

		Text textA = TextBuilder.create("XXX：").setSize(80).setColor(Color.RED).setPosition(Align.SURFACE_CENTER).build();

		Text textB = TextBuilder.create("让我们为您祝福，让我们为您欢笑，").setSize(60).setColor(Color.RED).setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textA).build();
		Text textC = TextBuilder.create("因为今天是您的生日，").setSize(60).setColor(Color.RED).setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textB).build();
		Text textD = TextBuilder.create("我们把缀满幸福快乐和平安的祝福悄然奉送，").setSize(60).setColor(Color.RED).setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textC).build();
		Text textE = TextBuilder.create("祝您：心想事成!幸福快乐!生日快乐!").setSize(60).setColor(Color.RED).setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textD).build();

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
