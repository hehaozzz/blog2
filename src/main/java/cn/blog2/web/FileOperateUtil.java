package cn.blog2.web;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileOperateUtil {
	Logger logger = LoggerFactory.getLogger(FileOperateUtil.class);

	@RequestMapping({ "/fileOperate" })
	public String show(HttpServletRequest request) {
		return "fileOperate";
	}

	@RequestMapping({ "/upload" })
	public void upload(@RequestParam(value = "editormd-image-file", required = false) MultipartFile attach,
			HttpServletRequest request, HttpServletResponse response, Model model)
					throws IllegalStateException, IOException {
		try {
			long startTime = System.currentTimeMillis();
			request.setCharacterEncoding("utf-8");
			response.setHeader("Content-Type", "text/html");
			String rootPath = request.getSession().getServletContext().getRealPath("/resources/upload/");
			// 文件路径不存在则需要创建文件路径
			File filePath = new File(rootPath);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			// 最终文件名
			File realFile = new File(rootPath + File.separator + attach.getOriginalFilename());
			this.logger.info("realFile:" + realFile);
			FileUtils.copyInputStreamToFile(attach.getInputStream(), realFile);
			response.getWriter().write( "{\"success\": 1, \"message\":\"上传成功\",\"url\":\"/blog2/resources/upload/" + attach.getOriginalFilename() + "\"}" );
			long endTime = System.currentTimeMillis();
			this.logger.info("上传用时：" + String.valueOf(endTime - startTime) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getWriter().write( "{\"success\":0}" );
				response.getWriter().write("error|上传失败");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@RequestMapping({ "/downLoad" })
	public ResponseEntity<byte[]> download(@RequestParam("name") String name, @RequestParam("filePath") String filePath)
			throws IOException {
		File file = new File(filePath);
		HttpHeaders headers = new HttpHeaders();
		String fileName = new String(name.getBytes("UTF-8"), "iso-8859-1");
		this.logger.info("路径：" + filePath);
		this.logger.info("文件名：" + fileName);
		headers.setContentDispositionFormData("attachment", fileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
	}
}