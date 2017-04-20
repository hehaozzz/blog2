package cn.blog2.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码生成器
 * 
 * @see ------------------------------------------------------------------------
 *      --------------------------------------
 * @see 可生成数字、大写、小写字母及三者混合类型的验证码
 * @see 支持自定义验证码字符数量,支持自定义验证码图片的大小,支持自定义需排除的特殊字符,支持自定义干扰线的数量,支持自定义验证码图文颜色
 * @see ------------------------------------------------------------------------
 *      --------------------------------------
 * @see 另外,给Shiro加入验证码有多种方式,也可以通过继承修改FormAuthenticationFilter类,通过Shiro去验证验证码
 * @see 而这里既然使用了SpringMVC,也为了简化操作,就使用此工具生成验证码,并在Controller中处理验证码的校验
 * @see ------------------------------------------------------------------------
 *      --------------------------------------
 * @create Sep 29, 2013 4:23:13 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class VerifyCodeUtil {
	private static Random random = new Random();
	/**
	 * 验证码类型为仅数字,即0~9
	 */
	public static final int TYPE_NUM_ONLY = 0;

	/**
	 * 验证码类型为仅字母,即大小写字母混合
	 */
	public static final int TYPE_LETTER_ONLY = 1;

	/**
	 * 验证码类型为数字和大小写字母混合
	 */
	public static final int TYPE_ALL_MIXED = 2;

	/**
	 * 验证码类型为数字和大写字母混合
	 */
	public static final int TYPE_NUM_UPPER = 3;

	/**
	 * 验证码类型为数字和小写字母混合
	 */
	public static final int TYPE_NUM_LOWER = 4;

	/**
	 * 验证码类型为仅大写字母
	 */
	public static final int TYPE_UPPER_ONLY = 5;

	/**
	 * 验证码类型为仅小写字母
	 */
	public static final int TYPE_LOWER_ONLY = 6;

	private VerifyCodeUtil() {
	}

	/**
	 * 生成随机颜色
	 */
	private static Color generateRandomColor() {
		Random random = new Random();
		return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
	}

	/**
	 * 生成图片验证码
	 * 
	 * @param type
	 *            验证码类型,参见本类的静态属性
	 * @param length
	 *            验证码字符长度,要求大于0的整数
	 * @param excludeString
	 *            需排除的特殊字符
	 * @param width
	 *            图片宽度(注意此宽度若过小,容易造成验证码文本显示不全,如4个字符的文本可使用85到90的宽度)
	 * @param height
	 *            图片高度
	 * @param interLine
	 *            图片中干扰线的条数
	 * @param randomLocation
	 *            每个字符的高低位置是否随机
	 * @param backColor
	 *            图片颜色,若为null则表示采用随机颜色
	 * @param foreColor
	 *            字体颜色,若为null则表示采用随机颜色
	 * @param lineColor
	 *            干扰线颜色,若为null则表示采用随机颜色
	 * @return 图片缓存对象
	 */
	public static BufferedImage generateImageCode(int type, int length, String excludeString, int width, int height,
			int interLine, boolean randomLocation, Color backColor, Color foreColor, Color lineColor) {
		String textCode = generateTextCode(type, length, excludeString);
		return generateImageCode(textCode, width, height, interLine, randomLocation, backColor, foreColor, lineColor);
	}

	/**
	 * 生成验证码字符串
	 * 
	 * @param type
	 *            验证码类型,参见本类的静态属性
	 * @param length
	 *            验证码长度,要求大于0的整数
	 * @param excludeString
	 *            需排除的特殊字符（无需排除则为null）
	 * @return 验证码字符串
	 */
	public static String generateTextCode(int type, int length, String excludeString) {
		if (length <= 0) {
			return "";
		}
		StringBuffer verifyCode = new StringBuffer();
		int i = 0;
		Random random = new Random();
		switch (type) {
		case TYPE_NUM_ONLY:
			while (i < length) {
				int t = random.nextInt(10);
				// 排除特殊字符
				if (null == excludeString || excludeString.indexOf(t + "") < 0) {
					verifyCode.append(t);
					i++;
				}
			}
			break;
		case TYPE_LETTER_ONLY:
			while (i < length) {
				int t = random.nextInt(123);
				if ((t >= 97 || (t >= 65 && t <= 90))
						&& (null == excludeString || excludeString.indexOf((char) t) < 0)) {
					verifyCode.append((char) t);
					i++;
				}
			}
			break;
		case TYPE_ALL_MIXED:
			while (i < length) {
				int t = random.nextInt(123);
				if ((t >= 97 || (t >= 65 && t <= 90) || (t >= 48 && t <= 57))
						&& (null == excludeString || excludeString.indexOf((char) t) < 0)) {
					verifyCode.append((char) t);
					i++;
				}
			}
			break;
		case TYPE_NUM_UPPER:
			while (i < length) {
				int t = random.nextInt(91);
				if ((t >= 65 || (t >= 48 && t <= 57))
						&& (null == excludeString || excludeString.indexOf((char) t) < 0)) {
					verifyCode.append((char) t);
					i++;
				}
			}
			break;
		case TYPE_NUM_LOWER:
			while (i < length) {
				int t = random.nextInt(123);
				if ((t >= 97 || (t >= 48 && t <= 57))
						&& (null == excludeString || excludeString.indexOf((char) t) < 0)) {
					verifyCode.append((char) t);
					i++;
				}
			}
			break;
		case TYPE_UPPER_ONLY:
			while (i < length) {
				int t = random.nextInt(91);
				if ((t >= 65) && (null == excludeString || excludeString.indexOf((char) t) < 0)) {
					verifyCode.append((char) t);
					i++;
				}
			}
			break;
		case TYPE_LOWER_ONLY:
			while (i < length) {
				int t = random.nextInt(123);
				if ((t >= 97) && (null == excludeString || excludeString.indexOf((char) t) < 0)) {
					verifyCode.append((char) t);
					i++;
				}
			}
			break;
		}
		return verifyCode.toString();
	}

	/**
	 * 已有验证码,生成验证码图片
	 * 
	 * @param textCode
	 *            文本验证码
	 * @param width
	 *            图片宽度(注意此宽度若过小,容易造成验证码文本显示不全,如4个字符的文本可使用85到90的宽度)
	 * @param height
	 *            图片高度
	 * @param interLine
	 *            图片中干扰线的条数
	 * @param randomLocation
	 *            每个字符的高低位置是否随机
	 * @param backColor
	 *            图片颜色,若为null则表示采用随机颜色
	 * @param foreColor
	 *            字体颜色,若为null则表示采用随机颜色
	 * @param lineColor
	 *            干扰线颜色,若为null则表示采用随机颜色
	 * @return 图片缓存对象
	 */
	public static BufferedImage generateImageCode(String textCode, int width, int height, int interLine,
			boolean randomLocation, Color backColor, Color foreColor, Color lineColor) {
		// 创建内存图像
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics graphics = bufferedImage.getGraphics();
		// 画背景图
		graphics.setColor(null == backColor ? generateRandomColor() : backColor);
		graphics.fillRect(0, 0, width, height);
		// 画干扰线
		Random random = new Random();
		if (interLine > 0) {
			int x = 0, y = 0, x1 = width, y1 = 0;
			for (int i = 0; i < interLine; i++) {
				graphics.setColor(null == lineColor ? generateRandomColor() : lineColor);
				y = random.nextInt(height);
				y1 = random.nextInt(height);
				graphics.drawLine(x, y, x + x1 + 40, y + y1 + 20);
			}
		}
		// 添加噪点
		float yawpRate = 0.05f;// 噪声率
		int area = (int) (yawpRate * width * height);
		for (int i = 0; i < area; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int rgb = getRandomIntColor();
			bufferedImage.setRGB(x, y, rgb);
		}
		@SuppressWarnings("unused")
		Color c = getRandColor(200, 250);
		// shear(graphics, width, height, c);// 使图片扭曲
		graphics.setColor(getRandColor(100, 160));
		// 字体大小为图片高度的80%
		int fsize = (int) (height * 0.8);
		int fx = height - fsize;
		int fy = fsize;
		// 设定字体
		graphics.setFont(new Font("Default", Font.PLAIN, fsize));
		// 写验证码字符
		for (int i = 0; i < textCode.length(); i++) {
			fy = randomLocation ? (int) ((Math.random() * 0.3 + 0.6) * height) : fy;
			graphics.setColor(null == foreColor ? generateRandomColor() : foreColor);
			// 将验证码字符显示到图象中
			graphics.drawString(textCode.charAt(i) + "", fx, fy);
			fx += fsize * 0.9;
		}
		graphics.dispose();
		return bufferedImage;
	}

	private static int getRandomIntColor() {
		int[] rgb = getRandomRgb();
		int color = 0;
		for (int c : rgb) {
			color = color << 8;
			color = color | c;
		}
		return color;
	}

	private static int[] getRandomRgb() {
		int[] rgb = new int[3];
		for (int i = 0; i < 3; i++) {
			rgb[i] = random.nextInt(255);
		}
		return rgb;
	}

	private static Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	@SuppressWarnings("unused")
	private static void shear(Graphics g, int w1, int h1, Color color) {
		shearX(g, w1, h1, color);
		shearY(g, w1, h1, color);
	}

	private static void shearX(Graphics g, int w1, int h1, Color color) {
		int period = random.nextInt(2);
		boolean borderGap = true;
		int frames = 1;
		int phase = random.nextInt(2);
		for (int i = 0; i < h1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
			g.copyArea(0, i, w1, 1, (int) d, 0);
			if (borderGap) {
				g.setColor(color);
				g.drawLine((int) d, i, 0, i);
				g.drawLine((int) d + w1, i, w1, i);
			}
		}
	}

	private static void shearY(Graphics g, int w1, int h1, Color color) {
		int period = random.nextInt(40) + 10; // 50;
		boolean borderGap = true;
		int frames = 20;
		int phase = 7;
		for (int i = 0; i < w1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
			g.copyArea(i, 0, 1, h1, 0, (int) d);
			if (borderGap) {
				g.setColor(color);
				g.drawLine(i, (int) d, i, 0);
				g.drawLine(i, (int) d + h1, i, h1);
			}
		}
	}
}