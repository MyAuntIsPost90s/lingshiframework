package lingshi.mybaties.mapperextend;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

public interface BaseMapper<T> {
	int insert(T t);

	int update(T t);

	long count(T t);

	PageList<T> getListWithPage(T t, PageBounds pageBounds);

	List<T> getList(T t);

	T getSingle(Object id);

	<K> int batchDelete(List<K> ids);
}
