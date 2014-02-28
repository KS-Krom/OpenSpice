/*
 * 
 */
package util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

/**
 * The Class UtilPicture.
 */
public class UtilPicture {

	/** The Constant IMAGES. */
	private final static String IMAGES = "images";

	/** The Constant PUBLIC_IMAGES. */
	private final static String PUBLIC_IMAGES = "public/" + IMAGES;

	/**
	 * Creates the path.
	 * 
	 * @param nickname
	 *            the nickname
	 * @return the string
	 */
	public static String createPicturePath(String nickname) {
		return UtilUpload.createPath(PUBLIC_IMAGES, nickname);
	}

	/**
	 * Creates the path.
	 * 
	 * @param nickname
	 *            the nickname
	 * @param fileName
	 *            the file name
	 * @return the string
	 */
	public static String createPicturePath(String nickname, String fileName) {
		return UtilUpload.createPath(PUBLIC_IMAGES, nickname, fileName);
	}

	/**
	 * Resize.
	 * 
	 * @param nickname
	 *            the nickname
	 * @param filename
	 *            the filename
	 * @param file
	 *            the file
	 */
	public static void resize(String nickname, String filename, File file) {
		try {
			BufferedImage bimg = ImageIO.read(file);
			ImageIO.write(
					bimg,
					"png",
					new File(UtilPicture.createPicturePath(nickname,
							file.getName())));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Resize image.
	 * 
	 * @param sourceImg
	 *            the source img
	 * @param destImg
	 *            the dest img
	 * @param height
	 *            the height
	 * @return the boolean
	 */
	public static Boolean resizeImage(String sourceImg, String destImg,
			int height) {
		BufferedImage origImage;
		try {

			origImage = ImageIO.read(new File(sourceImg));
			int type = origImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB
					: origImage.getType();

			int width = origImage.getWidth() / (height / 150);
			if (height == 0) {
				height = origImage.getHeight();
			}

			int fHeight = height;
			int fWidth = width;

			if (origImage.getHeight() > height || origImage.getWidth() > width) {
				fHeight = height;
				int wid = width;
				float sum = (float) origImage.getWidth()
						/ (float) origImage.getHeight();
				fWidth = Math.round(fHeight * sum);

				if (fWidth > wid) {
					fHeight = Math.round(wid / sum);
					fWidth = wid;
				}
			}

			BufferedImage resizedImage = new BufferedImage(fWidth, fHeight,
					type);
			Graphics2D g = resizedImage.createGraphics();
			g.setComposite(AlphaComposite.Src);

			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			g.drawImage(origImage, 0, 0, fWidth, fHeight, null);
			g.dispose();

			ImageIO.write(resizedImage, "png", new File(destImg));

			if (!sourceImg.equals(destImg)) {
				new File(sourceImg).delete();
			}
		} catch (IOException ex) {
			return false;
		}

		return true;
	}

	/**
	 * Validate.
	 * 
	 * @param filename
	 *            the filename
	 * @return true, if successful
	 */
	public static boolean validate(String filename) {
		Pattern pattern;
		Matcher matcher;

		pattern = Pattern.compile("([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)");

		matcher = pattern.matcher(filename);
		return matcher.matches();
	}

}
