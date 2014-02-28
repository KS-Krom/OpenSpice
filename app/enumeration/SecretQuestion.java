/*
 * 
 */
package enumeration;

import play.i18n.Messages;

/**
 * The Enum SecretQuestion.
 */
public enum SecretQuestion {

	/** The animal. */
	ANIMAL("secretQuestion.favoriteAnimal"), FATHER_MIDDLENAME(
			"secretQuestion.fatherMiddleName");

	/** The question. */
	public String question;

	/**
	 * Instantiates a new secret question.
	 * 
	 * @param question
	 *            the question
	 */
	private SecretQuestion(String question) {
		this.question = question;
	}

	public String getQuestion() {
		return Messages.get(question);
	}

	public static String returnQuestion(String name) {
		String question = "";

		for (SecretQuestion sq : SecretQuestion.values()) {
			if (name.equals(sq.name())) {
				question = sq.getQuestion();
			}
		}

		return question;
	}
}
