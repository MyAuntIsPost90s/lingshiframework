package lingshi.mybaties.mapperextend;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

public interface BaseMapper<T> {
	public int insert(T t);

	public int update(T t);

	public long count(T t);

	public PageList<T> getListWithPage(T t, PageBounds pageBounds);

	public List<T> getList(T t);

	public T getSingle(Object id);

	public <K> int batchDelete(List<K> id);
}
