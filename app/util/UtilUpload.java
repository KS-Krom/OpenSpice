package util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import play.Logger;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Http.Request;

public class UtilUpload {

	/** The Constant SLASH. */
	private final static String SLASH = "/";

	/** The Constant SLASH. */
	private final static String FILE = "file";

	/** The Constant SLASH. */
	private final static String PUBLIC = "public";

	/**
	 * Upload picture.
	 * 
	 * @param request
	 *            the request
	 * @param name
	 *            the name
	 * @param nickname
	 *            the nickname
	 * @return the string
	 */
	public static String uploadPicture(Request request, String name,
			String nickname) {
		String filename = null;

		MultipartFormData body = request.body().asMultipartFormData();
		if (null != body) {
			FilePart picture = body.getFile(name);
			if (picture != null) {
				filename = picture.getFilename();

				if (UtilPicture.validate(filename)) {

					File file = picture.getFile();

					try {
						String path = UtilPicture.createPicturePath(nickname,
								filename);

						filename = filename.replaceAll("(jpg|gif|bmp)", "png");

						File destFile = new File(path);
						FileUtils.moveFile(file, destFile);
						UtilPicture.resizeImage(path, UtilPicture
								.createPicturePath(nickname, filename), 150);
						Logger.debug("Path : " + destFile.getAbsolutePath());
					} catch (IOException e) {
						Logger.error("Message : " + e.getMessage());
						Logger.error("Localized Message : "
								+ e.getLocalizedMessage());
					}
				}

			}
		}

		return filename;
	}

	/**
	 * Upload picture.
	 * 
	 * @param request
	 *            the request
	 * @param name
	 *            the name
	 * @param nickname
	 *            the nickname
	 * @return the string
	 */
	public static String uploadFile(Request request, String name,
			String nickname) {
		String filename = null;

		MultipartFormData body = request.body().asMultipartFormData();
		if (null != body) {
			FilePart picture = body.getFile(name);
			if (picture != null) {
				filename = picture.getFilename();

				File file = picture.getFile();

				try {
					String path = UtilUpload.createPath(PUBLIC, FILE, nickname,
							filename);

					File destFile = new File(path);
					FileUtils.moveFile(file, destFile);
					Logger.debug("Path : " + destFile.getAbsolutePath());
				} catch (IOException e) {
					Logger.error("Message : " + e.getMessage());
					Logger.error("Localized Message : "
							+ e.getLocalizedMessage());
				}
			}

		}

		return filename;
	}

	public static String createPath(String... names) {
		StringBuilder path = new StringBuilder();
		int size = names.length;

		for (int i = 0; i < size; ++i) {

			path.append(names[i]);
			if (i != size - 1) {
				path.append(SLASH);
			}
		}

		return path.toString();

	}

	public static String createFilePath(String nickname) {
		return UtilUpload.createPath(PUBLIC, FILE, nickname);

	}

	public static String createFilePath(String nickname, String filename) {
		return UtilUpload.createPath(PUBLIC, FILE, nickname, filename);

	}
}
