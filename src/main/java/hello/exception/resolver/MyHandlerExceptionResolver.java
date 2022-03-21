package hello.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        log.info("call resolver", ex);

        try {
            if (ex instanceof IllegalArgumentException) {
            log.info("IllegalArgumentException resolver to 400");

            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                /**
                 * 예외를 response.sendError(xxx) 호출로 변경해서 서블릿에서 상태 코드에 따른 오류를 처리하도록 위임
                 */
            return new ModelAndView();
                //새로운 빈값으로 ModelAndView를 주면 view 와 model은 빈값  view 렌더링을 x
                // 정상적인 흐름으로 was까지 return이 된다 was에서는 response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());을 보고 400에러로 관리
                //즉 예외는 여기서 먹어버린다
                //ExceptionResolver로 예외를 해도 postHandle()은 호출되지 않는다

            }

        }catch (IOException e) {
            log.error("resolver ex", e);
        }


        return null;

        /**
         * 빈 ModelAndView: new ModelAndView() 처럼 빈 ModelAndView 를 반환하면 뷰를 렌더링 하지 않고, 정상 흐름(exception 먹음)으로 서블릿이 리턴된다.
         *
         * ModelAndView 지정: ModelAndView 에 View , Model 등의 정보를 지정해서 반환하면 뷰를 렌더링 한다.
         *
         * null: null 을 반환하면, 다음 ExceptionResolver 를 찾아서 실행한다.
         * 만약 처리할 수 있는 ExceptionResolver 가 없으면 예외 처리가 안되고, 기존에 발생한 예외를 서블릿 밖으로 던진다
         */


        /**
         * ExceptionResolver 활용
         * 예외 상태 코드 변환
         * 예외를 response.sendError(xxx) 호출로 변경해서 서블릿에서 상태 코드에 따른 오류를 처리하도록 위임
         * 이후 WAS는 서블릿 오류 페이지를 찾아서 내부 호출, 예를 들어서 스프링 부트가 기본으로 설정한 /error 가 호출됨

         * 뷰 템플릿 처리
         * ModelAndView 에 값을 채워서 예외에 따른 새로운 오류 화면 뷰 렌더링 해서 고객에게 제공

         * API 응답 처리
         * response.getWriter().println("hello"); 처럼 HTTP 응답 바디에 직접 데이터를 넣어주는 것도 가능하다. 여기에 JSON 으로 응답하면 API 응답 처리를 할 수 있다
         */
    }

}
