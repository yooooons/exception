package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "hello.exception.api")
public class ExControllerAdvice {
    /**
     *"현재 컨트롤러"에서 예외가 터지면 @ExceptionHandler 실행, 정상리턴 따라서 http상태코드는 200으로 뜬다
     *  @ResponseStatus 를 사용해서 원하는 상태코드 변경
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("Bad", e.getMessage());
    }

    @ExceptionHandler//(UserException.class) 생략가능
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX","내부오류");
        /**
         * Exception 최상위 예외, 따라서 Exception의 자식 예외까지 잡아준다(공통 처리)
         * 스프링에서는 보통 자세한 것이 우선순위가 높기때문에 위의 예외에서 해당되는 것들은 처리해주고 실수로 놓친 예외와 같은것을 잡아주는 역할도 한다
         */
    }
}
