/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.philb.swinguiannotations;

import javax.swing.SwingUtilities;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author PBradley
 */
@Aspect
public class UiThreadRunnerAspect {

    private static final Logger logger = LoggerFactory.getLogger(UiThreadRunnerAspect.class);

    @Around("@annotation(UiThread) && execution(* *.*(..))")
    public Object runOnEdt(ProceedingJoinPoint pjp) throws Throwable {

        if (SwingUtilities.isEventDispatchThread()) {
            return pjp.proceed();
        }

        Object[] result = new Object[1];
        Throwable[] errors = new Throwable[1];

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    result[0] = pjp.proceed();
                } catch (Throwable e) {
                    errors[0] = e;
                }
            }
        });

        if (errors[0] != null) {
            throw errors[0];
        }

        return result[0];
    }

    @Around("@annotation(AsyncUiThread) && execution(* *.*(..))")
    public void runOnEdtAsync(ProceedingJoinPoint pjp) throws Throwable {

        if (SwingUtilities.isEventDispatchThread()) {
            pjp.proceed();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    pjp.proceed();
                } catch (Throwable tx) {
                    logger.error("Async UI operation " + pjp + " failed", tx);
                }
            }
        });
    }
}
