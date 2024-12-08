package org.crimsonmc.thread;

import org.crimsonmc.scheduler.AsyncWorker;

/**
 * 描述一个可以被中断的线程的接口。<br> An interface to describe a thread that can be interrupted.
 * <p>
 * <p>在crimsonmc服务器停止时，crimsonmc会找到所有实现了{@code
 * InterruptibleThread}的线程，并逐一中断。<br> When a crimsonmc server is stopping, crimsonmc finds all
 * threads implements {@code InterruptibleThread}, and interrupt them one by one.</p>
 *
 * @author MagicDroidX(code) @ crimsonmc Project
 * @author 粉鞋大妈(javadoc) @ crimsonmc Project
 * @see AsyncWorker
 * @see ConsoleCommandReader
 * @since crimsonmc 1.0 | crimsonmc API 1.0.0
 */
public interface InterruptibleThread {
}
