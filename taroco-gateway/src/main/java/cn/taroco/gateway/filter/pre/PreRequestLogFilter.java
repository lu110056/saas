package cn.taroco.gateway.filter.pre;

import cn.taroco.common.constants.LogType;
import cn.taroco.common.constants.SecurityConstants;
import cn.taroco.common.entity.SysLog;
import cn.taroco.gateway.feign.SysLogService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xiaoleilu.hutool.util.StrUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Enumeration;

import static cn.taroco.gateway.filter.MyFilterConstants.PRE_REQUEST_LOG_ORDER;
import static cn.taroco.gateway.filter.MyFilterConstants.PRE_TYPE;

/**
 * 请求日志记录
 *
 * @author liuht
 * @date 2017/12/17
 */
@Component
public class PreRequestLogFilter extends ZuulFilter {

    private static final Log log = LogFactory.getLog(PreRequestLogFilter.class);

    @Autowired
    private SysLogService logService;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_REQUEST_LOG_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        final HttpServletRequest request = ctx.getRequest();
        log.info(String.format("send %s request to %s", request.getMethod(), request.getRequestURL().toString()));
        addLog(request, ctx);
        return null;
    }

    /**
     * 添加系统日志
     *
     * @param request 请求对象
     * @param ctx     RequestContext
     */
    private void addLog(HttpServletRequest request, RequestContext ctx) {
        final SysLog log = new SysLog();
        if (StrUtil.containsAnyIgnoreCase(request.getRequestURI(),
                SecurityConstants.OAUTH_TOKEN_URL, SecurityConstants.MOBILE_TOKEN_URL)) {
            log.setType(LogType.Login.name());
            log.setTitle(LogType.Login.name());
        } else {
            log.setType(LogType.Request.name());
            log.setTitle(LogType.Request.name());
        }
        log.setCreateBy(ctx.getZuulRequestHeaders().get(SecurityConstants.USER_HEADER));
        log.setCreateTime(new Date());
        log.setRemoteAddr(request.getRemoteAddr());
        log.setRequestUri(request.getRequestURI());
        log.setMethod(request.getMethod());

        if (HttpMethod.GET.matches(request.getMethod())) {
            StringBuilder params = new StringBuilder("?");
            final Enumeration<String> names = request.getParameterNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                params.append(name);
                params.append("=");
                params.append(request.getParameter(name));
                params.append("&");
            }
            if (params.length() > 0) {
                params.delete(params.length()-1, params.length());
            }
            log.setParams(params.toString());
        }
        if (!ctx.isChunkedRequestBody()) {
            try {
                ServletInputStream stream = request.getInputStream();
                if (stream != null) {
                    final String body = IOUtils.toString(stream, Charset.defaultCharset());
                    log.setParams(body);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logService.add(log);
    }
}
