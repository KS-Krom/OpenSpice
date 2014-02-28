import play.GlobalSettings;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.Results;
import play.mvc.SimpleResult;

public class Global extends GlobalSettings {

	@Override
	public Promise<SimpleResult> onHandlerNotFound(RequestHeader arg0) {
		return Promise.<SimpleResult> pure(Results
				.notFound(views.html.error.handlerNotFound.render()));
	}

	@Override
	public Promise<SimpleResult> onBadRequest(RequestHeader arg0, String arg1) {
		return Promise.<SimpleResult> pure(Results
				.badRequest(views.html.error.badRequest.render()));
	}

	@Override
	public Promise<SimpleResult> onError(RequestHeader arg0, Throwable arg1) {
		return Promise.<SimpleResult> pure(Results
				.internalServerError(views.html.error.error.render()));
	}

}
