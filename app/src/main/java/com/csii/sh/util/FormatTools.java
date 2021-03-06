package com.csii.sh.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Bitmap与DrawAble与byte[]与InputStream与String之间的转换工具类
 */
public class FormatTools {

	final int BUFFER_SIZE = 4096;
	private static FormatTools tools;
	
	public static FormatTools getInstance() {
		if (tools == null) {
			tools = new FormatTools();
			return tools;
		}
		return tools;
	}

	/**
	 * @brief 将byte[]转换成InputStream
	 * @param b
	 *            byte数组
	 * @return InputStream
	 * */
	public InputStream Bytes2InputStream(byte[] b) {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		return bais;
	}

	/**
	 * @brief 将byte数组转换成String
	 * 
	 * @param b
	 *            byte数组
	 * @return String
	 * @throws Exception
	 */
	public String Bytes2String(byte[] b) {
		InputStream is;
		try {
			is = Bytes2InputStream(b);
			return InputStream2String(is);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @brief byte[]转换成Bitmap
	 * @param b
	 *            byte数组
	 * @return Bitmap
	 * */
	public Bitmap Bytes2Bitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return null;
	}

	/**
	 * @brief byte[]转换成Drawable
	 * @param b
	 *            byte数组
	 * @return Drawable
	 * */
	public Drawable Bytes2Drawable(byte[] b) {
		Bitmap bitmap = this.Bytes2Bitmap(b);
		return this.Bitmap2Drawable(bitmap);
	}

	/**
	 * @brief Drawable转换成InputStream
	 * @param d
	 *            图片对象（Drawable）
	 * @return InputStream
	 * */
	public InputStream Drawable2InputStream(Drawable d) {
		Bitmap bitmap = this.Drawable2Bitmap(d);
		return this.Bitmap2InputStream(bitmap);
	}

	/**
	 * @brief Drawable转换成byte[]
	 * @param d
	 *            图片对象（Drawable）
	 * @return byte[]
	 * */
	public byte[] Drawable2Bytes(Drawable d) {
		Bitmap bitmap = this.Drawable2Bitmap(d);
		return this.Bitmap2Bytes(bitmap);
	}

	/**
	 * @brief Drawable转换成Bitmap
	 * @param d
	 *            图片对象（Drawable）
	 * @return Bitmap
	 * */
	public Bitmap Drawable2Bitmap(Drawable d) {
		Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d
				.getIntrinsicHeight(),
				d.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
						: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		d.draw(canvas);
		return bitmap;
	}

	/**
	 * @brief Bitmap转换成byte[]
	 * @param bm
	 *            图片对象（Bitmap）
	 * @return byte[]
	 * */
	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		return baos.toByteArray();
	}

	/**
	 * @brief Bitmap转换成Drawable
	 * @param bm
	 *            图片对象（Bitmap）
	 * @return Drawable
	 * */
	public Drawable Bitmap2Drawable(Bitmap bm) {
		BitmapDrawable bd = new BitmapDrawable(bm);
		Drawable d = bd;
		return d;
	}

	/**
	 * @brief 将Bitmap转换成InputStream
	 * @param bm
	 *            图片对象（Bitmap）
	 * @param quality
	 *            图片质量
	 * @return InputStream
	 * */
	public InputStream Bitmap2InputStream(Bitmap bm, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	/**
	 * @brief 将Bitmap转换成InputStream
	 * @param bm
	 *            图片对象（Bitmap）
	 * @return InputStream
	 * */
	public InputStream Bitmap2InputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	/**
	 * @brief 将String转换成byte[]
	 * 
	 * @param in
	 *            字符串
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] String2Bytes(String in) {
		try {
			return in.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * @brief 将String转换成InputStream
	 * 
	 * @param in
	 *            转化字符串
	 * @return InputStream
	 * @throws Exception
	 */
	public InputStream String2InputStream(String in) throws Exception {
		ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes());
		return is;
	}

	/**
	 * @brief 将String转换成InputStream
	 * 
	 * @param in
	 *            ：转化字符串，encoding：编码格式
	 * @return InputStream
	 * @throws Exception
	 */
	public InputStream String2InputStream(String in, String encoding)
			throws Exception {
		ByteArrayInputStream is = new ByteArrayInputStream(
				in.getBytes(encoding));
		return is;
	}

	/**
	 * @brief 将InputStream转换成byte[]
	 * 
	 * @param is
	 *            数据流
	 * @return byte[]
	 * */
	public byte[] InputStream2Bytes(InputStream is) {
		String str = "";
		byte[] readByte = new byte[1024];
		int readCount = -1;
		try {
			while ((readCount = is.read(readByte, 0, 1024)) != -1) {
				str += new String(readByte).trim();
			}
			return str.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @brief 将InputStream转换成Bitmap
	 * @param is
	 *            数据流
	 * @return Bitmap
	 * */
	public Bitmap InputStream2Bitmap(InputStream is) {
		return BitmapFactory.decodeStream(is);
	}

	/**
	 * @brief InputStream转换成Drawable
	 * @param is
	 *            数据流
	 * @return Drawable
	 * */
	public Drawable InputStream2Drawable(InputStream is) {
		Bitmap bitmap = this.InputStream2Bitmap(is);
		return this.Bitmap2Drawable(bitmap);
	}

	/**
	 * @brief 将InputStream转换成某种字符编码的String
	 * 
	 * @param in
	 *            数据流
	 * @param encoding
	 *            编码格式
	 * @return String
	 * @throws Exception
	 */
	public String InputStream2String(InputStream in, String encoding)
			throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return new String(outStream.toByteArray(), encoding);
	}

	/**
	 * @brief 将InputStream转换成String
	 * 
	 * @param in
	 *            数据流
	 * @return String
	 * @throws Exception
	 * 
	 */
	public String InputStream2String(InputStream in) throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return new String(outStream.toByteArray(), "UTF-8");
	}



	/***
	 * json转map
	 *
	 * @param jsonStr
	 * @return
	 *//*
	public Map<String, String> parseJSON2Map(String jsonStr) {
		if (jsonStr == null || "".equals(jsonStr)) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		// 最外层解析
		JSONObject json = JSON.parseObject(jsonStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			map.put(k.toString(), v.toString());
		}
		return map;
	}*/

	/**
	 * bitmap转为base64 
	 * @param bitmap 
	 * @return 
	 */  
	public String bitmapToBase64(Bitmap bitmap) {
	  
	    String result = null;
	    ByteArrayOutputStream baos = null;
	    try {  
	        if (bitmap != null) {  
	            baos = new ByteArrayOutputStream();
	            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	  
	            baos.flush();  
	            baos.close();  
	  
	            byte[] bitmapBytes = baos.toByteArray();  
	            result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
	        }  
	    } catch (IOException e) {
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            if (baos != null) {  
	                baos.flush();  
	                baos.close();  
	            }  
	        } catch (IOException e) {
	            e.printStackTrace();  
	        }  
	    }  
	    return result;  
	}  
	  
	/** 
	 * base64转为bitmap 
	 * @param base64Data 
	 * @return 
	 */  
	public Bitmap base64ToBitmap(String base64Data) {
	    byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
	    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	} 
	
	
}