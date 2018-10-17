package asp.citic.ptframework.common.tools;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import asp.citic.ptframework.logger.PTLogger;

/**
 * 流操作助手
 */
public final class PTStreamOperator {
	/**
	 * 类标记
	 */
	private static final String TAG = "PTStreamOperator";
	/** 读取缓冲区大小. */
	private static final int READBUF_SIZE = 1024;
	/**
	 * 私有构造方法
	 */
	private PTStreamOperator(){
		//To be a Utility class
	}
	/**
	 * 获取输入流的字节数组.
	 */
	public static byte[] getInputStreamBytes(InputStream ins){
		return getInputStreamBytes(ins, false);
	}

	/**
	 * 获取输入流的字节数组.
	 */
	public static byte[] getInputStreamBytes(InputStream ins,boolean autoClose){
		if (ins == null){
			return null;
		}
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			copy(ins, baos, autoClose);
			return baos.toByteArray();
		}  finally{
			close(baos);
		}
	}

	/**
	 * 将输入流复制到输出流.
	 */
	public static void copy(InputStream ins, OutputStream outs){
		copy(ins, outs, false);
	}

	/**
	 * 将输入流复制到输出流.
	 */
	public static void copy(InputStream ins, OutputStream outs, boolean autoClose){
		 byte[] buf = new byte[READBUF_SIZE];
		int size;
		try{
			while ((size = ins.read(buf)) != -1){
				outs.write(buf, 0, size);
			}
		} catch (IOException e){
			PTLogger.error(TAG,e.getMessage());
		}finally{
			if (autoClose) {
				close(ins);
				close(outs);
			}
		}
	}

	/**
	 * 关闭流.
	 */
	public static void close(Closeable stream){
		if (stream == null){
			return;
		}
		try{
			stream.close();
		} catch (IOException e){
			PTLogger.error(TAG,e.getMessage());
		}
	}
}
