package lingshi.gateway.model;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class RequestFile {
	private MultipartHttpServletRequest mRequest;

	public RequestFile(HttpServletRequest request) {
		this.mRequest = (MultipartHttpServletRequest) request;
	}

	/**
	 * 获取文件
	 * 
	 * @param name
	 * @return
	 */
	public List<MultipartFile> getFiles(String name) {
		return mRequest.getFiles(name);
	}

	/**
	 * 获取默认文件
	 * 
	 * @return
	 */
	public MultipartFile getFile() {
		Iterator<String> filenames = mRequest.getFileNames();
		if (filenames.hasNext()) {
			return mRequest.getFile(filenames.next());
		}
		return null;
	}

	/**
	 * 获取文件
	 * 
	 * @param name
	 * @return
	 */
	public MultipartFile getFile(String name) {
		return mRequest.getFile(name);
	}

	/**
	 * 获取文件数量
	 * 
	 * @return
	 */
	public int getFileCount() {
		int count = 0;
		try {
			Iterator<String> names = mRequest.getFileNames();
			while (names.hasNext()) {
				List<MultipartFile> list = mRequest.getFiles(names.next());
				for (MultipartFile item : list) {
					if (item != null && item.getBytes() != null && item.getBytes().length > 0 && !item.isEmpty())
						count++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 判断是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return getFileCount() < 1;
	}

	/**
	 * 保存文件
	 * 
	 * @param path
	 * @param file
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void saveFile(String path, MultipartFile file) throws IllegalStateException, IOException {
		file.transferTo(new File(path));
	}
}
