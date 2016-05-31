package com.ht.baselib.network;

import android.os.Handler;
import android.os.Message;

import com.ht.baselib.utils.LogUtils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Msg: 任务管理类 1、提供获取任务管理实例方法；2、提供销毁任务管理中线程池的方法
 * Update:  2015/10/12
 * Version: 1.0
 * Created by hxm on 2015/10/12 14:33.
 */
public final class TaskManager implements Runnable {
    /**
     * 请求结果
     */
    private static final int REQUEST_RESULT = 100;
    /**
     * 任务管理器实例
     */
    private static TaskManager taskManager = null;
    /**
     * 任务队列,,被this保护
     */
    private final Queue<Command> commandQueue = new LinkedList<Command>();
    /**
     * 线程池
     */
    private ExecutorService cachedThreadPool = null;
    /**
     * 句柄（用于线程处理完后，返回主线程）
     */
    private static Handler handler = null;


    private TaskManager() {
        this.cachedThreadPool = Executors.newCachedThreadPool();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REQUEST_RESULT://普通请求
                        Command cmd = (Command) msg.obj;
                        try {
                            if (cmd.getRspListener() != null) {//存在回调接口
                                if (cmd.isSuccess()) {
                                    cmd.getRspListener().onSuccess(cmd, cmd.getRspObject());
                                } else {
                                    cmd.getRspListener().onFailure(cmd);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    default:
                        LogUtils.v("----- TaskManager Handler接收到未知Message");
                }
            }
        };
    }

    /**
     * 获取任务管理器实例
     *
     * @return
     */
    public static synchronized TaskManager getInstance() {
        if (null == taskManager) {
            taskManager = new TaskManager();
        }
        return taskManager;
    }

    /**
     * 添加任务对象
     *
     * @param cmd 任务对象
     */
    public synchronized void addCommand(Command cmd) {
        this.commandQueue.offer(cmd);
        this.cachedThreadPool.execute(this);
    }


    /**
     * 获取任务对象
     *
     * @return
     */
    private synchronized Command getCommand() {
        Command cmd = null;
        if (!this.commandQueue.isEmpty()) {
            cmd = this.commandQueue.poll();

        }
        return cmd;
    }


    /**
     * 执行任务
     *
     * @param cmd 任务对象
     */
    private void executeCmd(Command cmd) {
        Message msg = handler.obtainMessage();
        msg.obj = cmd;
        msg.what = REQUEST_RESULT;
        try {
            cmd.setRspObject(executeComCmd(cmd));
        } catch (Exception e) {
            cmd.setRspObject(e);
            LogUtils.we(e);
        } catch (OutOfMemoryError omm) {
            System.gc();
            System.runFinalization();
            Exception oomE = new Exception("----------Exception of OOM------------");
            cmd.setRspObject(oomE);
            LogUtils.we(oomE);
        }

    }

    /**
     * 执行普通任务
     *
     * @param cmd
     * @return
     * @throws Exception
     */
    private Object executeComCmd(Command cmd) throws Exception {
        Object obj = null;
        if (cmd.getOperation() == null) {
            JsonOperation jsonOperation = JsonOperation.obtain();
            obj = jsonOperation.doOperate(cmd);
            JsonOperation.release(jsonOperation);
        } else {
            Class<?> clazz = Class.forName(cmd.getOperation());
            Operation oInterface = (Operation) clazz.newInstance();
            obj = oInterface.doOperate(cmd);
        }
        return obj;
    }


    /**
     * 销毁线程池（注：退出应用时调用）
     */
    public void destroyAllThread() {
        if (!this.cachedThreadPool.isShutdown()) {
            this.cachedThreadPool.shutdown();
        }
    }

    @Override
    public void run() {
        Command cmd = getCommand();
        if (cmd != null) {
            executeCmd(cmd);// 请求处理
        }
    }

}
