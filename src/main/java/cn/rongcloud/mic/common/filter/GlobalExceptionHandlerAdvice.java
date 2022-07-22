package cn.rongcloud.mic.common.filter;

/**
 * Created by sunyinglong on 2020/7/6
 */
import cn.rongcloud.mic.common.rest.RestException;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler({MissingServletRequestParameterException.class,
        IllegalArgumentException.class})
    public RestResult handleRequestParameterException(Exception ex) {
        logException(ex);
        return getResponseData(RestResultCode.ERR_REQUEST_PARA_ERR, ex.getMessage());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler({ServletRequestBindingException.class})
    public RestResult handleInvalidTokenException(Exception ex) {
        logException(ex);
        return getResponseData(RestResultCode.ERR_INVALID_AUTH, ex.getMessage());
    }


    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler({DataAccessException.class})
    public RestResult handleDatabaseException(Exception ex) {
        logException(ex);
        return getResponseData(
            RestResultCode.ERR_OTHER, ((DataAccessException) ex).getMostSpecificCause().getMessage());
    }

    // 400
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler({TypeMismatchException.class,
        HttpMessageNotReadableException.class,
        MethodArgumentNotValidException.class,
        MissingServletRequestPartException.class,
        BindException.class})
    public RestResult handleBadRequestException(Exception ex) {
        logException(ex);
        return getResponseData(RestResultCode.ERR_BAD_REQUEST, ex.getMessage());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class,
        HttpMediaTypeNotSupportedException.class,
        HttpMediaTypeNotAcceptableException.class,
        MissingPathVariableException.class,
        ConversionNotSupportedException.class,
        HttpMessageNotWritableException.class,
        IOException.class,
        RuntimeException.class,
        AsyncRequestTimeoutException.class})
    public RestResult handleOtherException(Exception ex) {
        logException(ex);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        printWriter.print(ex);
        return getResponseData(RestResultCode.ERR_OTHER,  stringWriter.toString());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(RestException.class)
    public RestResult handleMiMicroAPIException(RestException ex) {
        logException(ex);
        return ex.getRestResult();
    }

    private void logException(Exception ex) {
        log.error("caught exception:", ex);
    }


    private RestResult getResponseData(RestResultCode err, String errMsg) {
        return RestResult.generic(err, errMsg);
    }
}
