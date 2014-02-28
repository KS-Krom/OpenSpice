/*
 * 
 */
package models;

import play.libs.Crypto;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import controllers.routes;
import factory.UserFactory;

/**
 * The Class Secured.
 */
public class Secured extends Security.Authenticator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see play.mvc.Security.Authenticator#getUsername(play.mvc.Http.Context)
	 */
	@Override
	public String getUsername(Context arg0) {

		User user = UserFactory.findByMail(arg0.session().get("email"));

		if (null != user) {
			return Crypto.extractSignedToken(arg0.session().get("token"))
					+ user.token;
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * play.mvc.Security.Authenticator#onUnauthorized(play.mvc.Http.Context)
	 */
	@Override
	public Result onUnauthorized(Context arg0) {
		return redirect(routes.Index.logout());
	}

}
