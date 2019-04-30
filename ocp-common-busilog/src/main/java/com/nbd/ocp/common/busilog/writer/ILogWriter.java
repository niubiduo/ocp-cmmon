package com.nbd.ocp.common.busilog.writer;


import com.nbd.ocp.common.busilog.dto.BusiLogDto;

/**
 * 业务日志发送器
 * @author jin
 */
public interface ILogWriter {
	/**
	 * 发送业务日志对象
	 * @param busiLogDto 业务日志对象
	 */
	void write(BusiLogDto busiLogDto);
}

