package lingshi.mybaties.mapperextend;

public class Pager {
	private int page;
	private int rows;

	public Pager(int page, int rows) {
		this.page = page;
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
}
