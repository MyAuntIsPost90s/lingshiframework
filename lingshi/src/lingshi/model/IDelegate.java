package lingshi.model;

public interface IDelegate<T> {
	T invoke(Object... args);
}
