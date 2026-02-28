class WebPage<T> implements Content<T> {
    private String url;
    private T content;

    public WebPage(String url, T content) {
        this.url = url;
        this.content = content;
    }

    public String getUrl() {
        return this.url;
    }

    public T getContent() {
        return content;
    }
}
