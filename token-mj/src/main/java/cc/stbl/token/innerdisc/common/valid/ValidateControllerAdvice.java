package cc.stbl.token.innerdisc.common.valid;

import cc.stbl.framework.protocol.provider.Response;
import cc.stbl.token.innerdisc.restapi.ResponseCode;
import com.ks.common.datastore.exception.StructWithCodeException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ValidateControllerAdvice {

    Logger logger= LoggerFactory.getLogger(ValidateControllerAdvice.class);
    /**
     * bean校验未通过异常
     *
     * @see javax.validation.Valid
     * @see org.springframework.validation.Validator
     * @see org.springframework.validation.DataBinder
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public Response validExceptionHandler(MethodArgumentNotValidException e) {
//        logger.error(e.getMessage(),e);
        return processResult(e.getBindingResult());
    }

    /**
     * bean校验未通过异常
     *
     * @see javax.validation.Valid
     * @see org.springframework.validation.Validator
     * @see org.springframework.validation.DataBinder
     */
    @ExceptionHandler({BindException.class})
    @ResponseBody
    public Response validExceptionHandler(BindException e) {
//        logger.error(e.getMessage(),e);
        return processResult(e.getBindingResult());
    }

    @ExceptionHandler({AuthorizationException.class})
    @ResponseBody
    public Response validAuthorizationException(HttpServletRequest request, AuthorizationException exception){
        logger.error("access[{}]error,no author[{}]",request.getRequestURI(),exception.getMessage());
        return Response.bulid(ResponseCode.OPT_NOAUTH_ERROR);
    }
    @ExceptionHandler({StructWithCodeException.class})
    @ResponseBody
    public Response structWithCodeException(HttpServletRequest request,StructWithCodeException exception){
        logger.warn("request[],error_code[],error[]",request.getRequestURI(),exception.getCode(),exception.getMessage());
        return exception.toResponse();
    }

    private Response processResult(BindingResult br){
        AbstractBindingResult bindingResult = (AbstractBindingResult)br;
        Map<String,String> errors = new LinkedHashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(),error.getDefaultMessage());
        }
        Response<Map<String, String>> error = Response.error(ResponseCode.ERROR_PARAMS_ERROR.msg, errors);
        error.setCode(ResponseCode.ERROR_PARAMS_ERROR.code);
        return error;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public Response httpMessageNotReadableException(HttpMessageNotReadableException e){
        return Response.bulid(ResponseCode.ERROR_PARAMS_ERROR);
    }
}

