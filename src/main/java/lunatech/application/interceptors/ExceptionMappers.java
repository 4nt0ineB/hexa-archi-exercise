package lunatech.application.interceptors;

import jakarta.ws.rs.core.Response;
import lunatech.domain.permission.ForbiddenActionException;
import lunatech.domain.todo.UnknownTodoException;
import lunatech.domain.user.UnknownUserException;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.jboss.resteasy.reactive.RestResponse;

class ExceptionMappers {

    @ServerExceptionMapper
    public RestResponse<String> mapException(UnknownTodoException e) {
        return RestResponse.status(Response.Status.NOT_FOUND, "Unknown todo: " + e.id);
    }

    @ServerExceptionMapper
    public RestResponse<String> mapException(UnknownUserException x) {
        return RestResponse.status(Response.Status.NOT_FOUND, "Unknown user: " + x.username);
    }

    @ServerExceptionMapper
    public RestResponse<String> mapException(ForbiddenActionException e) {
        return RestResponse.status(Response.Status.FORBIDDEN, e.getMessage());
    }

}